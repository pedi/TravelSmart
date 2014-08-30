/**
 * Created by mohist on 8/30/14.
 */
var NUMBER_OF_STOPS = 4;
var STOPS = ["opp Buona Vista Station", "opp Anglo-Chinese Junior College", "Ayer Rajah Ind Est", "Kent Ridge Station"];
var STOP_ID_MAP = {
    "opp Buona Vista Station" : 0,
    "opp Anglo-Chinese Junior College" : 1,
    "Ayer Rajah Ind Est" : 2,
    "Kent Ridge Station" : 3
};
var BUSES_BY_STOP_MAP = {
    "opp Buona Vista Station" : [74,91,92,95,191,196,198,200],
    "opp Anglo-Chinese Junior College" : [74,91,92,95,191,196,198,200],
    "Ayer Rajah Ind Est" : [14,33,92,95,166,198,200],
    "Kent Ridge Station" : [95]
};
var STOP_BY_BUSES_MAP = {
    "74" : [STOPS[0], STOPS[1]],
    "91" : [STOPS[0], STOPS[1]],
    "92" : [STOPS[0], STOPS[1], STOPS[2]],
    "95" : [STOPS[0], STOPS[1], STOPS[2], STOPS[3]],
    "191" : [STOPS[0], STOPS[1]],
    "196" : [STOPS[0], STOPS[1]],
    "198" : [STOPS[0], STOPS[1], STOPS[2]],
    "200" : [STOPS[0], STOPS[1], STOPS[2]],
    "14" : [STOPS[2]],
    "33" : [STOPS[2]],
    "166" : [STOPS[2]]
};