package com.example.supporty.goal;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.supporty.HomeActivity;
import com.example.supporty.MypageActivity;
import com.example.supporty.R;
import com.example.supporty.RecordActivity;
import com.example.supporty.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Retrofit;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.Toolbar;


public class GoalActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private GoalListFragment goalListFragment;
    private AddGoalFragment addGoalFragment;

    // Retrofit 통신
    private final String TAG = this.getClass().getSimpleName();
    private GoalApiService goalApiService;
    private Retrofit retrofit = RetrofitClient.getClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        goalApiService = retrofit.create(GoalApiService.class);

        fragmentManager = getSupportFragmentManager();
        goalListFragment = new GoalListFragment();
        addGoalFragment = new AddGoalFragment();

        // 처음에 목표 목록 프래그먼트를 표시
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, goalListFragment)
                .commit();




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("목표");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // 이전 버튼 아이콘 설정
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 버튼 클릭 시 RecordActivity로 이동
                Intent intent = new Intent(GoalActivity.this, RecordActivity.class);
                startActivity(intent);
                finish(); // 현재 액티비티 종료
            }
        });





        // 하단바
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_home) {
                    Intent homeIntent = new Intent(GoalActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (menuItem.getItemId() == R.id.navigation_mypage) {
                    Intent mypageIntent = new Intent(GoalActivity.this, MypageActivity.class);
                    startActivity(mypageIntent);
                    return true;
                }
                return false;
            }
        });
    }

    /*
    public void showAddGoalFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addGoalFragment)
                .addToBackStack(null)
                .commit();
    }
     */
    public void showAddGoalFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        AddGoalFragment addGoalFragment = new AddGoalFragment(); // 새 인스턴스 생성
        fragmentTransaction.replace(R.id.fragment_container, addGoalFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void showGoalListFragment() {
        fragmentManager.popBackStack();
    }
}
