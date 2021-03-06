package com.fptuni.capstone.pgss.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.adapters.UserInfoWindowAdapter;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
import com.fptuni.capstone.pgss.helpers.MapMarkerHelper;
import com.fptuni.capstone.pgss.helpers.PubNubHelper;
import com.fptuni.capstone.pgss.interfaces.CarParkClient;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.CarPark;
import com.fptuni.capstone.pgss.models.Geo;
import com.fptuni.capstone.pgss.network.CarParkAdvancePackage;
import com.fptuni.capstone.pgss.network.GetCoordinatePackage;
import com.fptuni.capstone.pgss.network.NotificationPackage;
import com.fptuni.capstone.pgss.network.RealtimeMapPackage;
import com.fptuni.capstone.pgss.network.ServiceGenerator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int MAP_ZOOM_TO = 15;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    private GoogleMap map;
    private LatLng centerLocation;
    private LatLng currentLocation;
    private LatLng placeLocation;
    private boolean fromYou;
    private String placeName;
    private PlaceAutocompleteFragment autocompleteFragment;
    private boolean refreshCarParkData;

    private GoogleApiClient googleApiClient;

    private HashMap<Integer, Marker> markerMap;

    PubNub pubNub;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ProgressBar toolbarProgress;
    private NavigationView navigationMenu;
    private ActionBarDrawerToggle drawerToggle;

    private double searchRange;
    private int numberOfCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        initiateInstance();
        googleApiClient.connect();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fromYou) {
            getCarParkData(currentLocation.latitude, currentLocation.longitude);
        } else {
            getCarParkData(placeLocation.latitude, placeLocation.longitude);
        }
    }

    private void initiateInstance() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_user_map);
        mapFragment.getMapAsync(this);

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.fragment_user_autocomplete_search);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latLng = place.getLatLng();
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM_TO));
                fromYou = false;
                placeName = place.getName().toString();
                placeLocation = latLng;
            }

            @Override
            public void onError(Status status) {
            }
        });

        markerMap = new HashMap<>();
        centerLocation = new LatLng(0, 0);
        currentLocation = new LatLng(0, 0);
        placeLocation = new LatLng(0, 0);
        fromYou = true;

        // Create an instance of GoogleAPIClient.
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // Drawer
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarProgress = (ProgressBar) findViewById(R.id.toolbar_progress);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_user);
        navigationMenu = (NavigationView) findViewById(R.id.navigationview_user);
        setupDrawerContent();
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        View header = navigationMenu.getHeaderView(0);
        TextView tvUsername = (TextView) header.findViewById(R.id.textview_header_user_username);
        Account account = AccountHelper.get(this);
        if (account != null) {
            tvUsername.setText(account.getUsername());
        } else {
            tvUsername.setText(R.string.user_guest);
        }

        searchRange = 2;
        numberOfCar = 10;
    }

    private void setupDrawerContent() {
        navigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onDrawerItemClick(item);
                return true;
            }
        });
    }

    private void onDrawerItemClick(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_user_map_view:
                // TODO: navigation drawer map view click
                Toast.makeText(this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_user_list_view:
                if (fromYou) {
                    intent = CarParkListActivity.createIntent(this, currentLocation, "bạn");
                } else {
                    intent = CarParkListActivity.createIntent(this, placeLocation, placeName);
                }
                startActivity(intent);
                break;
            case R.id.nav_user_reserved_list:
                intent = new Intent(this, TransactionActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_user_about_us:
                // TODO: navigation drawer about us click
                Toast.makeText(this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_user_share:
                // TODO: navigation drawer share click
                Toast.makeText(this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_user_rate:
                // TODO: navigation drawer rate click
                Toast.makeText(this, item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_user_log_out:
                intent = new Intent(this, LoginActivity.class);
                AccountHelper.clear(this);
                startActivity(intent);
                finish();
                break;
            default:
        }
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onStart() {
//        googleApiClient.connect();
        initiatePubnub();
        super.onStart();
    }

    @Override
    protected void onStop() {
//        googleApiClient.disconnect();
        pubNub.unsubscribeAll();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initiatePubnub() {
        pubNub = PubNubHelper.getPubNub();

        pubNub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, final PNMessageResult message) {
                String channel = message.getChannel();
                if (channel.equals(PubNubHelper.CHANNEL_REALTIME_MAP)) {
                    JsonElement json = message.getMessage();
                    Gson gson = new Gson();
                    final RealtimeMapPackage mapPackage = gson.fromJson(json, RealtimeMapPackage.class);
                    Integer id = mapPackage.getId();
                    if (markerMap.containsKey(id)) {
                        final Marker marker = markerMap.get(id);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CarPark carPark = (CarPark) marker.getTag();
                                carPark.setAvailableLot(mapPackage.getAvailableLot());
                                marker.setIcon(BitmapDescriptorFactory
                                        .fromBitmap(MapMarkerHelper
                                                .getParkingMarker(getBaseContext(),
                                                        mapPackage.getAvailableLot())));
                            }
                        });
                    }
                } else if (channel.equals(PubNubHelper.CHANNEL_NOTIFICATION)) {
                    JsonElement json = message.getMessage();
                    Gson gson = new Gson();
                    Account account = AccountHelper.get(UserActivity.this);
                    NotificationPackage notiPackage = gson.fromJson(json, NotificationPackage.class);
                    if (account.getUsername().equals(notiPackage.getUsername())) {
                        NotificationManager notiManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification notification = new Notification.Builder(UserActivity.this)
                                .setContentTitle("Reserve Parking Lot of " +
                                        notiPackage.getCarParkName())
                                .setSmallIcon(R.drawable.user_nav_select_range_icon)
                                .setContentText("Your reserved parking lot is " +
                                        notiPackage.getLotName())
                                .build();
                        notiManager.notify(0, notification);
                    }
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
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (ActivityCompat.checkSelfPermission(UserActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(UserActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return true;
                }
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                currentLocation = latLng;
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM_TO));
                fromYou = true;
                return true;
            }
        });
        map.getUiSettings().setCompassEnabled(true);
        map.setInfoWindowAdapter(new UserInfoWindowAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // TODO: show car park detail page
                marker.hideInfoWindow();
                CarPark carPark = (CarPark) marker.getTag();
                Intent intent = CarParkDetailActivity.createIntent(UserActivity.this, carPark);
                startActivity(intent);
            }
        });
        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int reason) {
                refreshCarParkData = reason != GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION;
            }
        });
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = map.getCameraPosition().target;
                if (latLng.longitude != centerLocation.longitude
                        && latLng.latitude != centerLocation.latitude
                        && refreshCarParkData) {
                    centerLocation = latLng;
                    getCarParkData(centerLocation.latitude, centerLocation.longitude);
                }
            }
        });
    }

    /***
     * This callback is triggered when the google api client is ready to be used.
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("UserActivity", "onConnected");
        map.clear();
        locationSettingRequest();
        if (centerLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLng(centerLocation));
            map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM_TO));
        }
    }

    private void getCurrentLocation() {
        Log.i("UserActivity", "getCurrentLocation");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = null;
        // When first start location and connect to google api client, it will need sometime to
        // get the location data
        while (location == null) {
            // TODO: display progess bar
            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        currentLocation = latLng;
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(MAP_ZOOM_TO));
    }

    private void getCarParkData(final double lat, final double lon) {
        if (!toolbarProgress.isShown()) {
            toolbarProgress.setVisibility(View.VISIBLE);
        }
        CarParkClient client = ServiceGenerator.createService(CarParkClient.class);
        Call<GetCoordinatePackage> call =
                client.getCoordinateNearestCarParkByRange(lat, lon, numberOfCar, searchRange);
        call.enqueue(new Callback<GetCoordinatePackage>() {
            @Override
            public void onResponse(final Call<GetCoordinatePackage> call,
                                   Response<GetCoordinatePackage> response) {
                if (toolbarProgress.isShown()) {
                    toolbarProgress.setVisibility(View.INVISIBLE);
                }
                markerMap.clear();
                map.clear();
                List<CarParkAdvancePackage> list = response.body().getResult();
                for (CarParkAdvancePackage data : list) {
                    final Geo geo = data.getGeo();
                    final CarPark carPark = data.getCarPark();
                    carPark.setAvailableLot(data.getAvailableLot());
                    if (fromYou) {
                        float[] results = new float[3];
                        Location.distanceBetween(currentLocation.latitude, currentLocation.longitude,
                                geo.getLatitude(), geo.getLongitude(), results);
                        carPark.setAwayDistance(results[0]);
                        carPark.setFromTarget("bạn");
                    } else {
                        float[] results = new float[3];
                        Location.distanceBetween(placeLocation.latitude, placeLocation.longitude,
                                geo.getLatitude(), geo.getLongitude(), results);
                        carPark.setAwayDistance(results[0]);
                        carPark.setFromTarget(placeName);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LatLng latLng = new LatLng(geo.getLatitude(), geo.getLongitude());
                            Marker marker = map.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(carPark.getName()));
                            marker.setIcon(BitmapDescriptorFactory
                                    .fromBitmap(MapMarkerHelper
                                            .getParkingMarker(getBaseContext(), carPark.getAvailableLot())));
                            marker.setTag(carPark);
                            markerMap.put(carPark.getId(), marker);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GetCoordinatePackage> call, Throwable t) {
                getCarParkData(lat, lon);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("UserActivity", "onActivityResult");
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                Log.i("UserActivity", String.valueOf(resultCode));
                switch (resultCode) {
                    case RESULT_OK:
                        getCurrentLocation();
                        break;
                    case RESULT_CANCELED:
                        // keep asking
                        break;
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void locationSettingRequest() {
        Log.i("UserActivity", "locationSettingRequest");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(this.googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                Status status = result.getStatus();
                LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getCurrentLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    UserActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }
}
