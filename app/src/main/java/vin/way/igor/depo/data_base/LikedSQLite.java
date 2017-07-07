package vin.way.igor.depo.data_base;

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

public class LikedSQLite extends SQLiteOpenHelper {
    SQLiteDatabase db;
    private ArrayList<HashMap<String,String>>arrayList=new ArrayList<>();

    public LikedSQLite(Context context){
        super(context,"SQLiteTransport",null,1);
    }

    public void create_table()
    {
        db.execSQL("create table "+ "liked_transport" +" (" +
                "id integer primary key autoincrement,"+
                "type text,"+
                "number text,"+
                "route text,"+
                "first_name text,"+
                "last_name text,"+
                "begin_time text,"+
                "end_time text,"+
                "from_depo text,"+
                "to_depo text,"+
                "time_interval text,"
                +");");
        Log.e("LIKEDSQLITEcReated","OK");
    }

    public void Add_Item(String first_name, String last_name,String type,String number,String route,String begin_time,String end_time,String from_depo,String to_depo,String time_interval)
    {
        db = this.getWritableDatabase();
        ContentValues contentValues;
        contentValues=new ContentValues();
        contentValues.put("type",type);
        contentValues.put("number",number);
        contentValues.put("route",route);
        contentValues.put("first_name",first_name);
        contentValues.put("last_name",last_name);
        contentValues.put("begin_time",begin_time);
        contentValues.put("end_time",end_time);
        contentValues.put("from_depo",from_depo);
        contentValues.put("to_depo",to_depo);
        contentValues.put("time_interval",time_interval);

        Cursor cursor=db.rawQuery("select 1 from liked_transport where route=?",new String[]{contentValues.get("route").toString()});
        boolean exist=(cursor.getCount()>0);
        Log.e("***Cursor:",cursor.getCount()+"");
        cursor.close();
        long rows=0;
        if(!exist)
        {
             rows=db.insert("liked_transport",null,contentValues);
             Log.e("***ADD***","row inserted "+contentValues.toString()+" || "+rows);
        }
        else{
            Log.e("***ADD***"," such row already exists");
        }

        db.close();
    }

    public void Delete_Item(String number,String type){
        Log.e("Trying to delete",type+" number "+number);
        ContentValues contentValues;
        contentValues=new ContentValues();
        db = this.getWritableDatabase();
        db.beginTransaction();
        Boolean toDrop=false;
            try{
            db.execSQL("create table " + "liked_transport_tmp" + " (" +
                    "id integer primary key autoincrement," +
                    "type text," +
                    "number text," +
                    "route text," +
                    "first_name text," +
                    "last_name text," +
                    "begin_time text," +
                    "end_time text," +
                    "from_depo text," +
                    "to_depo text," +
                    "time_interval text"
                    + ");");
                Cursor cursor=db.rawQuery("SELECT * FROM liked_transport",null);
                for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
                {
                    contentValues.clear();
                    int first_nameColIndex=cursor.getColumnIndex("first_name");
                    int number_nameColIndex=cursor.getColumnIndex("number");
                    int last_nameColIndex=cursor.getColumnIndex("last_name");
                    int typeColIndex=cursor.getColumnIndex("type");
                    int routeColIndex=cursor.getColumnIndex("route");
                    int begin_timeColIndex=cursor.getColumnIndex("begin_time");
                    int end_timeColIndex=cursor.getColumnIndex("end_time");
                    int from_depoColIndex=cursor.getColumnIndex("from_depo");
                    int to_depoColIndex=cursor.getColumnIndex("to_depo");
                    int time_intervalColIndex=cursor.getColumnIndex("time_interval");

                    if(cursor.getString(typeColIndex).equals(type) && cursor.getString(number_nameColIndex).equals(number) )
                    {
                        toDrop=true;
                    }
                    else{
                        contentValues.put("first_name",cursor.getString(first_nameColIndex));
                        contentValues.put("number",cursor.getString(number_nameColIndex));
                        contentValues.put("type",cursor.getString(typeColIndex));
                        contentValues.put("last_name",cursor.getString(last_nameColIndex));
                        contentValues.put("route",cursor.getString(routeColIndex));
                        contentValues.put("begin_time",cursor.getString(begin_timeColIndex));
                        contentValues.put("end_time",cursor.getString(end_timeColIndex));
                        contentValues.put("from_depo",cursor.getString(from_depoColIndex));
                        contentValues.put("to_depo",cursor.getString(to_depoColIndex));
                        contentValues.put("time_interval",cursor.getString(time_intervalColIndex));
                        db.insert("liked_transport_tmp",null,contentValues);
                    }
                    Log.e("ContentValues to Drop","Number= "+cursor.getString(number_nameColIndex)+" |Type= "+ cursor.getString(typeColIndex));
                }
                cursor.close();

                Log.e("Content values puted","OK");

                if(toDrop){
                Log.e("Droped collumn","True");
                db.execSQL("drop table liked_transport;");
                db.execSQL("create table " + "liked_transport" + " (" +
                        "id integer primary key autoincrement," +
                        "type text," +
                        "number text," +
                        "route text," +
                        "first_name text," +
                        "last_name text," +
                        "begin_time text," +
                        "end_time text," +
                        "from_depo text," +
                        "to_depo text," +
                        "time_interval text"
                        + ");");

                db.execSQL("insert into liked_transport select id, type, number,route,first_name," +
                        "last_name,begin_time,end_time,from_depo,to_depo,time_interval from liked_transport_tmp;");
                db.execSQL("drop table liked_transport_tmp;");
            }
            else{
                Log.e("Droped collumn","False");
                db.execSQL("drop table liked_transport_tmp;");
            }
            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
            db.close();
            }
    }

    public ArrayList<HashMap<String,String>> Get_Liked_Array()
    {
        arrayList=new ArrayList<>();
                db = this.getWritableDatabase();
                Cursor cursor=db.rawQuery("SELECT * FROM liked_transport",null);
                    for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
                    {
                        HashMap<String,String>hm=new HashMap<>();
                        int first_nameColIndex=cursor.getColumnIndex("first_name");
                        int number_nameColIndex=cursor.getColumnIndex("number");
                        int last_nameColIndex=cursor.getColumnIndex("last_name");
                        int typeColIndex=cursor.getColumnIndex("type");
                        int routeColIndex=cursor.getColumnIndex("route");
                        int begin_timeColIndex=cursor.getColumnIndex("begin_time");
                        int end_timeColIndex=cursor.getColumnIndex("end_time");
                        int from_depoColIndex=cursor.getColumnIndex("from_depo");
                        int to_depoColIndex=cursor.getColumnIndex("to_depo");
                        int time_intervalColIndex=cursor.getColumnIndex("time_interval");

                        hm.put("first_name",cursor.getString(first_nameColIndex));
                        hm.put("number",cursor.getString(number_nameColIndex));
                        hm.put("type",cursor.getString(typeColIndex));
                        hm.put("last_name",cursor.getString(last_nameColIndex));
                        hm.put("route",cursor.getString(routeColIndex));
                        hm.put("begin_time",cursor.getString(begin_timeColIndex));
                        hm.put("end_time",cursor.getString(end_timeColIndex));
                        hm.put("from_depo",cursor.getString(from_depoColIndex));
                        hm.put("to_depo",cursor.getString(to_depoColIndex));
                        hm.put("time_interval",cursor.getString(time_intervalColIndex));

                        arrayList.add(hm);
                    }
        Log.e("***|SQListSize|***:"," "+arrayList.size());
       Log.e("Version",db.getVersion()+"");
        cursor.close();
        db.close();
        return arrayList;
    }

    public Boolean isLiked(String number,String type){
        db = this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM liked_transport",null);
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
        {
            int number_nameColIndex=cursor.getColumnIndex("number");
            int typeColIndex=cursor.getColumnIndex("type");
            if (cursor.getString(number_nameColIndex).equals(number)
                    && cursor.getString(typeColIndex).equals(type))
            {
                cursor.close();
                db.close();
                return true;
            }
        }
        cursor.close();
        db.close();
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
       // db.execSQL("drop table taxi;");
        db.execSQL("create table " + "liked_transport" + " (" +
                    "id integer primary key autoincrement," +
                    "type text," +
                    "number text," +
                    "route text," +
                    "first_name text," +
                    "last_name text," +
                    "begin_time text," +
                    "end_time text," +
                    "from_depo text," +
                    "to_depo text," +
                    "time_interval text"
                    + ");");
        Log.e("LIKEDSQLITEcReated", "OK");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*if (oldVersion==1 && newVersion==2) {
            ContentValues contentValues;
            contentValues=new ContentValues();
            db.beginTransaction();
            try
            {
                db.execSQL("create table " + "liked_transport" + " (" +
                        "id integer primary key autoincrement," +
                        "type text," +
                        "number text," +
                        "route text," +
                        "first_name text," +
                        "last_name text," +
                        "begin_time text," +
                        "end_time text," +
                        "from_depo text," +
                        "to_depo text," +
                        "time_interval text"
                        + ");");

                    for(int i=0;i<arrayList.size();i++)
                    {
                        contentValues.clear();
                        contentValues.put("first_name",arrayList.get(i).get("first_name"));
                        contentValues.put("number",arrayList.get(i).get("number"));
                        contentValues.put("type",arrayList.get(i).get("type"));
                        contentValues.put("last_name",arrayList.get(i).get("last_name"));
                        contentValues.put("route",arrayList.get(i).get("route"));
                        contentValues.put("begin_time",arrayList.get(i).get("begin_time"));
                        contentValues.put("end_time",arrayList.get(i).get("end_time"));
                        contentValues.put("from_depo",arrayList.get(i).get("from_depo"));
                        contentValues.put("to_depo",arrayList.get(i).get("to_depo"));
                        contentValues.put("time_interval",arrayList.get(i).get("time_interval"));


                        db.insert("liked_transport",null,contentValues);
                    }
                db.execSQL("create table " + "liked_transport_tmp" + " (" +
                        "id integer primary key autoincrement," +
                        "type text," +
                        "number text," +
                        "route text," +
                        "first_name text," +
                        "last_name text," +
                        "begin_time text," +
                        "end_time text," +
                        "from_depo text," +
                        "to_depo text," +
                        "time_interval text"
                        + ");");
                db.execSQL("insert into liked_transport_tmp select id, type, number,route,first_name," +
                        "last_name,begin_time,end_time,from_depo,to_depo,time_interval from liked_transport;");
                db.execSQL("drop table liked_transport;");

                db.execSQL("create table " + "liked_transport" + " (" +
                        "id integer primary key autoincrement," +
                        "type text," +
                        "number text," +
                        "route text," +
                        "first_name text," +
                        "last_name text," +
                        "begin_time text," +
                        "end_time text," +
                        "from_depo text," +
                        "to_depo text," +
                        "time_interval text"
                        + ");");

                db.execSQL("insert into liked_transport select id, type, number,route,first_name," +
                        "last_name,begin_time,end_time,from_depo,to_depo,time_interval from liked_transport_tmp;");
                db.execSQL("drop table liked_transport_tmp;");

                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
            }
        }*/
    }


}
