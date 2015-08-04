package com.mychild.view.Parent;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mychild.utils.DirectionsJSONParser;
import com.mychild.view.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Antony on 11-06-2015.
 */
public class ParentTransportMapRouteActivity extends FragmentActivity {
    private GoogleMap mMap;
    public static double mSourceLatitude = 13.012731, mSourceLongitude = 77.578157;
    public static double mDestinationLatitude = 13.013333, mDestinationLongitude = 77.76556;
    private LatLng currentgeo = new LatLng(13.013333, 77.76556);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_maproute);
        setUpMapIfNeeded();
        getDirection();


    }

    private void setUpMapIfNeeded() {
        // TODO Auto-generated method stub
        if (mMap == null)
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(
                R.id.map)).getMap();

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void getDirection() {
        DownloadTask downloadTask = new DownloadTask();

        downloadTask
                .execute("https://maps.googleapis.com/maps/api/directions/json?origin=13.012731,77.578157&destination=13.013333,77.76556"/*"https://maps.googleapis.com/maps/api/directions/json?origin="
                        + mSourceLatitude
                        + ","
                        + mSourceLongitude
                        + "&destination="
                        + mDestinationLatitude
                        + ","
                        + mDestinationLongitude*/);
    }


    // Fetches data from url passed
    class DownloadTask extends AsyncTask<String, Void, String> {
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }


        /**
         * A method to download json data from url
         */
        public String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        /**
         * A class to parse the Google Places in JSON format
         */
        private class ParserTask extends
                AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

            // Parsing the data in non-ui thread
            @Override
            protected List<List<HashMap<String, String>>> doInBackground(
                    String... jsonData) {
                JSONObject jObject;
                List<List<HashMap<String, String>>> routes = null;

                try {
                    jObject = new JSONObject(jsonData[0]);
                    DirectionsJSONParser parser = new DirectionsJSONParser();
                    // Starts parsing data
                    routes = parser.parse(jObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return routes;
            }

            // Executes in UI thread, after the parsing process
            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> result) {
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;
                MarkerOptions markerOptions = new MarkerOptions();
                String distance = "";
                String duration = "";

                try {
                    if (result.size() < 1) {
                        return;
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) { // Get distance from the list
                            distance = point.get("distance");
                            continue;
                        } else if (j == 1) { // Get duration from the list
                            duration = point.get("duration");
                            continue;
                        }
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.RED);
                }

                // distanceAndTime.setText("Distance:" + distance + ", Duration:" +
                // duration);

                // Drawing polyline in the Google Map for the i-th route
                ParentTransportMapRouteActivity.this.mMap.addPolyline(lineOptions);
                List<LatLng> polyPoints = lineOptions.getPoints(); // route is
                // instance of
                // PolylineOptions
                LatLngBounds.Builder bc = new LatLngBounds.Builder();
                for (LatLng item : polyPoints) {
                    bc.include(item);
                }
                LatLng point = new LatLng(mSourceLatitude, mSourceLongitude);
                mMap.addMarker(new MarkerOptions().position(point).title("Source"));
                point =currentgeo;
                mMap.addMarker(new MarkerOptions().position(point).title(
                        "Destination"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bc.build(), 50));

            }
        }
    }
}
