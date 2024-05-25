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
import com.example.supporty.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import retrofit2.Retrofit;
import android.view.MenuItem;

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

    public void showAddGoalFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addGoalFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showGoalListFragment() {
        fragmentManager.popBackStack();
    }
}
