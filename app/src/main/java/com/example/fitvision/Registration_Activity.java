package com.example.fitvision;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class Registration_Activity extends AppCompatActivity {
    private EditText etname;
    private EditText etmobile_no;
    private EditText etemail;
    private EditText etusername;
    private EditText etpassword;
    private AppCompatButton registerbutton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private ImageView ivlogo;
    ProgressDialog progressDialog;
    Animation animtranslate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
                setTitle("Registration activity");


                preferences = PreferenceManager.getDefaultSharedPreferences(Registration_Activity.this);
                editor =preferences.edit();

                ivlogo = findViewById(R.id.IvregisterLogo);
                etname = findViewById(R.id.etname);
                etmobile_no = findViewById(R.id.etmobile_no);
                etemail = findViewById(R.id.etemailid);
                etusername = findViewById(R.id.etusername);
                etpassword = findViewById(R.id.etPassword);
                registerbutton = findViewById(R.id.registerbutton);


        animtranslate = AnimationUtils.loadAnimation(Registration_Activity.this, R.anim.toptobottomtranslate);
        ivlogo.setAnimation(animtranslate);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);

                registerbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etname.getText().toString().isEmpty()) {
                            etname.setError("Enter your Name");
                        } else if (etname.getText().toString().length() < 8) {
                            etname.setError("Please enter a name with at least 8 characters");
                        } else if (!etname.getText().toString().matches(".*[A-Z].*")) {
                            etname.setError("Please enter at least 1 uppercase letter");
                        } else if (!etname.getText().toString().matches(".*[a-z].*")) {
                            etname.setError("Please enter at least 1 lowercase letter");
                        } else if (etmobile_no.getText().toString().isEmpty()) {
                            etmobile_no.setError("Enter your mobile number");
                        } else if (etmobile_no.getText().toString().length() < 10) {
                            etmobile_no.setError("Please enter a 10-digit number");
                        } else if (etemail.getText().toString().isEmpty()) {
                            etemail.setError("Enter your email");
                        } else if (!etemail.getText().toString().matches(".*[@].*")) {
                            etemail.setError("Email must contain @");
                        } else if (etusername.getText().toString().isEmpty()) {
                            etusername.setError("Enter your username");
                        } else if (etusername.getText().toString().length() < 8) {
                            etusername.setError("Please enter a username with at least 8 characters");
                        } else if (!etusername.getText().toString().matches(".*[A-Z].*")) {
                            etusername.setError("Please enter at least 1 uppercase letter");
                        } else if (!etusername.getText().toString().matches(".*[a-z].*")) {
                            etusername.setError("Please enter at least 1 lowercase letter");
                        } else if (etpassword.getText().toString().isEmpty()) {
                            etpassword.setError("Enter your password");
                        } else if (etpassword.getText().toString().length() < 8) {
                            etpassword.setError("Please enter a password with at least 8 characters");
                        }
                        else
                        {
                            progressDialog=new ProgressDialog(Registration_Activity.this);
                            progressDialog.setTitle("Please wait..");
                            progressDialog.setMessage("Registration is in Process");
                            progressDialog.setCanceledOnTouchOutside(true);
                            progressDialog.show();

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + etmobile_no.getText().toString(),
                                    30, TimeUnit.SECONDS,
                                    Registration_Activity.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                           progressDialog.dismiss();
                                            Toast.makeText(Registration_Activity.this,"Verification Successfully Done✅🎉",Toast.LENGTH_SHORT);


                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Registration_Activity.this,"Verification Failed..",Toast.LENGTH_SHORT);

                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationcode, @NonNull
                                        PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                            Intent intent= new Intent(Registration_Activity.this,VerifyOTPActivity.class);
                                            intent.putExtra("verificationcode",verificationcode);
                                            intent.putExtra("name",etname.getText().toString());
                                            intent.putExtra("mobileno",etmobile_no.getText().toString());
                                            intent.putExtra("emailid",etemail.getText().toString());
                                            intent.putExtra("username",etusername.getText().toString());
                                            intent.putExtra("password",etpassword.getText().toString());
                                            startActivity(intent);

                                        }
                                    }
                            );




                        }
                    }

                });
            }
        }



