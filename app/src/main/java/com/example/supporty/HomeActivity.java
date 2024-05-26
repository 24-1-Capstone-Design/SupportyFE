package com.example.supporty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;



// 추후 삭제
import android.widget.Button;
import android.view.View;
import com.example.supporty.goal.GoalActivity;




public class HomeActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        //하단 네비게이션바 코드
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_home) {
                Intent homeIntent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            } else if (menuItem.getItemId() == R.id.navigation_mypage) {
                Intent mypageIntent = new Intent(HomeActivity.this, MypageActivity.class);
                startActivity(mypageIntent);
            } else if(menuItem.getItemId() == R.id.navigation_record) {
                Intent recordIntent = new Intent(HomeActivity.this, RecordActivity.class);
                startActivity(recordIntent);
            }
            return false;
        });


        // GoalActivity 실행 테스트를 위한 임시 코드 (추후 삭제)
        // Goal Activity로 이동하는 버튼 클릭 시
        Button goalButton = findViewById(R.id.goal_button);
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Goal Activity로 이동
                Intent intent = new Intent(HomeActivity.this, GoalActivity.class);
                startActivity(intent);
            }
        });

    }
}


