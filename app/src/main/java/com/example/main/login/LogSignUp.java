package com.example.main.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.main.R;

public class LogSignUp extends AppCompatActivity {
    private Button joinNow, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginsignup);

        joinNow = (Button) findViewById(R.id.join_now);
        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogSignUp.this, com.example.main.login.login.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        joinNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogSignUp.this, Register.class);
                startActivity(intent);
            }
        });
    }
}
