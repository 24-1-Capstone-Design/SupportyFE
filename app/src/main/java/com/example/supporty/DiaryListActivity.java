package com.example.supporty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.AlertDialog;
import android.content.DialogInterface;


public class DiaryListActivity extends AppCompatActivity {

    private ListView listView;
    //커스텀 어댑터
    private DiaryListAdapter adapter;
    private ArrayList<DiaryData> diaryList;
    private ApiService apiServiceInterface;
    private Retrofit retrofit = RetrofitClient.getClient();

    //내부 클래스인 DiaryListAdapter (커스텀 어댑터)
    private class DiaryListAdapter extends BaseAdapter {

        private Context context;
        private List<DiaryData> diaryList;
        private LayoutInflater inflater;


        public DiaryListAdapter(Context context, ArrayList<DiaryData> diaryList) {
            this.context = context;
            this.diaryList = diaryList;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return diaryList.size();
        }

        @Override
        public Object getItem(int position) {
            return diaryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        // 일기 삭제 함수
        private void deleteDiary(DiaryData diary) {
            //일기 삭제 api 호출
            apiServiceInterface.deleteDiary(diary.getId(), diary.getDiaryDate()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        //  해당 일기를 리스트에서 제거
                        diaryList.remove(diary);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(context, "일기가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "일기 삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(context, "서버 통신 실패", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //상세 정보 보이기 위한 함수
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_diary, parent, false);
                holder = new ViewHolder();
                holder.textTitle = convertView.findViewById(R.id.textTitle);
                holder.layoutDetails = convertView.findViewById(R.id.layoutDetails);
                holder.textContent = convertView.findViewById(R.id.textContent);
                holder.textFeelings = convertView.findViewById(R.id.textFeelings);
                holder.diaryEditBtn = convertView.findViewById(R.id.diaryEditBtn);
                holder.diaryDelBtn = convertView.findViewById(R.id.diaryDelBtn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            DiaryData diary = diaryList.get(position);
            holder.textTitle.setText(diary.getDiaryDate() + "의 일기");
            holder.textContent.setText(diary.getDiaryContent());
            holder.textFeelings.setText("감정: " + diary.getBigFeeling() + " / " + diary.getMidFeeling() + " / " + diary.getSmallFeeling());

            //일기 목록 아이템 선택시 상세정보 보이게/안보이게
            convertView.setOnClickListener(v -> {
                if (holder.layoutDetails.getVisibility() == View.VISIBLE) {
                    holder.layoutDetails.setVisibility(View.GONE);
                } else {
                    holder.layoutDetails.setVisibility(View.VISIBLE);
                }
            });

            //수정 버튼 클릭시의 이벤트
            holder.diaryEditBtn.setOnClickListener(v -> {
                //DiaryActivity 에 넘어가게 만듦(정보도 넘겨줌)
                Intent intent = new Intent(context, DiaryActivity.class);
                intent.putExtra("diaryData", diary);
                context.startActivity(intent);
            });

            // 삭제 버튼 클릭 시의 이벤트
            holder.diaryDelBtn.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("이 일기를 지우면 다시 이 날짜의 일을 기록할 수 없습니다.\n 그래도 삭제하시겠습니까?");
                builder.setPositiveButton("예", (dialog, which) -> {
                    // 예 버튼을 누르면 일기 삭제 함수 호출
                    deleteDiary(diary);
                });
                builder.setNegativeButton("아니오", (dialog, which) -> dialog.dismiss());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            });


            return convertView;
        }

        private class ViewHolder {
            TextView textTitle;
            LinearLayout layoutDetails;
            TextView textContent;
            TextView textFeelings;
            Button diaryEditBtn;
            Button diaryDelBtn;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        // 일기 목록에서 뒤돌아가기 버튼 누르면 일기 작성 activity 로 넘어감
        ImageButton iconBack = findViewById(R.id.list_back);
        iconBack.setOnClickListener(v -> {
            // 아이콘 버튼 클릭 시 이벤트 처리
            Intent intent = new Intent(DiaryListActivity.this, DiaryActivity.class);
            startActivity(intent);
        });

        listView = findViewById(R.id.post_list);
        diaryList = new ArrayList<>();

        adapter = new DiaryListAdapter(this, diaryList);
        listView.setAdapter(adapter);

        apiServiceInterface = retrofit.create(ApiService.class);

        fetchDiaries();

        // 리스트뷰 아이템 클릭 리스너 설정
        listView.setOnItemClickListener((parent, view, position, id) -> {
            adapter.notifyDataSetChanged();
        });

    }

    //전체 일기 목록 보여줌
    private void fetchDiaries() {
        String id = SharedPreferencesManager.getUserId(this);
        apiServiceInterface.getDiaries(id).enqueue(new Callback<List<DiaryData>>() {
            @Override
            public void onResponse(@NonNull Call<List<DiaryData>> call, @NonNull Response<List<DiaryData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    diaryList.clear();
                    diaryList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DiaryListActivity.this, "일기 목록을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DiaryData>> call, @NonNull Throwable t) {
                Toast.makeText(DiaryListActivity.this, "서버 통신 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
