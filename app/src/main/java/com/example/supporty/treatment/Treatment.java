package com.example.supporty.treatment;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Treatment {
    @SerializedName("visitId")
    private int visitId;

    @SerializedName("visitDate")
    private Date visitDate;

    @SerializedName("nextVisitDate")
    private Date nextVisitDate;

    @SerializedName("visitMemo")
    private String visitMemo;

    @SerializedName("hospitalName")
    private String hospitalName;


    public int getVisitId() {
        return visitId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public Date getNextVisitDate() {
        return nextVisitDate;
    }

    public String getVisitMemo() {
        return visitMemo;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setNextVisitDate(Date nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    public void setVisitMemo(String visitMemo) {
        this.visitMemo = visitMemo;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

}
