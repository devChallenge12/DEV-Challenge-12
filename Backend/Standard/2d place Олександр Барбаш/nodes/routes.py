from aiohttp import web

from .handlers import update, get, remove, dump


urls = [
    web.get('/update', update),
    web.get('/get', get),
    web.get('/remove', remove),
    web.get('/dump', dump)
]