package com.example.main.match;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.main.R;
import com.example.main.match.Match;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Health extends AppCompatActivity implements View.OnClickListener  {
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;
    private CardView LWeight , GWeight , EatHealthy , Exercise ,Support , Anxiety , Care, Problem ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        LWeight = findViewById(R.id.LWeight);
        GWeight = findViewById(R.id.GWeight);
        EatHealthy = findViewById(R.id.EatHealthy);
        Exercise = findViewById(R.id.Exercise);
        Support = findViewById(R.id.Support);
        Anxiety = findViewById(R.id.Anxiety);
        Care = findViewById(R.id.Care);
        Problem = findViewById(R.id.Problem);

        LWeight.setOnClickListener(this);
        GWeight.setOnClickListener(this);
        EatHealthy.setOnClickListener(this);
        Exercise.setOnClickListener(this);
        Support.setOnClickListener(this);
        Anxiety.setOnClickListener(this);
        Care.setOnClickListener(this);
        Problem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i ;
        switch(v.getId()) {

            case R.id.LWeight:
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("loose weight");
                Log.v("Tag" , "ttrbut coding set");
                i= new Intent(this, Match.class);
                startActivity(i);break;
            case R.id.GWeight :
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("gain weight");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.EatHealthy :
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("eat healthy");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.Exercise :
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("exercise");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.Support:
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("mental support");
                Log.v("Tag" , "ttrbut coding set");
                i= new Intent(this,Match.class);
                startActivity(i);break;

            case R.id.Anxiety:
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("anxiety");
                Log.v("Tag" , "ttrbut coding set");
                i= new Intent(this,Match.class);
                startActivity(i);break;

            case R.id.Care :
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("self-care");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.Problem :
                usersDb.child("HealthTable").child(currentUId).child("Type").setValue("problem solving ");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            default: break ; }

    }
}