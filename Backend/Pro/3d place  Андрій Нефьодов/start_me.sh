#!/usr/bin/env bash
set -x
chmod 755 *.sh
./stop_me.sh
gradle clean shadowJar


#Start new servers
sleep 2
java -jar build/libs/distdb-1.0-SNAPSHOT-all.jar server ./src/dist/config/distdb.yaml &
sleep 2
java -jar build/libs/distdb-1.0-SNAPSHOT-all.jar server ./src/dist/config/distdb_node2.yaml &
sleep 2
java -jar build/libs/distdb-1.0-SNAPSHOT-all.jar server ./src/dist/config/distdb_node3.yaml &
sleep 2
java -jar build/libs/distdb-1.0-SNAPSHOT-all.jar server ./src/dist/config/distdb_node4.yaml &