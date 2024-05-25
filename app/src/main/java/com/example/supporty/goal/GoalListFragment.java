package com.example.supporty.goal;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import com.example.supporty.R;
import com.example.supporty.RetrofitClient;
import com.example.supporty.SharedPreferencesManager;

import org.w3c.dom.Text;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

public class GoalListFragment extends Fragment {

    private LinearLayout goalContainer;
    private Button addGoalButton;

    // Retrofit 통신
    private final String TAG = this.getClass().getSimpleName();
    private GoalApiService goalApiService;
    private Retrofit retrofit = RetrofitClient.getClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_list, container, false);
        goalContainer = view.findViewById(R.id.goal_container);
        addGoalButton = view.findViewById(R.id.add_goal_button);

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoalActivity) getActivity()).showAddGoalFragment();
            }
        });

        // Load goals from the database and display them
        loadGoals();

        return view;
    }

    private void loadGoals() {
        GoalApiService goalApiService = RetrofitClient.getClient().create(GoalApiService.class);
        String userId = SharedPreferencesManager.getUserId(getContext()); // Context 전달
        Call<List<GoalRes>> call = goalApiService.goalGetRequest(userId); // userId 전달
        call.enqueue(new Callback<List<GoalRes>>() {
            @Override
            public void onResponse(Call<List<GoalRes>> call, Response<List<GoalRes>> response) {
                if (response.isSuccessful()) {
                    List<GoalRes> goalsResponse = response.body();
                    goalContainer.removeAllViews(); // 중복 추가 방지
                    if (goalsResponse != null) {
                        //목표가 있는 경우
                        for (GoalRes goal : goalsResponse) {
                            // Create view for each goal and add it to the container
                            View goalView = createGoalView(goal);
                            goalContainer.addView(goalView);
                        }
                    } else {
                        // 목표가 없는 경우
                        TextView noGoalsTextView = new TextView(getContext());
                        noGoalsTextView.setText("목표가 없습니다.");
                        goalContainer.addView(noGoalsTextView);
                    }

                    Log.d(TAG, "GoalList 로드 성공.");

                } else {
                    Toast.makeText(getContext(), "목표 가져오기 실패", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "GoalList 로드 실패.");

                }
            }

            @Override
            public void onFailure(Call<List<GoalRes>> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: 목표 추가 실패", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "네트워크 오류: " + t.getMessage());
            }
        });
    }

    private View createGoalView(GoalRes goal) {
        View goalView = getLayoutInflater().inflate(R.layout.item_goal, null);
        TextView goalTitle = goalView.findViewById(R.id.goal_title);
        TextView goalContent = goalView.findViewById(R.id.goal_content);

        goalTitle.setText(goal.getGoalTitle());
        goalContent.setText(goal.getGoalContent());

        return goalView;
    }
}