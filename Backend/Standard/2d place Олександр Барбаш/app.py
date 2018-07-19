from aiohttp import web


def get_main_app(config):
    # init Main APP
    app = web.Application()
    app["config"] = config

    return app

def middlewares_setup(app, middlewares):
    for m in middlewares:
        app.middlewares.append(m)


def subapp_setup(app, subapps):
    for sub in subapps:
        route, subapp = sub
        app.add_subapp(route, subapp)
        subapp["db"] = app["db"]
