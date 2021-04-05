package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChooseGoal extends AppCompatActivity implements View.OnClickListener {
    private CardView skillCard,languageCard,healthCard;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUId;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_goal);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();

        skillCard=(CardView)findViewById(R.id.skill_card);
        languageCard=(CardView)findViewById(R.id.language_card);
        healthCard=(CardView)findViewById(R.id.health_card);
        skillCard.setOnClickListener(this);
        languageCard.setOnClickListener(this);
        healthCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      Intent i ;
      switch(v.getId()) {
          case R.id.skill_card :
              usersDb.child("AllUsers").child(currentUId).child("Goals").setValue("skill");
              usersDb.child("SkillTable").child(currentUId).setValue(true);
              i= new Intent(this, skills.class);
              startActivity(i);
              break;
          case R.id.language_card :
              usersDb.child("AllUsers").child(currentUId).child("Goals").setValue("language");
              usersDb.child("LanguageTable").child(currentUId).setValue(true);
              i= new Intent(this, languages.class);
              startActivity(i);
              break;
          case R.id.health_card :
              usersDb.child("AllUsers").child(currentUId).child("Goals").setValue("health");
              usersDb.child("HealthTable").child(currentUId).setValue(true);
              i= new Intent(this, Health.class);
              startActivity(i);break;
          default: break ;

      }

    }
}
