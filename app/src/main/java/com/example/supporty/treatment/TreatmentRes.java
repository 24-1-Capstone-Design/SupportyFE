package com.example.supporty.treatment;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TreatmentRes {  // 서버로부터 받을 데이터
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

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


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getVisitId() {
        return visitId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public Date getNextVisitDate() {
        return nextVisitDate;
    }

    public String stNextVisitDate() {
        if (nextVisitDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return dateFormat.format(nextVisitDate);
        } else {
            return "";
        }
    }


    public String getVisitMemo() {
        return visitMemo;
    }

    public String getHospitalName() {
        return hospitalName;
    }


    @Override
    public String toString() {
        return "VisitRes{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", visitId='" + visitId + '\'' +
                ", visitDate='" + visitDate + '\'' +
                ", nextVisitDate='" + nextVisitDate + '\'' +
                ", visitMemo=" + visitMemo +
                ", hospitalName=" + hospitalName +
                '}';
    }

}
