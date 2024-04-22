package com.cookandroid.supporty_fe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DiaryActivity extends AppCompatActivity{
    private Spinner spinner1, spinner2, spinner3;
    private TextView subcategoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        subcategoryTextView = findViewById(R.id.subcategoryTextView);

        // 각 스피너에 어댑터 연결
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.options1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.options2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.options3, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        // 스피너 선택 시 이벤트 처리
        spinner1.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner2.setOnItemSelectedListener(new SpinnerSelectedListener());
        spinner3.setOnItemSelectedListener(new SpinnerSelectedListener());
    }

    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 선택된 스피너에 따라 하위 카테고리 표시
            // 여기서는 단순히 텍스트를 변경하고 보이도록 설정하였으나 실제로는 데이터에 따라 동적으로 처리해야 합니다.
            subcategoryTextView.setText("하위 카테고리 선택됨: " + parent.getItemAtPosition(position));
            subcategoryTextView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // 아무것도 선택되지 않았을 때의 동작
            subcategoryTextView.setVisibility(View.GONE);
        }
    }
}
