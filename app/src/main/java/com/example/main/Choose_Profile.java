package com.example.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class Choose_Profile extends AppCompatActivity {
  CircleImageView circleImageView;
  Button button;
  Uri imageuri;

  public static final int Image_CODE=1;
@Override
    protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.choose_profile);
    circleImageView = findViewById(R.id.user);
    button.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            openimageform();
        }
    } );
}
private void openimageform(){
    Intent intent = new Intent() ;
    intent.setType("image/*");
    getIntent().setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,Image_CODE);
}

@Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data){
    super.onActivityResult(requestCode,resultCode,data);
    if (requestCode==Image_CODE && resultCode== RESULT_OK && data != null && data.getData()!= null) {
       imageuri = data.getData();
        circleImageView.setImageURI(imageuri);

    }
    }

}
