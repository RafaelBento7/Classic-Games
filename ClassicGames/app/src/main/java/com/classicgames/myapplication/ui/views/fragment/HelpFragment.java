package com.classicgames.myapplication.ui.views.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classicgames.myapplication.databinding.FragmentHelpBinding;
import com.classicgames.myapplication.utils.HelpViewPageAdapter;

public class HelpFragment extends Fragment {

    FragmentHelpBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.HelpFragmentViewPager.setAdapter(new HelpViewPageAdapter(getContext()));

        return view;
    }
}