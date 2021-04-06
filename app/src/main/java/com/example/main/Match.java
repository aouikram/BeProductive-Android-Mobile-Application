package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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
        currentUId = mAuth.getCurrentUser().getUid();
        UsersList = new ArrayList<>();
        // check what goals does the user have
        checkUserGoal(); // checks Goal TYPE then Adds users with same goal type tp the list
        // choose random users from the list
        List<String> randomList = getRandomUsers(UsersList);
        //changing the users Group attribute to true for the selected users
        setGroupTrue(randomList,userGoal);
        // create a GroupTable that will contain all the groups , each group's id is a child , and the users in the group are children of the group
        // they belong to
        String GroupId =randomList.get(1); // group Id is same as the first user in tha group
        usersDb.child("GroupTable").child(GroupId);
          for(int i=0 ; i<randomList.size(); i++)
          {    // insert users
              usersDb.child("GroupTable").child(GroupId).child("user"+i).setValue(randomList.get(i));
          }
        }

    private void setGroupTrue(List<String> randomList, String userGoal) {
         String Goal;
        switch (userGoal){
            case "skill":
                Goal="SkillTable";
                break;
            case "health":
                Goal="HealthTable";
                break;
            case "language":
                Goal="LanguageTable";
                break;
            default:
                Goal="LanguageTable";
                break;
        }
        DatabaseReference GoalsDb = FirebaseDatabase.getInstance().getReference().child("Users").child(Goal);
        GoalDb.child("group").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("group").getValue() != null) {
                    if (dataSnapshot.exists() && dataSnapshot.child("group").getValue().toString().equals("false")&& randomList.contains(dataSnapshot.getKey())) {
                       GoalsDb.child(dataSnapshot.getKey()).child("group").setValue("true");
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
 }
    public List<String> getRandomUsers(List<String> UsersList) {
        int size = UsersList.size();
        if (size<=10) { return UsersList ; }
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
            return newList;
        }
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
                        checkUserGoalType(userGoal);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void checkUserGoalType(String userGoal){
        String Goal ;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        switch (userGoal){
            case "skill":
                Goal="SkillTable";
                break;
            case "health":
                Goal="HealthTable";
                break;
            case "language":
                Goal="LanguageTable";
                break;
            default:
                Goal="LanguageTable";
                break;
        }
        GoalDb = FirebaseDatabase.getInstance().getReference().child("Users").child(Goal);
        DatabaseReference userDb = GoalDb.child(user.getUid());

        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Type").getValue() != null){
                        userGoalType = dataSnapshot.child("Type").getValue().toString();
                        getSameGoalUsers(GoalDb , UsersList);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    private void getSameGoalUsers(DatabaseReference GoalDb , List<String> UsersList) {
            GoalDb.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.child("type").getValue() != null) {
                        if (dataSnapshot.exists() && dataSnapshot.child("type").getValue().toString().equals(userGoalType) && dataSnapshot.child("group").getValue().toString().equals("false")) {
                            String UID = dataSnapshot.getKey();
                            UsersList.add(UID);
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
        }

    }
