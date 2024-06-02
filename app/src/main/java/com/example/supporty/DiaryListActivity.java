package com.example.supporty;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


//커스텀 어댑터(일기 상세정보 표시)
class DiaryListAdapter extends BaseAdapter {

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DiaryData diary = diaryList.get(position);
        holder.textTitle.setText(diary.getDiaryDate() + "의 일기");
        holder.textContent.setText(diary.getDiaryContent());
        holder.textFeelings.setText("감정: " + diary.getBigFeeling() + " / " + diary.getMidFeeling() + " / " + diary.getSmallFeeling());

        convertView.setOnClickListener(v -> {
            if (holder.layoutDetails.getVisibility() == View.VISIBLE) {
                holder.layoutDetails.setVisibility(View.GONE);
            } else {
                holder.layoutDetails.setVisibility(View.VISIBLE);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView textTitle;
        LinearLayout layoutDetails;
        TextView textContent;
        TextView textFeelings;
    }
}
public class DiaryListActivity extends AppCompatActivity {

    private ListView listView;
    //커스텀 어댑터
    private DiaryListAdapter adapter;
    private ArrayList<DiaryData> diaryList;
    private ApiService apiServiceInterface;
    private Retrofit retrofit = RetrofitClient.getClient();

    //서버로부터 받은 DiaryData 들을 이 변수에 모두 저장
    private List<DiaryData> diaries;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        listView = findViewById(R.id.post_list);
        diaryList = new ArrayList<DiaryData>();

        adapter = new DiaryListAdapter(this, diaryList);
        listView.setAdapter(adapter);

        apiServiceInterface = retrofit.create(ApiService.class);

        fetchDiaries();

        // 리스트뷰 아이템 클릭 리스너 설정(클릭하면 그 위치에 맞는 diaries 의 getText 를 해서 상세정보 보이게 해줘
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // 해당 아이템의 확장 및 축소 상태 변경
            DiaryData selectedDiary = diaries.get(position);
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
//                    diaries = response.body();
//                    for (DiaryData diary : diaries) {
//                        String diaryText = diary.getDiaryDate() + "의 일기";
//                        diaryList.add(diaryText);
//                    }
//                    adapter.notifyDataSetChanged();
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
