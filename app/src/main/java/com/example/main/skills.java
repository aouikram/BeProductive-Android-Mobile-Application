package com.example.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.main.users.skillType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class skills extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String currentUId;
    Button button ;
    Spinner spinner;
    TextView textView;
    String item ;
    skillType skillType;
    String[] types = {"Choose type","Editing","Cloud computing","Analytics","coding","communication","design"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        textView=findViewById(R.id.choices_of_skills);
        button=findViewById(R.id.button_matcheMe);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("SkillTable").child(currentUId);
        spinner=findViewById(R.id.spinner);
        spinner.setOnItemClickListener((AdapterView.OnItemClickListener) this);

       skillType = new skillType();
       ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,types);
       arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       spinner.setAdapter(arrayAdapter);
       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SaveValue(item);
           }
       });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         item=spinner.getSelectedItem().toString();
         textView.setText(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    void SaveValue (String item)
    {
        if(item=="Choose type")
        {
            Toast.makeText(this,"please select a skill type",Toast.LENGTH_SHORT).show();
        }
        else {
            skillType.setSkill(item);
            String id =databaseReference.push().getKey();
            databaseReference.child("Type").setValue(skillType);
            Toast.makeText(this,"Type saved",Toast.LENGTH_SHORT).show();
        }
    }
}