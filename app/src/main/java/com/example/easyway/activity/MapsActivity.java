package com.example.easyway.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.easyway.R;
import com.example.easyway.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String origem = getIntent().getStringExtra("Origem");
        String destino = getIntent().getStringExtra("Destino");
        String apiKey = "AIzaSyBBrje-m4EVpbf3ju-NHrgL96hlmAjPT80";

        if (origem != null && destino != null) {

            String[] origemCoords = origem.split(",");
            String[] destinoCoords = destino.split(",");

            double origemLatitude = Double.parseDouble(origemCoords[0]);
            double origemLongitude = Double.parseDouble(origemCoords[1]);
            double destinoLatitude = Double.parseDouble(destinoCoords[0]);
            double destinoLongitude = Double.parseDouble(destinoCoords[1]);

            LatLng origemMarcador = new LatLng(origemLatitude, origemLongitude);
            LatLng destinoMarcador = new LatLng(destinoLatitude, destinoLongitude);

            mMap.addMarker(new MarkerOptions().position(origemMarcador).title("Origem"));
            mMap.addMarker(new MarkerOptions().position(destinoMarcador).title("Destino"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origemMarcador, 10));

        } else {
            Toast.makeText(this, "Coordenadas de origem ou destino não foram definidas.", Toast.LENGTH_SHORT).show();
        }

        String urlStr = "https://maps.googleapis.com/maps/api/directions/json?origin="
                +                   origem
                + "&destination=" + destino
                + "&key=" +         apiKey;
        new DirectionsTask().execute(urlStr);
    }

    private class DirectionsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                String urlStr = params[0];
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                response = sb.toString();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            parseAndDrawRoute(result); // Desenha a rota no mapa após a resposta
        }

        private void parseAndDrawRoute(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray routes = jsonObject.getJSONArray("routes");
                JSONObject route = routes.getJSONObject(0);
                JSONArray legs = route.getJSONArray("legs");

                PolylineOptions polylineOptions = new PolylineOptions();

                // Loop para percorrer cada trecho da rota
                for (int j = 0; j < legs.length(); j++) {
                    JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

                    for (int i = 0; i < steps.length(); i++) {
                        String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");
                        List<LatLng> points = decodePolyline(polyline);
                        polylineOptions.addAll(points);
                    }
                }

                mMap.addPolyline(polylineOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private List<LatLng> decodePolyline(String encoded) {
            List<LatLng> poly = new ArrayList<>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((lat / 1E5), (lng / 1E5));
                poly.add(p);
            }

            return poly;
        }
    }
}

