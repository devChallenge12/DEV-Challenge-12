# distdb

## To start
From the root directory execute command:
`./start_me.sh` on macos, unix

##Description of fault tolerance technique
In case one master is down it will not receive latest updates, and deletes.
In case two uses will update the same record on two different servers simultaneously

##Description of onboarding/restore algorithm
For onboarding
Get list of all servers. Get snapshot of latest data like list of
{key, value}.

For restore
When master gets up it calls restore method with timestamp HIS last updated record.
And receives list of changed records since that period.
List of {key, value}

##Restore limitation.
Since server list is not in db, but in memory, it's hard to test failure.
Use POST /downtime for that


## To update.
Use swagger.
http://localhost:8080/swagger#/update

## To get latest value:
http://localhost:8080/swagger#!/key/getLatestValue

## To test onboarding use:

`curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ \
    "serverToConnectUrl": "http://localhost:8080", \
    "myServerUrl": "http://localhost:8083" \
  }' 'http://localhost:8083/onboard/me'`

then

http://localhost:8083/swagger#!/dump/getDump

## To test restore

somehow do not allow 8083 to receive updates. Not enough time to figure out how to do it without shutting down server on 8083,
But then server list would be lost, since no real db is used for demo purposes.

After network problem use

http://localhost:8083/swagger#!/restore/restoreMe
Additional details can be got from

curl -X GET --header 'Accept: application/json' 'http://localhost:8081/restore/20170101T000000Z'

## Valid auth token
Valid tokes are located in tokens.txt



## Requirenments
* Java 8
* Gradle 2.4 +