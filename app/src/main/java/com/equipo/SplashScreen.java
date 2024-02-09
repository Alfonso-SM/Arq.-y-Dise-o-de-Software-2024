package com.equipo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;




public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 1000;
    Animation bottomAnim,topAnim;
    TextView logo_text,first_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        //hooks
        logo_text = findViewById(R.id.text_Splash);
        logo_text.setAnimation(bottomAnim);
        first_text = findViewById(R.id.text_TOP);
        first_text.setAnimation(topAnim);
        new Handler().postDelayed(() ->{
                Intent intent;
                intent = new Intent(SplashScreen.this, Log_in.class);
                startActivity(intent);
                finish();
        }, SPLASH_SCREEN);
    }
}