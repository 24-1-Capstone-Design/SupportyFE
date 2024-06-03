package com.example.supporty.treatment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.supporty.HomeActivity;
import com.example.supporty.MypageActivity;
import com.example.supporty.R;
import com.example.supporty.RecordActivity;
import com.example.supporty.RetrofitClient;
import com.example.supporty.SharedPreferencesManager;
import com.example.supporty.goal.GoalActivity;
import com.example.supporty.goal.Utils;
import com.example.supporty.treatment.TreatmentPagerAdapter;
import com.example.supporty.treatment.TreatmentApiService;
import com.example.supporty.treatment.TreatmentData;
import com.example.supporty.treatment.TreatmentRes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreatmentActivity extends AppCompatActivity {
    private static final String TAG = TreatmentActivity.class.getSimpleName();

    private Button btnAdd;
    private ViewPager2 viewPager;
    private TreatmentPagerAdapter adapter;
    List<TreatmentRes> treatmentResList = new ArrayList<>();
    private TextView dateTextView;
    private String selectedDate;
    private Calendar selectedDateCalendar;
    private String currentDate = getCurrentDateString(); // 현재 날짜 문자열로 가져오기
    private DatePickerDialog datePickerDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        btnAdd = findViewById(R.id.btn_add);
        viewPager = findViewById(R.id.treatment_view_pager);
        adapter = new TreatmentPagerAdapter(this, TreatmentActivity.this);
        viewPager.setAdapter(adapter);

        dateTextView = findViewById(R.id.date_text_view);
        dateTextView.setText(currentDate);
        loadTreatments();

        selectedDateCalendar = Calendar.getInstance();
        selectedDate = dateTextView.getText().toString();
        loadTreatments();

        // 툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("진료");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back); // 이전 버튼 아이콘 설정
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이전 버튼 클릭 시 RecordActivity로 이동
                Intent intent = new Intent(TreatmentActivity.this, RecordActivity.class);
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
                    Intent homeIntent = new Intent(TreatmentActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (menuItem.getItemId() == R.id.navigation_mypage) {
                    Intent mypageIntent = new Intent(TreatmentActivity.this, MypageActivity.class);
                    startActivity(mypageIntent);
                    return true;
                }
                return false;
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedYear = selectedDateCalendar.get(Calendar.YEAR);
                int selectedMonth = selectedDateCalendar.get(Calendar.MONTH);
                int selectedDay = selectedDateCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TreatmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 사용자가 선택한 날짜 처리
                        selectedDateCalendar.set(Calendar.YEAR, year);
                        selectedDateCalendar.set(Calendar.MONTH, monthOfYear);
                        selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // 선택한 날짜를 텍스트뷰에 표시
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String selectedDateString = sdf.format(selectedDateCalendar.getTime());
                        dateTextView.setText(selectedDateString);

                        // selectedDate 업데이트
                        selectedDate = selectedDateString;

                        loadTreatments();
                    }
                }, selectedYear, selectedMonth, selectedDay);

                datePickerDialog.show();
            }
        });


        // 추가 버튼 클릭 시
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTreatmentDialog();
            }
        });
    }

    // 진료 추가 다이얼로그 메서드
    private void showAddTreatmentDialog() {

        if (selectedDateCalendar == null) {
            selectedDateCalendar = Calendar.getInstance();
        }

        // AlertDialog.Builder를 사용하여 다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("새로운 진료 추가");

        // LayoutInflater를 사용하여 XML 레이아웃을 인플레이트하여 다이얼로그에 추가
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_treatment, null);
        builder.setView(dialogView);
        final Context context = this;

        EditText etHospitalName = dialogView.findViewById(R.id.et_hospital_name);
        EditText etVisitMemo = dialogView.findViewById(R.id.et_visit_memo);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnDone = dialogView.findViewById(R.id.btn_done);
        Button btnNextVisitDatePicker = dialogView.findViewById(R.id.btn_date_pick);
        //Button btnNextVisitTimePicker = dialogView.findViewById(R.id.btn_time_pick);

        final Calendar nextVisitDateCalendar = Calendar.getInstance();

        // 다음 예약일 - 날짜 선택 버튼 클릭 시 DatePickerDialog 표시
        btnNextVisitDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextVisitSelectedYear = nextVisitDateCalendar.get(Calendar.YEAR);
                int nextVisitSelectedMonth = nextVisitDateCalendar.get(Calendar.MONTH);
                int nextVisitSelectedDay = nextVisitDateCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TreatmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 사용자가 선택한 날짜 처리
                        nextVisitDateCalendar.set(Calendar.YEAR, year);
                        nextVisitDateCalendar.set(Calendar.MONTH, monthOfYear);
                        nextVisitDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // 선택한 날짜를 버튼 텍스트에 표시
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String nextVisitSelectedDateString = sdf.format(nextVisitDateCalendar.getTime());
                        btnNextVisitDatePicker.setText(nextVisitSelectedDateString);

                    }
                }, nextVisitSelectedYear, nextVisitSelectedMonth, nextVisitSelectedDay);

                datePickerDialog.show();
            }
        });

        /*
        // 다음 예약일 - 시간 선택 버튼 클릭 시 TimePickerDialog 표시
        btnNextVisitTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 시간 선택을 위해 TimePickerDialog를 표시
                int nextVisitSelectedHour = nextVisitDateCalendar.get(Calendar.HOUR_OF_DAY);
                int nextVisitSelectedMinute = nextVisitDateCalendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(TreatmentActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // 사용자가 선택한 시간을 설정
                        nextVisitDateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        nextVisitDateCalendar.set(Calendar.MINUTE, minute);

                        // 선택한 시간을 버튼 텍스트에 표시
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String nextVisitSelectedTimeString = sdf.format(nextVisitDateCalendar.getTime());
                        btnNextVisitDatePicker.setText(nextVisitSelectedTimeString);
                    }
                }, nextVisitSelectedHour, nextVisitSelectedMinute, true);

                timePickerDialog.show();
            }
        }); */


        AlertDialog dialog = builder.create();

        // 취소 버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 완료 버튼
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hospitalName = etHospitalName.getText().toString().trim();
                String visitMemo = etVisitMemo.getText().toString().trim();
                // 선택한 nextVisitDate 날짜 가져오기
                String yearNext = String.valueOf(nextVisitDateCalendar.get(Calendar.YEAR));
                String monthNext = String.valueOf(nextVisitDateCalendar.get(Calendar.MONTH) + 1); // 월은 0부터 시작하므로 +1 처리
                String dayNext = String.valueOf(nextVisitDateCalendar.get(Calendar.DAY_OF_MONTH));

                /*
                // 선택한 nextVisitDate 시간 가져오기
                String hourNext = String.valueOf(nextVisitDateCalendar.get(Calendar.HOUR_OF_DAY));
                String minuteNext = String.valueOf(nextVisitDateCalendar.get(Calendar.MINUTE)); */

                // 날짜와 시간을 nextVisitDate에 저장
                //String stNextVisitDate = yearNext + "-" + monthNext + "-" + dayNext + " " + hourNext + ":" + minuteNext;
                String stNextVisitDate = yearNext + "-" + monthNext + "-" + dayNext;


                // nextVisitDateTime을 SimpleDateFormat을 사용하여 Date 객체로 변환하여 저장
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date nextVisitDate = null;
                try {
                    nextVisitDate = sdf.parse(stNextVisitDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                // 사용자 ID 가져오기
                String userId = SharedPreferencesManager.getUserId(TreatmentActivity.this);
                // visitId: 랜덤 생성
                int visitId = Utils.generateRandomVisitId();
                // visitDate: visitDateStr 타입 변환 (string -> date)
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date visitDate = null;
                try {
                    visitDate = sdf2.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // 새로운 TreatmentData 생성
                TreatmentData treatmentData = new TreatmentData(userId, visitId, visitDate, nextVisitDate, visitMemo, hospitalName);

                // Retrofit을 사용하여 서버에 데이터 저장 요청
                TreatmentApiService treatmentApiService = RetrofitClient.getClient().create(TreatmentApiService.class);
                Call<TreatmentRes> call = treatmentApiService.treatmentPostRequest(userId, treatmentData);
                call.enqueue(new Callback<TreatmentRes>() {
                    @Override
                    public void onResponse(Call<TreatmentRes> call, Response<TreatmentRes> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // 성공적으로 저장된 경우
                            Toast.makeText(TreatmentActivity.this, "Treatment 저장 성공!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            loadTreatments();
                        } else {
                            // 서버 오류 등으로 저장 실패한 경우
                            Toast.makeText(TreatmentActivity.this, "Treatment 저장 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TreatmentRes> call, Throwable t) {
                        // 네트워크 오류 등으로 통신 실패한 경우
                        Toast.makeText(TreatmentActivity.this, "Treatment 저장 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Treatment 저장 실패: " + t.getMessage());
                    }
                });
            }
        });

        dialog.show();
    }


    private void saveTreatment(TreatmentData treatmentData) {
        // 서버에 데이터 저장 요청
        String userId = SharedPreferencesManager.getUserId(TreatmentActivity.this);
        TreatmentApiService treatmentApiService = RetrofitClient.getClient().create(TreatmentApiService.class);
        Call<TreatmentRes> call = treatmentApiService.treatmentPostRequest(userId, treatmentData);
        call.enqueue(new Callback<TreatmentRes>() {
            @Override
            public void onResponse(Call<TreatmentRes> call, Response<TreatmentRes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 성공적으로 저장된 경우
                    Toast.makeText(TreatmentActivity.this, "진료 저장 성공!", Toast.LENGTH_SHORT).show();
                    // 저장된 데이터 리스트에 추가
                    treatmentResList.add(response.body());
                    // 어댑터에 변경을 알림
                    adapter.notifyDataSetChanged();
                } else {
                    // 서버 오류 등으로 저장 실패한 경우
                    Toast.makeText(TreatmentActivity.this, "진료 저장 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TreatmentRes> call, Throwable t) {
                // 네트워크 오류 등으로 통신 실패한 경우
                Toast.makeText(TreatmentActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 진료 데이터 로드
    public void loadTreatments() {
        // 사용자 ID 가져오기
        String userId = SharedPreferencesManager.getUserId(TreatmentActivity.this);
        // 선택된 날짜 가져오기
        String visitDate = dateTextView.getText().toString();
        // TreatmentApiService의 인스턴스 생성
        TreatmentApiService treatmentApiService = RetrofitClient.getClient().create(TreatmentApiService.class);
        // 사용자와 날짜에 해당하는 진료 데이터 요청
        Call<List<TreatmentRes>> call = treatmentApiService.treatmentGetRequest(userId, visitDate);
        call.enqueue(new Callback<List<TreatmentRes>>() {
            @Override
            public void onResponse(Call<List<TreatmentRes>> call, Response<List<TreatmentRes>> response) {
                if (response.isSuccessful()) {
                    // 응답이 성공한 경우
                    Toast.makeText(TreatmentActivity.this, "데이터 불러오기 성공!", Toast.LENGTH_SHORT).show();
                    List<TreatmentRes> newTreatmentList = response.body();
                    Log.d("TreatmentActivity", "New Treatment List: " + newTreatmentList.toString());
                    // 기존 진료 목록을 지우고 새로운 진료 목록으로 교체
                    treatmentResList.clear();
                    treatmentResList.addAll(newTreatmentList);
                    // 새로운 진료 목록을 어댑터에 설정하고 변경 사항을 알림
                    adapter.setTreatmentList(newTreatmentList);
                    adapter.notifyDataSetChanged();
                } else {
                    // 응답이 실패하거나 데이터가 null인 경우 처리
                    Toast.makeText(TreatmentActivity.this, "데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Treatment 불러오기 실패." + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<TreatmentRes>> call, Throwable t) {
                // 네트워크 오류가 발생한 경우 오류 메시지 표시
                Toast.makeText(TreatmentActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Treatment 불러오기 실패: " + t.getMessage());
            }
        });
    }





    private String getCurrentDateString() {
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(todayDate);
    }

}
