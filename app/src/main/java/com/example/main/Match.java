package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.main.users.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Match extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private String currentUId;
    private String userGoalType;
    private String userGoal;
    private DatabaseReference GoalDb;
    private DatabaseReference usersDb;
    private List<String> UsersList;
    private Integer groupSize ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        Log.v("TAG", "Authentification sucess");
        currentUId = mAuth.getCurrentUser().getUid();
        UsersList = new ArrayList<>();
        Log.v("TAG", "hello function");
        checkUserGoal();
        Log.v("TAG", "function ended ");
        }

    private void setGroupTrue(List<String> randomList, DatabaseReference GoalDb, String userGoalType) {
        if(randomList.size()<=1)
        {
            Log.v("TAG", " lonely user ");
            Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT).show();
            Intent i= new Intent(this, MainActivity.class);
            startActivity(i);
        }
        else {
        GoalDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("group").getValue() != null) {
                    if (dataSnapshot.exists() && dataSnapshot.child("group").getValue().toString().equals("false")&& randomList.contains(dataSnapshot.getKey())) {
                       GoalDb.child(dataSnapshot.getKey()).child("group").setValue("true");
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        Log.v("TAG", "group attribute set to true ");
        String GroupId =randomList.get(0); // group Id is same as the first user in tha group
        Log.v("TAG", GroupId);
        FirebaseDatabase.getInstance().getReference().child("Users").child("GroupTable").child(userGoalType+"Groups").child(GroupId);
        Log.v("TAG", "Table created");
        for(int i=0 ; i<randomList.size(); i++)
        {    // insert users
            Log.v("TAG", "hi");
            usersDb.child("GroupTable").child(userGoalType+"Groups").child(GroupId).child("user"+i).setValue(randomList.get(i));
        }

 }}
    public List<String> getRandomUsers(List<String> UsersList) {
        int size = UsersList.size();
        if (size<=10) { Log.v("TAG", "random list created"); return UsersList ; }
        else if(size>=10){ 
            groupSize=size/4;
            Random rand = new Random();
            // create a temporary list for storing
            // selected element
            List<String> newList = new ArrayList<>();
            for (int i = 0; i < groupSize; i++) {
                // take a raundom index between 0 to size
                int randomIndex = rand.nextInt(UsersList.size());
                newList.add(UsersList.get(randomIndex));
            }
            Log.v("TAG", "random list created");
            return newList;
        }
        Log.v("TAG", "random list not created");
        return UsersList;
    }
    public void checkUserGoal(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb1 = usersDb.child("AllUsers").child(user.getUid());
        userDb1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Goals").getValue() != null){
                        userGoal = dataSnapshot.child("Goals").getValue().toString();
                        Log.v("TAG", userGoal);

                    } checkUserGoalType(userGoal);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void checkUserGoalType(String userGoal){
        String GoalTable ;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        switch (userGoal){
            case "skill":
                GoalTable="SkillTable";
                break;
            case "health":
                GoalTable="HealthTable";
                break;
            case "language":
                GoalTable="LanguageTable";
                break;
            default:
                GoalTable="LanguageTable";
                break;
        }
        GoalDb = FirebaseDatabase.getInstance().getReference().child("Users").child(GoalTable);
        DatabaseReference userDb2 = FirebaseDatabase.getInstance().getReference().child("Users").child(GoalTable).child(user.getUid());

        userDb2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Type").getValue() != null){
                        userGoalType = dataSnapshot.child("Type").getValue().toString();
                        Log.v("TAG", userGoalType);

                    } CheckGroups(GoalDb , UsersList , userGoalType);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void CheckGroups(DatabaseReference GoalDb, List<String> UsersList, String userGoalType) {
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("GroupTable").child(userGoalType + "Groups");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    long number = snapshot.getChildrenCount();
                    if (number < 4 && i==0 ) {
                        GoalDb.child(currentUId).child("group").setValue("true");
                        FirebaseDatabase.getInstance().getReference().child("Users").child("GroupTable").child(userGoalType + "Groups").child(snapshot.getKey()).child("user"+number).setValue(currentUId);
                        i++;
                    }
                }
                if(i==0)
                       CreatNewGroup(GoalDb, UsersList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void CreatNewGroup(DatabaseReference GoalDb , List<String> UsersList )
    {
        GoalDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    if (snapshot.child("Type").getValue() != null) {
                        if (snapshot.exists() && snapshot.child("Type").getValue().toString().equals(userGoalType) && snapshot.child("group").getValue().toString().equals("false")) {
                            String UID = snapshot.getKey();
                            UsersList.add(UID);
                            Log.v("TAG", "user added in list");
                        }
                }
            } Log.v("TAG", "list created");
                setGroupTrue(getRandomUsers(UsersList),GoalDb,userGoalType); }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }

    }
