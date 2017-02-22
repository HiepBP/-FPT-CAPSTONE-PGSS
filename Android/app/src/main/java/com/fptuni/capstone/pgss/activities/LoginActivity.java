package com.fptuni.capstone.pgss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edittext_login_username)
    EditText etUsername;
    @BindView(R.id.edittext_login_password)
    EditText etPassword;
    @BindView(R.id.button_login)
    Button btnSignIn;
    @BindView(R.id.textview_login_register)
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_login)
    void onLoginButtonClick(View view) {
        Toast.makeText(this, "Login button click", Toast.LENGTH_SHORT).show();
        // TODO: login process
    }

    @OnClick(R.id.textview_login_register)
    void onRegisterTextClick(View view) {
        // Call Register Activity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}

