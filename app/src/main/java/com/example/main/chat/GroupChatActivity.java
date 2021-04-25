package com.example.main.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.main.MainActivity;
import com.example.main.R;
import com.example.main.adapters.AdapterGroupChat;
import com.example.main.models.ModelGroupChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupChatActivity extends AppCompatActivity {
private String groupId ;
private ImageButton attachBtn , sendBtn ;
private EditText messageEt ;
private FirebaseAuth firebaseAuth;
private DatabaseReference usersDatabase;
private String userGoal , userGoalType , currentUId ;
private DatabaseReference GoalDb;
private RecyclerView chatRv;
private ArrayList<ModelGroupChat> groupChatList;
private AdapterGroupChat adapterGroupChat;
private Boolean notify = false ;

private static final int CAMERA_REQUEST_CODE = 100 ;
private static final int STORAGE_REQUEST_CODE = 200 ;
private static final int IMAGE_PICK_CAMERA_CODE = 300 ;
private static final int IMAGE_PICK_GALLERY_CODE = 400 ;
String[] cameraPermissions;
String[] storagePermissions ;
Uri image_rui = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
          attachBtn = findViewById(R.id.attach);
          messageEt = findViewById(R.id.messageEt);
          sendBtn = findViewById(R.id.sendBtn);
          chatRv = findViewById(R.id.chatRv);


       Intent i = getIntent();
       groupId = i.getStringExtra("groupId");

       if (groupId==null){
           Intent intent = new Intent(this , MainActivity.class);
           startActivity(i);
       }
       firebaseAuth = FirebaseAuth.getInstance();
       usersDatabase = FirebaseDatabase.getInstance().getReference("Users");
       checkGoal();


        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String message = messageEt.getText().toString().trim();
               if(TextUtils.isEmpty(message)){
                   Toast.makeText(GroupChatActivity.this , "Can't send empty message", Toast.LENGTH_SHORT).show();
}                else {
                   sendMessage(message);
               }
           }
       });
        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showImagePickDialog();
            }
        });
    }

    private void loadGroupMessages(String userGoalType) {
        groupChatList = new ArrayList<>();
        DatabaseReference referenceDb = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(this.userGoalType +"Groups");
        referenceDb.child(groupId).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("type").exists()){
                    ModelGroupChat model = ds.getValue(ModelGroupChat.class);
                    groupChatList.add(model);}
                }
                adapterGroupChat = new AdapterGroupChat(GroupChatActivity.this , groupChatList);
                chatRv.setAdapter(adapterGroupChat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {
        String timestamp = ""+System.currentTimeMillis();
        HashMap<String , Object> hashMap=new HashMap<>();
        hashMap.put("sender",""+firebaseAuth.getUid());
        hashMap.put("message",""+message);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("type",""+"text");
        DatabaseReference referenceDb = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
        referenceDb.child(groupId).child("Messages").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                messageEt.setText("");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                 Toast.makeText(GroupChatActivity.this , ""+e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void checkGoal(){
        Log.v("TAG", "a");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userDb1 = usersDatabase.child("AllUsers").child(user.getUid());
        userDb1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Goals").getValue() != null){
                        userGoal = dataSnapshot.child("Goals").getValue().toString();


                    } checkGoalType(userGoal);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void checkGoalType(String userGoal){
        Log.v("TAG", "b");
        String GoalTable ;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        switch (userGoal){
            case "skill":
                GoalTable="SkillTable";
                break;
            case "health":
                GoalTable="HealthTable";
                break;
            case "language":
                GoalTable="LanguageTable";
                break;
            default:
                GoalTable="LanguageTable";
                break;
        }
        GoalDb = FirebaseDatabase.getInstance().getReference().child("Users").child(GoalTable);
        DatabaseReference userDb2 = FirebaseDatabase.getInstance().getReference().child("Users").child(GoalTable).child(user.getUid());

        userDb2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("Type").getValue() != null){
                        userGoalType = dataSnapshot.child("Type").getValue().toString();
                        Log.v("TAG", userGoalType);

                    }
                }loadGroupInfo(userGoalType);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadGroupInfo(String userGoalType) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
        reference.orderByChild("groupId").equalTo(groupId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot ds: snapshot.getChildren()){
                     String groupTitle = (String) ds.child("groupTitle").getValue();

                 }loadGroupMessages(userGoalType);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE : {
                   if(grantResults.length>0){
                       boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                       boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                       if(cameraAccepted && storageAccepted) {
                           PickFromCamera();
                       }
                       else {
                           Toast.makeText(this,"permissions are necessary",Toast.LENGTH_SHORT).show();
                       }
                   } else {
                   }
            } break ;
            case STORAGE_REQUEST_CODE :{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted){
                        PickFromGallery();
                    }
                    else{
                        Toast.makeText(this,"storage permission necssary",Toast.LENGTH_SHORT).show();
                    }
                }else {

                }
            } break ;
        }
    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data ) {
           if(resultCode == RESULT_OK){
               if(requestCode == IMAGE_PICK_GALLERY_CODE){
                   image_rui = data.getData();
                   try {
                       sendImageMessage(image_rui);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

               }
               else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                   try {
                       sendImageMessage(image_rui);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendImageMessage(Uri image_rui) throws IOException {
        String timeStamp = ""+System.currentTimeMillis();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        notify = true ;
        ProgressDialog progressDialog = new ProgressDialog ( this );
        progressDialog.setMessage("Sending image ...");
        progressDialog.show();
        String fileNameAndPath = "ChatImages/"+"post_"+timeStamp;
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , image_rui);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 ,baos);
        byte[] data = baos.toByteArray();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               progressDialog.dismiss();
               Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
               while(!uriTask.isSuccessful());
               String downloadUri = uriTask.getResult().toString();
               if(uriTask.isSuccessful()){
                   DatabaseReference referenceDb = FirebaseDatabase.getInstance().getReference("Users").child("GroupTable").child(userGoalType+"Groups");
                   referenceDb.child(groupId).child("Messages").child(timeStamp).child("message").setValue(downloadUri);
                   referenceDb.child(groupId).child("Messages").child(timeStamp).child("sender").setValue(firebaseAuth.getUid());
                   referenceDb.child(groupId).child("Messages").child(timeStamp).child("timestamp").setValue(timeStamp);
                   referenceDb.child(groupId).child("Messages").child(timeStamp).child("type").setValue("image");
                   loadGroupMessages(userGoalType);
               }
            }
        });
    }

    private void showImagePickDialog() {
        String[] options = {"Camera" , "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0) {
                    //camera
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        PickFromCamera();
                    }
                }
                if(which==1){
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    }
                    else{
                        PickFromGallery();
                    }
                }

            }
        });
        builder.create().show();
    }

    private void PickFromGallery() {
        Intent intent = new Intent (Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent , IMAGE_PICK_GALLERY_CODE);
    }

    private void PickFromCamera() {
        ContentValues cv= new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,"Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION,"Temp Descr");
        image_rui= getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui);
        startActivityForResult(intent ,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result ;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermissions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1 ;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE);

    }
}