package com.example.supporty.goal;
// GoalInfoItem
// 목표 정보 저장 객체
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.io.Serial;
import java.util.Date;

public class GoalData {    // 사용자가 목표 추가 시 서버에 보낼 데이터
    @SerializedName("id")
    private String id;

    @SerializedName("goalTitle")
    private String goalTitle;

    @SerializedName("goalContent")
    private String goalContent;

    @SerializedName("isAchieved")
    private Boolean isAcieved;

    // goalId와 goalDate는 서버에서 생성

    public GoalData(String id, String goalTitle, String goalContent, Boolean isAchieved) {
        this.id = id;
        this.goalTitle = goalTitle;
        this.goalContent = goalContent;
        this.isAcieved = false; // 목표 생성될 때 디폴트 false(미달성)
    }
}