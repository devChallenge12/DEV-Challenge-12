from aiohttp import web

from app import get_main_app, subapp_setup
from nodes.app import get_app as get_nodes_app, on_startup, on_shutdown

from db import init_db

import config as configuration


def run_in_subprocess(name, cmd, **kwargs):
    """Run component in subprocess.
    Take name, cmd
    Return Popen instance"""

    import subprocess

    print("{} starting...".format(name))
    try:
        process = subprocess.Popen(cmd, **kwargs)
    except Exception as err:
        print("{} not running. {}".format(name, err))
        raise
    print("{} is running".format(name))
    return process


def run_celery_worker(name, **kwargs):
    """Celery worker run function.
    Take name, celery_autoscale.
    Return process instance."""

    celery_cmd = [
        "celery",
        "-A",
        "nodes.tasks",
        "worker",
        "--loglevel=ERROR",
        "--hostname={}@%h".format(name)
    ]

    worker = run_in_subprocess("Celery {}".format(name), celery_cmd)

    return worker



def init_apps(config):
    """Init all apps and return Main App"""

    # get apps
    main_app = get_main_app(config)
    nodes_app = get_nodes_app(config)

    # get db
    db = init_db(config)

    main_app["db"] = db

    # setup subapps
    subapp_setup(main_app, [
        ("/nodes", nodes_app)
    ])

    # signals
    main_app.on_startup.append(on_startup)
    main_app.on_shutdown.append(on_shutdown)

    return main_app


def main():

    config = configuration.config

    worker = run_celery_worker(config["node_name"])

    app = init_apps(config)

    host = app["config"]["webapi_host"]
    port = app["config"]["webapi_port"]


    web.run_app(app, host=host, port=port)

    worker.terminate()


if __name__ == "__main__":
    main()
