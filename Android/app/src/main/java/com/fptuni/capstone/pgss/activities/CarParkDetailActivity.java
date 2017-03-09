package com.fptuni.capstone.pgss.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
import com.fptuni.capstone.pgss.helpers.PubNubHelper;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.models.CarPark;
import com.fptuni.capstone.pgss.network.ControlPubnubPackage;
import com.fptuni.capstone.pgss.network.MobilePubnubPackage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarParkDetailActivity extends AppCompatActivity {

    private static final String EXTRA_CAR_PARK = "carPark";

    private Account account;
    private PubNub pubNub;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textview_carparkdetail_name)
    TextView tvName;
    @BindView(R.id.textview_carparkdetail_distance)
    TextView tvDistanceAway;
    @BindView(R.id.textview_carparkdetail_available_lot)
    TextView tvAvailableLot;
    @BindView(R.id.textview_carparkdetail_address)
    TextView tvAddress;
    @BindView(R.id.textview_carparkdetail_phone)
    TextView tvPhone;
    @BindView(R.id.textview_carparkdetail_email)
    TextView tvEmail;
    @BindView(R.id.textview_carparkdetail_description)
    TextView tvDescription;
    @BindView(R.id.button_carparkdetail_call)
    Button btnCall;
    @BindView(R.id.button_carparkdetail_route)
    Button btnRoute;
    @BindView(R.id.button_carparkdetail_reserve)
    Button btnReserve;

    private CarPark carPark;

    public static Intent createIntent(Context context, @NonNull CarPark carPark) {
        Intent intent = new Intent(context, CarParkDetailActivity.class);
        intent.putExtra(EXTRA_CAR_PARK, carPark);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_park_detail);

        initiateFields();
        initiateViews();
        initiatePubnub();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarBackground();
        tvName.setText(carPark.getName());
        String availableText = getString(R.string.carparkdetail_text_available)
                + String.valueOf(carPark.getAvailableLot());
        tvDistanceAway.setText(getDistanceString(carPark.getAwayDistance()));
        tvAvailableLot.setText(availableText);
        tvAddress.setText(carPark.getAddress());
        tvPhone.setText(carPark.getPhone());
        tvEmail.setText(carPark.getEmail());
        tvDescription.setText(carPark.getDescription());

        if (account == null) {
            btnReserve.setClickable(false);
            btnReserve.setAlpha(0.3f);
        }
    }

    private void setToolbarBackground() {
        int availableLot = carPark.getAvailableLot();
        int color;
        if (isBetween(availableLot, 0, 0)) {
            color = getResources().getColor(R.color.colorShortAvailable);
        } else if (isBetween(availableLot, 1, 10)) {
            color = getResources().getColor(R.color.colorAverageAvailable);
        } else {
            color = getResources().getColor(R.color.colorPlentifulAvailable);
        }
        toolbar.setBackgroundColor(color);
    }


    private void initiateFields() {
        carPark = (CarPark) getIntent().getSerializableExtra(EXTRA_CAR_PARK);
        account = AccountHelper.get(this);
    }

    @OnClick(R.id.button_carparkdetail_call)
    protected void onCallButtonClick(View view) {
        String uri = "tel:" + carPark.getPhone();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(Uri.decode(uri)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @OnClick(R.id.button_carparkdetail_route)
    protected void onRouteButtonClick(View view) {
        String uri = "google.navigation:q=" +
                carPark.getLat() + "," +
                carPark.getLon();
        Uri directionUri = Uri.parse(Uri.decode(uri));
        Intent intent = new Intent(Intent.ACTION_VIEW, directionUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    @OnClick(R.id.button_carparkdetail_reserve)
    protected void onReserveButtonClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.carparkdetail_reserve_dialog_title)
                .setMessage(R.string.carparkdetail_reserve_dialog_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reserveParkingLot();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    private void reserveParkingLot() {
        PubNub pubNub = PubNubHelper.getPubNub();
        ControlPubnubPackage message = new ControlPubnubPackage();
        message.setUsername(account.getUsername());
        message.setCommand("reserve");
        message.setDeviceName("Detector 1");
        message.setHubName(carPark.getName());
        pubNub.publish()
                .channel("control")
                .message(message)
                .usePOST(true)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (status.isError()) {
                            Toast.makeText(CarParkDetailActivity.this,
                                    R.string.carparkdetail_reserve_dialog_failed, Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(CarParkDetailActivity.this,
                                    R.string.carparkdetail_reserve_dialog_successful, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
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
                if (channel.equals("realtime map")) {
                    JsonObject json = message.getMessage().getAsJsonObject();
                    String hubName = json.get("hub_name").getAsString();
                    final int availableLot = json.get("available").getAsInt();
                    if (hubName.equals(carPark.getName())) {
                        if (availableLot == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnReserve.setClickable(false);
                                    btnReserve.setAlpha(0.3f);
                                    String availableText =
                                            getString(R.string.carparkdetail_text_available)
                                                    + String.valueOf(availableLot);
                                    tvAvailableLot.setText(availableText);
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btnReserve.setClickable(true);
                                    btnReserve.setAlpha(1f);
                                    String availableText =
                                            getString(R.string.carparkdetail_text_available)
                                                    + String.valueOf(availableLot);
                                    tvAvailableLot.setText(availableText);
                                }
                            });
                        }
                    }

                } else if (channel.equals("mobile")) {
                    String json = message.getMessage().toString();
                    Gson gson = new Gson();
                    MobilePubnubPackage data = gson.fromJson(json, MobilePubnubPackage.class);
                    if (data.getUsername().equals(account.getUsername())) {
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification notification = new Notification.Builder(CarParkDetailActivity.this)
                                .setContentTitle("Reserve Parking Lot of " + data.getHubName())
                                .setSmallIcon(R.drawable.user_nav_select_range_icon)
                                .setContentText("Your reserved lot is " + data.getLotName())
                                .build();
                        notificationManager.notify(0, notification);
                    }
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {

            }
        });
    }

    private boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private String getDistanceString(double distance) {
        DecimalFormat distanceInKmFormat = new DecimalFormat("#.##");
        return distanceInKmFormat.format(distance / 1000) + "Kms. away";
    }
}
