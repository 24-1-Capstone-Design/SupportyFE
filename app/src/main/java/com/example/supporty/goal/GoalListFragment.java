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
import java.util.Date;
import com.example.supporty.R;
import com.example.supporty.RetrofitClient;
import com.example.supporty.SharedPreferencesManager;

import org.w3c.dom.Text;

import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

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

        // GoalApiService 초기화
        goalApiService = RetrofitClient.getClient().create(GoalApiService.class);

        // goalContainer 초기화
        goalContainer = view.findViewById(R.id.goal_container);

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
                    Log.d(TAG, "GoalList 로드 실패." + response.code());

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
        Button editButton = goalView.findViewById(R.id.edit_button);
        Button deleteButton = goalView.findViewById(R.id.delete_button);

        goalTitle.setText(goal.getGoalTitle());
        goalContent.setText(goal.getGoalContent());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGoal(goal.getGoalId());
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGoal(goal.getGoalId());
            }
        });

        return goalView;
    }

    private void deleteGoal(int goalId) {
        String userId = SharedPreferencesManager.getUserId(getContext());
        Call<Void> call = goalApiService.goalDeleteRequest(userId, goalId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "목표 삭제 성공", Toast.LENGTH_SHORT).show();
                    loadGoals();
                } else {
                    Toast.makeText(getContext(), "목표 삭제 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("deleteGoal", "Failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: 목표 삭제 실패", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void editGoal(int goalId) {
        String userId = SharedPreferencesManager.getUserId(getContext());

        // 다이얼로그를 사용하여 사용자 입력 받기
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("목표 수정");

        // 다이얼로그 레이아웃 설정
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // 목표 제목 입력
        final EditText inputTitle = new EditText(getContext());
        inputTitle.setHint("목표 제목");
        layout.addView(inputTitle);

        // 목표 내용 입력
        final EditText inputContent = new EditText(getContext());
        inputContent.setHint("목표 내용");
        layout.addView(inputContent);

        builder.setView(layout);

        // 확인 버튼 클릭 시
        builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String goalTitle = inputTitle.getText().toString().trim();
                String goalContent = inputContent.getText().toString().trim();

                // 유효성 검사
                if (goalTitle.isEmpty() || goalContent.isEmpty()) {
                    Toast.makeText(getContext(), "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Date goalDate = Utils.generateCurrentDate();

                GoalData goalData = new GoalData(userId, goalId, goalTitle, goalContent, false, goalDate);

                GoalApiService goalApiService = RetrofitClient.getClient().create(GoalApiService.class);
                Call<GoalRes> call = goalApiService.goalPatchRequest(userId, goalId, goalData);
                call.enqueue(new Callback<GoalRes>() {
                    @Override
                    public void onResponse(Call<GoalRes> call, Response<GoalRes> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "목표 수정 성공", Toast.LENGTH_SHORT).show();
                            loadGoals();
                        } else {
                            Toast.makeText(getContext(), "목표 수정 실패", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GoalRes> call, Throwable t) {
                        Toast.makeText(getContext(), "네트워크 오류: 목표 수정 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 취소 버튼 클릭 시
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}