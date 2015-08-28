package in.ka4tik.colortwister.model;


import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import in.ka4tik.colortwister.utils.StopWatch;

public class ColorTwister {

    public static final int TIME_OUT = 5;
    public static final int LIVES = 10;
    private int score, lives;
    private Random rnd;
    private boolean isGameOver, askedActualColor;
    private Color actualColor; // actual color of text
    private Color printedColor; // color which is printed
    private Color answerColor;
    private String displayText;
    private Timer timer;
    private StopWatch stopWatch;

    public ColorTwister() {
        score = 0;
        lives = ColorTwister.LIVES;
        rnd = new Random();
        isGameOver = false;
        stopWatch = new StopWatch();
        stopWatch.start();
        generateNewTwister();
    }

    private String distortString(String str) {
        str = str.toLowerCase();
        String distortedString = "";
        for (int i = 0; i < str.length(); i++) {
            if (rnd.nextBoolean())
                distortedString += Character.toUpperCase(str.charAt(i));
            else
                distortedString += str.charAt(i);
        }
        return distortedString;
    }

    private void generateNewTwister() {
        if (timer != null)
            timer.cancel();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                processTimerExpired();
            }
        }, TIME_OUT * 1000);
        actualColor = Color.randomColor();
        printedColor = Color.randomColor();
        askedActualColor = rnd.nextBoolean();
        if (askedActualColor)
            answerColor = actualColor;
        else
            answerColor = printedColor;
        displayText = distortString(printedColor.toString());
        stopWatch.reset();
    }

    public boolean processAnswer(Color color) {
        boolean wasCorrect = false;
        if (answerColor == color) {
            score++;
            wasCorrect = true;
        } else
            lives--;
        if (lives == 0)
            isGameOver = true;

        generateNewTwister();
        return wasCorrect;
    }

    private void processTimerExpired() {
        lives--;
        if (lives == 0)
            isGameOver = true;
        generateNewTwister();
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public String getDisplayText() {
        return displayText;
    }

    public boolean isAskedActualColor() {
        return askedActualColor;
    }

    public Color getActualColor() {
        return actualColor;
    }

    public int getTimeRemaining() {
        return TIME_OUT - stopWatch.getElapsedTimeSeconds();
    }
    public long getTimeRemainingMilliSec(){
        return TIME_OUT*1000 - stopWatch.getElapsedTimeMillis();
    }

}
