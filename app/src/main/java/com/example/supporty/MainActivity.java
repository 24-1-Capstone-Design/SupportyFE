package com.example.supporty;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
        apiServiceInterface.loginRequest(data).enqueue(new Callback<SignupRes>() {
            @Override
            public void onResponse(@NonNull Call<SignupRes> call, @NonNull Response<SignupRes> response) {
                SignupRes result = response.body();
                assert result != null;
                int statusCode = response.code();
                if(statusCode==200) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<SignupRes> call, Throwable t) {
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
        apiServiceInterface = retrofit.create(ApiService.class);

        goSignup = findViewById(R.id.goSignup);

        //회원가입 버튼 누르면 회원가입 창으로 넘어감
        goSignup.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = loginId.getText().toString();
                String passwd = loginPasswd.getText().toString();
                login(new SignupData(id, passwd));
            }
        });


    }
}