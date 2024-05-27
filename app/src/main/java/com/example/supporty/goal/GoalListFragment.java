package com.example.supporty.goal;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class GoalListFragment extends Fragment {

    private LinearLayout goalContainer;
    private Button addGoalButton;
    private Calendar currentCalendar = Calendar.getInstance();
    private TextView dateTextView;
    private String currentDate = getCurrentDateString(); // 현재 날짜 문자열로 가져오기
    private String selectedDate;


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
        dateTextView = view.findViewById(R.id.date_text_view);
        dateTextView.setText(currentDate);
        selectedDate = dateTextView.getText().toString(); // dateTextView 날짜 (이동 가능)
        ImageButton prevDateButton = view.findViewById(R.id.prev_date_button);
        ImageButton nextDateButton = view.findViewById(R.id.next_date_button);

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GoalActivity) getActivity()).showAddGoalFragment();
            }
        });


        prevDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(-1);
                loadGoals();

                // 현재 날짜와 dateTextView에 표시된 날짜가 다른 경우 addGoalButton 비활성화
                if (!currentDate.equals(selectedDate)) {
                    addGoalButton.setEnabled(false);
                    addGoalButton.setBackgroundColor(Color.GRAY); // 비활성화 시 회색으로 변경

                } else {
                    addGoalButton.setEnabled(true);
                    addGoalButton.setBackgroundColor(getResources().getColor(R.color.mainColor)); // 원래 색상으로 변경
                }
                Log.d(TAG, "currentDate: " + currentDate);
                Log.d(TAG, "selectedDate: " + selectedDate);
            }
        });

        nextDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDate(1);
                loadGoals();

                // 현재 날짜와 dateTextView에 표시된 날짜가 다른 경우 addGoalButton 비활성화
                if (!currentDate.equals(selectedDate)) {
                    addGoalButton.setEnabled(false);
                    addGoalButton.setBackgroundColor(Color.GRAY); // 비활성화 시 회색으로 변경

                } else {
                    addGoalButton.setEnabled(true);
                    addGoalButton.setBackgroundColor(getResources().getColor(R.color.mainColor)); // 원래 색상으로 변경
                }
                Log.d(TAG, "currentDate: " + currentDate);
                Log.d(TAG, "selectedDate: " + selectedDate);
            }
        });

        // GoalApiService 초기화
        goalApiService = RetrofitClient.getClient().create(GoalApiService.class);

        // goalContainer 초기화
        goalContainer = view.findViewById(R.id.goal_container);

        updateDateDisplay(dateTextView);

        // Load goals from the database and display them
        loadGoals();

        return view;
    }

    private void loadGoals() {
        GoalApiService goalApiService = RetrofitClient.getClient().create(GoalApiService.class);
        String userId = SharedPreferencesManager.getUserId(getContext()); // Context 전달
        String selectedDate = dateTextView.getText().toString();
        Call<List<GoalRes>> call = goalApiService.goalGetRequest(userId, selectedDate); // userId와 selectedDate 전달
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
        CheckBox achievedCheckbox = goalView.findViewById(R.id.achieved_checkbox);

        // 초기 goalTitle, goalContent 설정
        goalTitle.setText(goal.getGoalTitle());
        goalContent.setText(goal.getGoalContent());
        // achievedCheckbox 초기 상태 설정
        achievedCheckbox.setChecked(goal.isAchieved());

        // 목표 달성 여부에 따라 취소선 추가/제거
        if (goal.isAchieved()) {
            goalTitle.setPaintFlags(goalTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            goalTitle.setPaintFlags(goalTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        // currentDate와 selectedDate 비교하여 editButton 활성화/비활성화 설정
        if (!currentDate.equals(selectedDate)) {
            editButton.setEnabled(false);
            editButton.setAlpha(0.5f); // 반투명
            //editButton.setBackgroundColor(Color.GRAY); // 회색
        } else {
            editButton.setEnabled(true);
            editButton.setAlpha(1.0f); // 붍투명
        }

        // editButton 클릭 리스너 설정
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGoal(goal.getGoalId());
            }
        });

        // deleteButton 클릭 리스너 설정
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGoal(goal.getGoalId());
            }
        });

        // achievedCheckbox 클릭 리스너 설정
        achievedCheckbox.setOnClickListener(new View.OnClickListener() {
            int goalId = goal.getGoalId();
            @Override
            public void onClick(View v) {
                boolean isChecked = achievedCheckbox.isChecked();
                if (isChecked) {
                    goalTitle.setPaintFlags(goalTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    achievedTrue(goalId); // db isAchieved를 true로 바꾸는 함수
                } else {
                    goalTitle.setPaintFlags(goalTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    achievedFalse(goalId); // db isAchieved를 false로 바꾸는 함수
                }
                //updateGoalAchievement(goalId, isChecked);
            }
        });

        return goalView;
    }

    // 목표 삭제 메서드
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

    // 목표 수정 메서드
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
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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

                GoalData goalData = new GoalData(userId, goalId, goalTitle, goalContent, null, null);

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

    // 현재 날짜를 문자열로 변환하는 메서드
    private String getCurrentDateString() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(todayDate);
    }

    // 날짜 이동 메서드
    private void changeDate(int days) {
        currentCalendar.add(Calendar.DAY_OF_MONTH, days);
        updateDateDisplay(dateTextView);
        selectedDate = dateTextView.getText().toString(); // 날짜 이동 -> selectedDate 업데이트
        Log.d(TAG, "selectedDate updated: " + selectedDate);
        loadGoals();
    }

    private void updateDateDisplay(TextView textView) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(currentCalendar.getTime());
        textView.setText(dateString);
    }



    // 목표 달성 상태 업데이트 메서드
    private void updateGoalAchievement(int goalId, boolean isAchieved) {
        String userId = SharedPreferencesManager.getUserId(getContext());
        GoalApiService goalApiService = RetrofitClient.getClient().create(GoalApiService.class);

        /*
        // 기존 목표 정보를 사용하여 GoalData 객체 생성
        GoalData goalData = new GoalData(
                userId,
                goal.getGoalId(),
                goal.getGoalTitle(),
                goal.getGoalContent(),
                isAchieved,  // 업데이트할 달성 상태
                goal.getGoalDate()
        ); */

        // 목표 달성 상태 업데이트 요청
        //Call<GoalRes> updateCall = goalApiService.goalPatchRequest(userId, goal.getGoalId(), goalData);
        GoalData goalData = new GoalData(userId, goalId, null, null, isAchieved, null);

        Call<GoalRes> updateCall = goalApiService.goalPatchRequest(userId, goalId, goalData);
        updateCall.enqueue(new Callback<GoalRes>() {
            @Override
            public void onResponse(Call<GoalRes> call, Response<GoalRes> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "목표 상태 업데이트 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "상태 코드: " + statusCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GoalRes> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: 목표 상태 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }





    // db isAchieved를 true로 바꾸는 함수
    private void achievedTrue(int goalId) {
        String userId = SharedPreferencesManager.getUserId(getContext());
        GoalApiService goalApiService = RetrofitClient.getClient().create(GoalApiService.class);

        // GoalData 객체 생성
        GoalData goalData = new GoalData(
                userId,
                goalId,
                null,
                null,
                true, 
                null // goalDate는 변경하지 않으므로 null로 설정
        );

        // 목표 달성 상태 업데이트 요청
        Call<GoalRes> updateCall = goalApiService.goalPatchRequest(userId, goalId, goalData);
        updateCall.enqueue(new Callback<GoalRes>() {
            @Override
            public void onResponse(Call<GoalRes> call, Response<GoalRes> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "목표 상태 업데이트 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "목표 상태 업데이트 실패 - 상태 코드: " + statusCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GoalRes> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: 목표 상태 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // db isAchieved를 false로 바꾸는 함수
    private void achievedFalse(int goalId) {
        String userId = SharedPreferencesManager.getUserId(getContext());
        GoalApiService goalApiService = RetrofitClient.getClient().create(GoalApiService.class);

        // GoalData 객체 생성
        GoalData goalData = new GoalData(userId, goalId, null, null, false, null );

        // 목표 달성 상태 업데이트 요청
        Call<GoalRes> updateCall = goalApiService.goalPatchRequest(userId, goalId, goalData);
        updateCall.enqueue(new Callback<GoalRes>() {
            @Override
            public void onResponse(Call<GoalRes> call, Response<GoalRes> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "목표 상태 업데이트 성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "목표 상태 업데이트 실패 - 상태 코드: " + statusCode, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GoalRes> call, Throwable t) {
                Toast.makeText(getContext(), "네트워크 오류: 목표 상태 업데이트 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }




}