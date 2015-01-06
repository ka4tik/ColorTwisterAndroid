package in.ka4tik.colortwister;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ColorTwisterActivity extends ActionBarActivity {

    Button RedButton,GreenButton,BlueButton,YellowButton;
    TextView score, lives,twister,question,bestscore;
    boolean isGameOver=false;
    ColorTwisterGame game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_twister);


        RedButton=(Button)findViewById(R.id.red);
        GreenButton=(Button)findViewById(R.id.green);
        YellowButton=(Button)findViewById(R.id.yellow);
        BlueButton=(Button)findViewById(R.id.blue);

        score=(TextView)findViewById(R.id.score);
        lives =(TextView)findViewById(R.id.timer);
        twister=(TextView)findViewById(R.id.twister);
        bestscore=(TextView)findViewById(R.id.bestscore);
        question=(TextView)findViewById(R.id.question);
        startNewGame();


    }
    void startNewGame()
    {
        game=new ColorTwisterGame();
        RedButton.setOnClickListener(new ButtonClickListener(Color.RED));
        GreenButton.setOnClickListener(new ButtonClickListener(Color.GREEN));
        YellowButton.setOnClickListener(new ButtonClickListener(Color.YELLOW));
        BlueButton.setOnClickListener(new ButtonClickListener(Color.BLUE));
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
        if(!game.isGameOver())
        {
            score.setText("Score: " + Integer.toString(game.getScore()));
            lives.setText("Lives: " + Integer.toString(game.getLives()));
            twister.setTextColor(game.getPrint_color());
            twister.setText(game.getDisplay_text());
            if(game.getAskedPrintColor())
               question.setText("What's the color of above text?");
            else
                question.setText("What's the above text?");
        }
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
