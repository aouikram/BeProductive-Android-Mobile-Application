package com.example.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChooseGoal extends AppCompatActivity implements View.OnClickListener {
    private CardView skillCard,languageCard,healthCard;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb,UsersDb;
    private String currentUId;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_goal);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        UsersDb = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers");


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
              usersDb.child("SkillTable").child(currentUId);
              usersDb.child("SkillTable").child(currentUId).child("group").setValue(false);
              i= new Intent(this, Skills.class);
              startActivity(i);break;
          case R.id.language_card :
              usersDb.child("AllUsers").child(currentUId).child("Goals").setValue("language");
              usersDb.child("LanguageTable").child(currentUId);
              usersDb.child("LanguageTable").child(currentUId).child("group").setValue(false);
              i= new Intent(this, languages.class);
              startActivity(i);break;
          case R.id.health_card :
              usersDb.child("AllUsers").child(currentUId).child("Goals").setValue("health");
              usersDb.child("HealthTable").child(currentUId);
              usersDb.child("HealthTable").child(currentUId).child("group").setValue(false);
              i= new Intent(this, Health.class);
              startActivity(i);break;
          default: break ;

      }

    }
   /* @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            SendUserToRegisterNext();
        }
        else
        {
            CheckUserExistence();
        }
    }

    private void CheckUserExistence()
    {
        final String current_user_id = mAuth.getCurrentUser().getUid();

        UsersDb.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToRegisterNext();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
    private void SendUserToRegisterNext()
    {
        Intent SetupIntent = new Intent(ChooseGoal.this, RegisterNext.class);
        SetupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupIntent);
        finish();
    }

    private void SendUserToChooseGoal()
    {
        Intent loginIntent = new Intent(ChooseGoal.this, ChooseGoal.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }*/

}
