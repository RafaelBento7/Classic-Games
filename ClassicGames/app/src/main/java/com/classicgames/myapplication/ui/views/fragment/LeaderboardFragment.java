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

        binding.LeaderboardTvSnakeBigMapRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getSnakeBigRecord()));
        binding.LeaderboardTvSnakeMediumMapRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getSnakeMediumRecord()));
        binding.LeaderboardTvSnakeSmallMapRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getSnakeSmallRecord()));
        binding.LeaderboardTvTrueColorsRecord.setText(String.valueOf(MyApplication.getInstance().getRecords().getTrueColorsRecord()));
        int[] mastermindRecords = MyApplication.getInstance().getRecords().getMastermindRecord();
        String minutes = String.valueOf(mastermindRecords[0]);
        String seconds = String.valueOf(mastermindRecords[1]);
        String hours = "";
        int hoursInt = 0;
        if (mastermindRecords[1] < 10) seconds = "0" + seconds;
        if (mastermindRecords[0] > 59) {
            hoursInt = mastermindRecords[0] / 60;
            mastermindRecords[0] = mastermindRecords[0] % 60;
            minutes = String.valueOf(mastermindRecords[0]);
            hours = String.valueOf(hoursInt);
        }
        if (mastermindRecords[0] < 10) minutes = "0" + minutes;
        if (hoursInt < 10) hours = "0" + hours;
        if (Integer.parseInt(hours) == 0)
            binding.LeaderboardTvMastermindTimeRecord.setText(getResources().getString(R.string.timer_minutes_seconds,minutes,seconds));
        else binding.LeaderboardTvMastermindTimeRecord.setText(getResources().getString(R.string.timer_hour_minutes_seconds,hours,minutes,seconds));
        binding.LeaderboardTvMastermindAttemptsRecord.setText(String.valueOf(mastermindRecords[2]));

        return view;
    }
}