package com.example.fitvision;

import static com.example.fitvision.R.id.tvverifyrecentotp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitvision.Common.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class VerifyOTPActivity extends AppCompatActivity {
    EditText inputnumber1, inputnumber2, inputnumber3, inputnumber4, inputnumber5, inputnumber6;
    Button btverifynow;
    TextView tvmobileno;

    ProgressDialog progressDialog;
    TextView tvresendotp;
    private String strvarificationcode, strname, strmobileno, stremail, strusername, strpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactivity);

        tvmobileno = findViewById(R.id.textmobileshownuber);
        inputnumber1 = findViewById(R.id.inputotp1);
        inputnumber2 = findViewById(R.id.inputotp2);
        inputnumber3 = findViewById(R.id.inputotp3);
        inputnumber4 = findViewById(R.id.inputotp4);
        inputnumber5 = findViewById(R.id.inputotp5);
        inputnumber6 = findViewById(R.id.inputotp6);
        btverifynow = findViewById(R.id.btverifynow);
        tvresendotp = findViewById(R.id.tvverifyrecentotp);


        strvarificationcode = getIntent().getStringExtra("verificationcode");
        strname = getIntent().getStringExtra("name");
        strmobileno = getIntent().getStringExtra("mobileno");
        stremail = getIntent().getStringExtra("emailid");
        strusername = getIntent().getStringExtra("username");
        strpassword = getIntent().getStringExtra("password");

        tvmobileno.setText(strmobileno);

        btverifynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputnumber1.getText().toString().trim().isEmpty() ||
                        inputnumber2.getText().toString().trim().isEmpty() ||
                        inputnumber3.getText().toString().trim().isEmpty() ||
                        inputnumber4.getText().toString().trim().isEmpty() ||
                        inputnumber5.getText().toString().trim().isEmpty() ||
                        inputnumber6.getText().toString().trim().isEmpty()) ;

                {
                    Toast.makeText(VerifyOTPActivity.this, "Verification Failed..", Toast.LENGTH_SHORT);
                }

                String otpcode = inputnumber1.getText().toString() +
                        inputnumber2.getText().toString() +
                        inputnumber3.getText().toString() +
                        inputnumber4.getText().toString() +
                        inputnumber5.getText().toString() +
                        inputnumber6.getText().toString();

                if (strvarificationcode != null) {

                    progressDialog = new ProgressDialog(VerifyOTPActivity.this);
                    progressDialog.setTitle("Verifying otp");
                    progressDialog.setMessage("Please wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();


                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            strvarificationcode,
                            otpcode);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        UserRegisterDetails();

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(VerifyOTPActivity.this, "Enter the Correct Otp", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });


        tvresendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + strmobileno,
                        30, TimeUnit.SECONDS,
                        VerifyOTPActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "Verification Successfully Done✅🎉", Toast.LENGTH_SHORT);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "Verification Failed..", Toast.LENGTH_SHORT);

                            }

                            @Override
                            public void onCodeSent(@NonNull String newverificationcode, @NonNull
                            PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                strvarificationcode = newverificationcode;

                            }
                        }

                );
            }
        });

        numberotpmove();

    }


    private void UserRegisterDetails() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", strname);
        params.put("mobileno", strmobileno);
        params.put("emailid", stremail);
        params.put("username", strusername);
        params.put("password", strpassword);


        client.post(Urls.registerUserWebService
                , params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("success");
                            if (status.equals("1")) {
                                progressDialog.dismiss();
                                Toast.makeText(VerifyOTPActivity.this, "Registration Successfully Done✅🎉", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                                startActivity(intent);


                            } else {

                                Toast.makeText(VerifyOTPActivity.this, "Already Data Present😔", Toast.LENGTH_SHORT);
                                progressDialog.dismiss();
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(VerifyOTPActivity.this, "Server error", Toast.LENGTH_SHORT);
                        progressDialog.dismiss();


                    }
                }


        );


    }

    private void numberotpmove() {

        inputnumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputnumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputnumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputnumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputnumber5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    inputnumber6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}












