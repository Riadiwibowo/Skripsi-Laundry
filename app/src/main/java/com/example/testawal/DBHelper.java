package com.example.testawal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

/*    public static final String DBNAME="test.db";
    public DBHelper(Context context) {
        super(context, "test.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users(fullname TEXT primary key, email TEXT, phonenumber TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");
    }*/

    private static final String TABLE_NAME = "users";
    private static final String COL1 = "fullname";
    private static final String COL2 = "email";
    private static final String COL3 = "phonenumber";
    private static final String COL4 = "password";

    //name diisi nama table
    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    //onCreate artinya ketika pertama kali method ini dipanggil
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+"("+COL1+" TEXT PRIMARY KEY, "+COL2+" TEXT, "+COL3+" TEXT, "+COL4+" TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "drop table if exists " + TABLE_NAME;
        db.execSQL(query);
    }

    public Boolean insertData(String fullname, String email, String phonenumber, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("fullname", fullname);
        values.put("email", email);
        values.put("phonenumber", phonenumber);
        values.put("password", password);

        long result = db.insert("users", null, values);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkemail(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=?", new String[] {email});
        if(cursor.getCount()>0) return true;
        else
            return false;
    }

    public Boolean checkemailpassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where email=? and password=?", new String[] {email, password});
        if(cursor.getCount()>0) return true;
        else
            return false;
    }
}
