package in.ka4tik.colortwister;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class ColorTwisterActivity extends ActionBarActivity {


    Button RedButton,GreenButton,BlueButton,YellowButton;
    TextView score, lives,twister,question,bestscore,timer;
    boolean isGameOver=false;
    ColorTwisterGame game;
    Timer timerGameEnd,timerEverySecond;
    int timeRemaning;
    int bestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_twister);

        RedButton=(Button)findViewById(R.id.red);
        GreenButton=(Button)findViewById(R.id.green);
        YellowButton=(Button)findViewById(R.id.yellow);
        BlueButton=(Button)findViewById(R.id.blue);

        score=(TextView)findViewById(R.id.score);
        lives =(TextView)findViewById(R.id.lives);
        timer=(TextView)findViewById(R.id.timer);
        twister=(TextView)findViewById(R.id.twister);
        bestscore=(TextView)findViewById(R.id.bestscore);
        question=(TextView)findViewById(R.id.question);

        startNewGame();
    }

    void startNewGame()
    {
        //getting preferences
        //for getting best score
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        bestScore = prefs.getInt("bestScore", 0); //0 is the default value

        game=new ColorTwisterGame();
        RedButton.setEnabled(true);
        GreenButton.setEnabled(true);
        YellowButton.setEnabled(true);
        BlueButton.setEnabled(true);

        RedButton.setOnClickListener(new ButtonClickListener(Color.RED));
        GreenButton.setOnClickListener(new ButtonClickListener(Color.GREEN));
        YellowButton.setOnClickListener(new ButtonClickListener(Color.YELLOW));
        BlueButton.setOnClickListener(new ButtonClickListener(Color.BLUE));

        timeRemaning=ColorTwisterGame.TIME_OUT;
        update_view();
    }
    private class ButtonClickListener implements View.OnClickListener {
        int color;

        public ButtonClickListener(int color) {
            this.color = color;
        }

        public void onClick(View view) {

           game.process_answer(color);
           update_view();
        }
    }
    void update_view()
    {

        if(timerGameEnd!=null)
            timerGameEnd.cancel();
        if(timerEverySecond!=null)
            timerEverySecond.cancel();
        timeRemaning=ColorTwisterGame.TIME_OUT;
        if(!game.isGameOver())
        {
            if(game.getScore()>bestScore)
                bestScore=game.getScore();

            score.setText("Score: " + Integer.toString(game.getScore()));
            lives.setText("Lives: " + Integer.toString(game.getLives()));
            timer.setTextColor(Color.BLACK);
            timer.setText(Integer.toString(timeRemaning));
            twister.setTextColor(game.getPrint_color());
            twister.setText(game.getDisplay_text());
            bestscore.setText("Best Score: " + Integer.toString(bestScore));

            if(game.getAskedPrintColor())
               question.setText("What's color of above text?");
            else
                question.setText("What's the above text?");

            //create a timerGameEnd for 5 seconds
            timerEverySecond=new Timer();
            timerEverySecond.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeRemaning--;
                            if(timeRemaning<=2)
                                timer.setTextColor(Color.RED);
                            timer.setText(Integer.toString(timeRemaning));
                        }
                    });
                }
            },1000,1000);
            //delay,peroid
            timerGameEnd =new Timer();
            timerGameEnd.schedule(new TimerTask() {
                @Override
                public void run() {
                    //this is required as in android only thread that created the ui can update it
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            game.sendTimerExpired();
                            update_view();
                        }
                    });

                }
            }, ColorTwisterGame.TIME_OUT*1000);

        }
        else
        {
            RedButton.setEnabled(false);
            GreenButton.setEnabled(false);
            YellowButton.setEnabled(false);
            BlueButton.setEnabled(false);
            SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("bestScore", bestScore);
            editor.apply();
            showGameOverPopUp(findViewById(R.id.twister));
        }
    }

    void showGameOverPopUp(View anchorView)
    {
        LayoutInflater inflater=(LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupview=inflater.inflate(R.layout.gameover_popup,null);
        final PopupWindow popupWindow=new PopupWindow(popupview, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        Button play_again=(Button)popupview.findViewById(R.id.playagain);
        play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                startNewGame();
            }
        });
        popupWindow.showAsDropDown(anchorView, 50, -30);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_twister, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
