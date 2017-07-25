package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by Роман on 10.03.2017.
 */

public class Button {
    private Texture texture;
    private Point startPos;
    Brick touchChecker;

    public Button(int textureId, Resources res, Canvas canvas, double kForOx, double kForOy, double kForSize) {
        Bitmap tmp = BitmapFactory.decodeResource(res, textureId);
        tmp = Bitmap.createScaledBitmap(tmp, (int) ((double)canvas.getWidth() / kForSize), (int)((double)canvas.getWidth() / kForSize), true);
        Point pos = new Point(canvas.getWidth(), canvas.getHeight());
        pos = new Point(-tmp.getWidth() / 2 + canvas.getWidth() * kForOx, -tmp.getHeight() / 2.0 + canvas.getHeight() * kForOy);
        if (kForOx == -1) {
            pos = new Point();//tmp.getWidth() /2 + 10, tmp.getHeight()/2 + 10);
        }
        texture = new Texture(tmp, pos, 0);
        touchChecker = new Brick(texture.img.getWidth(), texture.img.getHeight(), pos);
    }

    public Button(int textureId, Resources res, Canvas canvas, int w, int h, Point pos) {
        Bitmap tmp = BitmapFactory.decodeResource(res, textureId);
        tmp = Bitmap.createScaledBitmap(tmp, w, h, true);
        texture = new Texture(tmp, pos, 0);
        startPos = pos;
        touchChecker = new Brick(texture.img.getWidth(), texture.img.getHeight(), pos);
    }

    public void draw(Canvas canvas) {
        texture.draw(canvas);
    }

    public boolean onButtonTap(MotionEvent event) {
        return touchChecker.inBrick(event);
    }

    public boolean animatedDraw(Canvas c, double speed) {
        double sin = Math.sin(speed * (double) System.currentTimeMillis() / 2000.0);
        Paint paint = new Paint();
        paint.setAlpha(175 - (int)(80 * sin));
        texture.draw(c, paint);
        return true;
    }
}
