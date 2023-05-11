package com.example.growpro01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.growpro01.Activities.InicioSesionsActivity;
import com.example.growpro01.Activities.MainActivity;

public class SplashScreen extends Activity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, InicioSesionsActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}