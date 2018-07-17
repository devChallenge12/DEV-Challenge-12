#!/usr/bin/env bash
#Stop previously launched servers
lsof -i:8080|awk -F ' ' '{print $2}' |tail -n +2 | xargs kill
lsof -i:8081|awk -F ' ' '{print $2}' |tail -n +2 | xargs kill
lsof -i:8082|awk -F ' ' '{print $2}' |tail -n +2 | xargs kill
lsof -i:8083|awk -F ' ' '{print $2}' |tail -n +2 | xargs kill
sleep 3