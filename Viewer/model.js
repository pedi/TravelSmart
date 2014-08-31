/**
 * Created by mohist on 8/31/14.
 */
var BusLines = [];
var BusStop = function(name) {
    this.name = name;
    this.waitingList = [];
};

var BusStopView = function(originX, originY, radius, color) {
    this.originX = originX;
    this.originY = originY;
    this.radius = radius;
    this.color = color;
};
BusStopView.prototype.render = function(ctx) {
    drawCircle(ctx, this.originX, this.originY, this.radius, this.color);
};
var BusStopsConnectionView = function(originX, originY, endX, endY, width, color) {
    this.originX = originX;
    this.originY = originY;
    this.endX = endX;
    this.endY = endY;
    this.color = color;
    this.width = width;
};
BusStopsConnectionView.prototype.render = function(ctx) {
    drawStraightLine(ctx, this.originX, this.originY,
        this.endX, this.endY, this.color, this.width);
};