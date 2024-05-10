package com.example.supporty;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.EditText;

import com.cookandroid.supporty_fe.R;


public class DiaryActivity extends AppCompatActivity {

    private EditText etTitle, etAuthorInfo, etDescription;
    //감정 스피너
    private Spinner spinner1, spinner2, spinner3;
    private ArrayAdapter<CharSequence> adapter1, adapter2, adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        //뷰 초기화
        etTitle = findViewById(R.id.etTitle);
        etAuthorInfo = findViewById(R.id.etAuthorInfo);
        etDescription = findViewById(R.id.etDescription);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);

        adapter1 = ArrayAdapter.createFromResource(this,
                R.array.items1, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        adapter2 = ArrayAdapter.createFromResource(this,
                R.array.items_empty, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setEnabled(false);

        adapter3 = ArrayAdapter.createFromResource(this,
                R.array.items_empty, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setEnabled(false);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When an item is selected in spinner1, enable spinner2
                spinner2.setEnabled(true);
                adapter2 = ArrayAdapter.createFromResource(DiaryActivity.this,
                        R.array.items2, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When an item is selected in spinner2, enable spinner3
                spinner3.setEnabled(true);
                adapter3 = ArrayAdapter.createFromResource(DiaryActivity.this,
                        R.array.items3, android.R.layout.simple_spinner_item);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner3.setAdapter(adapter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
