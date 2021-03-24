package com.example.main;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.example.main.users.User;
import com.example.main.sql.sqlite;


public class Register extends AppCompatActivity {
    private Button mRegister;
    private EditText mEmail,mName,mLastName,mPassword;
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener ;
    private RadioGroup mRadioGroup;
    EditText editTextCarrierNumber;
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
        setContentView(R.layout.register);
        mAuth =FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
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
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mName = (EditText) findViewById(R.id.name);
        RegisterBirthday = (EditText)findViewById(R.id.birthday);
        calendaricon = (ImageView) findViewById(R.id.calendar);
        mLastName = (EditText) findViewById(R.id.lastname);
        calendaricon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, android.R.style.Theme_DeviceDefault_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        RegisterBirthday.setText(day +"/" + month +"/" + year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
        RegisterBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                day = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        RegisterBirthday.setText(day +"/" + month +"/" + year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if radioButton is not checked if it is continue
                int selectId = mRadioGroup.getCheckedRadioButtonId();

                final RadioButton radioButton = (RadioButton) findViewById(selectId);

                if(radioButton.getText() == null){
                    return;
                }
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();
                final String lastname = mLastName.getText().toString();
                String birthday = RegisterBirthday.getText().toString();
                String phone = editTextCarrierNumber.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this,"email can not be empty" , Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(name)) {
                    mName.setError("Please enter your name");
                } else if (TextUtils.isEmpty(lastname)) {
                    mLastName.setError("Please enter your lastname");
                } else if (TextUtils.isEmpty(password)) {
                   mPassword.setError("Please enter a password");
                } else if (TextUtils.isEmpty(birthday)) {
                    RegisterBirthday.setError("Please enter your birthday date");
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
                            // task is succesful we add infos about user in database
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(userId);
                            Map userInfo = new HashMap<>();


                            userInfo.put("name", name);
                            userInfo.put("Lastname", lastname);
                            userInfo.put("Birthday", birthday);
                            userInfo.put("phone", phone);
                            userInfo.put("sex", radioButton.getText().toString());
                            userInfo.put("profileImageUrl", "default");
                            currentUserDb.updateChildren(userInfo);
                            Intent intent = new Intent(Register.this, MainActivity.class);
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





