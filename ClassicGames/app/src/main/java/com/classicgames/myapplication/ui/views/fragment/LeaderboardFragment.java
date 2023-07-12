package com.classicgames.myapplication.ui.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.FragmentLeaderboardBinding;

public class LeaderboardFragment extends Fragment {

    FragmentLeaderboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        // Snake
        binding.LeaderboardTvSnakeBigMapRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getSnakeBigRecord()));
        binding.LeaderboardTvSnakeMediumMapRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getSnakeMediumRecord()));
        binding.LeaderboardTvSnakeSmallMapRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getSnakeSmallRecord()));
        // TrueColors
        binding.LeaderboardTvTrueColorsRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getTrueColorsRecord()));
        // Mastermind
        int[] mastermindRecords = MyApplication.getInstance().getRecords().getMastermindRecord();
        binding.LeaderboardTvMastermindTimeRecord.setText(GetTimerRecordStr(mastermindRecords));
        binding.LeaderboardTvMastermindAttemptsRecord.setText(String.valueOf(mastermindRecords[2]));
        // Wordle
        int[] wordleRecords = MyApplication.getInstance().getRecords().getWordleRecord();
        binding.LeaderboardTvWordlePointsRecord.setText(String.valueOf(wordleRecords[2]));
        binding.LeaderboardTvWordleTimeRecord.setText(GetTimerRecordStr(mastermindRecords));

        return view;
    }

    private String GetTimerRecordStr(int[] records) {
        String minutes = String.valueOf(records[0]);
        String seconds = String.valueOf(records[1]);
        String hours = "";
        int hoursInt = 0;
        if (records[1] < 10) seconds = "0" + seconds;
        if (records[0] > 59) {
            hoursInt = records[0] / 60;
            records[0] = records[0] % 60;
            minutes = String.valueOf(records[0]);
            hours = String.valueOf(hoursInt);
        }
        if (records[0] < 10) minutes = "0" + minutes;
        if (hoursInt < 10) hours = "0" + hours;
        if (Integer.parseInt(hours) == 0)
            return getResources().getString(R.string.timer_minutes_seconds,minutes,seconds);
        else return getResources().getString(R.string.timer_hour_minutes_seconds,hours,minutes,seconds);
    }
}