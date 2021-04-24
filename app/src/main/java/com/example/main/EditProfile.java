package com.example.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
     CircleImageView edit_profile_pic;
     EditText edit_username,edit_age,edit_country;
     RadioButton male , female ;
     RadioGroup radiogroup ;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;
    final static int Gallery_Pick = 1;
    private ProgressDialog loadingBar;
    String currentUserID;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        edit_profile_pic = (CircleImageView) findViewById(R.id.edit_profile_pic);
        edit_username  = (EditText) findViewById(R.id.edit_username);
        edit_age = (EditText) findViewById(R.id.edit_age);
        edit_country = (EditText) findViewById(R.id.edit_country);
        radiogroup = (RadioGroup) findViewById(R.id.radioGroup);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        loadingBar = new ProgressDialog(this);
        save = (Button) findViewById(R.id.save);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("AllUsers").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    edit_username.setText(snapshot.child("fullname").getValue().toString());
                    edit_age.setText(snapshot.child("age").getValue().toString());
                    edit_country.setText(snapshot.child("country").getValue().toString());
                    String profileImage = snapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(profileImage).placeholder(R.drawable.profile).into(edit_profile_pic);
                    String sexe = snapshot.child("sexe").getValue().toString();
                    if(sexe.equals("Male")){
                        male.setChecked(true);
                    }else{
                        female.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        edit_profile_pic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });

    }

    private void save() {
            String fullname = edit_username.getText().toString();
            String age = edit_age.getText().toString();
            String country = edit_country.getText().toString();
            String sexe ="Male" ;
            if( male.isChecked()) { sexe = male.getText().toString();}
            if( female.isChecked()) { sexe = female.getText().toString();}
            if((TextUtils.isEmpty(fullname))&&(TextUtils.isEmpty(age))&&(TextUtils.isEmpty(country))){
                Toast.makeText(this, "Check your Info...", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loadingBar.setTitle("Editing account");
                loadingBar.setMessage("Please wait, while we edite your Account...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                HashMap userMap = new HashMap();
                userMap.put("fullname", fullname);
                userMap.put("age", age);
                userMap.put("country", country);
                userMap.put("sexe", sexe);
                UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            SendUserToMainActivity();
                            Toast.makeText(EditProfile.this, "Your Account is Edited Successfully.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                        else
                        {
                            String message = task.getException().getMessage();
                            Toast.makeText(EditProfile.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(EditProfile.this,MainActivity.class);
        mainIntent.putExtra("edit","edit");
        startActivity(mainIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if(resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we are creating your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            Uri downUri = task.getResult();
                            Toast.makeText(EditProfile.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = downUri.toString();
                            UsersRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Intent selfIntent = new Intent(EditProfile.this, EditProfile.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(EditProfile.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(EditProfile.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    }
                });

            }
            else
            {
                Toast.makeText(this, "Error Occured: Image can't be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }
    }
