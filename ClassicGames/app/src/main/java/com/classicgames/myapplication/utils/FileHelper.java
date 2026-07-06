package com.classicgames.myapplication.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class FileHelper {
    private final ArrayList<String> wordList;

    public FileHelper(InputStream wordListStream) throws IOException {
        wordList = new ArrayList<>();

        // try-with-resources closes the reader (and the underlying stream) automatically.
        try (BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream))) {
            String line;
            while ((line = in.readLine()) != null) {
                String word = line.trim();
                if (!word.isEmpty()) wordList.add(word);
            }
        }
    }

    public String GetWord() {
        if (wordList.isEmpty()) return "";
        int index = new Random().nextInt(wordList.size());
        return wordList.get(index);
    }
}
