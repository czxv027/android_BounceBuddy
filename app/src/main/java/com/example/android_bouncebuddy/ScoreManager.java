package com.example.android_bouncebuddy;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScoreManager {
    private static final String PREFS_NAME = "bounce_buddy_scores";
    private static final String KEY_SCORES = "scores";

    // 保存当前分数，追加到已有分数后面
    public static void saveScore(Context context, int score) {
        List<Integer> scores = getScores(context);
        scores.add(score);
        // 转成字符串保存，逗号分隔
        StringBuilder sb = new StringBuilder();
        for (int s : scores) {
            sb.append(s).append(",");
        }
        // 去掉最后逗号
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_SCORES, sb.toString()).apply();
    }

    // 读取所有分数
    public static List<Integer> getScores(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedScores = prefs.getString(KEY_SCORES, "");
        List<Integer> scores = new ArrayList<>();
        if (!savedScores.isEmpty()) {
            String[] split = savedScores.split(",");
            for (String s : split) {
                try {
                    scores.add(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return scores;
    }

    // 清空所有分数（可选）
    public static void clearScores(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_SCORES).apply();
    }
}
