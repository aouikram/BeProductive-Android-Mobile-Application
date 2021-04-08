package com.example.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;



public class JourneyFragment extends Fragment {
    private Button setGoal;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journey_home, container, false);
        setGoal = (Button) rootView.findViewById(R.id.button2);
        setGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
      return rootView ;
    }

public void send(){
    Intent intent = new Intent(getActivity(), ChooseGoal.class);
    startActivity(intent);
}
/*public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LogSignUp.class);
        startActivity(intent);
        return;
    }*/
}