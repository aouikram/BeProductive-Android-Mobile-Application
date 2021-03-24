package com.example.main;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

    public class WelcomeActivity extends AppCompatActivity {
        Animation animation , animationtext;
        ImageView image;
        TextView logo;
        private static int SCREEN = 4000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_welcome);
            animation = AnimationUtils.loadAnimation(this,R.anim.animation);
            animationtext = AnimationUtils.loadAnimation(this,R.anim.animationtext1);

            image = (ImageView) findViewById(R.id.logo);
            logo = (TextView) findViewById(R.id.textView);
            image.setAnimation(animation);
            logo.setAnimation(animationtext);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this,LogSignUp.class);
                    startActivity(intent);
                    finish();
                }
            },SCREEN);



        }
    }

