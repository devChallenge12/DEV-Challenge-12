# Final task

Each instance has in memory storage and RESTful API public and private interfaces.
If you want to add replication instance, set it to `REPLICATIONS` environment param (see docker-compose.yml how it looks like).

RESTful API endpoints work with Content-Type: test/plain to not spend time for encoding/decoding. There are:

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/?fileName=:filename` | Returns dump file called `:filename` or `default` in format `name;value` for each item (per row). |
| `GET` | `/:name` | Returns value from `:name` item |
| `PUT` | `/:name` | Set value for `:name` item |
| `DELETE` | `/:name` | Remove `:name` item |

# How replication works

When you set/update/delete any key/value, firstly it applies to current instance, 
then it will concurrently apply changes to replications. Current instance immediately
returns response without waiting for replication.

# How to run/test

Execute the following command:

`docker-compose up --build`

It will run 3 replications for you and one instance for testing. 

Test instance will run 3 replications in itself, run tests and exit.

Live instances will be run on `8001`, `8002`, `8003` ports. Therefore, you will be able to work with them manually using RESTful API.

# How it was coded

* Created in memory storage with own interface to work with.
* Created service with main business logic.
* Created controller for RESTful API interface.
* Created router and server.
* Created replication client to work with other instances.
* Added replicating to service on update/remove data (this is only actions which change data).
* Wrote README.md for you.

# What can be improved next.

* Add checking status of instances.
* Store failed replications and try to do it when instance will be ok.
* Concurrently do everything with filesystem, but work with in memory storage. If an instance fails, it will be able to start from the last changes.