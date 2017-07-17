package com.example.dasaraa.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dasaraa on 7/17/2017.
 */

public class NetworkMonitor extends BroadcastReceiver{

    public boolean checkNetworkConnection(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null&&networkInfo.isConnected());
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(checkNetworkConnection(context)){

            final DBHelper  dbHelper=new DBHelper(context);
            final SQLiteDatabase sqLiteDatabase=dbHelper.getWritableDatabase();

            Cursor cursor=dbHelper.getPatientDetails(sqLiteDatabase);

            while(cursor.moveToNext()){
                int sync_status=cursor.getInt(cursor.getColumnIndex(DBController.SYNC_STATUS));
                if(sync_status==DBHelper.SYNC_STATUS_FAILED){
                    final String firstName=cursor.getString(cursor.getColumnIndex(DBController.FIRST_NAME));
                    final String lastName=cursor.getString(cursor.getColumnIndex(DBController.LAST_NAME));
                    final String patientBloodPressure=cursor.getString(cursor.getColumnIndex(DBController.PATIENT_BLOOD_PRESSURE));
                    final float patientWeight=cursor.getInt(cursor.getColumnIndex(DBController.PATIENT_BLOOD_PRESSURE));
                    final PatientDetails patientDetails=new PatientDetails(firstName,lastName,patientWeight,patientBloodPressure);

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, DBHelper.URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String rsponse=jsonObject.get("response").toString();
                                        if(rsponse.equals("OK")){
                                            dbHelper.saveToLocalDatabase(patientDetails,DBHelper.SYNC_STATUS_OK,sqLiteDatabase);
                                            context.sendBroadcast(new Intent(DBHelper.UI_UPDATE_BROADCAST));
                                        }
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError{
                            HashMap<String,String> map=new HashMap<String, String>();
                            map.put("FirstName",firstName);
                            map.put("lastName",lastName);
                            map.put("PatientBloodPressure",patientBloodPressure);
                            map.put("PatientWeight",""+patientWeight);
                            return map;
                        }
                    };
                    Singleton.getInstance(context).addToRequestQue(stringRequest);
                }
            }

            dbHelper.close();

        }
    }
}
