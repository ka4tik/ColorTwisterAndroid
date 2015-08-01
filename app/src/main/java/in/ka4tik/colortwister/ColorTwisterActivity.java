package in.ka4tik.colortwister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class ColorTwisterActivity extends Activity {


    private ImageView RedButton, GreenButton, BlueButton, YellowButton;
    private TextView score, lives, twister, question, bestScoreTextView, timer;
    private ColorTwisterGame game;
    private Timer timerGameEnd, timerEverySecond;
    private int timeRemaining;
    private int bestScore;
    private final int IN_GAME_WAIT_TIME = 500; // milli seconds
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_twister);

        RedButton = (ImageView) findViewById(R.id.red);
        GreenButton = (ImageView) findViewById(R.id.green);
        YellowButton = (ImageView) findViewById(R.id.yellow);
        BlueButton = (ImageView) findViewById(R.id.blue);

        score = (TextView) findViewById(R.id.score);
        lives = (TextView) findViewById(R.id.lives);
        timer = (TextView) findViewById(R.id.timer);
        twister = (TextView) findViewById(R.id.twister);
        bestScoreTextView = (TextView) findViewById(R.id.bestscore);
        question = (TextView) findViewById(R.id.question);


        startNewGame();
    }

    public void onPause() {
        super.onPause();

    }

    public void onResume() {
        super.onResume();

    }

    void startNewGame() {
        //getting preferences
        //for getting best score
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        bestScore = prefs.getInt("bestScore", 0); //0 is the default value

        game = new ColorTwisterGame();


        RedButton.setOnClickListener(new ButtonClickListener(Color.RED));
        GreenButton.setOnClickListener(new ButtonClickListener(Color.GREEN));
        YellowButton.setOnClickListener(new ButtonClickListener(Color.YELLOW));
        BlueButton.setOnClickListener(new ButtonClickListener(Color.BLUE));

        timeRemaining = ColorTwisterGame.TIME_OUT;
        updateView();
        enableButtons();

    }

    private class ButtonClickListener implements View.OnClickListener {
        int color;

        public ButtonClickListener(int color) {
            this.color = color;
        }

        public void onClick(View view) {

            boolean wasCorrect = game.processAnswer(color);
            if (wasCorrect) {
                if (mMediaPlayer != null) mMediaPlayer.release();
                mMediaPlayer = MediaPlayer.create(ColorTwisterActivity.this, R.raw.reward_music);
                mMediaPlayer.start();
            } else {
                if (mMediaPlayer != null) mMediaPlayer.release();
                mMediaPlayer = MediaPlayer.create(ColorTwisterActivity.this, R.raw.aarrrrhh);
                mMediaPlayer.start();

            }
            cancelTimers();
            disableButtons();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateView();
                        }
                    });
                }
            }, IN_GAME_WAIT_TIME);
        }

    }

    void cancelTimers() {
        if (timerGameEnd != null)
            timerGameEnd.cancel();
        if (timerEverySecond != null) {
            timerEverySecond.cancel();
            timeRemaining = ColorTwisterGame.TIME_OUT;
        }
    }

    void updateView() {

        if (mMediaPlayer != null) mMediaPlayer.release();

        enableButtons();

        if (!game.isGameOver()) {
            if (game.getScore() > bestScore)
                bestScore = game.getScore();

            score.setText(Integer.toString(game.getScore()));
            lives.setText(Integer.toString(game.getLives()));
            timer.setTextColor(Color.BLACK);
            timer.setText(Integer.toString(timeRemaining));
            twister.setTextColor(game.getPrintColor());
            twister.setText(game.getDisplayText());
            bestScoreTextView.setText(Integer.toString(bestScore));

            if (game.getAskedPrintColor())
                question.setText("What's color of above text?");
            else
                question.setText("What's the above text?");

            //create a timerGameEnd for 5 seconds
            timerEverySecond = new Timer();
            timerEverySecond.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            timeRemaining--;
                            if (timeRemaining > 0) {
                                if (mMediaPlayer != null) mMediaPlayer.release();

                                mMediaPlayer = MediaPlayer.create(ColorTwisterActivity.this, R.raw.beep_tim);
                                mMediaPlayer.start();
                            }
                            if (timeRemaining <= 2)
                                timer.setTextColor(Color.RED);
                            timer.setText(Integer.toString(timeRemaining));
                        }
                    });
                }
            }, 1000, 1000);
            //delay,period
            timerGameEnd = new Timer();
            timerGameEnd.schedule(new TimerTask() {
                @Override
                public void run() {
                    //this is required as in android only thread that created the ui can update it
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          game.sendTimerExpired();
                                          cancelTimers();
                                          disableButtons();
                                          new Timer().schedule(new TimerTask() {
                                              @Override
                                              public void run() {
                                                  runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          updateView();
                                                      }
                                                  });
                                              }
                                          }, IN_GAME_WAIT_TIME);
                                      }

                                  }
                    );

                }
            }, ColorTwisterGame.TIME_OUT * 1000);

        } else {
            disableButtons();
            SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("bestScore", bestScore);
            editor.apply();
            Intent intent = new Intent(this, GameOverActivity.class);

            intent.putExtra("Score", game.getScore());
            startActivity(intent);
        }
    }

    void disableButtons() {
        RedButton.setEnabled(false);
        GreenButton.setEnabled(false);
        YellowButton.setEnabled(false);
        BlueButton.setEnabled(false);
    }

    void enableButtons() {
        RedButton.setEnabled(true);
        GreenButton.setEnabled(true);
        YellowButton.setEnabled(true);
        BlueButton.setEnabled(true);
    }


}


