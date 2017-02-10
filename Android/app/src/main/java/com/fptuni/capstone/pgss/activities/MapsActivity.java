package com.fptuni.capstone.pgss.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.helpers.MapMarkerHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapMarkerHelper markerHelper;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        testPubnub();
    }

    private void testPubnub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-ed7a8b02-ed34-11e6-a504-02ee2ddab7fe");
        pnConfiguration.setPublishKey("pub-c-85b2050b-5425-4964-972f-90910aa358ca");
        pnConfiguration.setSecure(false);

        PubNub pubNub = new PubNub(pnConfiguration);
        pubNub.subscribe()
                .channels(Arrays.asList("debug"))
                .execute();

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                Log.d("Pubnub", message.getMessage().toString());
                JsonObject json = message.getMessage().getAsJsonObject();
                String test = json.get("test").getAsString();
                if (test.equals("detected")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            marker.setIcon(BitmapDescriptorFactory
                                    .fromBitmap(markerHelper.getParkingMarker(getBaseContext(), 99)));
                        }
                    });
                } else if (test.equals("undetected")) {
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        marker.setIcon(BitmapDescriptorFactory
                                .fromBitmap(markerHelper.getParkingMarker(getBaseContext(), 100)));
                    }
                });
                }

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markerHelper = MapMarkerHelper.getInstance();

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney");
        marker = mMap.addMarker(markerOptions);
        marker.setIcon(BitmapDescriptorFactory
                .fromBitmap(markerHelper.getParkingMarker(this.getBaseContext(), 100)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
