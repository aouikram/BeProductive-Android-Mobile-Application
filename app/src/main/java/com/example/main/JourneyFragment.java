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
    private Button setGoal , signOut , button_Tracker , progress;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journey_home, container, false);
        setGoal = (Button) rootView.findViewById(R.id.button2);
        signOut = (Button) rootView.findViewById(R.id.button4);
        progress = (Button) rootView.findViewById(R.id.progress);
        setGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendProg();
            }
        });
      return rootView ;
    }


    private void sendProg() {
        Intent intent = new Intent(getActivity(), TrackProgress.class);
        startActivity(intent);
    }
    public void send(){
    Intent intent = new Intent(getActivity(), ChooseGoal.class);
    startActivity(intent);
}
public void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(getActivity(), LogSignUp.class);
        startActivity(intent);
        return;
    }

}