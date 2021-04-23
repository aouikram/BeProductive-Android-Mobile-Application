package com.example.main;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.main.adapters.NotesAdapter;
import com.example.main.models.NotesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatFragment extends Fragment implements NotesAdapter.OnRecyclerItemClick{
    private ArrayList<NotesModel> modelList;
    DatabaseHelper mDatabaseHelper;
    private TextView groupTitleTv;
    private FirebaseAuth mAuth ;
    private String userGoal , userGoalType , currentUId ;
    private DatabaseReference usersDatabase;
    private DatabaseReference GoalDb;
    private CardView groupCard , change_goal_card;
    private String groupId;
    private RecyclerView recyclerView;
    private CardView take_note_card;
    private NotesAdapter notesAdapter;


    public ChatFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.chat_home, container, false);
        groupTitleTv = view.findViewById(R.id.groupTitleTv);
        groupCard = view.findViewById(R.id.groupCard);
        recyclerView = view.findViewById(R.id.recycler);
        take_note_card = (CardView) view.findViewById(R.id.take_note_card);
        change_goal_card = (CardView) view.findViewById(R.id.change_goal_card);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        populateView();
        take_note_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sendNote();

            }
        } );
        change_goal_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGoal();
            }
        });
        groupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        System.out.println(currentUId);

        Log.v("TAG", "ref about to be called");
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        checkGoal();


        return view ;
    }



    private void sendGoal() {
        Intent intent = new Intent(getActivity(), ChooseGoal.class);
        startActivity(intent);
    }

    private void sendNote() {
        Intent intent = new Intent(getActivity(), AddNoteActivity.class);
        startActivity(intent);
    }

    private void send() {

        Intent i = new Intent(getActivity(), GroupChatActivity.class);
        if (groupId != null) {
        i.putExtra("groupId" , groupId);
        Log.v("TAG","group Id set in intent");
        startActivity(i); }
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
                        checkGoalType(userGoal);
                    }
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
    public String getGroupId(){
        DatabaseReference userDb1 = usersDatabase.child("AllUsers").child(currentUId);
        userDb1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("groupId").getValue() != null){
                        groupId = dataSnapshot.child("groupId").getValue().toString();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        }); return groupId;
    }

    private void loadGroupChatList(String userGoalType) {
        Log.v("TAG", "c");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("participants").child(mAuth.getUid()).exists()){
                        groupId = ds.child("groupId").getValue(String.class) ;
                        Log.v("TAG","group id set");
                        groupTitleTv.setText(ds.child("groupTitle").getValue(String.class));

                    }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void populateView() {
        Cursor data = mDatabaseHelper.getData();
        modelList = new ArrayList<>();
        while(data.moveToNext()){
            NotesModel note= new NotesModel();
            note.setHead(data.getString(1));
            modelList.add(note);
        }
        notesAdapter = new NotesAdapter(modelList, getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public void onClick(int pos) {

        }
    }





