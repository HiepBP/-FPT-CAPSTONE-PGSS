package com.fptuni.capstone.pgss.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.interfaces.AccountClient;
import com.fptuni.capstone.pgss.models.Account;
import com.fptuni.capstone.pgss.network.AccountPackage;
import com.fptuni.capstone.pgss.network.ServiceGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();
        String username = etUsername.getText().toString();
        String fullname = etFullname.getText().toString();

        if (!validateEmail(email)) {
            return;
        }
        if (!validatePassword(password, confirmPassword)) {
            return;
        }

        Account account = new Account(username, password);
        account.setConfirmPassword(confirmPassword);
        account.setEmail(email);
        account.setFullname(fullname);
        AccountClient client = ServiceGenerator.createService(AccountClient.class);
        Call<AccountPackage> call = client.register(account);
        call.enqueue(new Callback<AccountPackage>() {
            @Override
            public void onResponse(Call<AccountPackage> call, Response<AccountPackage> response) {
                final AccountPackage result = response.body();
                if (result.isSuccess()) {
                    // finish create, back to Login Activity
                    finish();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<AccountPackage> call, final Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });
    }

    private boolean validateEmail(String email) {
        // TODO: validate email format
        return true;
    }

    private boolean validatePassword(String password, String confirmPassword) {
        // TODO: validate password
        return true;
    }

    @OnClick(R.id.textview_register_login)
    void onLoginTextClick(View view) {
        // return to login activity
        finish();
    }
}
