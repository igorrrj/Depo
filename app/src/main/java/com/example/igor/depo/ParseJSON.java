package com.example.igor.depo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ParseJSON {



    static ArrayList<ArrayList<HashMap<String, String>> >result;

    static ArrayList<HashMap<String, String>>result_demo;

    private JSONArray trams = null,demo=null;

    private String json;
    public ParseJSON(String json){
        this.json = json;
        result=new ArrayList<>();
        result_demo=new ArrayList<>();
    }



    /*

    JSONArray jsonArray=new JSONArray(json);

    for(int i=0;i<jsonArray.length();++i)
    {
        JSONObject jsonObject=jsonArray.getJSONObject(i);
        JSONArray jsonArray1=new JSONArray(jsonObject);
        for(int j=0;j<jsonArray1.length();++j)
        {

        }
    }
*/
    protected void parseJSON(){
        try {

             trams = new JSONArray(json);

            for(int i=0;i<trams.length();i++){
                JSONArray jsonArray1 = trams.getJSONArray(i);
                for(int j=0;j<jsonArray1.length();++j)
                {
                    JSONObject dd=jsonArray1.getJSONObject(j);

                    Log.e("OBJ", dd.getString("first_name"));

                    HashMap<String,String>map=new HashMap<>();

                    map.put("id",dd.getString("id"));
                    map.put("first_name",dd.getString("first_name"));
                    map.put("last_name",dd.getString("last_name"));
                    map.put("number_name",dd.getString("number_name"));
                    result_demo.add(map);

                }
                result.add(result_demo);
                result_demo=new ArrayList<>();

            }


            Log.e("LIST", result.toString());
            Log.e("LIST_SIZE:", result.size()+"");


        } catch (JSONException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }
}