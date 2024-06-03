package com.example.supporty;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private TextView goSignup;
    private Button loginButton;

    private EditText loginId, loginPasswd;

    private ApiService apiServiceInterface;
    private Retrofit retrofit = RetrofitClient.getClient();

    private void login(SignupData data) {
        Log.d("Login", "로그인 요청 시작");
        apiServiceInterface.loginRequest(data).enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(@NonNull Call<LoginRes> call, @NonNull Response<LoginRes> response) {
                Log.d("Login", "서버 응답 받음");
                if (response.isSuccessful()) {
                    Log.d("Login", "응답 성공");
                    //답장 온 body 를 LoginRes 객체에 넣음
                    LoginRes loginResponse = response.body();
                    if(loginResponse != null) {
                        //서버로부터 받은 정보들 변수에 저장
                        User user = loginResponse.getUser();
                        String userId = user.getId();
                        String nickname = user.getNickname();
                        String joinDate = user.getJoinDate();
                        SharedPreferencesManager.saveUserInfo(MainActivity.this, userId, nickname, joinDate);

                        SharedPreferencesManager.saveLoginInfo(MainActivity.this, userId, data.getPasswd());
                    }
                    else {
                        Log.d("Login", "응답 본문이 없음");
                        Toast.makeText(getApplicationContext(),"정보들이 없음", Toast.LENGTH_LONG).show();
                    }
                    int statusCode = response.code(); // 응답 상태 코드 가져오기
                    if (statusCode == 200) {
                        // 상태 코드가 200인 경우(성공한 경우)
                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d("Login", "상태 코드: " + response.code());
                        // 상태 코드가 200이 아닌 경우
                        Toast.makeText(getApplicationContext(), "상태 코드: " + statusCode, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("Login", "응답 실패");
                    // 서버의 응답이 실패한 경우
                    Toast.makeText(getApplicationContext(), "서버 응답 실패0", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginRes> call, Throwable t) {
                // 통신 실패 시 처리
                Log.e("로그인 오류", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //로그인 확인 버튼
        loginButton = findViewById(R.id.loginButton);
        loginId = findViewById(R.id.loginId);
        loginPasswd = findViewById(R.id.loginPasswd);
        goSignup = findViewById(R.id.goSignup);
        apiServiceInterface = retrofit.create(ApiService.class);

        //회원가입 버튼 누르면 회원가입 창으로 넘어감
        goSignup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "로그인 버튼 클릭됨");
                String id = loginId.getText().toString();
                String passwd = loginPasswd.getText().toString();
                //로그인 버튼 누르면 서버로 요청보내서 DB에 있는지 확인
                login(new SignupData(id, passwd));
            }
        });

        // 자동 로그인 처리 부분
        if(SharedPreferencesManager.isLoggedIn(this)) {
            String savedUserId = SharedPreferencesManager.getUserId(this);
            String savedPasswd = SharedPreferencesManager.getPasswd(this);
            if (savedUserId != null && !savedUserId.isEmpty() && savedPasswd != null && !savedPasswd.isEmpty()) {
                login(new SignupData(savedUserId, savedPasswd));
            }
        }
    }
}
