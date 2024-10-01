package com.example.fitvision;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitvision.Common.Urls;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SetUpNewPasswordActivity extends AppCompatActivity {
String strmobileno;
EditText etnewpassword,etconfirmpassword;
AppCompatButton btsetuppassword;
ProgressDialog progressDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_new_password);

        etnewpassword=findViewById(R.id.etsetupnewpasswordNewpassword);
        etconfirmpassword=findViewById(R.id.etsetupnewpasswordConfirmpassword);
        btsetuppassword=findViewById(R.id.btsetupnewpassword);


        strmobileno=getIntent().getStringExtra("mobile");
        Toast.makeText(this, strmobileno, Toast.LENGTH_SHORT).show();

        btsetuppassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etnewpassword.getText().toString().isEmpty()|| etconfirmpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(SetUpNewPasswordActivity.this, "Please enter New or Confirm Password", Toast.LENGTH_SHORT).show();

                } else if (!etnewpassword.getText().toString().equals(etconfirmpassword.getText().toString()))
                {
                    etconfirmpassword.setError("Password did not match");
                }
                else {

                    progressDialog=new ProgressDialog(SetUpNewPasswordActivity.this);
                    progressDialog.setTitle("Password is Updating");
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    forgetPassword();
                }
            }
        });



    }

    private void forgetPassword() {

        AsyncHttpClient client=new AsyncHttpClient();//client server communcation

        RequestParams params=new RequestParams();//put the data
        params.put("mobileno",strmobileno);
        params.put("password",etnewpassword.getText().toString());

        client.post(Urls.forgetPasswordWebService,params,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            String status = response.getString("success");

                            if (status.equals("1"))
                            {
                                Toast.makeText(SetUpNewPasswordActivity.this, "New Password Set up Successfully ", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(SetUpNewPasswordActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(SetUpNewPasswordActivity.this, "Password is not changed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        Toast.makeText(SetUpNewPasswordActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }
}