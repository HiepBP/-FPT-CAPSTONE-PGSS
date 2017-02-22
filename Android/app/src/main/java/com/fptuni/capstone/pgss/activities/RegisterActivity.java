package com.fptuni.capstone.pgss.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.edittext_register_email)
    EditText etEmail;
    @BindView(R.id.edittext_register_password)
    EditText etPassword;
    @BindView(R.id.edittext_register_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.edittext_register_username)
    EditText etUsername;
    @BindView(R.id.edittext_register_fullname)
    EditText etFullname;
    @BindView(R.id.button_register)
    Button btnRegister;
    @BindView(R.id.textview_register_login)
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_register)
    void onRegisterButtonClick(View view) {
        Toast.makeText(this, "On register click", Toast.LENGTH_SHORT).show();
        // TODO: register click
    }

    @OnClick(R.id.textview_register_login)
    void onLoginTextClick(View view) {
        // return to login activiy
        finish();
    }
}
