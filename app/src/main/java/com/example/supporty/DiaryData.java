package com.example.supporty;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiaryData {
    @SerializedName("id")
    private String id;
    @SerializedName("diary_date")
    private String diaryDate;
    @SerializedName("diary_content")
    private String diaryContent;
    @SerializedName("big_feeling")
    private String bigFeeling;
    @SerializedName("mid_feeling")
    private String midFeeling;
    @SerializedName("small_feeling")
    private String smallFeeling;

    public DiaryData(String id, String diaryDate, String diaryContent, String bigFeeling, String midFeeling, String smallFeeling) {
        this.id = id;
        this.diaryDate = diaryDate;
        this.diaryContent = diaryContent;
        this.bigFeeling = bigFeeling;
        this.midFeeling = midFeeling;
        this.smallFeeling= smallFeeling;
    }

    public String getDiaryContent() {
        return diaryContent;
    }
    public String getDiaryDate() {
    // 날짜 포맷 변경
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    try {
        Date date = inputFormat.parse(diaryDate);
        return outputFormat.format(date);
    } catch (ParseException e) {
        e.printStackTrace();
        return diaryDate; // 에러 발생 시 원래 날짜 그대로 반환
    }
   }

   public String getBigFeeling() {
        return bigFeeling;
   }

   public String getMidFeeling() {
        return midFeeling;
   }

   public String getSmallFeeling() {
        return smallFeeling;
   }



}

