package com.example.fitvision;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class splashActivity extends AppCompatActivity {
    ImageView ivlogo;
    TextView tvtitle;
    Animation animtranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_splash);

        ivlogo = findViewById(R.id.ivMainlogo);
        tvtitle = findViewById(R.id.tvsplashtitle);

        animtranslate = AnimationUtils.loadAnimation(splashActivity.this, R.anim.toptobottomtranslate);
        ivlogo.setAnimation(animtranslate);
        tvtitle.setAnimation(animtranslate);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1500);
    }
}
