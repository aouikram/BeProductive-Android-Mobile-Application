package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Skills extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;
    private ImageView BackendIv,MarketingIV,EditingIv,AnimationIV,CommunicationIv,FrontendIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_skill);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        Log.v("Tag" , "authetification success ");

        BackendIv = findViewById(R.id.BackendIV);
        FrontendIv = findViewById(R.id.FrontendIv);
        MarketingIV = findViewById(R.id.MarketingIV);
        EditingIv = findViewById(R.id.EditingIv);
        AnimationIV = findViewById(R.id.AnimationIV);
        CommunicationIv = findViewById(R.id.CommunicationIv);

        BackendIv.setOnClickListener(this);
        MarketingIV.setOnClickListener(this);
        EditingIv.setOnClickListener(this);
        AnimationIV.setOnClickListener(this);
        CommunicationIv.setOnClickListener(this);
        FrontendIv.setOnClickListener(this);

        Log.v("Tag" , "listener for button set");
    }

    @Override
    public void onClick(View v) {
        Intent i ;
        switch(v.getId()) {

            case R.id.BackendIV:
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("backend");
                Log.v("Tag" , "ttrbut coding set");
                i= new Intent(this,Match.class);
                startActivity(i);break;
            case R.id.FrontendIv :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("frontend");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.MarketingIV :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("marketing");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.EditingIv :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("editing");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.AnimationIV :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("animation");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.CommunicationIv :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("communication");
                i= new Intent(this, Match.class);
                startActivity(i);break;
            default: break ; }
    }
}

