package com.classicgames.myapplication.ui.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.classicgames.myapplication.databinding.FragmentLeaderboardBinding;
import com.classicgames.myapplication.utils.StatsViewPageAdapter;

/**
 * Records &amp; stats screen. Like the Help screen, it is a swipeable pager with
 * one page per game showing that game's personal bests and lifetime stats.
 */
public class LeaderboardFragment extends Fragment {

    FragmentLeaderboardBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(getLayoutInflater());

        binding.LeaderboardViewPager.setAdapter(new StatsViewPageAdapter(getContext()));

        return binding.getRoot();
    }
}
