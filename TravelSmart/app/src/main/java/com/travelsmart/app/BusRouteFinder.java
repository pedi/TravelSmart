package com.travelsmart.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mohist on 8/30/14.
 */
public class BusRouteFinder {

    private final String API_KEY = "AIzaSyCJEBzjtvxIkKV_aZs8TbYpUxN_J_vkJEI";
    private final String BUS_STOP_QUERY_PREFIX = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
    private final String ROUTES_TO_DESTINATION_QUERY_PREFIX = "https://maps.googleapis.com/maps/api/directions/json";

    private double originLatitude;
    private double originLongitude;

    private int radius = 200;
    public int getRadius() {return radius;}
    public void setRadius(int newRadius) {radius = newRadius;}

    BusRouteFinder(double[] origin) throws JSONException {

        mapOriginToNearestBusStopLocation(origin);
    }

    private String buildNearestBusStopURI(double[] origin) {
        Uri.Builder builder = Uri.parse(BUS_STOP_QUERY_PREFIX).buildUpon();

        String location = origin[0] + "," + origin[1];
        builder.appendQueryParameter("location", location)
                .appendQueryParameter("radius", radius+"")
                .appendQueryParameter("types", "bus_station")
                .appendQueryParameter("key", API_KEY);
        return builder.build().toString();
    }

    public interface OnAsyncGetCompletd {
        void OnAsyncGetCompletd(JSONObject response);
    }
    private class AsyncGet extends AsyncTask<String, Void, JSONObject> {

        private OnAsyncGetCompletd listener;

        public AsyncGet(OnAsyncGetCompletd listener){
            this.listener=listener;
        }


        @Override
        protected JSONObject doInBackground(String... query) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;
            try {
                response = httpclient.execute(new HttpGet(query[0]));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    response.getEntity().writeTo(out);
                    String responseString = out.toString();
                    out.close();
                    // convert response string to json object
                    return new JSONObject(responseString);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else{
                //Closes the connection.
                try {
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        protected void onPostExecute(JSONObject response) {
            listener.OnAsyncGetCompletd(response);
        }
    }

    private JSONObject httpGet(String query) throws IOException, JSONException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(query));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            String responseString = out.toString();
            out.close();
            // convert response string to json object
            return new JSONObject(responseString);
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
    }

    private double computeDistanceDiffSquare(double[] origin, double[] destination) {
        return (destination[0] - origin[0]) * (destination[0] - origin[0]) +
                (destination[1] - origin[1]) * (destination[1] - origin[1]);
    }

    private double[] findNearestBusStop(JSONArray stopsData, double[] origin) throws JSONException {
        JSONObject firstStop = stopsData.getJSONObject(0);
        JSONObject location = firstStop.getJSONObject("geometry").getJSONObject("location");
        double latitude = location.getDouble("lat");
        double longitude = location.getDouble("lng");
        double[] nearestCoords = {latitude, longitude};
        double leastDistanceDiffSquare = computeDistanceDiffSquare(origin, nearestCoords);
        int nearestStopIndex = 0;

        if (stopsData.length() == 1)
            return nearestCoords;
        else {
            for (int i=1; i<stopsData.length(); i++) {
                location = firstStop.getJSONObject("geometry").getJSONObject("location");
                latitude = location.getDouble("lat");
                longitude = location.getDouble("lng");
                double [] coords = {latitude, longitude};
                double distanceDiffSquare = computeDistanceDiffSquare(origin, coords);
                if (distanceDiffSquare < leastDistanceDiffSquare) {
                    nearestStopIndex = i;
                    nearestCoords = coords;
                }
            }
        }
        return nearestCoords;
    }

    private void mapOriginToNearestBusStopLocation(double[] origin) throws JSONException {
        String queryURI = buildNearestBusStopURI(origin);


//        originLatitude = origin[0];
//        originLongitude = origin[1];
//        new AsyncGet(new OnAsyncGetCompletd() {
//            @Override
//            public void OnAsyncGetCompletd(JSONObject response) {
//                double[] nearestStop = new double[0];
//                double[] origin = {originLatitude, originLongitude};
//                try {
//                    nearestStop = findNearestBusStop(response.getJSONArray("results"), origin);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                originLatitude = nearestStop[0];
//                originLongitude = nearestStop[1];
//                Log.d("nearest", originLatitude + " " + originLongitude);
//
//            }
//        }).execute(queryURI);


        try {

            JSONObject response = httpGet(queryURI);
            double[] nearestStop = findNearestBusStop(response.getJSONArray("results"), origin);

            originLatitude = nearestStop[0];
            originLongitude = nearestStop[1];
            Log.d("here", originLatitude + "," + originLongitude);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean onlyOneDestinationBusStop(String destination) throws Exception {
        ArrayList<String> busStops= getPossibleBusStops(destination);
        if (busStops.size() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<String> getPossibleBusLines(String destination) throws Exception {
        ArrayList<String> possibleBuses = new ArrayList<String>();
        JSONArray routes = getRoutesToDestination(destination);
        for (int i=0; i< routes.length(); i++) {
            String arrivalStop = routes.getJSONObject(i).getJSONArray("legs").getJSONObject(0)
                    .getJSONArray("steps").getJSONObject(0).getJSONObject("transit_details")
                    .getJSONObject("arrival_stop").getString("name");
            if (arrivalStop.equals(destination)) {
                String busNo = routes.getJSONObject(i).getJSONArray("legs").getJSONObject(0)
                        .getJSONArray("steps").getJSONObject(0).getJSONObject("transit_details")
                        .getJSONObject("line").getString("short_name");
                possibleBuses.add(busNo);
            }
        }
        return possibleBuses;
    }

    public ArrayList<String> getPossibleBusStops(String destination) throws Exception {
        JSONArray routes = getRoutesToDestination(destination);
        ArrayList<String> busStops = new ArrayList<String>();
        for (int i=0; i<routes.length(); i++) {
            String arrivalStop = routes.getJSONObject(i).getJSONArray("legs").getJSONObject(0)
                    .getJSONArray("steps").getJSONObject(0).getJSONObject("transit_details")
                    .getJSONObject("arrival_stop").getString("name");
            if(!busStops.contains(arrivalStop)) {
                busStops.add(arrivalStop);
            }
        }
        return busStops;
    }

    private String buildRoutesToDestinationURI(String destination) {
        Uri.Builder builder = Uri.parse(ROUTES_TO_DESTINATION_QUERY_PREFIX).buildUpon();

        String origin = originLatitude+ "," + originLongitude;
        builder.appendQueryParameter("origin", origin)
                .appendQueryParameter("destination", destination)
                .appendQueryParameter("mode", "transit")
                .appendQueryParameter("alternatives", "true")
                .appendQueryParameter("departure_time",((System.currentTimeMillis())/1000)+"")
                .appendQueryParameter("key", API_KEY);
        return builder.build().toString();
    }

    private String previousDestination = "";
    private JSONArray previousRoutes = new JSONArray();
    private JSONArray getRoutesToDestination(String destination) throws Exception {
        if (previousDestination.equals(destination))
            return previousRoutes;
        String URI = buildRoutesToDestinationURI(destination);

        JSONObject result = httpGet(URI);
        String status = result.getString("status");
        if (status.equals("OK")) {
            JSONArray routes = result.getJSONArray("routes");
            JSONArray filteredRoutes = removeIndirectRoutes(routes);
            previousDestination = destination;
            previousRoutes = filteredRoutes;
            if (filteredRoutes.length() == 0) {
                throw new Exception("no direct routes");
            }
            return filteredRoutes;
        } else {
            throw new Exception("getRoutes failed");
        }
    }
    private JSONArray removeIndirectRoutes (JSONArray routes) throws JSONException {
        JSONArray filteredRoutes = new JSONArray();
        for (int i=0; i< routes.length(); i++) {
            JSONArray steps = routes.getJSONObject(i).getJSONArray("legs").getJSONObject(0)
                    .getJSONArray("steps");
            for (int j=1; j<steps.length(); j++) {
                String travelMode = steps.getJSONObject(j).getString("travel_mode");
                if (travelMode.equals("TRANSIT")) {
                    break;
                }
                if (j==steps.length()-1) {
                    filteredRoutes.put(routes.getJSONObject(i));
                }
            }
        }
        return filteredRoutes;
    }
}
