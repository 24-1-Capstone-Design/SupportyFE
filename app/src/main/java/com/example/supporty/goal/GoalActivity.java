package com.example.supporty.goal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar; //툴바
import com.google.android.material.bottomnavigation.BottomNavigationView; //네비게이션바
import android.view.Menu;
import android.view.MenuItem;
public class GoalActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    //public static Item = currentItem = null;

}










/*
public class GoalActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private GoalListFragment goalListFragment;
    private AddGoalFragment addGoalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        fragmentManager = getSupportFragmentManager();
        goalListFragment = new GoalListFragment();
        addGoalFragment = new AddGoalFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, goalListFragment)
                .commit();
    }

    // Method to switch to AddGoalFragment
    public void showAddGoalFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addGoalFragment)
                .addToBackStack(null)
                .commit();
    }

    // Method to add goal and switch back to GoalListFragment
    public void addGoalAndShowListFragment(Goal goal) {
        // Call Retrofit API to add goal to database
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.addGoal(goal);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // Handle success response
                fragmentManager.popBackStack();
                goalListFragment.refreshGoalList();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                Toast.makeText(GoalActivity.this, "Failed to add goal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/
