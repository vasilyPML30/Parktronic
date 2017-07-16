package com.ifkbhit.parktronic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by Роман on 04.03.2017.
 */
public class Texture {
    public Point pos;
    public Bitmap img;
    public double k;
    public double w, h;

    public Texture(Bitmap b) {
        img = b;
        w = img.getWidth();
        h = img.getHeight();
    }
    public Texture(Bitmap b, Point p, double k) {
        img = b;
        pos = p;
        this.k = k;
        w = img.getWidth();
        h = img.getHeight();
    }
    void scale() {
        Log.d("TEXTURE", k + "");
        double w = img.getWidth() * k;
        double h = img.getHeight() * k;
        img = Bitmap.createScaledBitmap(img, (int)w, (int)h, false);
    }

    public Point getCenter() {
        return new Point((2 * pos.x + img.getWidth()) / 2.0 , (2 * pos.y + img.getHeight()) / 2);
    }

    public Point getPanelCenter() {
        return new Point(img.getWidth() / 2.0, img.getHeight() / 2);
    }

    public void setPos(Point p) {
        pos = p;
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(img, (float)pos.x, (float)pos.y, new Paint());
    }
    void draw(Canvas canvas, Paint p) {
        canvas.drawBitmap(img, (float)pos.x, (float)pos.y, p);
    }

    public void setScaled(double k) {
        double w = img.getWidth();
        double h = img.getHeight();
        double x = w * (k - 1) / 2;
        double y = h * (k - 1) / 2;
        img = Bitmap.createScaledBitmap(img, (int)(w * k), (int)(h * k), true);
        pos.sum1(new Point(-x, -y));
    }

    public void reverse() {
        img = Config.reverseBitmap(img);
    }
}
