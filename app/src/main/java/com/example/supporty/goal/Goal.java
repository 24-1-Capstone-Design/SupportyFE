package com.example.supporty.goal;
import com.google.gson.annotations.SerializedName;

public class Goal {
    @SerializedName("goalId")
    private int goalId;

    @SerializedName("goalTitle")
    private String goalTitle;

    @SerializedName("goalContent")
    private String goalContent;

    @SerializedName("isAchieved")
    private String isAchieved;

    @SerializedName("goalDate")
    private String goalDate;

    public int getGoalId() {
        return goalId;
    }

    public String getGoalTitle() {
        return goalTitle;
    }

    public String getGoalContent() {
        return goalContent;
    }

    public String getIsAchieved() {
        return isAchieved;
    }

    public String getGoalDate() { return goalDate; }

}