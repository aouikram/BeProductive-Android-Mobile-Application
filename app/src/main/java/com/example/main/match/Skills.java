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

public class Skills extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;
    private CardView BackendIv,MarketingIV,EditingIv,AnimationIV,DrawingIv,FrontendIv ,MachineLearningIV, WritingIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skills);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        Log.v("Tag" , "authetification success ");

        BackendIv = findViewById(R.id.BackendIV);
        FrontendIv = findViewById(R.id.FrontendIv);
        MarketingIV = findViewById(R.id.MarketingIV);
        EditingIv = findViewById(R.id.EditingIv);
        AnimationIV = findViewById(R.id.AnimationIV);
        DrawingIv = findViewById(R.id.DrawingIv);
        MachineLearningIV = findViewById(R.id.MachineLearningIV);
        WritingIv = findViewById(R.id.WritingIv);

        BackendIv.setOnClickListener(this);
        MarketingIV.setOnClickListener(this);
        EditingIv.setOnClickListener(this);
        AnimationIV.setOnClickListener(this);
        DrawingIv.setOnClickListener(this);
        FrontendIv.setOnClickListener(this);
        MachineLearningIV.setOnClickListener(this);
        WritingIv.setOnClickListener(this);

        Log.v("Tag" , "listener for button set");
    }

    @Override
    public void onClick(View v) {
        Intent i ;
        switch(v.getId()) {

            case R.id.BackendIV:
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("backend");
                Log.v("Tag" , "ttrbut coding set");
                i= new Intent(this, Match.class);
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

            case R.id.DrawingIv :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("drawing");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.MachineLearningIV :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("machingLearning");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            case R.id.WritingIv :
                usersDb.child("SkillTable").child(currentUId).child("Type").setValue("writing");
                i= new Intent(this, Match.class);
                startActivity(i);break;

            default: break ; }
    }
}

