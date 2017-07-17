package com.example.dasaraa.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;

    ArrayList<PatientDetails> list=new ArrayList<PatientDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       readFromLocalStorage();
        /*broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocalStorage();

            }
        };*/
        saveToAppServer("ajay");
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null&&networkInfo.isConnected());
    }

    public void saveToLocalStorage(String name,int sync_status){
        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        PatientDetails patientDetails=new PatientDetails("Ajay","Dasaraju",100,"120");

        try {
            dbHelper.saveToLocalDatabase(patientDetails, sync_status, sqLiteDatabase);
            readFromLocalStorage();
            dbHelper.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void readFromLocalStorage(){
        list.clear();
        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase sqLiteDatabase=dbHelper.getReadableDatabase();
        Cursor cursor=dbHelper.getPatientDetails(sqLiteDatabase);
        while (cursor.moveToNext()){
            String name=cursor.getString(cursor.getColumnIndex(DBController.FIRST_NAME));
            int sync_status=cursor.getInt(cursor.getColumnIndex(DBController.SYNC_STATUS));
            list.add(new PatientDetails());
        }
        cursor.close();
        dbHelper.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(broadcastReceiver,new IntentFilter(DBHelper.UI_UPDATE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void saveToAppServer(final String name){
        DBHelper dbHelper=new DBHelper(this);
        SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();
        if(checkNetworkConnection()){
            StringRequest stringRequest=new StringRequest(Request.Method.POST, DBHelper.URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String rsponse = jsonObject.get("response").toString();
                                if (response.equals("OK")) {
                                    saveToLocalStorage(name, DBHelper.SYNC_STATUS_OK);
                                } else {
                                    saveToLocalStorage(name, DBHelper.SYNC_STATUS_FAILED);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error){
                    saveToLocalStorage(name,DBHelper.SYNC_STATUS_FAILED);

                }
            }){
                @Override
                protected Map<String,String> getParams() throws AuthFailureError{
                    Map<String,String> params=new HashMap<String,String>();
                    params.put("name",name);

                    return params;
                }
            };
            Singleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
            saveToLocalStorage(name,DBHelper.SYNC_STATUS_OK);

        }else{
            saveToLocalStorage(name,DBHelper.SYNC_STATUS_FAILED);
        }
        readFromLocalStorage();
        dbHelper.close();

    }
}
