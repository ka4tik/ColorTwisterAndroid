package in.ka4tik.colortwister;

import android.graphics.Color;

import java.util.Random;

public class ColorTwisterGame {


    public static final int TIME_OUT = 5;
    private int score;
    private int lives;
    private Random rnd;
    private boolean isGameOver;
    private int printColor;
    private int answer;
    private String displayText;
    private boolean askedPrintColor;

    public ColorTwisterGame() {
        score = 0;
        lives = 10;
        rnd = new Random();
        isGameOver = false;
        generateNewTwister();

    }

    String getColorString(int color) {
        String str = "";
        if (color == Color.RED)
            str = "red";
        else if (color == Color.YELLOW)
            str = "yellow";
        else if (color == Color.GREEN)
            str = "green";
        else if (color == Color.BLUE)
            str = "blue";
        return str;
    }

    String distortString(String str) {
        String distortedString = "";
        for (int i = 0; i < str.length(); i++) {
            if (rnd.nextBoolean())
                distortedString += Character.toUpperCase(str.charAt(i));
            else
                distortedString += str.charAt(i);
        }
        return distortedString;
    }

    void generateNewTwister() {
        printColor = getRandomColor();
        int current_text = getRandomColor();
        askedPrintColor = rnd.nextBoolean();
        if (askedPrintColor)
            answer = printColor;
        else
            answer = current_text;
        displayText = distortString(getColorString(current_text));
    }

    int getPrintColor() {
        return printColor;
    }

    int getRandomColor() {
        int ret = Color.RED;
        switch (rnd.nextInt(4)) {
            case 0:
                ret = Color.RED;
                break;
            case 1:
                ret = Color.GREEN;
                break;
            case 2:
                ret = Color.YELLOW;
                break;
            case 3:
                ret = Color.BLUE;
                break;
        }
        return ret;
    }

    boolean processAnswer(int color) {
        boolean wasCorrect = false;
        if (answer == color) {
            score++;
            wasCorrect = true;
        } else
            lives--;
        if (lives == 0)
            isGameOver = true;
        generateNewTwister();
        return wasCorrect;
    }

    void sendTimerExpired() {
        lives--;
        if (lives == 0)
            isGameOver = true;
        generateNewTwister();
    }

    boolean isGameOver() {
        return isGameOver;
    }

    int getScore() {
        return score;
    }

    int getLives() {
        return lives;
    }

    String getDisplayText() {
        return displayText;
    }

    boolean getAskedPrintColor() {
        return askedPrintColor;
    }

}
