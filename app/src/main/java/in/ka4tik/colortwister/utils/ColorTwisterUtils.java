package in.ka4tik.colortwister.utils;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;

public class ColorTwisterUtils {

    public static Rect drawText(Canvas canvas, String captionString, int xOffset, int yOffset, int color, int textSize) {
        TextPaint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(color);
        paintText.setTextSize(textSize);
        paintText.setStyle(Paint.Style.FILL_AND_STROKE);
        paintText.setStrokeWidth(1);
      //  paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);
        Rect rectText = new Rect();
        paintText.getTextBounds(captionString, 0, captionString.length(), rectText);
        canvas.drawText(captionString, xOffset, yOffset + rectText.height(), paintText);
        return new Rect(xOffset, yOffset, xOffset + rectText.width(), yOffset + rectText.height());
    }
}
