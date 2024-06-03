package com.example.supporty.treatment;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TreatmentData { // 서버에 보낼 데이터
    @SerializedName("id")
    private String id;

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


    public TreatmentData(String id, int visitId, Date visitDate, Date nextVisitDate, String visitMemo, String hospitalName) {
        this.id = id;
        this.visitId = visitId;
        this.visitDate = visitDate;
        this.nextVisitDate = nextVisitDate;
        this.visitMemo = visitMemo;
        this.hospitalName = hospitalName;
    }
}
