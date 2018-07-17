from aiohttp import web
from functools import wraps



def json_response(fn):

    @wraps(fn)
    async def wrapped(request):
        data = await fn(request)
        return web.json_response(data)

    return wrapped



def required(*args):
    """Decorator for handlers"""
    required_vars = args

    def decorator(fn):
        @wraps(fn)
        async def wrapped(request):

            for var in required_vars:
                if var not in request.query:
                    return {
                        "success": False,
                        "msg": "{} is required".format(", ".join(required_vars))
                    }

            return await fn(request)

        return wrapped

    return decorator