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
import android.widget.EditText;
import android.widget.Toast;
import com.example.supporty.R;
import com.example.supporty.RetrofitClient;
import com.example.supporty.SharedPreferencesManager;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Date;
import java.util.Random;

public class AddGoalFragment extends Fragment {

    private EditText goalTitleInput;
    private EditText goalContentInput;
    private Button saveButton;
    private Button backButton;

    // Retrofit 통신
    private final String TAG = this.getClass().getSimpleName();
    private GoalApiService goalApiService;
    private Retrofit retrofit = RetrofitClient.getClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_goal, container, false);
        goalTitleInput = view.findViewById(R.id.goal_title_input);
        goalContentInput = view.findViewById(R.id.goal_content_input);
        saveButton = view.findViewById(R.id.save_button);
        backButton = view.findViewById(R.id.back_button);

        String userId = SharedPreferencesManager.getUserId(getContext());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalTitle = goalTitleInput.getText().toString();
                String goalContent = goalContentInput.getText().toString();

                int goalId = Utils.generateRandomGoalId();
                Date goalDate = Utils.generateCurrentDate();

                GoalData goalData = new GoalData(userId, goalId, goalTitle, goalContent, false, goalDate);
                GoalApiService goalApiService = RetrofitClient.getClient().create(GoalApiService.class);
                Call<GoalRes> call = goalApiService.goalPostRequest(userId, goalData);
                call.enqueue(new Callback<GoalRes>() {
                    @Override
                    public void onResponse(Call<GoalRes> call, Response<GoalRes> response) {
                        int statusCode = response.code(); // 응답 상태 코드 가져오기

                        if (response.isSuccessful()) {
                            // 데이터베이스에 목표 정보 저장 성공
                            Toast.makeText(getContext(), "목표 추가 성공!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "목표 추가 성공");
                            ((GoalActivity) getActivity()).showGoalListFragment();
                        } else {
                            // 서버 응답은 있으나 성공하지 못한 경우 (상태 코드가 200이 아닌 경우)
                            Log.d(TAG, "상태 코드: " + response.code());
                            Toast.makeText(getContext(), "상태 코드: " + statusCode, Toast.LENGTH_LONG).show();
                        }
                            Toast.makeText(getContext(), "목표 추가 실패", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "목표 추가 실패");
                    }

                    @Override
                    public void onFailure(Call<GoalRes> call, Throwable t) {
                        // 네트워크 오류 또는 예외 발생
                        Toast.makeText(getContext(), "네트워크 오류: 목표 추가 실패", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "네트워크 오류: " + t.getMessage());
                    }
                });
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoalActivity) getActivity()).showGoalListFragment();
            }
        });

        return view;
    }
}