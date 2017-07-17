package com.example.dasaraa.myapplication;

/**
 * Created by dasaraa on 7/17/2017.
 */

public class DBController {

    public static final int SYNC_STATUS_OK=0;

    public static final int SYNC_STATUS_FAILED=1;

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "PMAAPP1";

    // Contacts table name
    public static final String TABLE_CONTACTS = "patient";

    public static final  String url="http://10.0.0.7:8888/api/patient";

    public static final String UI_UPDATE_BROADCAST="com.example.myapplication.uiupdatebroadcast";

    // Contacts Table Columns names
   /* public static final String KEY_ID = "id";*/
    public static final String NAME = "name";
    // public static final String KEY_LAST_NAME = "lastName";
    // public static final String KEY_EMAIL = "email";
  //  public static  final String SYNC_STATUS="syncstatuss";


    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PATIENT_WEIGHT = "patientWeight";
    public static final String SYNC_STATUS="syncStatus";
    public static final String PATIENT_BLOOD_PRESSURE="patientBloodPressure";
}
