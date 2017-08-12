package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;

public class Bee extends Pointer {

    Paint pth_paint, crcl_paint;
    Path path;
    MyTime timer;
    int alpha;
    PointF pos;
    PathMeasure pm;
    float radius;
    int freq;

    Bee(Path path, int frequency) {
        radius = Config.BRICK_L / 2;
        freq = frequency;

        pth_paint = new Paint();
        pth_paint.setColor(Color.LTGRAY);
        pth_paint.setStrokeWidth(radius / 2);
        pth_paint.setStrokeCap(Paint.Cap.ROUND);
        pth_paint.setStyle(Paint.Style.STROKE);

        crcl_paint = new Paint();
        crcl_paint.setStrokeWidth(radius / 4);

        this.path = path;

        pm = new PathMeasure(path, false);

        timer = new MyTime();
        alpha = 0;
        float[] ps = {0, 0};
        pm.getPosTan(0, ps, null);
        pos = new PointF(ps[0], ps[1]);
    }

    @Override
    public void animate() {
        timer.Refresh();
        float[] ps = {0, 0};
        float pathLen = pm.getLength();
        if (timer.FromStartS <= 0.5) {
            alpha = (int)(timer.FromStartS * 2 * 255);
            pm.getPosTan(0, ps, null);
        }
        else if (timer.FromStartS <= 2.55) {
            alpha = 255;
            pm.getPosTan(pathLen * (float)(timer.FromStartS - 0.5) / 2, ps, null);
        }
        else if (timer.FromStartS <= 3) {
            alpha = 255 - (int)((timer.FromStartS - 2) * 2 * 255);
            pm.getPosTan(pathLen, ps, null);
        }
        else {
            alpha = 0;
            timer.Restart();
            pm.getPosTan(0, ps, null);
        }
        pos.x = ps[0];
        pos.y = ps[1];
    }

    private void drawArrow(Canvas canvas, float[] ps, float[] tg) {
        float[] pf = {tg[0] * -0.7f + tg[1] * 0.7f, tg[1] * -0.7f - tg[0] * 0.7f};
        canvas.drawLine(ps[0], ps[1], ps[0] + pf[0] * radius, ps[1] + pf[1] * radius, pth_paint);
        pf[0] = tg[0] * -0.7f + tg[1] * -0.7f;
        pf[1] = tg[1] * -0.7f - tg[0] * -0.7f;
        canvas.drawLine(ps[0], ps[1], ps[0] + pf[0] * radius, ps[1] + pf[1] * radius, pth_paint);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, pth_paint);
        float[] ps = {0, 0}, tg = {0, 0};
        float pathLen = pm.getLength();
        for (int i = 0; i < freq; ++i) {
            pm.getPosTan(pathLen * (i + 0.7f) / freq, ps, tg);
            drawArrow(canvas, ps, tg);
        }
        pm.getPosTan(pathLen, ps, tg);
        canvas.drawCircle(ps[0], ps[1], radius / 4, pth_paint);

        crcl_paint.setColor(Color.argb(alpha, 0, 0, 0));
        crcl_paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pos.x, pos.y, radius, crcl_paint);
        crcl_paint.setColor(Color.argb(alpha, 255, 200, 0));
        crcl_paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(pos.x, pos.y, radius, crcl_paint);
    }
}
