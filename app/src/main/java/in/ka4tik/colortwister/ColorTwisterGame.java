package in.ka4tik.colortwister;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by ka4tik on 1/6/15.
 */
public class ColorTwisterGame {


    public static final int TIME_OUT=5;
    private int score;
    private int lives;
    private Random rnd;
    private boolean isGameOver;
    private int print_color;
    private int answer;
    private String display_text;
    private boolean askedPrintColor;
    public ColorTwisterGame()
    {
        score=0;
        lives=10;
        rnd=new Random();
        isGameOver=false;
        generateNewTwister();

    }

    String colortoString(int color)
    {
        String str = "";
        if(color==Color.RED)
        str="red";
        else if(color==Color.YELLOW)
            str="yellow";
        else if(color==Color.GREEN)
            str="green";
        else if(color==Color.BLUE)
            str="blue";
        return str;
    }
    String distortString(String str) {
        String distored_str = "";
        for (int i = 0; i < str.length(); i++)
        {
            if(rnd.nextBoolean())
                distored_str+=Character.toUpperCase(str.charAt(i));
            else
                distored_str+=str.charAt(i);
        }
        return distored_str;
    }
    void generateNewTwister()
    {
        print_color=getRandomColor();
        int current_text=getRandomColor();
        askedPrintColor=rnd.nextBoolean();
        if(askedPrintColor)
            answer=print_color;
        else
            answer=current_text;
        display_text=distortString(colortoString(current_text));
    }
    int getPrint_color()
    {
        return print_color;
    }
    int getRandomColor()
    {
        int ret = Color.RED;
        switch(rnd.nextInt(4))
        {
            case 0:
                ret= Color.RED;
                break;
            case 1:
                ret= Color.GREEN;
                break;
            case 2:
                ret= Color.YELLOW;
                break;
            case 3:
                ret= Color.BLUE;
                break;
        }
        return ret;
    }
    void process_answer(int color)
    {
        if(answer==color)
            score++;
        else
            lives--;
        if(lives==0)
            isGameOver=true;
        generateNewTwister();
    }
    void sendTimerExpired()
    {
            lives--;
        if(lives==0)
            isGameOver=true;
        generateNewTwister();
    }

    boolean isGameOver()
    {
        return isGameOver;
    }
    int getScore()
    {
        return score;
    }
    int getLives()
    {
        return lives;
    }
    String getDisplay_text()
    {
        return display_text;
    }
    boolean getAskedPrintColor()
    {
        return askedPrintColor;
    }

}
