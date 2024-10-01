package com.example.fitvision;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    boolean doubletap = false;
    BottomNavigationView bottomNavigationView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
        editor = preferences.edit();

    bottomNavigationView =findViewById(R.id.bottom_nav_view);
    bottomNavigationView.setOnNavigationItemSelectedListener(this);
    bottomNavigationView.setSelectedItemId(R.id.navhome);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.home_top_menu,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.homemenumyoffer)
        {

        } else if (item.getItemId() == R.id.homemenumycart)
        {

        }  else if (item.getItemId()==R.id.homemenumyoffer)
        {
            Intent intent=new Intent(HomeActivity.this,MyProfileActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.menu_home_logout) {
            logout();
        }
        return true;
    }

    private void logout() {
        AlertDialog.Builder ad = new AlertDialog.Builder(HomeActivity.this);
        ad.setTitle("FITVISION");
        ad.setMessage("Are you sure you want to logout");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                editor.putBoolean("isLogin", false).commit();
                startActivity(i);
            }
        }).create().show();

    }


    HomeFragment homeFragment =new HomeFragment();
    SearchFragment searchFragment= new SearchFragment();
    MusicFragment musicFragment= new MusicFragment();
    DietFragment dietFragment=new DietFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.navhome)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,homeFragment).commit();
        }
        else if (item.getItemId()==R.id.navSerach)
        {
           getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,searchFragment).commit();
        }
        else if (item.getItemId()==R.id.navmusic)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,musicFragment).commit();
        }
        else if (item.getItemId()==R.id.navdiet)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.homeframelayout,dietFragment).commit();
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        if (doubletap) {
            finishAffinity();

        } else {
            Toast.makeText(HomeActivity.this, "Press Again to Exit", Toast.LENGTH_SHORT).show();
            doubletap = true;

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {

                    doubletap = false;
                }
            }, 2000);
        }

    }

}