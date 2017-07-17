package com.example.dasaraa.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by dasaraa on 7/17/2017.
 */

public class Singleton {

    private static Singleton singleton;

    private RequestQueue requestQueue;

    private static Context context;


    private Singleton(Context context){
        this.context=context;
        requestQueue=getRequestQueue();

    }

    private  RequestQueue getRequestQueue(){
        if(requestQueue==null)
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        return  requestQueue;
    }

    public static  Singleton getInstance(Context context){
        if(singleton==null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton(context);
                }
            }
        }
        return  singleton;
    }

    public<T> void addToRequestQue(Request<T> request){
        getRequestQueue().add(request);
    }
}
