from aiohttp import web

from .routes import urls


def get_app(config):

    app = web.Application()
    app["config"] = config

    app.add_routes(urls)

    return app


async def on_startup(app):
    """Add node to shared list of nodes"""
    db = app["db"]

    node_name = app["config"]["node_name"]
    node_port = app["config"]["webapi_port"]
    node_host = "{}:{}".format(node_name, node_port)

    db.add_nodes(node_host)

async def on_shutdown(app):
    """Delete node to shared list of nodes"""
    db = app["db"]

    node_name = app["config"]["node_name"]
    node_port = app["config"]["webapi_port"]
    node_host = "{}:{}".format(node_name, node_port)

    db.remove_node(node_host)

