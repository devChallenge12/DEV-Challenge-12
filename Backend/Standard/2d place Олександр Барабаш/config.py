"""
Configuration and settings file
"""
import os


#Project base dir
BASE_DIR = os.path.dirname(__file__)

config = {
    "base_dir": BASE_DIR,
    "node_name": os.environ.get("NODE_NAME", "node1"),
    "webapi_host": os.environ.get("WEBAPI_HOST", "0.0.0.0"),
    "webapi_port": int(os.environ.get("WEBAPI_PORT", 8080)),
    "redis_datastore": os.environ.get("REDIS_DATASTORE", "redis://localhost/1"),
    "redis_settingstore": os.environ.get("REDIS_SETTINGSTORE", "redis://localhost/0")
}

