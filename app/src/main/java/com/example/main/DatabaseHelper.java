package com.example.main;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context,"login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
     db.execSQL("Create table user(email text primary key , password text , name text , lastname text  ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
   db.execSQL("drop table if exists user");
    }
    public Boolean insert(String email , String password , String name , String lastname ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("name", name);
        contentValues.put("lastname", lastname);
        long ins = db.insert("user", null, contentValues);
        if (ins == -1)
            return false;
        else
            return true;
    }
    public Boolean checkMailInDB(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email=?",new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true ;
    }
}
