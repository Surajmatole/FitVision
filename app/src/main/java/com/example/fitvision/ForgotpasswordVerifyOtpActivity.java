package com.example.fitvision;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class ForgotpasswordVerifyOtpActivity extends AppCompatActivity {
    EditText inputnumber1, inputnumber2, inputnumber3, inputnumber4, inputnumber5, inputnumber6;
    Button btverifynow;
    TextView tvmobileno,tvresendotp;
    ProgressDialog progressDialog;
    private String strvarificationcode, strmobileno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        strmobileno = getIntent().getStringExtra("mobileno");


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
                    Toast.makeText(ForgotpasswordVerifyOtpActivity.this, "Verification Failed..", Toast.LENGTH_SHORT);
                }

                String otpcode = inputnumber1.getText().toString() +
                        inputnumber2.getText().toString() +
                        inputnumber3.getText().toString() +
                        inputnumber4.getText().toString() +
                        inputnumber5.getText().toString() +
                        inputnumber6.getText().toString();

                if (strvarificationcode != null) {

                    progressDialog = new ProgressDialog(ForgotpasswordVerifyOtpActivity.this);
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
                                        Intent intent=new Intent(ForgotpasswordVerifyOtpActivity.this,SetUpNewPasswordActivity.class);
                                        intent.putExtra("mobile",strmobileno);
                                        startActivity(intent);

                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ForgotpasswordVerifyOtpActivity.this, "Enter the Correct Otp", Toast.LENGTH_SHORT).show();
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
                        ForgotpasswordVerifyOtpActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotpasswordVerifyOtpActivity.this, "Verification Successfully Done✅🎉", Toast.LENGTH_SHORT);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotpasswordVerifyOtpActivity.this, "Verification Failed..", Toast.LENGTH_SHORT);

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