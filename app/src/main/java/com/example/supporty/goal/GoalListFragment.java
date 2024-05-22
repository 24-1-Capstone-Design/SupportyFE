package com.example.supporty.goal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class GoalListFragment extends Fragment {
    private RecyclerView recyclerView;
    private GoalAdapter adapter;
    private ArrayList<Goal> goalList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 가짜 데이터 생성 (실제로는 Retrofit 등을 통해 데이터를 가져와야 함)
        goalList = new ArrayList<>();
        goalList.add(new Goal("목표 1", "목표 1의 설명"));
        goalList.add(new Goal("목표 2", "목표 2의 설명"));

        adapter = new GoalAdapter(goalList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    // 목표 목록을 새로고침하는 메서드
    public void refreshGoalList() {
        // 데이터를 다시 가져와서 리스트를 갱신하는 로직을 여기에 구현한다.
    }
}

