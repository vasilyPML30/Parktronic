package com.ifkbhit.parktronic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class Obstacle {
    private Texture[] images;
    private Point position;
    private int curImg = 0;
    static double xPos = 0;
    static int direction = 0;
    static int leftBound, rightBound, screenWidth;
    static MyTime timer = null;
    private boolean captured = false;
    private boolean[] sensors;
    static double front_speed, back_speed;

    Obstacle(Bitmap[] img, Point pos, int h, boolean[] sns) {
        images = new Texture[img.length];

        for (int i = 0; i < images.length; i ++) {
            images[i] = new Texture(Bitmap.createScaledBitmap(img[i],
                    (int)((double)img[i].getWidth() / img[i].getHeight() * h), h, true));
            position = pos;
            images[i].setPos(position);
            sensors = sns;
            if (timer == null) {
                timer = new MyTime();
            }
        }

    }

    void switchImage() {
        curImg = (curImg + 1) % images.length;
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
            xPos = Math.max(xPos, (int) (30 - images[curImg].w));
            xPos = Math.min(xPos, (int) (leftBound - images[curImg].w));
        }
        else {
            xPos = Math.max(xPos, rightBound);
            xPos = Math.min(xPos, screenWidth - 30);
        }
        return xPos == tmpXpos;
    }

    void animate(boolean switchSegment) {
        timer.Refresh();
        if (switchSegment) {
            int state = (int)(timer.FromStart / 500) % 6;
            switch (state) {
                case 3:
                    sensors = new boolean[]{true, false, false, false};
                    break;
                case 2:
                case 4:
                    sensors = new boolean[]{false, true, false, false};
                    break;
                case 1:
                case 5:
                    sensors = new boolean[]{false, false, true, false};
                    break;
                case 0:
                    sensors = new boolean[]{false, false, false, true};
                    break;
            }
            curImg = state / 3;
        }
        if (!move(direction * timer.Delta *
                (xPos < screenWidth / 2 ? front_speed : back_speed))) {
            direction *= -1;
        }
    }

    void draw(Canvas canvas) {
        images[curImg].setPos(new Point(position.x + xPos, position.y));
        images[curImg].draw(canvas);
    }

    boolean onTap(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        return position.x + xPos <= x && x <= position.x + xPos + images[curImg].w &&
                position.y <= y && y <= position.y + images[curImg].h;
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
            dist = 0.9 / (leftBound - 30) * (leftBound - xPos - images[curImg].w);
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
