package in.ka4tik.colortwister;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

import in.ka4tik.colortwister.utils.ColorTwisterUtils;


public class GameOverScreen extends Screen {

    private Canvas canvas;
    private Rect playAgainRect;

    public GameOverScreen(Game game) {
        super(game);
        canvas = game.getGraphics().getCanvas();
    }

    @Override
    public void update(float deltaTime) {

        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (playAgainRect != null && playAgainRect.contains(event.x, event.y)) {
                    game.setScreen(new MainGameScreen(game));
                    break;
                }
            }
        }

    }

    @Override
    public void present(float deltaTime) {

        ColorTwisterUtils.drawText(canvas, "Game Over!", 100, 480 / 2, Color.BLUE, 20);
        playAgainRect = ColorTwisterUtils.drawText(canvas, "Play Again!", 80, 480 / 2 + 50, Color.GREEN, 30);

    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }


}
