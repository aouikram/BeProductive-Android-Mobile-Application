package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Skills extends AppCompatActivity implements View.OnClickListener {
    private Button CodingButton;
    private Button CommunicationButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        Log.v("Tag" , "authetification success ");
        CodingButton = findViewById(R.id.coding_button);
        CommunicationButton =  findViewById(R.id.communication_button);

        CodingButton.setOnClickListener(this);
        CommunicationButton.setOnClickListener(this);
        Log.v("Tag" , "listener for button set");
    }

    @Override
    public void onClick(View v) {
        Intent i ;
        switch(v.getId()) {
            case R.id.coding_button:
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("coding");
                Log.v("Tag" , "ttrbut coding set");
                i= new Intent(this,Match.class);
                startActivity(i);break;
            case R.id.communication_button :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("communication");
                i= new Intent(this, Match.class);
                startActivity(i);break;
            default: break ; }
    }
}

