package com.classicgames.myapplication.ui.views.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityPatchNotesBinding;

public class PatchNotesActivity extends AppCompatActivity {

    ActivityPatchNotesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatchNotesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.PatchNotesToolbar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.PatchNotesToolbar.ToolbarTitle.setText(R.string.patch_notes);
    }
}