package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Panel {
    double w, h;
    MyTime mvTimer = null, invTimer = null;
    boolean moveFlag = false, invertFlag = false, invalidFlag = false, reverse = false;
    float panelAngle = 0;
    Texture panel;
    Resources res;
    boolean reversable;
    int[] state = {0,0,0,0};
    int cur_l = -1, cur_r = -1;



    void moveX(double dx) {
        panel.xPos += dx;
    }

    void setInvertFlag(boolean value) {
        invertFlag = value;
    }

    void switchReverse() {
        reverse ^= true;
    }

    void setMoveFlag(boolean value) {
        moveFlag = value;
    }

    boolean getInvertFlag() {
        return invertFlag;
    }

    boolean getMoveFlag() {
        return moveFlag;
    }

    boolean getInvalidFlag() {
        return invalidFlag;
    }

    void setInvalidFlag(boolean value) {
        invalidFlag = value;
    }

    void move() {
        if (!moveFlag) {
            return;
        }

        if (mvTimer == null) {
            mvTimer = new MyTime();
        }

        double cft = 10;
        mvTimer.Refresh();
        panel.xPos -= cft * panel.xPos * mvTimer.DeltaS;

        if (Math.abs(panel.xPos) < 3.0) {
            panel.xPos = 0;
            moveFlag = false;
            mvTimer = null;
        }

    }

    private double func(double arg) {
        return 2 - Math.abs(arg - 2);
    }

    void invert() {
        if (!invertFlag) {
            return;
        }
        if (invTimer == null) {
            invTimer = new MyTime();
        }

        double period = 4.0;
        double tm = 2.0;
        double cft = 0.001 / tm * period;
        double integral = 4.0;

        invTimer.Refresh();
        double mid = (func(invTimer.FromStart * cft) + func((invTimer.FromStart - invTimer.Delta) * cft)) / 2.0;
        panelAngle += 180 / integral * mid * invTimer.Delta * cft;

        if (invTimer.FromStartS >= tm) {
            invTimer = null;
            invertFlag = false;
            invalidFlag = true;
            panelAngle = 0;
        }
    }

    Bitmap getBitmap(int id) {
        return BitmapFactory.decodeResource(res, id);
    }


    private boolean between(double x, double a, double b) {
        return x >= a && x < b;
    }

    private int getLevel(double val, boolean isUp) {
        if (isUp) {
            double MAX = 0.9;
            if (between(val, 0, MAX / 4.0)) {
                return 4;
            }
            if(between(val, MAX / 4,MAX / 2)) {
                return 3;
            }
            if(between(val, MAX / 2, MAX * 0.75)) {
                return 2;
            }
            if(between(val, MAX * 0.75, MAX)) {
                return 1;
            }
        }
        else {
            Double MAX = 1.1;
            if (between(val, 0, MAX / 4.0)) {
                return 4;
            }
            if (between(val, MAX / 4, MAX / 2)) {
                return 3;
            }
            if (between(val, MAX / 2, MAX * 0.75)) {
                return 2;
            }
            if (between(val, MAX * 0.75, MAX)) {
                return 1;
            }
        }
        return 0;
    }

    private void clear() {
        for (int i = 0; i < 4; i++)
            state[i] = 0;
    }

    void setPanel(double[] info,  boolean isUp) {
        double min_l = 100;
        for (int i = 0; i < 4; i++) {
            if (info[i] == -1) {
                clear();
            }
            if(info[i] == -2) {
                state[i] = 0;
            }
            else {
                state[i] = getLevel(info[i], isUp);
                if(min_l > info[i]) {
                    min_l = info[i];
                }
            }
        }

        if (min_l == 100) {
            cur_l = -1;
            cur_r = -1;
        }
        else {
            if (isUp) {
                double MAX = 0.9;
                if (min_l < 0) {
                    min_l = 100;
                }
                if (min_l < MAX / 4.0) {
                    cur_l = 0;
                    double tmp_max = MAX / 4.0;
                    if(tmp_max * 0.5 < min_l) {
                        cur_r = 3;
                    }
                    else {
                        cur_r = 0;
                    }
                }
                else if (min_l < MAX / 2.0) {
                    cur_l = 0;
                    double tmp_max = MAX / 2.0;
                    if (min_l > 0.75 * tmp_max) {
                        cur_r = 5;
                    }
                    else {
                        cur_r = 4;
                    }
                }
                else if (MAX * 0.75 > min_l) {
                    cur_l = 0;
                    if (MAX / 2.0 + MAX / 8.0 < min_l) {
                        cur_r = 7;
                    }
                    else {
                        cur_r = 6;
                    }
                }
                else if (min_l <= MAX){
                    cur_l = 0;
                    if (MAX * 7.0 / 8.0 < min_l) {
                        cur_r = 9;
                    }
                    else {
                        cur_r = 8;
                    }
                }
                else {
                    cur_l = -1;
                    cur_r = -1;
                }
            }
            else {
                double MAX = 1.1;
                if (min_l < 0) {
                    min_l = 100;
                }
                if (min_l < MAX / 4.0)
                {
                    cur_l = 0;
                    double tmp_max = MAX / 4.0;
                    if(tmp_max * 0.75 < min_l) {
                        cur_r = 5;
                    }
                    else if(tmp_max * 0.5 < min_l) {
                        cur_r = 4;
                    }
                    else if(tmp_max * 0.25 < min_l) {
                        cur_r = 3;
                    }
                    else {
                        cur_r = 0;
                    }
                }
                else if(min_l < MAX / 2.0) {
                    cur_l = 0;
                    double tmp_max = MAX / 2.0;
                    if (min_l > 0.75 * tmp_max) {
                        cur_r = 7;
                    }
                    else {
                        cur_r = 6;
                    }
                }
                else if(MAX * 0.75 > min_l) {
                    cur_l = 0;
                    if(MAX / 2.0 + MAX / 8.0 < min_l) {
                        cur_r = 9;
                    }
                    else {
                        cur_r = 8;
                    }
                }
                else if (min_l <= MAX) {
                    cur_l = 1;
                    if (MAX * 7.0 / 8.0 < min_l) {
                        cur_r = 1;
                    }
                    else {
                        cur_r = 0;
                    }
                }
                else {
                    cur_l = -1;
                    cur_r = -1;
                }
            }
        }
    }

    void draw(Canvas canvas) { }

}
