package com.example.supporty.goal;
// GoalInfoItem
// 목표 정보 저장 객체
import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

import java.io.Serial;

@org.parceler.Parcel
public class GoalData {    // 사용자가 목표 추가 시 서버에 보낼 데이터
    @SerializedName("id")
    private String id;
    @SerializedName("passwd")
    private String passwd;

    @SerializedName("goal_id")
    private String goalId;

    @SerializedName("goal_title")
    private String goalTitle;

    @SerializedName("goal_content")
    private String goalContent;

    @SerializedName("is_achieved")
    private String isAcieved;

    @SerializedName("goal_date")
    private String goalDate;


    public GoalData(String id, String passwd) {
        this.id = id;
        this.passwd = passwd;
        this.goalId = goal_id;
    }
}

    /*
    @Override
    public String toString() {
        return "GoalInfoItem"{" +
    }
}
*/



