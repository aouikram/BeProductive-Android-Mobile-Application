package com.example.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class OthersProfile extends AppCompatActivity {
    private TextView userName, userProfName, userCountry;
    private CircleImageView userProfileImage;
    private String age , sexe ;
    private DatabaseReference  UsersRef;
    private DatabaseReference   AgeRef , SexeRef;
    private FirebaseAuth mAuth;
    private String senderUserId, receiverUserId;
    private TextView Age,Sexe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_profile);

        mAuth = FirebaseAuth.getInstance();
        senderUserId = mAuth.getCurrentUser().getUid();


        receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers");
        AgeRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(receiverUserId);
        SexeRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(receiverUserId);
        Age = (TextView) findViewById(R.id.Age);
        Sexe = (TextView) findViewById(R.id.Sexe);
        getAge();
        getSexe();

        IntializeFields();

        UsersRef.child(receiverUserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();


                    Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userProfName.setText(myProfileName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }

    private void IntializeFields()
    {
        userProfName = (TextView) findViewById(R.id.person_full_name);
        userCountry = (TextView) findViewById(R.id.person_country);

        userProfileImage = (CircleImageView) findViewById(R.id.person_profile_pic);

    }
    public void getSexe() {
        SexeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("sexe").getValue() != null){
                        sexe = dataSnapshot.child("sexe").getValue().toString();
                        Sexe.setText(sexe );

                    }
                } else
                {
                    Sexe.setText("");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void getAge() {
        AgeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("age").getValue() != null){
                        age = dataSnapshot.child("age").getValue().toString();
                        Age.setText(age + " years");

                    }
                } else
                {
                    Age.setText("18 years");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
