package com.classicgames.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.classicgames.myapplication.databinding.ActivityMainBinding;
import com.classicgames.myapplication.ui.views.fragment.GamesFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new GamesFragment());

        binding.MainActivityBottomNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.BottomNav_Games:
                    replaceFragment(new GamesFragment());
                    break;
                case R.id.BottomNav_Leaderboard:
                    //replaceFragment(new FlightSearchFragment());
                    break;
                case R.id.BottomNav_Help:
                    //replaceFragment(new RestaurantsFragment());
                    break;
                case R.id.BottomNav_News:
                    //replaceFragment(new StoresFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ActivityMain_Fragment,fragment).commit();
    }

    /* TODO
        TO CHANGE TO HELP FRAGMENT
    private void snakeHelp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.snake_game));
        builder.setMessage(
                getResources().getString(R.string.snake_rules)+"\n\n"+
                getResources().getString(R.string.speed)+"\n"+
                getResources().getString(R.string.snake_speed_level1)+"\n"+
                getResources().getString(R.string.snake_speed_level2)+"\n"+
                getResources().getString(R.string.snake_speed_level3)+"\n"+
                getResources().getString(R.string.snake_speed_level4)+"\n"+
                getResources().getString(R.string.snake_speed_level5)+"\n"+
                getResources().getString(R.string.snake_speed_level6)+"\n");
        builder.setPositiveButton(getResources().getString(R.string.lets_try), (dialog, which) -> {});
        builder.show();
    }

    private void trueColorsHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.true_colors_game));
        builder.setMessage(getResources().getString(R.string.true_colors_rules));
        builder.setPositiveButton(getResources().getString(R.string.lets_try), (dialog, which) -> {});
        builder.show();
    }

    private void mastermindHelp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getResources().getString(R.string.mastermind_game));
        builder.setMessage(
                getResources().getString(R.string.mastermind_help)+"\n"+
                getResources().getString(R.string.mastermind_help2)+"\n"+
                getResources().getString(R.string.mastermind_help3)+"\n"+
                getResources().getString(R.string.mastermind_help4)+"\n"+
                getResources().getString(R.string.mastermind_help5)+"\n"+
                getResources().getString(R.string.mastermind_help6)+"\n");
        builder.setPositiveButton(getResources().getString(R.string.lets_try), (dialog, which) -> {});
        builder.show();
    }*/
}