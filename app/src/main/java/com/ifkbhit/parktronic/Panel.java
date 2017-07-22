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

    void setPanel(double[] info,  boolean isUp) { }

    void draw(Canvas canvas) { }

}
