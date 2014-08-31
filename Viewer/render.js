/**
 * Created by mohist on 8/31/14.
 */
function drawCircle(context, centerX, centerY, radius, color) {
    context.save()
    context.beginPath();
    context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
    context.fillStyle = color;
    context.fill();
    context.lineWidth = 0;
    context.stroke();
    context.restore();
}

function drawStraightLine(context, originX, originY, endX, endY, color, width) {
    context.save();
    context.beginPath();
    context.moveTo(originX, originY);
    context.lineTo(endX, endY);
    context.lineWidth = width;
    context.strokeStyle = color;
    context.stroke();
    context.restore();
}