package in.ka4tik.colortwister;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;


public class ColorTwisterGame extends AndroidGame {

    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
