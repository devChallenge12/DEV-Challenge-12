import os
from celery import Celery
import urllib.request

import config as configuration


config = configuration.config

celery_app = Celery(
    'tasks',
    broker=config["redis_datastore"],
    backend=config["redis_datastore"]
)

celery_app.conf.update(
    task_serializer = 'json',
    result_serializer='json'
)


@celery_app.task
def update_on_all_nodes(nodes, current_node, name, value):
    pattern = "http://{}/nodes/update?name={}&value={}&replicate=true"
    for node in nodes:
        if node != current_node:
            url = pattern.format(node, name, value)
            print(url)
            contents = urllib.request.urlopen(url).read()
    return True


@celery_app.task
def remove_on_all_nodes(nodes, current_node, name):
    pattern = "http://{}/nodes/remove?name={}&replicate=true"
    for node in nodes:
        if node != current_node:
            url = pattern.format(node, name)
            print(url)
            contents = urllib.request.urlopen(url).read()
    return True