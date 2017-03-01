package com.fptuni.capstone.pgss.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarParkDetailActivity extends AppCompatActivity {

    private static final String EXTRA_CAR_PARK = "carPark";

    private Account account;

    @BindView(R.id.textview_carparkdetail_name)
    TextView tvName;
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
    @BindView(R.id.button_carparkdetail_save)
    Button btnSave;
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
    }

    private void initiateViews() {
        ButterKnife.bind(this);
        tvName.setText(carPark.getName());
        tvAddress.setText(carPark.getAddress());
        tvPhone.setText(carPark.getPhone());
        tvEmail.setText(carPark.getEmail());
        tvDescription.setText(carPark.getDescription());

        if (account == null) {
            btnReserve.setClickable(false);
            btnReserve.setAlpha(0.3f);
        }
    }

    private void initiateFields() {
        carPark = (CarPark) getIntent().getSerializableExtra(EXTRA_CAR_PARK);
        account = AccountHelper.get(this);
    }

    @OnClick(R.id.button_carparkdetail_call)
    protected void onCallButtonClick(View view) {
        // TODO: on call button click
        Toast.makeText(this, "Call button click", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_carparkdetail_save)
    protected void onSaveButtonClick(View view) {
        // TODO: on save button click
        Toast.makeText(this, "Save button click", Toast.LENGTH_SHORT).show();
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
}
