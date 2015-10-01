package com.iandp.happy.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.iandp.happy.R;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_SHOW_TIME = (int) (1 * 1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(intent);

                finish();
            }
        }, SPLASH_SHOW_TIME);
    }
}
