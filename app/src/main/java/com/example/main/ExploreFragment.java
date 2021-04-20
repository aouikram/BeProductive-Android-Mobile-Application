package com.example.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.main.models.SearchFriend;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExploreFragment extends Fragment {
    private ImageButton SearchButton;
    private EditText SearchInputText;

    private RecyclerView SearchResultList;

    private DatabaseReference allUsersDatabaseRef;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.explore_home, container, false);
            allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers");
            SearchResultList = (RecyclerView) view.findViewById(R.id.search_result_list);
            SearchResultList.setHasFixedSize(true);
            SearchResultList.setLayoutManager(new LinearLayoutManager(getActivity()));

            SearchButton = (ImageButton) view.findViewById(R.id.search_people_friends_button);
            SearchInputText = (EditText) view.findViewById(R.id.search_box_input);


            SearchButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String searchBoxInput = SearchInputText.getText().toString();

                    SearchPeopleAndFriends(searchBoxInput);
                }
            });
            return view;
        }

    private void SearchPeopleAndFriends(String searchBoxInput)
    {
        Toast.makeText(getActivity(), "Searching....", Toast.LENGTH_LONG).show();

        Query searchPeopleAndFriendsQuery = allUsersDatabaseRef.orderByChild("fullname")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerOptions<SearchFriend> options=new FirebaseRecyclerOptions.Builder<SearchFriend>().
                setQuery(searchPeopleAndFriendsQuery, SearchFriend.class).build(); //query build past the query to FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<SearchFriend, FindFriendViewHolder> adapter=new FirebaseRecyclerAdapter<SearchFriend, ExploreFragment.FindFriendViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull ExploreFragment.FindFriendViewHolder holder, final int position, @NonNull SearchFriend model)
            {
                final String PostKey = getRef(position).getKey();
                holder.username.setText(model.getFullname());
                holder.status.setText(model.getStatus());

                Picasso.get().load(model.getProfileimage()).into(holder.profileimage);

               /* holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent findOthersIntent = new Intent(getActivity(), MainActivity.class);
                        findOthersIntent.putExtra("PostKey", PostKey);
                        startActivity(findOthersIntent);
                    }
                });*/

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String visit_user_id = getRef(position).getKey();

                        Intent profileIntent = new Intent(getActivity(), OthersProfile.class);
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }
            @NonNull
            @Override
            public ExploreFragment.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_users,viewGroup,false);

                ExploreFragment.FindFriendViewHolder viewHolder= new ExploreFragment.FindFriendViewHolder(view);
                return viewHolder;
            }
        };

        SearchResultList.setAdapter(adapter);
        adapter.startListening();
    }

    public class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView username, status;
        CircleImageView profileimage;
        View mView;

        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.all_users_profile_full_name);
            status = itemView.findViewById(R.id.all_users_status);
            profileimage = itemView.findViewById(R.id.all_users_profile_image);
        }
    }
}



