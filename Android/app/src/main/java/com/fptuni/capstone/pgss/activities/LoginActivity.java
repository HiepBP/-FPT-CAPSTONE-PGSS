package com.fptuni.capstone.pgss.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fptuni.capstone.pgss.R;
import com.fptuni.capstone.pgss.helpers.AccountHelper;
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
import timber.log.Timber;

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
    @BindView(R.id.textview_login_sign_in_guest)
    TextView tvSignInGuest;

    private MaterialDialog dialog;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.login_progress_title)
                .content(R.string.login_progress_content)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .build();
    }

    @OnClick(R.id.button_login)
    protected void onLoginButtonClick(View view) {
        dialog.show();
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        account = new Account(username, password);
        AccountClient client = ServiceGenerator.createService(AccountClient.class);
        Call<AccountPackage> call = client.login(account);
        call.enqueue(new Callback<AccountPackage>() {
            @Override
            public void onResponse(Call<AccountPackage> call, Response<AccountPackage> response) {
                dialog.dismiss();
                final AccountPackage result = response.body();
                if (result.isSuccess()) {
                    String role = result.getObjs().get(0);
                    account.setRole(role);
                    AccountHelper.save(LoginActivity.this, account);
                    Intent intent = null;
                    if (role.equals(Account.ROLE_MANAGER)) {
                        intent = new Intent(LoginActivity.this, ManagerActivity.class);
                    } else if (role.equals(Account.ROLE_USER)) {

                        intent = new Intent(LoginActivity.this, UserActivity.class);
                    }
                    startActivity(intent);
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
                dialog.dismiss();
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

    @OnClick(R.id.textview_login_register)
    protected void onRegisterTextClick(View view) {
        // Call Register Activity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.textview_login_sign_in_guest)
    protected void onSignInGuestTextClick(View view) {
        Intent intent = new Intent(LoginActivity.this, UserActivity.class);
        startActivity(intent);
        finish();
    }
}

