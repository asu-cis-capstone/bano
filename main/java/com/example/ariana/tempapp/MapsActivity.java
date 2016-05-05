package com.example.a.tester;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(33.416370, -111.933819);
        mMap.addMarker(new MarkerOptions().position(sydney).title("BAC 2nd Floor").snippet("they're pretty clean"));

        DatabaseTask elBano = new DatabaseTask();
        elBano.execute();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private class DatabaseTask extends AsyncTask<Void, Void, Void> {

        private ResultSet bathroomSet;

        @Override
        protected Void doInBackground(Void... params) {
            String url = "";
            String userName = "";
            String password = "";
            String dbName = "";
            String query;

            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("Driver loaded");

                Connection connection = DriverManager.getConnection(url + dbName, userName, password);
                System.out.println("database connected");

                Statement statement = connection.createStatement();
                query = "SELECT bldg, lat, lon FROM bano;";

                bathroomSet = statement.executeQuery(query);

            }
            catch(ClassNotFoundException e) {

            }
            catch (SQLException e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

           try {
               while (bathroomSet.next()) {

                   double lat = Double.parseDouble(bathroomSet.getString(2));
                   double lng = Double.parseDouble(bathroomSet.getString(3));
                   LatLng lSet = new LatLng(lat, lng);

                   mMap.addMarker(new MarkerOptions().position(lSet).title(bathroomSet.getString(1)));

               }
           }
            catch (SQLException e){

            }
        }
    }
}
