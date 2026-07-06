package com.classicgames.myapplication.ui.views.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.classicgames.myapplication.MyApplication;
import com.classicgames.myapplication.R;
import com.classicgames.myapplication.databinding.ActivityPatchNotesBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Renders the patch notes from {@code assets/patch_notes.json} instead of a pile
 * of per-version string resources. Each entry carries its text per language, so
 * adding a version (or a translation) only means editing the JSON.
 */
public class PatchNotesActivity extends BaseActivity {

    private static final String PATCH_NOTES_FILE = "patch_notes.json";
    private static final String FALLBACK_LANGUAGE = "en";

    ActivityPatchNotesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPatchNotesBinding.inflate(getLayoutInflater());
        binding.getRoot().setFitsSystemWindows(true);
        setContentView(binding.getRoot());
        setSupportActionBar(binding.PatchNotesToolbar.getRoot());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.PatchNotesToolbar.ToolbarTitle.setText(R.string.patch_notes);

        renderPatchNotes();
    }

    private void renderPatchNotes() {
        String language = MyApplication.getInstance() != null
                ? MyApplication.getInstance().getAppPreferences().getLanguage()
                : FALLBACK_LANGUAGE;

        LayoutInflater inflater = getLayoutInflater();
        try {
            JSONArray versions = new JSONObject(readAsset()).getJSONArray("versions");
            // Latest version first
            for (int i = versions.length() - 1; i >= 0; i--) {
                JSONObject entry = versions.getJSONObject(i);
                JSONObject notes = entry.getJSONObject("notes");
                String text = extractNote(notes, language);

                View item = inflater.inflate(R.layout.item_patch_note, binding.PatchNotesContainer, false);
                ((TextView) item.findViewById(R.id.PatchNote_Version)).setText(entry.getString("version"));
                ((TextView) item.findViewById(R.id.PatchNote_Text)).setText(text);
                binding.PatchNotesContainer.addView(item);
            }
        } catch (Exception e) {
            // A malformed/missing file shouldn't crash the screen
        }
    }

    /**
     * A note can be a single paragraph ({@code "en": "..."}) or an array where each
     * element is its own line ({@code "en": ["...", "..."]}). Arrays are joined with
     * newlines so a version can list its changes one per line. Falls back to
     * {@link #FALLBACK_LANGUAGE} when the requested language is missing.
     */
    private String extractNote(JSONObject notes, String language) {
        Object value = notes.opt(language);
        if (value == null || value == JSONObject.NULL) {
            value = notes.opt(FALLBACK_LANGUAGE);
        }
        if (value instanceof JSONArray) {
            JSONArray lines = (JSONArray) value;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < lines.length(); i++) {
                if (i > 0) sb.append('\n');
                sb.append(lines.optString(i));
            }
            return sb.toString();
        }
        return value != null ? value.toString() : "";
    }

    private String readAsset() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getAssets().open(PATCH_NOTES_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }
}
