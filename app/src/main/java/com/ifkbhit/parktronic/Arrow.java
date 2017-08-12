package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;

public class Arrow extends Pointer {

    Rect pos;
    int orient;
    Paint paint;
    Path polygon;
    int shift = 0;
    MyTime timer;
    int ampl;

    static final int VERTICAL = 0;
    static final int HORIZONTAL = 1;

    Arrow(Rect position, int orientation, int amplitude) {
        pos = position;
        orient = orientation;
        ampl = amplitude;
        paint = new Paint();
        paint.setStrokeWidth(5);
        timer = new MyTime();
        PointF[] arr;
        if (orient == VERTICAL) {
            arr = new PointF[]{
                    new PointF(pos.left + pos.width() / 2, pos.bottom),
                    new PointF(pos.right, pos.bottom - pos.height() / 3),
                    new PointF(pos.right - pos.width() / 4, pos.bottom - pos.height() / 3),
                    new PointF(pos.right - pos.width() / 4, pos.top),
                    new PointF(pos.left + pos.width() / 4, pos.top),
                    new PointF(pos.left + pos.width() / 4, pos.bottom - pos.height() / 3),
                    new PointF(pos.left, pos.bottom - pos.height() / 3),
                    new PointF(pos.left + pos.width() / 2, pos.bottom)
            };
        }
        else {
            arr = new PointF[]{
                    new PointF(pos.left, pos.top + pos.height() / 2),
                    new PointF(pos.left + pos.width() / 3, pos.bottom),
                    new PointF(pos.left + pos.width() / 3, pos.bottom - pos.height() / 4),
                    new PointF(pos.right, pos.bottom - pos.height() / 4),
                    new PointF(pos.right, pos.top + pos.height() / 4),
                    new PointF(pos.left + pos.width() / 3, pos.top + pos.height() / 4),
                    new PointF(pos.left + pos.width() / 3, pos.top),
                    new PointF(pos.left, pos.top + pos.height() / 2)
            };
        }
        polygon = new Path();
        polygon.moveTo(arr[0].x, arr[0].y);
        for (PointF pnt : arr) {
            polygon.lineTo(pnt.x, pnt.y);
        }
    }

    @Override
    void animate() {
        double sin = Math.sin(6 * (double) System.currentTimeMillis() / 2000.0);
        shift = (int)(ampl * sin);
    }

    @Override
    void draw(Canvas canvas) {
        canvas.save();
        if (orient == HORIZONTAL) {
            canvas.translate(shift, 0);
        }
        else {
            canvas.translate(0, shift);
        }
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(polygon, paint);
        paint.setColor(Color.rgb(255, 200, 0));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(polygon, paint);

        canvas.restore();
    }

}
