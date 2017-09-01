package com.ifkbhit.parktronic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class Obstacle {
    private Texture image;
    private Point position;
    static double xPos = 0;
    static int direction = 0;
    static int leftBound, rightBound, screenWidth;
    static MyTime timer = null;
    private boolean captured = false;
    private boolean[] sensors;
    static double front_speed, back_speed;

    Obstacle(Bitmap img, Point pos, int h, boolean[] sns) {
        image = new Texture(Bitmap.createScaledBitmap(img,
                (int)((double)img.getWidth() / img.getHeight() * h), h, true));
        position = pos;
        image.setPos(position);
        sensors = sns;
        if (timer == null) {
            timer = new MyTime();
        }
    }

    void setCaptured(boolean value) {
        captured = value;
    }

    boolean getCaptured() {
        return captured;
    }

    boolean move(double dist) {
        xPos += dist;
        double tmpXpos = xPos;
        if (xPos < (leftBound + rightBound) / 2) {
            xPos = Math.max(xPos, (int) (30 - image.w));
            xPos = Math.min(xPos, (int) (leftBound - image.w));
        }
        else {
            xPos = Math.max(xPos, rightBound);
            xPos = Math.min(xPos, screenWidth - 30);
        }
        return xPos == tmpXpos;
    }

    void animate() {
        timer.Refresh();
        if (!move(direction * timer.Delta *
                (xPos < screenWidth / 2 ? front_speed : back_speed))) {
            direction *= -1;
        }
    }

    void draw(Canvas canvas) {
        image.setPos(new Point(position.x + xPos, position.y));
        image.draw(canvas);
    }

    boolean onTap(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return position.x + xPos <= x && x <= position.x + xPos + image.w &&
                position.y <= y && y <= position.y + image.h;
    }

    static void setBounds(int left, int right, int width) {
        leftBound = left;
        rightBound = right;
        screenWidth = width;
        front_speed = (leftBound - 30) / 4000.0;
        back_speed = (screenWidth - 30 - rightBound) / 4000.0;
    }

    double[] getDists() {
        double dist;
        if (xPos < (leftBound + rightBound) / 2) {
            dist = 0.9 / (leftBound - 30) * (leftBound - xPos - image.w);
        }
        else {
            dist = 2.0 / (screenWidth - 30 - rightBound) * (xPos - rightBound);
        }
        double[] res = new double[4];
        for (int i = 0; i < 4; ++i) {
            res[i] = (sensors[i] ? dist : -1);
        }
        return res;
    }

}
