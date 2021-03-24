package com.example.main.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.main.users.User;

public class sqlite  extends SQLiteOpenHelper {
    private static int DATABASE_VERSION=1;
    private static  final String DATABASE_NAME= "userManager.db";
    private static  final String TABLE_USER="user";
    private static  final String COLUMN_USER_ID="user_id";
    private static  final String COLUMN_USER_NAME="user_name";
    private static final String COLUMN_USER_LASTNAME="user_lastname";
    private static final String COLUMN_USER_BIRTHDAY="user_birthday";
    private static  final String COLUMN_USER_EMAIL="user_email";
    private static final String COLUMN_USER_PASSWORD="user_password";

    private  String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_LASTNAME +" TEXT,"  + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" +  ")";
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    public sqlite(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }
    public Boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_LASTNAME,user.getLastname());
        values.put(COLUMN_USER_EMAIL, user.getMail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

       long ins = db.insert(TABLE_USER, null, values);
        if (ins == -1)
            return false;
        else
            return true;
    }

    public boolean checkUser(String email, String password){
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }
    public Boolean checkMailInDB(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where user_email=?",new String[]{email});
        if(cursor.getCount()>0) return false;
        else return true ;
    }
}
