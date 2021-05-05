package com.example.nvhuy.wallpaper.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.nvhuy.wallpaper.model.Liked_image;

import java.util.ArrayList;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    private  SQLiteDatabase db;
    private static final String TAG = "SQLITEHELPER";
    private static final String DATABASE_NAME = "IMAGE.DB";
    private static final String TABLE_IMAGE = "IMAGE";
    private static final String COLUMN_ID_IMAGE = "idImage";
    private static final String COLUMN_ID_USER= "idUser";
    private static final int DATABASE_VERSION = 1;
    public MyDatabaseHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String imageTable = "CREATE TABLE " + TABLE_IMAGE + "("
                + COLUMN_ID_IMAGE + " INTEGER PRIMARY KEY,"
                + COLUMN_ID_USER + " TEXT" + ")";
        db.execSQL(imageTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);


        // Recreate
        onCreate(db);
    }
    public void insertImage(Liked_image image) {

        Log.i(TAG, "MyDatabaseHelper.insert ... "+ image.getIdImage()  );
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID_IMAGE, image.getIdImage());
        values.put(COLUMN_ID_USER, image.getIdUser());
        // Inserting Row
        db.insert(TABLE_IMAGE, null, values);

        // Closing database connection
        db.close();
    }

    public void deleteImage(String id_image) {
        Log.i(TAG, "MyDatabaseHelper.update ... " + id_image );

        db = this.getWritableDatabase();
        db.delete(TABLE_IMAGE, COLUMN_ID_IMAGE + " = ?",
                new String[] { id_image });
        db.close();
    }
    public void unfollow(String id_user) {
        Log.i(TAG, "MyDatabaseHelper.delete ... " + id_user );

        db = this.getWritableDatabase();
        db.delete(TABLE_IMAGE, COLUMN_ID_USER + " = ?",
                new String[] { id_user });
        db.close();
    }

    public boolean checkLiked(String s){
        db = this.getWritableDatabase();
        ArrayList<Liked_image> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()){
            String id_image =cursor.getString(0);
            String id_user =cursor.getString(1);
            list.add(new Liked_image(id_image,id_user));
        }
        for(Liked_image x:list){
            if (x.getIdImage().equals(s)){
                return true;
            }
        }
        return false;
    }
    public boolean checkFollow(String s){
        db = this.getWritableDatabase();
        ArrayList<Liked_image> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        while (cursor.moveToNext()){
            String id_image =cursor.getString(0);
            String id_user =cursor.getString(1);
            list.add(new Liked_image(id_image,id_user));
        }
        for(Liked_image x:list){
            if (x.getIdUser().equals(s)){
                return true;
            }
        }
        return false;
    }

}
