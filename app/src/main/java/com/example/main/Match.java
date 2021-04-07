package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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

    private void setGroupTrue(List<String> randomList, DatabaseReference GoalDb) {
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
        FirebaseDatabase.getInstance().getReference().child("Users").child("GroupTable").child(GroupId);
        Log.v("TAG", "Table created");
        for(int i=0 ; i<randomList.size(); i++)
        {    // insert users
            Log.v("TAG", "hi");
            usersDb.child("GroupTable").child(GroupId).child("user"+i).setValue(randomList.get(i));
        }

 }
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

                    } getSameGoalUsers(GoalDb , UsersList , userGoalType);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void getSameGoalUsers(DatabaseReference GoalDb, List<String> UsersList, String userGoalType) {
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
                setGroupTrue(getRandomUsers(UsersList),GoalDb); }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
     /*       GoalDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.child("Type").getValue() != null) {
                        if (dataSnapshot.exists() && dataSnapshot.child("Type").getValue().toString().equals(userGoalType) && dataSnapshot.child("group").getValue().toString().equals("false")) {
                            String UID = dataSnapshot.getKey();
                            UsersList.add(UID);

                        }Log.v("TAG", "user added in list");

                    }
                    Log.v("TAG", "list created");
                    setGroupTrue(getRandomUsers(UsersList),GoalDb);
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
            });*/
        }

    }
