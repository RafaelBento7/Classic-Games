package com.classicgames.myapplication.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class FileHelper {
    private final ArrayList<String> wordList;

    public FileHelper(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        wordList = new ArrayList<>();

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
        }
    }

    public String GetWord() {
        int index = new Random().nextInt(wordList.size());
        String t = wordList.get(index);
        return wordList.get(index);
    }
}
