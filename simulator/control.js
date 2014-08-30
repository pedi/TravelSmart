/**
 * Created by mohist on 8/30/14.
 */
function init() {
    // origin select
    document.getElementById("origin_select").onchange = function(e) {
        var originSelect = e.target;
        var selectedOriginStopName = originSelect.options[originSelect.selectedIndex].value;

        // update destination select
        var selectOriginStopID = STOP_ID_MAP[selectedOriginStopName];
        var destinationSelect = document.getElementById("destination_select");
        destinationSelect.innerHTML = "";
        for (var i=selectOriginStopID+1; i<NUMBER_OF_STOPS; i++) {
            var option = document.createElement("option");
            option.value = STOPS[i];
            option.innerHTML = STOPS[i];
            destinationSelect.appendChild(option);
        }
    };
    // destination select
    document.getElementById("destination_select").onchange = function(e) {
        var originSelect = document.getElementById("origin_select");
        var selectedOriginStopName =  originSelect.options[originSelect.selectedIndex].value;

        var destinationSelect = e.target;
        var selectedDestinationStopName = destinationSelect.options[destinationSelect.selectedIndex].value;

        var busLinesOnOriginStop = BUSES_BY_STOP_MAP[selectedOriginStopName];
        var busLinesOnDestStop = BUSES_BY_STOP_MAP[selectedDestinationStopName];

        var matchingBusLines = [];
        for (var i=0; i<busLinesOnOriginStop.length; i++) {
            var originBusLine = busLinesOnOriginStop[i];
            for (var j=0; j<busLinesOnDestStop.length; j++) {
                var destBusLine = busLinesOnDestStop[j];
                if (originBusLine === destBusLine) {
                    matchingBusLines.push(originBusLine)
                    break;
                }
            }
        }

        // push bus lines in matching bracket to bus lines select
        var busLinesSelect = document.getElementById("bus_lines_select");
        busLinesSelect.innerHTML = "";
        for (var k=0; k<matchingBusLines.length; k++) {
            var option = document.createElement("option");
            option.value = matchingBusLines[k];
            option.innerHTML = matchingBusLines[k];
            busLinesSelect.appendChild(option);
        }
    };

    // simulator bus select
    document.getElementById("sim_bus_select").onchange = function(e) {
        var simBusSelect = e.target;
        var selectedBusLine = simBusSelect.options[simBusSelect.selectedIndex].value;
        var possibleStops = STOP_BY_BUSES_MAP[selectedBusLine];

        var stop_select = document.getElementById("sim_arrived_stop_select");
        stop_select.innerHTML = "";
        for (var k=0; k<possibleStops.length; k++) {
            var option = document.createElement("option");
            option.value = possibleStops[k];
            option.innerHTML = possibleStops[k];
            stop_select.appendChild(option);
        }
    }

    // book button
    document.getElementById("book_bus_btn").onclick = function(e) {
        var originSelect = document.getElementById("origin_select");
        var selectedOriginStopName =  originSelect.options[originSelect.selectedIndex].value;

        var destinationSelect = document.getElementById("destination_select");
        var selectedDestinationStopName = destinationSelect.options[destinationSelect.selectedIndex].value;

        var EZLinkID = parseInt(Math.random() * 100);

        var busLinesSelect = document.getElementById("bus_lines_select");
        var selectedBus = busLinesSelect.options[busLinesSelect.selectedIndex].value;

        jQuery.ajax(
            {
                type : "POST",
                url : "http://172.23.60.92:3000/passenger",
                data :  JSON.stringify({
                    "EZLinkID": EZLinkID,
                    "busName": selectedBus,
                    "originBusStopName": selectedOriginStopName,
                    "destinationBusStopName": selectedDestinationStopName
                }),
                success : function(data) {
                    console.log (data);
                }
            });
    }
}
init();