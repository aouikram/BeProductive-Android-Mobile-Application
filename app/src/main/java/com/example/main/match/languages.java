package com.example.main.match;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.main.R;
import com.example.main.match.Match;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class languages extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;
    private LinearLayout white_circle_1 , white_circle_2 , white_circle_3 , white_circle_4 , white_circle_japan , white_circle_port;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_language);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        white_circle_1 = findViewById(R.id.white_circle_1);
        white_circle_2 = findViewById(R.id.white_circle_2);
        white_circle_3 = findViewById(R.id.white_circle_3);
        white_circle_4 = findViewById(R.id.white_circle_4);
        white_circle_japan = findViewById(R.id.white_circle_japan);
        white_circle_port = findViewById(R.id.white_circle_port);

        white_circle_1.setOnClickListener(this);
        white_circle_2.setOnClickListener(this);
        white_circle_3.setOnClickListener(this);
        white_circle_4.setOnClickListener(this);
        white_circle_japan.setOnClickListener(this);
        white_circle_port.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent i ;
        switch(v.getId()) {

            case R.id.white_circle_1:
                usersDb.child("LanguageTable").child(currentUId).child("Type").setValue("chinese");
                Log.v("Tag" , "ttrbut coding set");
                i= new Intent(this, Match.class);
                startActivity(i);break;
            case R.id.white_circle_2 :
                usersDb.child("LanguageTable").child(currentUId).child("Type").setValue("french");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.white_circle_3 :
                usersDb.child("LanguageTable").child(currentUId).child("Type").setValue("spanish");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.white_circle_japan :
                usersDb.child("LanguageTable").child(currentUId).child("Type").setValue("japanese");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.white_circle_port :
                usersDb.child("LanguageTable").child(currentUId).child("Type").setValue("portuguese");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            default: break ; }
    }
}