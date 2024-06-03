package com.example.supporty;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.supporty.goal.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class RecordActivity extends AppCompatActivity {

    private Button goDiary, goGoal, goDrug;
    private CalendarView calendarView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);

        goDiary = findViewById(R.id.goDiary);
        goGoal = findViewById(R.id.goGoal);
        goDrug = findViewById(R.id.goDrug);

        goDiary.setOnClickListener(v -> {
            Intent intent = new Intent(RecordActivity.this, DiaryActivity.class);
            startActivity(intent);
        });
        goGoal.setOnClickListener(v -> {
            Intent intent = new Intent(RecordActivity.this, GoalActivity.class);
            startActivity(intent);
        });

        //캘린더 뷰
        calendarView = findViewById(R.id.calendarView);
        //현재 날짜 가져옴
        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();
        //현재 날짜로 캘린더뷰 초기화
        calendarView.setDate(currentTime, false, true);

        //날짜 선택되었을 때의 처리 여기다가 코드 작성
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

        });
        
        //하단 네비게이션바 코드
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_home) {
                Intent homeIntent = new Intent(RecordActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            } else if (menuItem.getItemId() == R.id.navigation_mypage) {
                Intent mypageIntent = new Intent(RecordActivity.this, MypageActivity.class);
                startActivity(mypageIntent);
            } else if(menuItem.getItemId() == R.id.navigation_record) {
                Intent recordIntent = new Intent(RecordActivity.this, RecordActivity.class);
                startActivity(recordIntent);
            }
            return false;
        });

    }
}
