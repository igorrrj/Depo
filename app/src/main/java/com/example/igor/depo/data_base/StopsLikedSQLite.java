package com.example.igor.depo.data_base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Igor on 29.03.2017.
 */

public class StopsLikedSQLite extends SQLiteOpenHelper {
    SQLiteDatabase db;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    int cnt = 0;
    final int DB_VERSION = 1;

    public StopsLikedSQLite(Context context) {
        super(context, "myDB", null, 2);
    }

    public void create_table() {
        db.execSQL("create table " + "liked_stops" + " (" +
                "id integer primary key autoincrement," +
                "name text," +
                "stops_list text"
                + ");");
    }

    public void Add_Item_Stop(String name, String stops_list) {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("stops_list", stops_list);


        Cursor cursor = db.rawQuery("select 1 from liked_stops where name=?", new String[]{contentValues.get("name").toString()});
        boolean exist = (cursor.getCount() > 0);
        Log.e("***Cursor:", cursor.getCount() + "");
        cursor.close();
        long rows = 0;
        if (!exist) {
            rows = db.insert("liked_stops", null, contentValues);
            Log.e("***ADD***", "row inserted " + contentValues.toString() + " || " + rows);
        } else {
            Log.e("***ADD***", " such row already exists");
        }

        db.close();
    }

    public void Delete_Item(String name) {
        Log.e("Trying to delete", name);
        ContentValues contentValues;
        contentValues = new ContentValues();
        db = this.getWritableDatabase();
        db.beginTransaction();
        Boolean toDrop = false;
        try {
            db.execSQL("create table " + "liked_stops_tmp" + " (" +
                    "id integer primary key autoincrement," +
                    "name text," +
                    "stops_list text"
                    + ");");
            Cursor cursor = db.rawQuery("SELECT * FROM liked_stops", null);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                contentValues.clear();
                int nameColIndex = cursor.getColumnIndex("name");
                int stops_listColIndex = cursor.getColumnIndex("stops_list");


                if (cursor.getString(nameColIndex).equals(name)) {
                    toDrop = true;
                } else {
                    contentValues.put("name", cursor.getString(nameColIndex));
                    contentValues.put("stops_list", cursor.getString(stops_listColIndex));
                    db.insert("liked_stops_tmp", null, contentValues);
                }
                Log.e("ContentValues to Drop", "Number= " + cursor.getString(nameColIndex));
            }
            cursor.close();

            Log.e("Content values puted", "OK");

            if (toDrop) {
                Log.e("Droped collumn", "True");
                db.execSQL("drop table liked_stops;");
                db.execSQL("create table " + "liked_stops" + " (" +
                        "id integer primary key autoincrement," +
                        "name text," +
                        "stops_list text"
                        + ");");

                db.execSQL("insert into liked_stops select id, name, stops_list from liked_stops_tmp;");
                db.execSQL("drop table liked_stops_tmp;");
            } else {
                Log.e("Droped collumn", "False");
                db.execSQL("drop table liked_stops_tmp;");
            }
            db.setTransactionSuccessful();

        } finally {

            db.endTransaction();
        }
    }

    public ArrayList<HashMap<String, String>> Get_Liked_Stops_Array() {
        arrayList = new ArrayList<>();
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM liked_stops", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            HashMap<String, String> hm = new HashMap<>();
            int nameColIndex = cursor.getColumnIndex("name");
            int stops_listColIndex = cursor.getColumnIndex("stops_list");

            hm.put("name", cursor.getString(nameColIndex));
            hm.put("stops_list", cursor.getString(stops_listColIndex));

            arrayList.add(hm);
        }
        Log.e("***|SQListSize|***:", " " + arrayList.size());
        Log.e("Version", db.getVersion() + "");
        return arrayList;
    }

    public Boolean isLiked(String name) {
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM liked_stops", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int nameColIndex = cursor.getColumnIndex("name");
            if (cursor.getString(nameColIndex).equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // db.execSQL("drop table taxi;");
        db.execSQL("create table " + "liked_stops" + " (" +
                "id integer primary key autoincrement," +
                "name text," +
                "stops_list text"
                + ");");
        Log.e("StopsLIKEDSQLITEcReated", "OK");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion==1 && newVersion==2) {
            ContentValues contentValues;
            contentValues=new ContentValues();
            db.beginTransaction();
            try
            {
                db.execSQL("create table " + "liked_stops" + " (" +
                        "id integer primary key autoincrement," +
                        "name text," +
                        "stops_list text"
                        + ");");

                for(int i=0;i<arrayList.size();i++)
                {
                    contentValues.clear();
                    contentValues.put("name",arrayList.get(i).get("name"));
                    contentValues.put("stops_list",arrayList.get(i).get("stops_list"));

                    db.insert("liked_stops",null,contentValues);
                }
                db.execSQL("create table " + "liked_stops_tmp" + " (" +
                        "id integer primary key autoincrement," +
                        "name text," +
                        "stops_list text"
                        + ");");
                db.execSQL("insert into liked_stops_tmp select id, name, stops_list from liked_stops;");
                db.execSQL("drop table liked_stops;");

                db.execSQL("create table " + "liked_stops" + " (" +
                        "id integer primary key autoincrement," +
                        "name text," +
                        "stops_list text"
                        + ");");

                db.execSQL("insert into liked_stops select id, name, stops_list from liked_stops_tmp;");
                db.execSQL("drop table liked_stops_tmp;");

                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
            }
        }
    }

}
