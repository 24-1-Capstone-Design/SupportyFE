package com.example.supporty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supporty.R;
import com.google.gson.annotations.SerializedName;

import java.io.DataInput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

class BigFeel{
    @SerializedName("big_feeling")
    private String big_feeling;

    public String getBig_feeling() {
        return big_feeling;
    }

}

class MidFeel {
    @SerializedName("mid_feeling")
    private String mid_feeling;


    public String getMid_feeling() {
        return mid_feeling;
    }

}

class SmallFeel {
    @SerializedName("small_feeling")
    private String small_feeling;

    public String getSmall_feeling() {
        return small_feeling;
    }

}


public class DiaryActivity extends AppCompatActivity {

    private EditText diary_content;

    private TextView diaryDate;
    //감정 스피너
    private Spinner spinner1, spinner2, spinner3;
    private ArrayAdapter<String> adapter1, adapter2, adapter3;

    private ApiService apiServiceInterface;

    private Retrofit retrofit = RetrofitClient.getClient();

    private Button postButton;


    //큰 감정 가져오기
    private void fetchBigFeelings() {
        apiServiceInterface.getBigFeelings().enqueue(new Callback<List<BigFeel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BigFeel>> call, @NonNull Response<List<BigFeel>> response) {
                if(response.isSuccessful()) {
                    List<BigFeel> resList = response.body();
                    if (resList != null && !resList.isEmpty()) {
                        List<String> bigFeelings = new ArrayList<>();
                        for (BigFeel res : resList) {
                            bigFeelings.add(res.getBig_feeling());
                        }
                        // ArrayAdapter 생성자에 컬렉션을 전달하여 초기화
                        adapter1 = new ArrayAdapter<>(DiaryActivity.this, android.R.layout.simple_spinner_item, bigFeelings);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner1.setAdapter(adapter1);


                    }
                }

                else {
                    // 서버 응답이 실패한 경우
                    Toast.makeText(DiaryActivity.this, "실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BigFeel>> call, Throwable t) {
                // 통신 실패 처리
                Toast.makeText(DiaryActivity.this, "통신실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //중간 감정 가져오기
    private void fetchMidFeelings(String selectedBigFeeling) {
        apiServiceInterface.getMidFeelings(selectedBigFeeling).enqueue(new Callback<List<MidFeel>>() {
            @Override
            public void onResponse(@NonNull Call<List<MidFeel>> call, @NonNull Response<List<MidFeel>> response) {
                if(response.isSuccessful()) {
                    List<MidFeel> resList = response.body();
                    if (resList != null && !resList.isEmpty()) {
                        List<String> midFeelings = new ArrayList<>();
                        for (MidFeel res : resList) {
                            midFeelings.add(res.getMid_feeling());
                        }
                        // ArrayAdapter 생성자에 컬렉션을 전달하여 초기화
                        adapter2 = new ArrayAdapter<>(DiaryActivity.this, android.R.layout.simple_spinner_item, midFeelings);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);


                    } else { //midFeeling 감정 없다면
                        //어댑터들 초기화
                        adapter2 = new ArrayAdapter<>(DiaryActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter2);
                        // small 어댑터도 초기화해줌
                        adapter3 = new ArrayAdapter<>(DiaryActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner3.setAdapter(adapter3);
                    }
                } else {
                    // 서버 응답이 실패한 경우
                    Toast.makeText(DiaryActivity.this, "두번 째 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MidFeel>> call, Throwable t) {
                // 통신 실패 처리
                Toast.makeText(DiaryActivity.this, "통신실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //작은 감정 가져오기
    private void fetchSmallFeelings(String selectedMidFeeling) {
        apiServiceInterface.getSmallFeelings(selectedMidFeeling).enqueue(new Callback<List<SmallFeel>>() {
            @Override
            public void onResponse(@NonNull Call<List<SmallFeel>> call, @NonNull Response<List<SmallFeel>> response) {
                if(response.isSuccessful()) {
                    List<SmallFeel> resList = response.body();
                    if (resList != null && !resList.isEmpty()) {
                        List<String> smallFeelings = new ArrayList<>();
                        for (SmallFeel res : resList) {
                            smallFeelings.add(res.getSmall_feeling());
                        }
                        // ArrayAdapter 생성자에 컬렉션을 전달하여 초기화
                        adapter3 = new ArrayAdapter<>(DiaryActivity.this, android.R.layout.simple_spinner_item, smallFeelings);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner3.setAdapter(adapter3);

                    } else {
                        adapter3 = new ArrayAdapter<>(DiaryActivity.this, android.R.layout.simple_spinner_item, new ArrayList<>());
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner3.setAdapter(adapter3);
                    }
                } else {
                    // 서버 응답이 실패한 경우
                    Toast.makeText(DiaryActivity.this, "세번 째 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<SmallFeel>> call, Throwable t) {
                // 통신 실패 처리
                Toast.makeText(DiaryActivity.this, "통신실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //현재 날짜 가져오는 함수
    private String getCurrentDateString() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(todayDate);
    }

    //일기 포스팅
    private void submitDiary() {
        String id = SharedPreferencesManager.getUserId(this); // Replace with actual user ID
        String date = getCurrentDateString();
        String content = diary_content.getText().toString();
        String bigFeeling = String.valueOf(spinner1.getSelectedItem());
        String midFeeling = String.valueOf(spinner2.getSelectedItem());
        String smallFeeling = String.valueOf(spinner3.getSelectedItem());

        DiaryData diary = new DiaryData(id, date, content, bigFeeling, midFeeling, smallFeeling);

        apiServiceInterface.postDiary(diary).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(DiaryActivity.this, "오늘의 일기 등록!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DiaryActivity.this, DiaryListActivity.class);
                    startActivity(intent);
                } else if(response.code() == 409)  {
                    Toast.makeText(DiaryActivity.this, "오늘의 일기를 이미 작성하셨습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiaryActivity.this, "일기 등록 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(DiaryActivity.this, "서버 통신 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //일기 수정 시 부르는 함수
    private void editDiary(String diaryDate) {
        String id = SharedPreferencesManager.getUserId(this);
        String date = diaryDate;
        String content = diary_content.getText().toString();
        String bigFeeling = String.valueOf(spinner1.getSelectedItem());
        String midFeeling = String.valueOf(spinner2.getSelectedItem());
        String smallFeeling = String.valueOf(spinner3.getSelectedItem());

        DiaryData diary = new DiaryData(id, date, content, bigFeeling, midFeeling, smallFeeling);

        apiServiceInterface.editDiary(diary).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    Toast.makeText(DiaryActivity.this, "수정 완료!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DiaryActivity.this, DiaryListActivity.class);
                    startActivity(intent);
                } else if(response.code() == 404)  {
                    Toast.makeText(DiaryActivity.this, "수정할 일기가 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiaryActivity.this, "수정 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(DiaryActivity.this, "서버 통신 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //일반 일기 작성에서 뒤돌아가기 버튼 누르면 "기록" 액티비티로 넘어감
        ImageButton iconBack = findViewById(R.id.record_back);
        iconBack.setOnClickListener(v -> {
            Intent intent = new Intent(DiaryActivity.this, RecordActivity.class);
            startActivity(intent);
        });

        // 목록 버튼 누르면 그동안 작성 일기 목록 나타남
        ImageButton iconList = findViewById(R.id.list_icon);
        iconList.setOnClickListener(v -> {
            //DiaryListActivitiy 로 넘어감
            Intent intent = new Intent(DiaryActivity.this, DiaryListActivity.class);
            startActivity(intent);
        });

        //diary 내용
        diary_content = findViewById(R.id.diary_content);

        //현재 날짜
        diaryDate = findViewById(R.id.diaryDate);
        diaryDate.setText(getCurrentDateString());

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);

        postButton = findViewById(R.id.btnCreatePost);

        apiServiceInterface = retrofit.create(ApiService.class);

        //첫 번째 BigFeeling fetch
        fetchBigFeelings();

        //첫 번째 스피너(BigFeeling) 선택 시
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //BigFeeling 선택되면 두 번째 스피너 fetch
                String selectedBigFeeling = adapter1.getItem(position);
                if (selectedBigFeeling != null) {
                    fetchMidFeelings(selectedBigFeeling);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        // 두 번째 스피너(MidFeeling) 선택 시
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //MidFeeling 선택되면 세 번째 스피너 fetch
                String selectedMidFeeling = adapter2.getItem(position);
                if (selectedMidFeeling != null) {
                    fetchSmallFeelings(selectedMidFeeling);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        postButton.setOnClickListener(v -> submitDiary());

        // 수정 버튼 눌렀을 때 넘어오는 intent 로 데이터 가져오기
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("diaryData")) {
            DiaryData diaryData = (DiaryData) intent.getSerializableExtra("diaryData");

            //게시글 등록을 수정 버튼으로 바꿈
            postButton.setText("수정 완료");
            //editDiary 함수를 불러서 수정하게!
            postButton.setOnClickListener(v -> editDiary(diaryData.getDiaryDate()));

            if (diaryData != null) {
                diaryDate.setText(diaryData.getDiaryDate());
                diary_content.setText(diaryData.getDiaryContent());
                // 첫 번째 감정 스피너 설정
                if (diaryData.getBigFeeling() != null) {
                    fetchBigFeelings();
                    spinner1.postDelayed(() -> {
                        int position = adapter1.getPosition(diaryData.getBigFeeling());
                        if(position != -1) {
                            spinner1.setSelection(position);
                        }
                    }, 500);

                }

                // 두 번째 감정 스피너 설정
                if (diaryData.getMidFeeling() != null) {
                    fetchMidFeelings(diaryData.getBigFeeling());
                    spinner2.postDelayed(() -> {
                        //Toast.makeText(DiaryActivity.this, "hello", Toast.LENGTH_SHORT).show();
                        int position = adapter2.getPosition(diaryData.getMidFeeling());
                        if (position != -1) {
                            spinner2.setSelection(position);
                        }
                    }, 1000);
                }

                // 세 번째 감정 스피너 설정
                if (diaryData.getSmallFeeling() != null) {
                    fetchSmallFeelings(diaryData.getMidFeeling());
                    spinner3.postDelayed(() -> {
                        int position = adapter3.getPosition(diaryData.getSmallFeeling());
                        if (position != -1) {
                            spinner3.setSelection(position);
                        }
                    }, 1500);
                }
            }
        }


    }
}