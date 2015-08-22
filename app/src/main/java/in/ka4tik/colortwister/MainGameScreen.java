package in.ka4tik.colortwister;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

import in.ka4tik.colortwister.model.ColorTwister;
import in.ka4tik.colortwister.utils.ColorTwisterUtils;

public class MainGameScreen extends Screen {


    private static final int CIRCLE_RADIUS = 100;
    private static final Point CIRCLE_CENTER = new Point(320 / 2, 480 / 2);
    private final int screenWidth, screenHeight;
    private final Canvas canvas;
    ColorTwister colorTwister;

    public MainGameScreen(Game game) {
        super(game);
        colorTwister = new ColorTwister();
        Graphics graphics = game.getGraphics();
        screenWidth = graphics.getWidth();
        screenHeight = graphics.getHeight();
        canvas = graphics.getCanvas();

    }

    @Override
    public void update(float deltaTime) {
        if (!colorTwister.isGameOver()) {

            List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
            game.getInput().getKeyEvents();

            int len = touchEvents.size();
            for (int i = 0; i < len; i++) {
                Input.TouchEvent event = touchEvents.get(i);
                if (event.type == Input.TouchEvent.TOUCH_UP) {
                    Point touchPoint = new Point(event.x, event.y);
                    Log.d("Touched ", touchPoint.toString());
                    if (isInsideArc(touchPoint, CIRCLE_CENTER, 0, 90, CIRCLE_RADIUS)) {//red touched
                        Log.d("Red touched", touchEvents.toString());
                        colorTwister.processAnswer(in.ka4tik.colortwister.model.Color.RED);
                        break;
                    }
                    if (isInsideArc(touchPoint, CIRCLE_CENTER, 90, 90, CIRCLE_RADIUS)) {//green touched
                        Log.d("Green touched", touchEvents.toString());
                        colorTwister.processAnswer(in.ka4tik.colortwister.model.Color.GREEN);
                        break;
                    }
                    if (isInsideArc(touchPoint, CIRCLE_CENTER, 180, 90, CIRCLE_RADIUS)) {//blue touched
                        Log.d("Yellow touched", touchEvents.toString());
                        colorTwister.processAnswer(in.ka4tik.colortwister.model.Color.BLUE);
                        break;
                    }
                    if (isInsideArc(touchPoint, CIRCLE_CENTER, 270, 90, CIRCLE_RADIUS)) {//yellow touched
                        Log.d("Blue touched", touchEvents.toString());
                        colorTwister.processAnswer(in.ka4tik.colortwister.model.Color.YELLOW);
                        break;
                    }

                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {

        game.getGraphics().clear(Color.BLACK);
        if (colorTwister.isGameOver()) {
            game.setScreen(new GameOverScreen(game));
        } else {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setShadowLayer(10, 2, 2, Color.GRAY);

            canvas.drawCircle(CIRCLE_CENTER.x, CIRCLE_CENTER.y, CIRCLE_RADIUS, paint);
            RectF oval = new RectF(
                    CIRCLE_CENTER.x - CIRCLE_RADIUS,
                    CIRCLE_CENTER.y - CIRCLE_RADIUS,
                    CIRCLE_CENTER.x + CIRCLE_RADIUS,
                    CIRCLE_CENTER.y + CIRCLE_RADIUS);

            paint.setStyle(Paint.Style.FILL);

            paint.setColor(Color.RED);
            canvas.drawArc(oval, 0, 90, true, paint);
            paint.setColor(Color.GREEN);
            canvas.drawArc(oval, 90, 90, true, paint);
            paint.setColor(Color.BLUE);
            canvas.drawArc(oval, 180, 90, true, paint);
            paint.setColor(Color.YELLOW);
            canvas.drawArc(oval, 270, 90, true, paint);

            ColorTwisterUtils.drawText(canvas, "Score: " + colorTwister.getScore(), 0, 0, Color.WHITE, 20);
            ColorTwisterUtils.drawText(canvas, "Lives: " + colorTwister.getLives(), screenWidth / 2 - 60, 0, Color.WHITE, 20);
            ColorTwisterUtils.drawText(canvas, "Time Left: " + colorTwister.getTimeRemaining(), screenWidth - 130, 0, Color.WHITE, 20);
            ColorTwisterUtils.drawText(canvas, colorTwister.getDisplayText(), 0, 30, getAndroidColor(colorTwister.getActualColor()), 20);
            if (colorTwister.isAskedActualColor())
                ColorTwisterUtils.drawText(canvas, "What's the color of above text", 0, 60, Color.WHITE, 20);
            else
                ColorTwisterUtils.drawText(canvas, "What's the above text", 0, 60, Color.WHITE, 20);
        }

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

    private int getAndroidColor(in.ka4tik.colortwister.model.Color color) {
        int androidColor = 0;
        if (color == in.ka4tik.colortwister.model.Color.RED)
            androidColor = android.graphics.Color.RED;
        else if (color == in.ka4tik.colortwister.model.Color.GREEN)
            androidColor = android.graphics.Color.GREEN;
        else if (color == in.ka4tik.colortwister.model.Color.YELLOW)
            androidColor = android.graphics.Color.YELLOW;
        else if (color == in.ka4tik.colortwister.model.Color.BLUE)
            androidColor = android.graphics.Color.BLUE;
        return androidColor;
    }

    private boolean areClockwise(Point v1, Point v2) {
        return -v1.x * v2.y + v1.y * v2.x > 0;
    }

    private boolean isWithinRadius(Point v, int radius) {
        return v.x * v.x + v.y * v.y <= radius * radius;
    }

    private boolean isInsideSector(Point point, Point center,
                                   Point sectorStart, Point sectorEnd,
                                   int radius) {
        Point relPoint = new Point(point.x - center.x, point.y - center.y);
        sectorEnd = new Point(sectorEnd.x - center.x, sectorEnd.y - center.y);
        sectorStart = new Point(sectorStart.x - center.x, sectorStart.y - center.y);
        return !areClockwise(sectorStart, relPoint) &&
                areClockwise(sectorEnd, relPoint) &&
                isWithinRadius(relPoint, radius);
    }

    private boolean isInsideArc(Point point, Point center, int startAngle, int swipeAngle, int radius) {

        Point startPoint = new Point(center.x + CIRCLE_RADIUS * Math.cos(toRadian(startAngle)),
                center.y + CIRCLE_RADIUS * Math.sin(toRadian(startAngle)));
        Point endPoint = new Point(center.x + CIRCLE_RADIUS * Math.cos(toRadian(startAngle + swipeAngle)),
                center.y + CIRCLE_RADIUS * Math.sin(toRadian(startAngle + swipeAngle)));

        Log.d("isInsideArc", startAngle + " " + startPoint.toString() + "  " + endPoint.toString());
        return isInsideSector(point, center, startPoint, endPoint, radius);
    }

    private double toRadian(int degree) {
        return (degree / 360.00) * 2 * Math.PI;
    }

    static class Point {
        public int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point(double x, double y) {
            this.x = (int) x;
            this.y = (int) y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
