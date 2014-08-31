/**
 * Created by mohist on 8/31/14.
 */
var CONNECTION_WIDTH = 3;
var STOPS_NAME =  ["opp Buona Vista Station", "opp Anglo-Chinese Junior College", "Ayer Rajah Ind Est", "Kent Ridge Station"];
var STOP_ID_MAP = {
    "opp Buona Vista Station" : 0,
    "opp Anglo-Chinese Junior College" : 1,
    "Ayer Rajah Ind Est" : 2,
    "Kent Ridge Station" : 3
};


var context = null;
var stops = [];
var stopViews = [];
var connectionViews = [];

function init() {
    context = document.getElementById("bus_visualization").getContext("2d");

    // create stops view and stops model
    for (var i=0; i<STOPS_NAME.length; i++) {
        var stopView = new BusStopView(200*(i+1), 250, 20, "grey");
        stopViews.push(stopView);

        var stop = new BusStop(STOPS_NAME[i]);
        stops.push(stop);
    }

    // create connections view
    for (i=0; i<STOPS_NAME.length-1; i++) {
        var connectionView = new BusStopsConnectionView(200*(i+1), 250, 200*(i+2), 250, 5, "green");
        connectionViews.push(connectionView);
    }

    renderAll(context);
}

function renderAll(ctx) {
    for (var i=0; i<connectionViews.length; i++) {
        connectionViews[i].render(ctx);
    }
    for (var i=0; i<stopViews.length; i++) {
        stopViews[i].render(ctx);
    }
}

var WAITING_MAX = 20;
function convertWaitingNoToColor(No) {
    var occupiedRate = No /WAITING_MAX;
    var redPortion = parseInt(256 * occupiedRate);
    var greenPortion = parseInt(256 - redPortion);
    var combinedRGB  = "#" + redPortion.toString(16) + greenPortion.toString(16) + "00";
    return combinedRGB;
}

function updateConnectionViewByLine(busNo) {
    for (var i=0; i<stops.length-1; i++) {
        var waitingNo = stops[i].waitingList[busNo];
        if (!waitingNo) {
            waitingNo = 0;
        }
        if (waitingNo > WAITING_MAX) {
            waitingNo = WAITING_MAX;
        }

        // convert no to color
        connectionViews[i].color = convertWaitingNoToColor(waitingNo);
    }
}

var selectedBusNo = "74";
function updateWaitingList() {
    jQuery.ajax(
        {
            type : "POST",
            url : "http://localhost:3000/smrt",
            data :  JSON.stringify({
                "BusName": selectedBusNo,
                "originBusStopName": BusStop[0],
                "destinationBusStopName": BusStop[BusStop.length-1]
            }),
            success : function(data) {
                console.log (data);
            }
        });

}