package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

class BlinkRect extends Pointer {

    Rect rect;
    Paint paint;

    BlinkRect(Rect rect) {
        this.rect = rect;
        paint = new Paint();
        paint.setColor(Color.rgb(250, 200, 0));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    public void animate() {
        double sin = Math.sin(8 * (double) System.currentTimeMillis() / 2000.0);
        paint.setAlpha(175 - (int)(80 * sin));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, paint);
    }
}
