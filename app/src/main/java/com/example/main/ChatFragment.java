package com.example.main;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatFragment extends Fragment {
    private TextView groupTitleTv;
    private FirebaseAuth mAuth ;
    private String userGoal , userGoalType , currentUId ;
    private DatabaseReference usersDatabase;
    private DatabaseReference GoalDb;
    private CardView groupCard ;
    private String groupId;

    public ChatFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.chat_home, container, false);
        groupTitleTv = view.findViewById(R.id.groupTitleTv);
        groupCard = view.findViewById(R.id.groupCard);
        groupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        Log.v("TAG", "ref about to be called");
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        checkGoal();


        return view ;
    }

    private void send() {

        Intent i = new Intent(getActivity(), GroupChatActivity.class);
        i.putExtra("groupId" , groupId);
        Log.v("TAG","group Id set in intent");
        startActivity(i);
    }

    public void checkGoal(){
        Log.v("TAG", "a");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb1 = usersDatabase.child("AllUsers").child(user.getUid());
        userDb1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Goals").getValue() != null){
                        userGoal = dataSnapshot.child("Goals").getValue().toString();


                    } checkGoalType(userGoal);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void checkGoalType(String userGoal){
        Log.v("TAG", "b");
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

                    } loadGroupChatList(userGoalType);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadGroupChatList(String userGoalType) {
        Log.v("TAG", "c");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("participants").child(mAuth.getUid()).exists()){
                        groupId = ds.child("groupId").getValue(String.class) ;
                        Log.v("TAG","group id set");
                        groupTitleTv.setText(ds.child("groupTitle").getValue(String.class));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

