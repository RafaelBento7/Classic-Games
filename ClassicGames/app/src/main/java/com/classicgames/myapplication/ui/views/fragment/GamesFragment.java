package com.classicgames.myapplication.ui.views.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.FragmentGamesBinding;
import com.classicgames.myapplication.ui.views.activity.MastermindActivity;
import com.classicgames.myapplication.ui.views.activity.SnakeActivity;
import com.classicgames.myapplication.ui.views.activity.TicTacToeActivity;
import com.classicgames.myapplication.ui.views.activity.TrueColorsActivity;
import com.classicgames.myapplication.ui.views.activity.WordleActivity;
import com.classicgames.myapplication.ui.dialog.CustomDialog;
import com.classicgames.myapplication.ui.dialog.SnakeMapSizeDialog;

import java.util.concurrent.atomic.AtomicInteger;

public class GamesFragment extends Fragment {

    FragmentGamesBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGamesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.GamesFragmentIBSnake.setOnClickListener(v -> snakeGame());
        binding.GamesFragmentIBTrueColors.setOnClickListener(v -> startActivity(new Intent(getContext(), TrueColorsActivity.class)));
        binding.GamesFragmentIBMastermind.setOnClickListener(v -> startActivity(new Intent(getContext(), MastermindActivity.class)));
        binding.GamesFragmentIBTicTacToe.setOnClickListener(v -> startActivity(new Intent(getContext(), TicTacToeActivity.class)));
        binding.GamesFragmentIBWordle.setOnClickListener(v -> startActivity(new Intent(getContext(), WordleActivity.class)));

        return view;
    }

    private void snakeGame(){
        AtomicInteger snakeMapSize = new AtomicInteger(0);
        SnakeMapSizeDialog snakeMapSizeDialog = new SnakeMapSizeDialog(getActivity());
        snakeMapSizeDialog.show();
        snakeMapSizeDialog.setOnDismissListener(dialog -> {
            snakeMapSize.set(snakeMapSizeDialog.getMapLevel());
            if (snakeMapSize.get() != 0){
                CustomDialog.DialogButtonClick dialogButtonClick = new CustomDialog.DialogButtonClick() {
                    @Override
                    public void onPositiveButtonClicked() {
                        openSnakeGame(snakeMapSize.get(), true);
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        openSnakeGame(snakeMapSize.get(), false);
                    }
                };

                CustomDialog obstaclesDialog = new CustomDialog(getActivity(),
                        getString(R.string.snake_obstacles),
                        null,
                        dialogButtonClick);
                obstaclesDialog.show();
            }
        });
    }

    private void openSnakeGame(int snakeMapSize, boolean obstaclesGame) {
        Intent intent = new Intent(getContext(), SnakeActivity.class);
        intent.putExtra(SnakeActivity.MAP_SIZE, snakeMapSize);
        intent.putExtra(SnakeActivity.OBSTACLES_GAME, obstaclesGame);
        startActivity(intent);
    }
}