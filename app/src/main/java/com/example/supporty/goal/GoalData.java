package com.example.supporty.goal;

import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.io.Serial;
import java.util.Date;
import java.util.UUID;

public class GoalData {    // 서버에 보낼 데이터
    @SerializedName("id")
    private String id;

    @SerializedName("goalId")
    private int goalId;

    @SerializedName("goalTitle")
    private String goalTitle;

    @SerializedName("goalContent")
    private String goalContent;

    @SerializedName("isAchieved")
    private Boolean isAcieved;

    @SerializedName("goalDate")
    private Date goalDate;


    public GoalData(String id, int goalId, String goalTitle, String goalContent, Boolean isAchieved, Date goalDate) {
        this.id = id;
        this.goalId = goalId;
        this.goalTitle = goalTitle;
        this.goalContent = goalContent;
        this.isAcieved = false; // 목표 생성될 때 디폴트 false(미달성)
        this.goalDate = goalDate;
    }
}