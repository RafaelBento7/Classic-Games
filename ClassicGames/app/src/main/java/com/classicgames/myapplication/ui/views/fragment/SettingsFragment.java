package com.classicgames.myapplication.ui.views.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.FragmentSettingsBinding;
import com.classicgames.myapplication.ui.dialog.LanguagePickerDialog;
import com.classicgames.myapplication.ui.views.activity.MainActivity;
import com.classicgames.myapplication.ui.views.activity.PatchNotesActivity;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.SettingsConsLayoutPatchNotes.setOnClickListener(v -> startActivity(new Intent(getContext(), PatchNotesActivity.class)));
        binding.SettingsConsLayoutLanguages.setOnClickListener(v -> new LanguagePickerDialog(this.getActivity()).show());//changeLanguage("en"));
        binding.SettingsConsLayoutSupport.setOnClickListener(v -> Toast.makeText(this.getContext(), R.string.coming_soon, Toast.LENGTH_SHORT).show());

        return view;
    }

    private void changeLanguage(String languageCode) {
        MyApplication.getInstance().getAppPreferences().saveLanguage(languageCode);
        if (getActivity() != null) {
            ((MainActivity)getActivity()).setLocale(languageCode);
            getActivity().recreate(); // Restart the activity to apply changes
        }
    }
}