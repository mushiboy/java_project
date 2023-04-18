package com.example.game233;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.util.Pair;

public class ScoreBoard implements saveHandler {
    private List<Pair<String, Integer>> scoresList = new ArrayList();
    private final String FILE_NAME;
    private final String PARENTDIRECTORY_NAME;

    public ScoreBoard(String PARENTDIRECTORY_NAME, String FILE_NAME) {
        this.checkValidFileString(PARENTDIRECTORY_NAME, "Directory name can only include letters, numbers and underscores.");
        this.checkValidFileString(FILE_NAME, "File name can only include letters, numbers and underscores.");
        this.PARENTDIRECTORY_NAME = PARENTDIRECTORY_NAME;
        this.FILE_NAME = FILE_NAME;
        this.load();
    }

    public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.getFilePath()));

            try {
                Iterator var2 = this.scoresList.iterator();

                while(true) {
                    if (!var2.hasNext()) {
                        writer.close();
                        break;
                    }

                    Pair<String, Integer> entry = (Pair)var2.next();
                    String var10001 = (String)entry.getKey();
                    writer.write(var10001 + ":" + String.valueOf(entry.getValue()) + "\n");
                }
            } catch (Throwable var5) {
                try {
                    writer.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            writer.close();
        } catch (FileNotFoundException var6) {
            (new File(this.PARENTDIRECTORY_NAME)).mkdir();
            this.save();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.getFilePath()));

            try {
                this.scoresList = (List)reader.lines().map((element) -> {
                    return element.split(":");
                }).map((element) -> {
                    return new Pair(element[0], Integer.parseInt(element[1]));
                }).collect(Collectors.toList());
                reader.close();
            } catch (Throwable var5) {
                try {
                    reader.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            reader.close();
        } catch (FileNotFoundException var6) {
            (new File(this.PARENTDIRECTORY_NAME)).mkdir();
            this.scoresList = new ArrayList();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

    }

    public void addScore(String player, int score) {
        if (!player.isBlank() && !player.isEmpty() && score >= 0) {
            this.scoresList.add(new Pair(player, score));
            this.scoresList.sort((score1, score2) -> {
                return (Integer)score2.getValue() - (Integer)score1.getValue();
            });
            this.save();
        } else {
            throw new IllegalArgumentException("innvalid score");
        }
    }

    public int getHighScore() {
        return this.scoresList.isEmpty() ? 0 : (Integer)((Pair)this.scoresList.get(0)).getValue();
    }

    public List<Pair<String, Integer>> getScores() {
        return this.scoresList;
    }

    private String getFilePath() {
        return this.PARENTDIRECTORY_NAME + "/" + this.FILE_NAME + ".txt";
    }

    private void checkValidFileString(String s, String message) {
        if (!Pattern.matches("[a-zA-Z0-9_]+", s)) {
            throw new IllegalArgumentException(message);
        }
    }
}
