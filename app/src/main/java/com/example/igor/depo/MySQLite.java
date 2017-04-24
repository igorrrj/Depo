package com.example.igor.depo;

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

public class MySQLite extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public ArrayList<HashMap<String,String>>arrayList=new ArrayList<>();
    int cnt=0;
    final int DB_VERSION=2;

    public MySQLite(Context context){
        super(context,"myDB",null,1);
    }

    public void create_table_for_transport(String name)
    {
        db.execSQL("create table "+ name +" (" +
                "id integer primary key autoincrement,"+
                "first_name text,"+
                "last_name text,"+
                    "number_name text");
    }


    public void Add_Taxi(String name, String numb_name)
    {

        db = this.getWritableDatabase();
        ContentValues contentValues;
        contentValues=new ContentValues();
        contentValues.put("first_name",name);
        contentValues.put("number_name",numb_name);
        Cursor cursor=db.rawQuery("select 1 from taxi where first_name=?",new String[]{contentValues.get("first_name").toString()});
        boolean exist=(cursor.getCount()>0);
        Log.e("***Cursor:",cursor.getCount()+"");
        cursor.close();
        long rows=0;
        if(!exist)
        {
             rows=db.insert("taxi",null,contentValues);
        }

        Log.e("***ADD***","row inserted "+contentValues.toString()+" || "+rows);
        db.close();

    }

    public ArrayList<HashMap<String,String>> Get_Taxi()
    {
        arrayList=new ArrayList<>();
                db = this.getWritableDatabase();
                Cursor cursor=db.rawQuery("SELECT * FROM taxi",null);

                    for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
                    {
                        HashMap<String,String>hm=new HashMap<>();
                        int nameColIndex=cursor.getColumnIndex("first_name");
                        int number_nameColIndex=cursor.getColumnIndex("number_name");
                        hm.put("name",cursor.getString(nameColIndex));
                        hm.put("number",cursor.getString(number_nameColIndex));
                        arrayList.add(hm);
                    }





        Log.e("***|SQListSize|***:"," "+arrayList.size());
//        Log.e("Version",db.getVersion()+"");

        return arrayList;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("drop table taxi;");
        db.execSQL("create table "+ "taxi" +" (" +
                "id integer primary key autoincrement,"+
                "first_name text,"+
                "number_name text"+");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion==1 && newVersion==2) {
            ContentValues contentValues;
            contentValues=new ContentValues();
            db.beginTransaction();
            try
            {
                db.execSQL("create table "+ "taxi" +" (" +
                        "id integer primary key autoincrement,"+
                        "first_name text,"+
                        "number_name text"+");");

                    for(int i=0;i<arrayList.size();i++)
                    {
                        contentValues.clear();
                        contentValues.put("first_name",arrayList.get(i).get("name"));
                        contentValues.put("number_name",arrayList.get(i).get("number"));
                        db.insert("taxi",null,contentValues);
                    }
                    db.execSQL("create temporary table "+ "taxi_tmp" +" (" +
                            "id integer primary key autoincrement,"+
                            "first_name text,"+
                            "number_name text"+");");
                db.execSQL("insert into taxi_tmp select id, first_name, number_name from taxi;");
                db.execSQL("drop table taxi;");

                db.execSQL("create table "+ "taxi" +" (" +
                        "id integer primary key autoincrement,"+
                        "first_name text,"+
                        "number_name text"+");");
                db.execSQL("insert into taxi select id, first_name, number_name from taxi_tmp;");
                db.execSQL("drop table taxi_tmp;");


                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
            }

        }


    }
}
