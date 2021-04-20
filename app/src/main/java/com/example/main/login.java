package com.example.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    //private EditText Email, Password;
    //private Button Login;
   // GoogleSignInClient mGoogleSignInClient;
   // private static int RC_SIGN_IN = 100;
   // private sqlite sqlite;
   // User user;
    private Button mLogin;
    private EditText mEmail;
    private EditText mPassword;
    private ImageButton mBack;
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth =FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mBack = (ImageButton) findViewById(R.id.Back);
        mLogin = (Button) findViewById(R.id.login_btn);
        mEmail = (EditText) findViewById(R.id.login_email);
        mPassword = (EditText) findViewById(R.id.login_password);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(login.this , new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(login.this,"sign_in_error" , Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });
    mBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
    });}
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
       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);*/

      /*  SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);*/

     //   signInButton.setEnabled(true);
    /*    Login = (Button) findViewById(R.id.login_btn);
        Password = (EditText) findViewById(R.id.login_password);
        Email = (EditText) findViewById(R.id.login_email); */


    /*    findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        }); */

     /*   Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogToAccount();
            }
        });
        sqlite = new sqlite(this);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
            }
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            // Signed in successfully, show authenticated UI.

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

        }
    }

    private void LogToAccount() {
        String eemail = Email.getText().toString();
        String ppassword = Password.getText().toString();
        if (TextUtils.isEmpty(eemail)) {
            Email.setError("Enter a valid mail");
        } else if (TextUtils.isEmpty(ppassword)) {
            Password.setError("Please enter a password");
        } else if (!isValidEmail(eemail)) {
            Email.setError("Invalid Email Address");
        }else if (sqlite.checkUser(Email.getText().toString().trim()
                , Password.getText().toString().trim())) {
            Intent intent = new Intent(login.this, MainActivity.class);
            intent.putExtra("EMAIL", Email.getText().toString().trim());
            startActivity(intent);
        }
    }*/
}


