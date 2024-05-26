package com.example.supporty;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.supporty.R;

import java.util.ArrayList;

public class DiaryListActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> diaryList;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_list);

        listView = findViewById(R.id.post_list);
        diaryList = new ArrayList<>();

        // 여기서 postList에 사용자의 댓글을 추가
        diaryList.add("240306 일기");
        diaryList.add("240302 일기");
        diaryList.add("240301");
        diaryList.add("240221");
        diaryList.add("240123");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, diaryList);
        listView.setAdapter(adapter);
    }
}