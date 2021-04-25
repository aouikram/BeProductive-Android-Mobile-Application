package com.example.main.match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.main.MainActivity;
import com.example.main.R;

public class LonelyUser extends AppCompatActivity {
    private Button continue_button , back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lonely_user);

        continue_button = (Button) findViewById(R.id.continue_button);
        back = (Button) findViewById(R.id.back);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LonelyUser.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LonelyUser.this, ChooseGoal.class);
                startActivity(intent);
            }
        });
    }
}