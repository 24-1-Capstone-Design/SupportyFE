package com.example.supporty.goal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.R;

public class AddGoalFragment extends Fragment {
    private EditText editTextTitle, editTextDescription;
    private Button buttonAddGoal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_goal, container, false);

        editTextTitle = view.findViewById(R.id.edit_text_title);
        editTextDescription = view.findViewById(R.id.edit_text_description);
        buttonAddGoal = view.findViewById(R.id.button_add_goal);

        buttonAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();

                if (!title.isEmpty() && !description.isEmpty()) {
                    // GoalActivity의 addGoalAndShowListFragment 메서드 호출
                    ((GoalActivity) requireActivity()).addGoalAndShowListFragment(new Goal(title, description));
                }
            }
        });

        return view;
    }
}

