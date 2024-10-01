package com.example.fitvision;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.example.fitvision.Common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {
    ImageView ivprofilephoto;

    TextView tvname,tvmobileno,tvEmailid,tvusername;
    AppCompatButton btnsignout,btneditprofile;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    SharedPreferences preferences;
    String strusername;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        preferences= PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);
        strusername =preferences.getString("username","");

        ivprofilephoto=findViewById(R.id.myprofileimage);
        tvname=findViewById(R.id.tvmyprofilename);
        tvEmailid=findViewById(R.id.tvmyprofileemailid);
        tvmobileno=findViewById(R.id.tvmyprofilemobilenumber);
        tvusername=findViewById(R.id.tvmyprofileusername);
        btnsignout=findViewById(R.id.btsignout);
        btneditprofile=findViewById(R.id.btnmyprofileditProfile);

         googleSignInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
         googleSignInClient = GoogleSignIn.getClient(MyProfileActivity.this, googleSignInOptions);

        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if (googleSignInAccount!=null)
        {
            String name =googleSignInAccount.getDisplayName();
            String email =googleSignInAccount.getEmail();

            tvname.setText(name);
            tvEmailid.setText(email);

            btnsignout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(MyProfileActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onStart() {
        super.onStart();
      progressDialog =new ProgressDialog(MyProfileActivity.this);
      progressDialog.setTitle("My Profile");
      progressDialog.setMessage("Please Wait...");
      progressDialog.setCanceledOnTouchOutside(true);
      progressDialog.show();

      getMyDetails();
    }

    private void getMyDetails() {
        AsyncHttpClient client =new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",strusername);
        client.post(Urls.myDetailsWebservices,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray=response.getJSONArray("getMyDetails");

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject= jsonArray.getJSONObject(i);
                                String strid =jsonObject.getString("id");
                                String strimage=jsonObject.getString(("image"));
                                String strname =jsonObject.getString("name");
                                String strmobileno=jsonObject.getString("mobileno");
                                String stremailid=jsonObject.getString("emailid");
                                String strusername=jsonObject.getString("username");

                                tvname.setText(strname);
                                tvmobileno.setText(strmobileno);
                                tvEmailid.setText(stremailid);
                                tvusername.setText(strusername);

                                Glide.with(MyProfileActivity.this)
                                        .load(Urls.WebServiceAddress+strimage)
                                       //if you get any issue write path till image..
                                        .skipMemoryCache(true)
                                        .error(R.drawable.imagenotfound)
                                        .into(ivprofilephoto);

                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(MyProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }


        );
        
    }
}