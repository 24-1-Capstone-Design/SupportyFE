package com.example.supporty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MypageActivity extends AppCompatActivity {
    //레트로핏 통신 위해 꼭 필요한 변수들 apiServiceInterface, retrofit
    private ApiService apiServiceInterface;
    private Retrofit retrofit = RetrofitClient.getClient();
    private String userId;
    private String nickname;
    private TextView userIdTextView;
    private TextView nicknameTextView;

    //회원탈퇴 버튼
    private Button delIdButton;

    //로그아웃 버튼
    private Button logoutButton;

    //회원탈퇴 함수
    private void delId(String id) {
        apiServiceInterface.deleteRequest(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Log.d("회원탈퇴", "회원탈퇴 성공");
                    Intent intent = new Intent(MypageActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(MypageActivity.this, SignupActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("회원탈퇴 오류",t.getMessage());
                t.printStackTrace();
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mypage);

        //하단 네비게이션바 코드
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_home) {
                Intent homeIntent = new Intent(MypageActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            } else if (menuItem.getItemId() == R.id.navigation_mypage) {
                Intent mypageIntent = new Intent(MypageActivity.this, MypageActivity.class);
                startActivity(mypageIntent);
            } else if(menuItem.getItemId() == R.id.navigation_record) {
                Intent recordIntent = new Intent(MypageActivity.this, RecordActivity.class);
                startActivity(recordIntent);
            }
            return false;
        });

//레트로핏 통신 위해서 apiServiceInterface 변수에 레트로핏 객체 할당
        apiServiceInterface = retrofit.create(ApiService.class);

// 서버로부터 받은 유저 정보 얻고싶으면 SharedPreferencesManager.getUserId.. 등으로 쓰면 됨.
        userId = SharedPreferencesManager.getUserId(this);
        nickname = SharedPreferencesManager.getNickname(this);

        userIdTextView = findViewById(R.id.userId);
        userIdTextView.setText(userId);

        delIdButton = findViewById(R.id.deleteId);
        logoutButton = findViewById(R.id.logout);

        nicknameTextView = findViewById(R.id.nickname);
        nicknameTextView.setText(nickname);

        //회원탈퇴 버튼 누르면 delId 함수 호출
        delIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delId(userId);
            }
        });

        //로그아웃 버튼 누르면 SharedPreferencesManager 에서 정보들 삭제
        logoutButton.setOnClickListener(v -> {
            SharedPreferencesManager.Logout(MypageActivity.this);
            Intent intent = new Intent(MypageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}