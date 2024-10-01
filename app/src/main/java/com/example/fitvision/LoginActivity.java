package com.example.fitvision;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fitvision.Common.NetworkChangeListener;
import com.example.fitvision.Common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {
    private ImageView ivlogo;
    private TextView tvtitle,tvForgotPassword, tvNewUser;
    private EditText etUsername, etPassword;
    Animation animtranslate;

    private AppCompatButton btLoginButton;
    private ProgressDialog progressDialog;

    private final NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    GoogleSignInOptions googleSignInOptions;   //show email option
    GoogleSignInClient googleSignInClient;    //selected option store

    AppCompatButton btnSigninWithGoogle;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();
        ivlogo = findViewById(R.id.IvLoginLogo);
        tvtitle = findViewById(R.id.tvMainTitle);
        tvNewUser = findViewById(R.id.tvloginNewUser);
        etUsername = findViewById(R.id.etusername);
        etPassword = findViewById(R.id.etPassword);

        btLoginButton = findViewById(R.id.btloginbutton);
        btnSigninWithGoogle=findViewById(R.id.btsignwithgoogle);
        tvForgotPassword = findViewById(R.id.tvloginforgotpassword);

        animtranslate = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.toptobottomtranslate);
        ivlogo.setAnimation(animtranslate);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);



        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ConfirmRegisterPasswordActivity.class);
                startActivity(intent);
            }
        });





        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);


        btnSigninWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        btLoginButton.setOnClickListener(v -> validateInputs());

        tvNewUser.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Registration_Activity.class);
            startActivity(intent);
        });
    }

    private void signIn() {

        Intent intent=new Intent(googleSignInClient.getSignInIntent());
        startActivityForResult(intent,999);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==999)
        {
            Task<GoogleSignInAccount> task =GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent intent=new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();

            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Something Went Wrong..😥", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }

    private void validateInputs() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty()) {
            etUsername.setError("Enter your username");
        } else if (password.isEmpty()) {
            etPassword.setError("Please enter your password");
        } else if (username.length() < 8) {
            etUsername.setError("Username must be at least 8 characters");
        } else if (password.length() < 8) {
            etPassword.setError("Password must be at least 8 characters");
        } else if (!username.matches(".*[A-Z].*") || !username.matches(".*[a-z].*") ||
                !username.matches(".*[0-9].*") || !username.matches(".*[=,$,#,@,!,_].*")) {
            etUsername.setError("Username must contain at least 1 uppercase, 1 lowercase, 1 number, and 1 special character");
        } else {
            // Proceed with login
            showProgressDialog();
            userLogin(username, password);
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }




    private void userLogin(String username, String password) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username", username);
        params.put("password", password);

        client.post(Urls.loginUserWebService, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                try {
                    String status = response.getString("success");
                    if ("1".equals(status)) {
                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        editor.putString("username",etUsername.getText().toString()).commit();
                        startActivity(intent);
                        finish();

                    } else
                    {
                        Toast.makeText(LoginActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e)
                {
                    Toast.makeText(LoginActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



}

