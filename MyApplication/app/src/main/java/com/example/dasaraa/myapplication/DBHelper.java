package com.example.dasaraa.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dasaraa on 7/17/2017.
 */

public class DBHelper  extends SQLiteOpenHelper {

    public static final int SYNC_STATUS_OK=0;

    public static final int SYNC_STATUS_FAILED=1;

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "PMA12";

    // Contacts table name
    public static final String TABLE_CONTACTS = "patient";

    public static final  String URL="http://10.0.0.7:8888/api/patient";

    public static final String UI_UPDATE_BROADCAST="com.example.myapplication.uiupdatebroadcast";

    // Patient  Table Columns names




   /*public static final String CREATE_TABLE="create table "+TABLE_CONTACTS+"(id integer primary key autoincrement,"+DBController.FIRST_NAME+" text,"
            +DBController.LAST_NAME+" text,"+DBController.PATIENT_BLOOD_PRESSURE+" text,"+DBController.SYNC_STATUS+
            " integer);";*/
   public static final String CREATE_TABLE="create table "+DBController.TABLE_CONTACTS+"(id integer primary key autoincrement,"+DBController.NAME+" text,"+DBController.SYNC_STATUS+
           " integer);";

    public static final String DROP_TABLE="drop table if exists "+TABLE_CONTACTS;

    public DBHelper(Context context) {
        super(context, DBController.DATABASE_NAME, null,DBController.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL(DROP_TABLE);

        // Create tables again
        onCreate(db);

    }

    public void saveToLocalDatabase(PatientDetails patientDetails,int sync_status,SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues=new ContentValues();
        contentValues.put(DBController.FIRST_NAME,patientDetails.getFirstName());
        contentValues.put(DBController.LAST_NAME,patientDetails.getLastName());
        contentValues.put(DBController.PATIENT_BLOOD_PRESSURE,patientDetails.getPatientBloodPressure());
        contentValues.put(DBController.PATIENT_WEIGHT,patientDetails.getPatientWeight());
        contentValues.put(DBController.SYNC_STATUS,sync_status);
        sqLiteDatabase.insert(TABLE_CONTACTS,null,contentValues);

    }

    public Cursor getPatientDetails(SQLiteDatabase sqLiteDatabase){
        Cursor res=null;
        String[] projection={DBController.FIRST_NAME,DBController.LAST_NAME,DBController.PATIENT_WEIGHT,DBController.PATIENT_BLOOD_PRESSURE,DBController.SYNC_STATUS};
        try {
            res=sqLiteDatabase.query(TABLE_CONTACTS,projection,null,null,null,null,null);
        }catch(Exception e){
            e.printStackTrace();
        }

        return res;

    }
}
