package com.example.main.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.main.R;
import com.example.main.adapters.PostAdapter;
import com.example.main.login.LogSignUp;
import com.example.main.models.Post;
import com.example.main.profile.EditProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private TextView userName, userProfName, userStatus, userCountry,userGender, userRelation, userDOB;
    private CircleImageView userProfileImage;
    private DatabaseReference profileUserRef,  PostsRef , AgeRef , SexeRef;
    private FirebaseAuth mAuth;
    private TextView MyPosts,Age,Sexe;
    private String currentUserId;
    private String age , sexe , country ;
    private int  countPosts = 0;
    private PostAdapter PostAdapter;
    private List<Post> postList;
    private Button signOut,edit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(currentUserId);
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        AgeRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(currentUserId);
       SexeRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(currentUserId);

        userProfName = (TextView) view.findViewById(R.id.my_profile_full_name);
        userCountry = (TextView) view.findViewById(R.id.my_country);


        postList = new ArrayList<>();
       PostAdapter = new PostAdapter(getContext(), postList);

        userProfileImage = (CircleImageView) view.findViewById(R.id.my_profile_pic);
        Age = (TextView) view.findViewById(R.id.Age);
        Sexe = (TextView) view.findViewById(R.id.Sexe);
        MyPosts = (TextView) view.findViewById(R.id.my_post_button);
        edit = (Button) view.findViewById(R.id.edit);
        signOut = (Button) view.findViewById(R.id.button4);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToEditProfileActivity();
            }
        });
        postss();
        getAge();
        getSexe();
        getCountry();


        MyPosts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToMyPosts();
            }
        });

        PostsRef.orderByChild("uid").startAt(currentUserId).endAt(currentUserId + "\uf8ff").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    countPosts = (int) dataSnapshot.getChildrenCount();
                    MyPosts.setText(Integer.toString(countPosts) + "  Posts");
                }
                else
                {
                    MyPosts.setText("0  Post");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });



        profileUserRef.addValueEventListener(new ValueEventListener()
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
                    //userName.setText("@" + myUserName);
                    userProfName.setText(myProfileName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

   return view;
    }

    private void getCountry() {
        profileUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("country").getValue() != null){
                        country = dataSnapshot.child("country").getValue().toString();
                        userCountry.setText(country);

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void SendUserToEditProfileActivity() {
        Intent intent = new Intent(getActivity(), EditProfile.class);
        startActivity(intent);
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



    private void SendUserToMyPosts()
    {
        Intent friendsIntent = new Intent(getActivity(), com.example.main.post.MyPosts.class);
        startActivity(friendsIntent);
    }
    private void postss(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if (post.getUid().equals(currentUserId)){
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                PostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LogSignUp.class);
        startActivity(intent);

    }

}


