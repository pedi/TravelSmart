# Designed for the specs of Visualization

## Server

## Visualizer
- Bus Information
  - Id
  - Bus Number
  - Bus Stops
- Stop Information
  - Id
  - Location
- Show bus status realtime:
  - busId
  - Capacity
  - Location
- Show stop status
  - # of people waiting
- Show passager information
  - current stop
  - intenting stop

## Android
POST :
    path :  /reserveBus
    requestBody : {
        ## using EZlink card ID as identifier
        commuterID : %ID%,     
        origin : %busID%,
        destination : %busID%,
        timestamp : %timestamp%,
        possibleBusRoutes : [ %busNo%, %busNo%, etc]
    }
    expected response : {
        status : [success or fail]
    }

