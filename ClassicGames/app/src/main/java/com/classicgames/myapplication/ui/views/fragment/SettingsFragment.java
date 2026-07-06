package com.classicgames.myapplication.ui.views.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.FragmentSettingsBinding;
import com.classicgames.myapplication.data.preferences.AppPreferences;
import com.classicgames.myapplication.ui.dialog.LanguagePickerDialog;
import com.classicgames.myapplication.ui.views.activity.PatchNotesActivity;
import com.classicgames.myapplication.utils.MessageBar;
import com.classicgames.myapplication.utils.ReviewHelper;
import com.classicgames.myapplication.utils.SoundManager;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    LanguagePickerDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        binding.SettingsRowPatchNotes.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            startActivity(new Intent(getContext(), PatchNotesActivity.class));
        });
        binding.SettingsRowLanguages.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            dialog = new LanguagePickerDialog(this.getActivity(), languageCode -> {
                dialog.dismiss();
                changeLanguage(languageCode);
            });
            dialog.show();
        });
        binding.SettingsRowSupport.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            MessageBar.show(getActivity(), R.string.coming_soon);
        });
        binding.SettingsRowRate.setOnClickListener(v -> {
            SoundManager.play(SoundManager.Sound.CLICK);
            ReviewHelper.launchReview(getActivity(), this::openStoreListing);
        });

        updateSfxIcon();
        binding.SettingsRowSfx.setOnClickListener(v -> {
            AppPreferences prefs = MyApplication.getInstance().getAppPreferences();
            prefs.setSfxEnabled(!prefs.isSfxEnabled());
            updateSfxIcon();
            // Plays only when SFX was just enabled; silent when turning off.
            SoundManager.play(SoundManager.Sound.CLICK);
        });

        updateMusicIcon();
        binding.SettingsRowMusic.setOnClickListener(v -> {
            AppPreferences prefs = MyApplication.getInstance().getAppPreferences();
            prefs.setMusicEnabled(!prefs.isMusicEnabled());
            updateMusicIcon();
            MyApplication.getInstance().getMusicManager().refreshFromSettings();
            SoundManager.play(SoundManager.Sound.CLICK);
        });

        return view;
    }

    /** Shows the speaker icon when SFX is on, the muted icon when off. */
    private void updateSfxIcon() {
        boolean enabled = MyApplication.getInstance().getAppPreferences().isSfxEnabled();
        binding.SettingsIvSfx.setImageResource(enabled ? R.drawable.ic_sound : R.drawable.ic_sound_off);
    }

    /** Shows the music note icon when music is on, the muted note when off. */
    private void updateMusicIcon() {
        boolean enabled = MyApplication.getInstance().getAppPreferences().isMusicEnabled();
        binding.SettingsIvMusic.setImageResource(enabled ? R.drawable.ic_music : R.drawable.ic_music_off);
    }

    /** Opens the app's Play Store listing, falling back to the browser if the Play app is absent. */
    private void openStoreListing() {
        if (getContext() == null) return;
        String packageName = getContext().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void changeLanguage(String languageCode) {
        MyApplication.getInstance().getAppPreferences().saveLanguage(languageCode);
        if (getActivity() != null) {
            getActivity().recreate(); // attachBaseContext re-applies the saved language
        }
    }
}