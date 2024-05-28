package com.example.supporty.goal;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class GoalRes { // 서버로부터 받을 데이터

    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("goalId")
    private int goalId;

    @SerializedName("goalTitle")
    private String goalTitle;

    @SerializedName("goalContent")
    private String goalContent;

    @SerializedName("isAchieved")
    private boolean isAchieved;

    @SerializedName("goalDate")
    private Date goalDate;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public int getGoalId() {
        return goalId;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public String getGoalContent() {
        return goalContent;
    }

    public boolean isAchieved() {
        return isAchieved;
    }

    public Date getGoalDate() {
        return goalDate;
    }

    @Override
    public String toString() {
        return "GoalRes{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", goalId='" + goalId + '\'' +
                ", goalTitle='" + goalTitle + '\'' +
                ", goalContent='" + goalContent + '\'' +
                ", isAchieved=" + isAchieved +
                ", goalDate='" + goalDate + '\'' +
                '}';
    }
}
