package com.classicgames.myapplication.ui.views.activity;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityMainBinding;
import com.classicgames.myapplication.ui.views.fragment.GamesFragment;
import com.classicgames.myapplication.ui.views.fragment.HelpFragment;
import com.classicgames.myapplication.ui.views.fragment.LeaderboardFragment;
import com.classicgames.myapplication.ui.views.fragment.SettingsFragment;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageCode = MyApplication.getInstance().getAppPreferences().getLanguage();
        setLocale(languageCode);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new GamesFragment());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
            }
        });
        binding.MainActivityBottomNavView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.BottomNav_Games){
                replaceFragment(new GamesFragment());
            } else if (item.getItemId() == R.id.BottomNav_Leaderboard) {
                replaceFragment(new LeaderboardFragment());
            } else if (item.getItemId() == R.id.BottomNav_Help) {
                replaceFragment(new HelpFragment());
            } else if (item.getItemId() == R.id.BottomNav_Settings) {
                replaceFragment(new SettingsFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ActivityMain_Fragment,fragment).commit();
    }
}