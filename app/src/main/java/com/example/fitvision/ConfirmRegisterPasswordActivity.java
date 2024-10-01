package com.example.fitvision;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.concurrent.TimeUnit;

public class ConfirmRegisterPasswordActivity extends AppCompatActivity {
EditText etConfirmRegisterMobileno;
AppCompatButton buttonConfirmverify;
ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_register_password);

        etConfirmRegisterMobileno=findViewById(R.id.etConfirmMobilenoverify);
        buttonConfirmverify=findViewById(R.id.ConfirmRegisterMobilenobtn);



        buttonConfirmverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etConfirmRegisterMobileno.getText().toString().isEmpty())
                {
                    etConfirmRegisterMobileno.setError("Please Enter Your Mobile number:");
                } else if (etConfirmRegisterMobileno.getText().toString().length()!=10) {

                    etConfirmRegisterMobileno.setError("Please Enter Valid Mobile number");
                }
                else
                {

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" +etConfirmRegisterMobileno.getText().toString(),
                            30, TimeUnit.SECONDS,
                            ConfirmRegisterPasswordActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterPasswordActivity.this,"Verification Successfully Done✅🎉",Toast.LENGTH_SHORT);


                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterPasswordActivity.this,"Verification Failed..",Toast.LENGTH_SHORT);

                                }

                                @Override
                                public void onCodeSent(@NonNull String verificationcode, @NonNull
                                PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                    Intent intent= new Intent(ConfirmRegisterPasswordActivity.this,ForgotpasswordVerifyOtpActivity.class);
                                    intent.putExtra("verificationcode",verificationcode);
                                    intent.putExtra("mobileno",etConfirmRegisterMobileno.getText().toString());
                                    startActivity(intent);

                                }
                            }
                    );
                }
            }
        });



    }
}