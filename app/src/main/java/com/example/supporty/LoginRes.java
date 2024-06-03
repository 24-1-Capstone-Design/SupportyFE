package com.example.supporty;

import com.google.gson.annotations.SerializedName;

public class LoginRes { //회원가입 시 서버로부터 받을 데이터
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("user")
    private User user;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

}

class User { // db로부터 받은 User 테이블 속성들
    @SerializedName("id")
    private String id;


    @SerializedName("nickname")
    private String nickname;

    @SerializedName("join_date")
    private String joinDate;

    public String getId() { return id; }

    public String getNickname() {
        return nickname;
    }

    public String getJoinDate() {
        return joinDate;
    }
}
