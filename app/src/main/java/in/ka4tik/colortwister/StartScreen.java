package in.ka4tik.colortwister;

import android.graphics.Color;
import android.graphics.Rect;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

import in.ka4tik.colortwister.utils.ColorTwisterUtils;


public class StartScreen extends Screen {

    private Rect startButtonRect;

    public StartScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            Input.TouchEvent event = touchEvents.get(i);
            if (event.type == Input.TouchEvent.TOUCH_UP) {
                if (startButtonRect != null && startButtonRect.contains(event.x, event.y)) {
                    game.setScreen(new MainGameScreen(game));
                    break;
                }

            }
        }
    }

    @Override
    public void present(float deltaTime) {

        startButtonRect = ColorTwisterUtils.drawText(game.getGraphics().getCanvas(), "Start Game", 70, 480 / 2, Color.GREEN, 35);
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
