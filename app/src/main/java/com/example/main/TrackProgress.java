package com.example.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.main.adapters.NotesAdapter;
import com.example.main.models.NotesModel;
import java.util.ArrayList;


public class TrackProgress extends AppCompatActivity implements NotesAdapter.OnRecyclerItemClick {
    DatabaseHelper mDatabaseHelper;
    private RecyclerView recyclerView;
    private ImageView add;
    private ArrayList<NotesModel> modelList;
    private NotesAdapter notesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_progress);
        recyclerView = findViewById(R.id.recycler);
        add = (ImageView) findViewById(R.id.add);
        mDatabaseHelper = new DatabaseHelper(this);

        populateView();

        add.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(TrackProgress.this, AddNoteActivity.class);
                                          startActivity(intent);

                                      }
                                  } );

    }

    private void populateView() {
        Cursor data = mDatabaseHelper.getData();
        modelList = new ArrayList<>();
        while(data.moveToNext()){
            NotesModel note= new NotesModel();
            note.setHead(data.getString(1));
            modelList.add(note);
        }
        notesAdapter = new NotesAdapter(modelList, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(notesAdapter);
    }
    @Override
    public void onClick(int pos) {
        Toast.makeText(this, "Pos is : " + pos, Toast.LENGTH_SHORT).show();


    }
}