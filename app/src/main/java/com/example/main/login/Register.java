package com.example.main.login;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.main.match.ChooseGoal;
import com.example.main.MainActivity;
import com.example.main.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    private Button mRegister;
    private EditText mEmail,mName,mPassword;
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener ;
    private EditText editTextCarrierNumber , mAge ;
    CountryCodePicker ccp;
    private int day,month,year;
    ImageView calendaricon;
    private EditText RegisterBirthday;

 /*


    private ProgressDialog loadingBar;
    private  sqlite sqlite;
    private User user; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        mAuth =FirebaseAuth.getInstance();

        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                    userDb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                if (dataSnapshot.hasChild("Goals")){
                                    Intent intent = new Intent(Register.this, ChooseGoal.class);
                                    startActivity(intent);
                                    finish();
                                    return;
                                }
                                else
                                {  Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    return;}
                                    }
                                }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { } });
                }
        }};
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        ccp.isValidFullNumber();
        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {

            }
        });
        ccp.getFormattedFullNumber();
        mRegister = (Button) findViewById(R.id.register_btn);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mAge= (EditText) findViewById(R.id.age);



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String age = mAge.getText().toString();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                String phone = editTextCarrierNumber.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this,"email can not be empty" , Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)) {
                   mPassword.setError("Please enter a password");
                } else if (TextUtils.isEmpty(age)) {
                    mAge.setError("Please enter your age");
                } else if (TextUtils.isEmpty(phone)) {
                    editTextCarrierNumber.setError("Please enter your number phone");
                } else if (!isValidEmail(email)) {
                    mEmail.setError("Invalid Email Address");
                }
                else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this , new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(Register.this,"sign_up_error" , Toast.LENGTH_SHORT).show();
                    }else {

                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(userId);
                            Map userInfo = new HashMap<>();



                            userInfo.put("age", age);
                            userInfo.put("phone", phone);
                            userInfo.put("email",email);
                            userInfo.put("uid",userId);
                            currentUserDb.updateChildren(userInfo);
                            Intent intent = new Intent(Register.this, RegisterNext.class);
                            startActivity(intent);
                            finish();
                            return;


                        }
                }

            });
            } }
                                     }); }
    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }
    /*
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        sqlite = new sqlite(this);
        user = new User();
    }





    private void CreateAccount() {
        String email = RegisterEmail.getText().toString();
        String name = RegisterName.getText().toString();
        String lastname = RegisterLastName.getText().toString();
        String password = RegisterPassword.getText().toString();
        String birthday = RegisterBirthday.getText().toString();
        String phone = editTextCarrierNumber.getText().toString();
        loadingBar = new ProgressDialog(this);
        if (TextUtils.isEmpty(email)) {
            RegisterEmail.setError("Enter a valid mail");
        }
        if (TextUtils.isEmpty(name)) {
            RegisterName.setError("Please enter your name");
        } else if (TextUtils.isEmpty(lastname)) {
            RegisterLastName.setError("Please enter your lastname");
        } else if (TextUtils.isEmpty(password)) {
            RegisterPassword.setError("Please enter a password");
        } else if (TextUtils.isEmpty(birthday)) {
            RegisterBirthday.setError("Please enter your birthday date");
        } else if (TextUtils.isEmpty(phone)) {
            editTextCarrierNumber.setError("Please enter your number phone");
        } else if (!isValidEmail(email)) {
            RegisterEmail.setError("Invalid Email Address");
        }
        else{
            Boolean checkMailInDB = sqlite.checkMailInDB(email);
            if (checkMailInDB==true){
                Boolean insert= sqlite.addUser(user);
                if(insert==true){
                    Toast.makeText(getApplicationContext(),"Registred Successfully",Toast.LENGTH_SHORT);
                    Intent i = new Intent(Register.this, ChooseGoal.class);
                    startActivity(i);
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Email already exists",Toast.LENGTH_SHORT);
            }

        } } */}
    /*   else {
            if (!sqlite.checkUser(RegisterEmail.getText().toString().trim())) {

                user.setName(RegisterName.getText().toString().trim());
                user.setMail(RegisterEmail.getText().toString().trim());
                user.setPassword(RegisterPassword.getText().toString().trim());
                sqlite.addUser(user);
                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT);
                Intent i = new Intent(Register.this, ChooseGoal.class);
                startActivity(i);
            }
            else {
                Toast.makeText(getApplicationContext(),"Email already exists",Toast.LENGTH_SHORT);
            }
        } }}*/





