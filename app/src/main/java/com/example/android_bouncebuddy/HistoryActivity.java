package com.example.android_bouncebuddy;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listView);

        List<Integer> scores = ScoreManager.getScores(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        // 按顺序显示每一局得分
        for (int i = 0; i < scores.size(); i++) {
            adapter.add("第 " + (i + 1) + " 局: " + scores.get(i) + " 分");
        }

        listView.setAdapter(adapter);
    }
}
