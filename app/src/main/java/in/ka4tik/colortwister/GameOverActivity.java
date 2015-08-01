package in.ka4tik.colortwister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        int score = getIntent().getIntExtra("Score", 0);

        TextView scoreTextView = (TextView) findViewById(R.id.score);
        scoreTextView.setText("You scored " + Integer.toString(score));

        final Button playAgainButton = (Button) findViewById(R.id.play_again);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameOverActivity.this, ColorTwisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
