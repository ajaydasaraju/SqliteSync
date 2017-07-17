package com.example.dasaraa.myapplication;

/**
 * Created by dasaraa on 7/17/2017.
 */

public class PatientDetails {

    private String firstName;

    private String lastName;

    private String email;

    private float patientWeight;

    private String patientBloodPressure;


    public PatientDetails(){

    }


    public PatientDetails(String firstName, String lastName,float patientWeight, String patientBloodPressure) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patientWeight = patientWeight;
        this.patientBloodPressure = patientBloodPressure;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(float patientWeight) {
        this.patientWeight = patientWeight;
    }



    public String getPatientBloodPressure() {
        return patientBloodPressure;
    }

    public void setPatientBloodPressure(String patientBloodPressure) {
        this.patientBloodPressure = patientBloodPressure;
    }

}
