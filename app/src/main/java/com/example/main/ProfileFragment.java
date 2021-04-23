package com.example.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.main.adapters.PostAdapter;
import com.example.main.models.Post;
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
    private DatabaseReference profileUserRef, FriendsRef, PostsRef;
    private FirebaseAuth mAuth;
    private TextView MyPosts, MyFriends;
    private String currentUserId;
    private int countFriends = 0, countPosts = 0;
    private RecyclerView recyclerView;
    private PostAdapter PostAdapter;
    private List<Post> postList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(currentUserId);
        //  FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
       // SexeRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").

       // userName = (TextView) view.findViewById(R.id.my_username);
        userProfName = (TextView) view.findViewById(R.id.my_profile_full_name);
       // userStatus = (TextView) view.findViewById(R.id.my_profile_status);
        userCountry = (TextView) view.findViewById(R.id.my_country);
       // userGender = (TextView) view.findViewById(R.id.my_gender);
        //recyclerView = view.findViewById(R.id.recycler_view);
       // recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
      //  recyclerView.setLayoutManager(mLayoutManager);
        postList = new ArrayList<>();
       PostAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(PostAdapter);
        userProfileImage = (CircleImageView) view.findViewById(R.id.my_profile_pic);
        MyFriends = (TextView) view.findViewById(R.id.my_friends_button);
        MyPosts = (TextView) view.findViewById(R.id.my_post_button);
        postss();

        MyFriends.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendUserToFriendsActivity();
            }
        });

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

        FriendsRef.child(currentUserId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    countFriends = (int) dataSnapshot.getChildrenCount();
                    MyFriends.setText(Integer.toString(countFriends) + "  Friends");
                }
                else
                {
                    MyFriends.setText("0 Friends");
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

    private void SendUserToFriendsActivity()
    {
        Intent friendsIntent = new Intent(getActivity(), FriendsActivity.class);
        startActivity(friendsIntent);
    }

    private void SendUserToMyPosts()
    {
        Intent friendsIntent = new Intent(getActivity(), MyPosts.class);
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
}


