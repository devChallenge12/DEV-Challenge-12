from aiohttp import web
from utils import json_response, required
from .tasks import update_on_all_nodes, remove_on_all_nodes

@json_response
@required("name", "value")
async def update(request):

    db = request.app["db"]
    config =  request.app["config"]

    name = request.query["name"]
    value = request.query["value"]

    replicate = True if "replicate" in request.query else False

    result = db.update(name, value)

    msg = "{} key was updated".format(name) if result else "{} key was not updated"

    # update on others nodes
    if not replicate:
        nodes = db.get_nodes()
        print(nodes)
        current_node = "{}:{}".format(config["node_name"], config["webapi_port"])
        print(current_node)
        print(name)
        print(value)

        update_on_all_nodes.delay(nodes, current_node, name, value)

    return {
        "success": True,
        "msg": msg
    }



@json_response
@required("name")
async def get(request):

    db = request.app["db"]

    name = request.query["name"]

    result = db.get(name)

    if result:
        return {
            "success": True,
            "name": name,
            "value": result
        }
    else:
        return {
            "success": False,
            "msg": "can not get {} key".format(name)

        }

@json_response
@required("name")
async def remove(request):

    db = request.app["db"]
    config =  request.app["config"]

    name = request.query["name"]

    replicate = True if "replicate" in request.query else False

    result = db.remove(name)

    # remove on others nodes
    if not replicate:
        nodes = db.get_nodes()
        current_node = config["node_name"]
        remove_on_all_nodes.delay(nodes, current_node, name)

    if result:
        return {
            "success": True,
            "msg": "{} key was removed".format(name)
        }
    else:
        return {
            "success": False,
            "msg": "{} key was not removed".format(name)
        }

@json_response
async def dump(request):

    db = request.app["db"]

    data = db.dump()
    if data:
        return {
            "success": True,
            "data": data
        }
    else:
        return {
            "success": False,
            "msg": "can not get data or db is empty"
        }

