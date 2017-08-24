package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Created by Роман on 10.03.2017.
 */

public class Button {
    Texture texture;
    Brick touchChecker;
    boolean active;

    public Button(int textureId, Resources res, Rect windowRect, double kForOx, double kForOy, double kForSize) {

        Bitmap tmp = BitmapFactory.decodeResource(res, textureId);
        tmp = Bitmap.createScaledBitmap(tmp, (int) ((double)windowRect.width() / kForSize), (int)((double)windowRect.width() / kForSize), true);
        Point pos = new Point(-tmp.getWidth() / 2 + windowRect.width() * kForOx, -tmp.getHeight() / 2.0 + windowRect.height() * kForOy);
        if (kForOx == -1) {
            pos.x = 0;
        }
        if (kForOy == -1) {
            pos.y = 0;
        }
        if (kForOx == -2) {
            pos.x = windowRect.width() - tmp.getWidth();
        }
        if (kForOy == -2) {
            pos.y = windowRect.height() - tmp.getHeight();
        }
        texture = new Texture(tmp, pos, 0);
        touchChecker = new Brick(texture.img.getWidth(), texture.img.getHeight(), pos);
        setActive(true);
    }

    public Button(int textureId, Resources res, int w, int h, Point pos) {
        Bitmap tmp = BitmapFactory.decodeResource(res, textureId);
        tmp = Bitmap.createScaledBitmap(tmp, w, h, true);
        texture = new Texture(tmp, pos, 0);
        touchChecker = new Brick(texture.img.getWidth(), texture.img.getHeight(), pos);
        setActive(true);
    }

    public void draw(Canvas canvas) {
        texture.draw(canvas);
    }

    public boolean onButtonTap(MotionEvent event) {
        return touchChecker.inBrick(event) && active;
    }

    public boolean animatedDraw(Canvas c, double speed) {
        double sin = Math.sin(speed * (double) System.currentTimeMillis() / 2000.0);
        Paint paint = new Paint();
        paint.setAlpha(175 - (int)(80 * sin));
        texture.draw(c, paint);
        return true;
    }

    public void setActive(boolean mode) {
        active = mode;
        texture.paint.setAlpha(mode ? 255 : 70);
    }
}
