package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Роман on 12.03.2017.
 */
public class Demo {
    private int cur_index = 0;
    private long last_time, max_time = 25;
    public static final int TYPE_ELLIPSE=  0, TYPE_CIRCLE = 1;
    public static final int UPPER_DEMO = 0, LOWER_DEMO = 1;
    private final int UP = -1, DOWN = 1;
    private int curType = -1;
    private int curPart = 1;
    private int curPosType = 0;
    private final double N = 600; //количество разбиений по оси x
    private Point center;
    private double R, r, currentRadius;
    private double rad_step, x_step, rad_d = -1, x_d = 1;
    private Point start, work;
    private int steps = 0;
    private ArrayList<Point> dots = new ArrayList<>();
    Demo(int demoType, int posType)
    {
        curType = demoType;
        curPosType = posType;
    }

    public void init(Car car)
    {
        last_time = System.currentTimeMillis();
        Point[][] points;
        int i;
        if(curPosType == UPPER_DEMO)
        {
            points = car.getUpper_dots();
            i = 3;

        }
        else
        {
            points = car.getLower_dots();
            i = 4;
        }

        Point A = points[0][2].sum(car.getPos());
        Point B = points[i][2].sum(car.getPos());


        r = new Line(A, B).getL() * 0.33;
        center = new Point(A.x, 0.4 * (A.y + B.y));
        if(i == 4)
            center.sum1(new Point(0, 2.5 * r));
        A = points[0][0].sum(car.getPos());
        B = points[0][4].sum(car.getPos());
        double L = new Line(A, B).getL();
        R = 0.6 * L;
        x_step = 2.0 * R / N;

        double x = -R;


        Log.d("INIT_DEMO", "center " + center );
        Log.d("INIT_DEMO", "x after first loop " + x + " ->" + (x + x_step * N));
        if(curType == TYPE_ELLIPSE){
            for(i = 0; i < N; i++)
            {
                dots.add(new Point(x, getY(x)).sum(center));
                x += x_step;
            }
            Log.d("INIT_DEMO", "x after first loop " + x);

            for(i = 0; i < N; i++)
            {
                dots.add(new Point(x, -getY(x)).sum(center));
                x -= x_step;
            }
            Log.d("INIT_DEMO", "count dots " + dots.size());
            //Log.d("DEMO_INIT", "Radius " + r + "&" + R+", center: " + center);
        }
        else if (curType == TYPE_CIRCLE) {
            Point center1 = center.sum(-r, 0);

            Point center2 = center.sum(r, 0);

            x_step = (center.x - 2 * r) * 2 / N;
            x = -r;

            for(; x < r;)
            {
                dots.add(new Point(getPointRing(center1, x, r, true)));
                x += x_step;
            }
            x = -r;
            for(; x < r;)
            {
                dots.add(new Point(getPointRing(center2, x, r, false)));
                x += x_step;
            }
            x = r;
            for(; x > -r;)
            {
                dots.add(new Point(getPointRing(center2, x, r, true)));
                x -= x_step;
            }
            x = r;
            for(; x > -r;)
            {
                dots.add(new Point(getPointRing(center1, x, r, false)));
                x -=  x_step;
            }
        }

    }




    private Point getPointRing(Point center, double x, double R, boolean isPositive)
    {
        double y = (isPositive? 1: -1) * Math.sqrt(R*R - x*x);
        return new Point(x, y).sum(center);
    }



    private double getY(double x)
    {
        double y = Math.sqrt(r * r * (1 - ((x * x) / (R * R))));
        return y;
    }

    public Point getPos(){
        if(System.currentTimeMillis() - last_time > max_time)
            cur_index++;
        if(cur_index >= dots.size())
            cur_index = 0;
        Point t = dots.get(cur_index);
        return t;
    }
    void draw(Canvas c){
        Paint p = new Paint();
        p.setStrokeWidth(5);
        p.setColor(Color.GREEN);
        if(System.currentTimeMillis() - last_time > max_time)
            cur_index++;
        if(cur_index >= dots.size())
            cur_index = 0;
        Point t = dots.get(cur_index);
        c.drawPoint((float)t.x, (float)t.y, p);
    }
}
