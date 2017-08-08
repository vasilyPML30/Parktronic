package com.ifkbhit.parktronic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Роман on 04.03.2017.
 */

public class Texture {
    public Point pos;
    public Bitmap img;
    public double k;
    public double w, h;
    public double xPos = 0;
    public double angle = 0;

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

    public Point getCenter() {
        return new Point((2 * (pos.x + xPos) + img.getWidth()) / 2.0 , (2 * pos.y + img.getHeight()) / 2);
    }

    public void setPos(Point p) {
        pos = p;
    }

    void draw(Canvas canvas) {
        canvas.save();
            canvas.rotate((float)-angle, (float)getCenter().x, (float)getCenter().y);
            canvas.drawBitmap(img, (float)(pos.x + xPos), (float)pos.y, new Paint());
        canvas.restore();
    }

    void draw(Canvas canvas, Paint p) {
        canvas.save();
            canvas.rotate((float)-angle, (float)getCenter().x, (float)getCenter().y);
            canvas.drawBitmap(img, (float)(pos.x + xPos), (float)pos.y, p);
        canvas.restore();
    }

    public void setScaled(double k) {
        double x = w * (k - 1) / 2;
        double y = h * (k - 1) / 2;
        img = Bitmap.createScaledBitmap(img, (int)(w * k), (int)(h * k), true);
        w = img.getWidth();
        h = img.getHeight();
        pos.sum1(new Point(-x, -y));
    }

    public void rotate(double angle) {
        this.angle = angle;
    }
}
