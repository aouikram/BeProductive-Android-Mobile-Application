package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.main.adapters.AdapterGroupChat;
import com.example.main.models.ModelGroupChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupChatActivity extends AppCompatActivity {
private String groupId ;
private Toolbar toolbar ;
private TextView groupTitleTv ;
private ImageButton attach , sendBtn ;
private EditText messageEt ;
private FirebaseAuth firebaseAuth;
private DatabaseReference usersDatabase;
private String userGoal , userGoalType , currentUId ;
private DatabaseReference GoalDb;
private RecyclerView chatRv;
private ArrayList<ModelGroupChat> groupChatList;
private AdapterGroupChat adapterGroupChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
          toolbar = findViewById(R.id.toolbar);
          groupTitleTv = findViewById(R.id.groupTitleTv);
          attach = findViewById(R.id.attach);
          messageEt = findViewById(R.id.messageEt);
          sendBtn = findViewById(R.id.sendBtn);
          chatRv = findViewById(R.id.chatRv);

       Intent i = getIntent();
       groupId = i.getStringExtra("groupId");
        Log.v("TAG","group Id received");

       firebaseAuth = FirebaseAuth.getInstance();
       usersDatabase = FirebaseDatabase.getInstance().getReference("Users");
       checkGoal();


       sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String message = messageEt.getText().toString().trim();
               if(TextUtils.isEmpty(message)){
                   Toast.makeText(GroupChatActivity.this , "Can't send empty message", Toast.LENGTH_SHORT).show();
}                else {
                   sendMessage(message);
               }
           }
       });
    }

    private void loadGroupMessages(String userGoalType) {
        groupChatList = new ArrayList<>();
        DatabaseReference referenceDb = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
        referenceDb.child(groupId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelGroupChat model = ds.getValue(ModelGroupChat.class);
                    groupChatList.add(model);
                }
                adapterGroupChat = new AdapterGroupChat(GroupChatActivity.this , groupChatList);
                chatRv.setAdapter(adapterGroupChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
        String timestamp = ""+System.currentTimeMillis();
        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("sender",""+firebaseAuth.getUid());
        hashMap.put("message",""+message);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("type",""+"text");
        DatabaseReference referenceDb = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
        referenceDb.child(groupId).child("Messages").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                messageEt.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 Toast.makeText(GroupChatActivity.this , ""+e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });


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

                    } loadGroupInfo(userGoalType );
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

private void loadGroupInfo(String userGoalType) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
        reference.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot ds: snapshot.getChildren()){
                     String groupTitle = (String) ds.child("groupTitle").getValue();
                     groupTitleTv.setText(groupTitle);
                 }loadGroupMessages(userGoalType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}