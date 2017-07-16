package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


public class Line {
    private Point A, B;
    private double k, b;
    private double L;
    private boolean isVertical;

    public double getK() {
        return k;
    }

    public double getB() {
        return b;
    }


    public double getL() {
        return L;
    }

    Line(Point A, Point B) {
        this.A = A;
        this.B = B;
        initLine();
    }

    public Point getPointA() {
        return A;
    }
    public Point getPointB() {
        return B;
    }

    private void initLine() {
        L = Config.destinationAB(A, B, false);
        if (B.x * 0.99 <= A.x && A.x <= B.x * 1.01 ) {
            isVertical = true;
            Log.d("CAR_CONSTRUCTOR", "Line: A " + A +" B " + B + " (x = " +A.x+")");
            return;
        }
        if (A.x > B.x) {
            Point tmp = A;
            A = B;
            B = tmp;
        }
        isVertical = false;
        k = (B.y - A.y) / (B.x - A.x);
        b = A.y - A.x * (B.y - A.y) / (B.x - A.x);
        Log.d("CAR_CONSTRUCTOR", "Line: A " + A + " B " + B + " (" + toString() + ")");
    }

    Line(double x1, double y1, double x2, double y2) {
        A = new Point(x1, y1);
        B = new Point(x2, y2);
        initLine();
    }

    byte cmpWithPoint(Point C) {
        double x = getPointByY(C.y).x;
        if(x > C.x)
            return -1;
        if(x < C.x)
            return 1;
        return 0;
    }

    public Point getPointByY(double y) {
        if (isVertical) {
            return new Point(A.x, y);
        }
        double x1 = A.x;
        double x2 = B.x;
        double y1 = A.y;
        double y2 = B.y;
        double x = (y - y1) * (x2 - x1) / (y2 - y1);
        x += x1;
        return new Point(x, y);
    }

    public Point intersectionPoint(Line line) { //not safe
        if(isVertical) {
            return new Point(A.x, line.getK() * A.x + line.getB());
        }
        if(line.isVertical) {
            return new Point(line.A.x, k * line.A.x + b);
        }
        double k1 = k, b1 = b, k2 = line.getK(), b2 = line.getB();
        Log.d("LINE_INTERSECTION", "k1 " + k + " k2 " + k2 + " b1 " + b1 + " b2 " + b2 );
        double x;
        try {
            x = (b2 - b1) / (k1 - k2);
        } catch (ArithmeticException e) {
            e.printStackTrace();
            x = 0;
        }
        double y = k1 * x + b1;
        return new Point(x, y);
    }

    public boolean isPointUpper(Point p) {
        double y = k * p.x + b;
        return p.y < y;
    }

    public double getY(double x)
    {
        return k * x + b;
    }

    public boolean isPointUnder(Point p) {
        double y = k * p.x + b;
        return p.y > y;
    }

    void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        canvas.drawLine((float)A.x, (float)A.y, (float)B.x, (float)B.y, p);
    }

    @Override
    public String toString() {
        return "y = " + k + " * x + " + b + ", L = " + L + "(POINTS {A: " + A + "; B: " + B + "})";
    }

    public boolean betweenByX(Point point) {
        if (A.x > B.x) {
            double t = A.x;
            A.x = B.x;
            B.x = t;
        }
        return A.x <= point.x && point.x <= B.x;
    }
}
