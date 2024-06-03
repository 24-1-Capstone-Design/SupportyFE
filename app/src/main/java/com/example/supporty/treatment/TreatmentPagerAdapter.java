package com.example.supporty.treatment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.supporty.R;
import com.example.supporty.RetrofitClient;
import com.example.supporty.SharedPreferencesManager;
import com.example.supporty.goal.GoalApiService;
import com.example.supporty.goal.GoalData;
import com.example.supporty.goal.GoalRes;
import com.example.supporty.goal.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TreatmentPagerAdapter extends RecyclerView.Adapter<TreatmentPagerAdapter.TreatmentViewHolder> {

    //private List<Treatment> treatmentList = new ArrayList<>();
    private List<Treatment> treatmentList;
    private Context context;
    private TreatmentActivity treatmentActivity;

    public TreatmentPagerAdapter(Context context, TreatmentActivity treatmentActivity) {
        this.context = context;
        this.treatmentActivity = treatmentActivity;
        this.treatmentList = new ArrayList<>();
    }

    public void setTreatmentList(List<TreatmentRes> treatmentResList) {
        this.treatmentList.clear(); // 기존 데이터 리스트 초기화
        // TreatmentRes를 Treatment으로 변환하여 리스트에 추가
        for (TreatmentRes treatmentRes : treatmentResList) {
            Treatment treatment = new Treatment();
            treatment.setVisitId(treatmentRes.getVisitId());
            treatment.setHospitalName(treatmentRes.getHospitalName());
            treatment.setNextVisitDate(treatmentRes.getNextVisitDate());
            treatment.setVisitMemo(treatmentRes.getVisitMemo());
            this.treatmentList.add(treatment);
        }
        notifyDataSetChanged(); // 변경 사항을 어댑터에 알림
    }


    @NonNull
    @Override
    public TreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_treatment, parent, false);
        return new TreatmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreatmentViewHolder holder, int position) {
        Treatment treatment = treatmentList.get(position);
        holder.bind(treatment);
    }

    @Override
    public int getItemCount() {
        return treatmentList.size();
    }

    public class TreatmentViewHolder extends RecyclerView.ViewHolder {

        private TextView hospitalNameTextView;
        private TextView nextVisitDateTextView;
        private TextView visitMemoTextView;

        private Button editButton;
        private Button deleteButton;
        private Treatment currentTreatment;

        public TreatmentViewHolder(@NonNull View itemView) {
            super(itemView);
            hospitalNameTextView = itemView.findViewById(R.id.tv_hospital_name);
            nextVisitDateTextView = itemView.findViewById(R.id.tv_next_visit_date);
            visitMemoTextView = itemView.findViewById(R.id.tv_visit_memo);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTreatment != null ) {
                        editTreatment(currentTreatment.getVisitId());
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTreatment != null ) {
                        deleteTreatment(currentTreatment.getVisitId());
                    }
                }
            });
        }

        public void bind(Treatment treatment) {
            this.currentTreatment = treatment;
            Log.d("TreatmentViewHolder", "Hospital Name: " + treatment.getHospitalName());
            Log.d("TreatmentViewHolder", "Next Visit Date: " + treatment.getNextVisitDate());
            Log.d("TreatmentViewHolder", "Visit Memo: " + treatment.getVisitMemo());

            hospitalNameTextView.setText(treatment.getHospitalName());
            nextVisitDateTextView.setText(formatDate(treatment.getNextVisitDate()));
            visitMemoTextView.setText(treatment.getVisitMemo());

        }

        private String formatDate(Date date) {
            if (date != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getDefault());
                return sdf.format(date);
            } else {
                return "날짜 없음";
            }
        }

        // 수정 메서드
        private void editTreatment(int visitId) {
            String userId = SharedPreferencesManager.getUserId(context);


            // 다이얼로그를 사용하여 사용자 입력 받기
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("수정");

            // 다이얼로그 레이아웃 설정
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);

            // 병원 이름 입력
            final EditText inputHospitalName = new EditText(context);
            inputHospitalName.setHint("병원 이름");
            layout.addView(inputHospitalName);

            // 메모 입력
            final EditText inputMemo = new EditText(context);
            inputMemo.setHint("메모");
            layout.addView(inputMemo);

            // Button to open DatePickerDialog
            final Button dateButton = new Button(context);
            dateButton.setText("날짜 선택");
            layout.addView(dateButton);

            /*
            // Button to open TimePickerDialog
            final Button timeButton = new Button(context);
            timeButton.setText("시간 선택");
            layout.addView(timeButton); */

            // Calendar instance to store the selected date and time
            final Calendar calendar = Calendar.getInstance();

            if (currentTreatment != null) {
                // 병원 이름 초기화
                String hospitalName = currentTreatment.getHospitalName();
                inputHospitalName.setText(hospitalName);

                // 방문 메모 초기화
                String visitMemo = currentTreatment.getVisitMemo();
                inputMemo.setText(visitMemo);

                // 다음 방문 날짜 초기화
                Date nextVisitDate = currentTreatment.getNextVisitDate();
                if (nextVisitDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateButton.setText(sdf.format(nextVisitDate));
                    // 기존 방문 날짜 저장
                    calendar.setTime(nextVisitDate);
                }
            }

            dateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            dateButton.setText(sdf.format(calendar.getTime()));
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            });

            /*
            timeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                            timeButton.setText(sdf.format(calendar.getTime()));
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }
            }); */


            builder.setView(layout);

            // 저장 클릭 시
            builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String hospitalName = inputHospitalName.getText().toString().trim();
                    String visitMemo = inputMemo.getText().toString().trim();
                    Date nextVisitDate;

                    // 유효성 검사
                    /*
                    if (hospitalName.isEmpty() || visitMemo.isEmpty() || stNextVisitDate.isEmpty()) {
                        Toast.makeText(context, "모든 필드를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    } */

                    // 사용자가 입력하지 않은 경우에는 기존 값을 유지하도록 처리
                    if (hospitalName.isEmpty()) {
                        hospitalName = currentTreatment.getHospitalName();
                    }
                    if (visitMemo.isEmpty()) {
                        visitMemo = currentTreatment.getVisitMemo();
                    }
                    if (dateButton.getText().toString().isEmpty()) {
                        nextVisitDate = currentTreatment.getNextVisitDate();
                    } else {
                        nextVisitDate = calendar.getTime();
                    }

                    String stNextVisitDate = formatDate(nextVisitDate);

                    Log.d("TreatmentData", "UserID: " + userId);
                    Log.d("TreatmentData", "VisitID: " + visitId);
                    Log.d("TreatmentData", "HospitalName: " + hospitalName);
                    Log.d("TreatmentData", "VisitMemo: " + visitMemo);
                    Log.d("TreatmentData", "NextVisitDate: " + stNextVisitDate);


                    TreatmentData treatmentData = new TreatmentData(userId, visitId, null, nextVisitDate, visitMemo, hospitalName);

                    TreatmentApiService treatmentApiService = RetrofitClient.getClient().create(TreatmentApiService.class);
                    Call<TreatmentRes> call = treatmentApiService.treatmentPatchRequest(userId, visitId, treatmentData);
                    call.enqueue(new Callback<TreatmentRes>() {
                        @Override
                        public void onResponse(Call<TreatmentRes> call, Response<TreatmentRes> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "진료 수정 성공", Toast.LENGTH_SHORT).show();
                                treatmentActivity.loadTreatments();
                            } else {
                                Toast.makeText(context, "진료 수정 실패", Toast.LENGTH_SHORT).show();
                                Log.e("API Error", "Response Code: " + response.code());
                                Log.e("API Error", "Response Message: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<TreatmentRes> call, Throwable t) {
                            Toast.makeText(context, "네트워크 오류: 진료 수정 실패", Toast.LENGTH_SHORT).show();
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

        // 삭제 메서드
        private void deleteTreatment(int visitId) {
            String userId = SharedPreferencesManager.getUserId(context);
            TreatmentApiService treatmentApiService = RetrofitClient.getClient().create(TreatmentApiService.class);
            Call<Void> call = treatmentApiService.treatmentDeleteRequest(userId, visitId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "진료 삭제 성공", Toast.LENGTH_SHORT).show();
                        treatmentActivity.loadTreatments();
                    } else {
                        Toast.makeText(context, "진료 삭제 실패: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.d("deleteTreatment", "Failed with status code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "네트워크 오류: 진료 삭제 실패", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}