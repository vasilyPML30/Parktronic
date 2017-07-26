package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by Роман on 03.03.2017.
 */
public class Brick {

    public Point pos;
    public double w, h;
    private boolean visible;
    Point minBorder, maxBorder;
    Point left_u, left_d, right_u, right_d;
    Point[] points;

    public int[][] state = new int[4][5];
    public double[] dest = new double[4];

    public boolean isVisible() {
        return visible;
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        visible = true;
    }

    public Brick(double w, double h, Point pos) {
        points = new Point[4];
        this.w = w;
        this.h = h;
        this.pos = pos;
        visible = true;

        left_u = pos;
        left_d = new Point(pos.x, pos.y + h);
        right_u = new Point(pos.x + w, pos.y);
        right_d = new Point(pos.x + w, pos.y + h);

        points[0] = left_u;
        points[1] = right_u;
        points[2] = right_d;
        points[3] = left_d;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                state[i][j] = 0;
            }
            dest[i] = 0;
        }
    }

    void refreshStates(Line[] lines){
        for (int i = 0; i < 4; i++) {
            for(int j = 0; j < 5; j++) {
                state[i][j] = lines[j].cmpWithPoint(points[i]);
            }
        }

    }

    void setBorder(Point min, Point max) {
        minBorder = min;
        maxBorder = max;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void draw(Canvas canvas) {
        if (!visible)
            return;
        Paint p = new Paint();
        p.setColor(Color.RED);
        RectF r = new RectF((float) pos.x, (float) pos.y, (float) pos.x + (float) w, (float) pos.y + (float) h);
        canvas.drawRect(r, p);
    }

    ArrayList<Integer>[] getStates() {
        ArrayList<Integer>[] res = new ArrayList[4];
        for (int i = 0; i < 4; i++) {
            res[i] = new ArrayList();
        }

        for (int i = 0; i < 4; i++) {
            int j = 0;
            while (j < 5 && state[i][j] != -1) {
                j++;
            }
            if (j < 5 && j > 0) {
                res[j - 1].add(i);
            }
        }
        return res;
    }

    void Move(double x, double y, boolean cantMove) {
        if (cantMove) {
            return;
        }

        pos.x += x;
        pos.y += y;

        if (pos.x + w > maxBorder.x) {
            pos.x = maxBorder.x - w;
        }
        if (pos.x < minBorder.x) {
            pos.x = minBorder.x;
        }
        if (pos.y + h > maxBorder.y) {
            pos.y = maxBorder.y - h;
        }
        if (pos.y < minBorder.y) {
            pos.y = minBorder.y;
        }

        left_u = pos;
        left_d = new Point(pos.x, pos.y + h);
        right_u = new Point(pos.x + w, pos.y);
        right_d = new Point(pos.x + w, pos.y + h);
        setPoints();
    }

    boolean checkWithLines(Line[] lines, boolean isUp) {
        setPoints();
        for (int j = 0; j < 4; j++)
        {
            Point p = points[j];
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].betweenByX(p))
                {
                    if (isUp) {
                        if (lines[i].isPointUnder(p)) {
                            double delta = points[j].y - lines[i].getY(p.x) + 1;
                            for (int g = 0; g < 4; g++) {
                                points[g].y -= delta;
                            }
                            return true;
                        }
                    }
                    else {
                        if (lines[i].isPointUpper(p)) {
                            double delta = (lines[i].getY(p.x) + 1) - points[j].y;
                            for (int g = 0; g < 4; g++) {
                                points[g].y += delta;
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    void setCenterPos(Point p) {
        pos = new Point(p.x - w/2 , p.y - h/2);
        left_u = pos;
        left_d = new Point(pos.x, pos.y + h);
        right_u = new Point(pos.x + w, pos.y);
        right_d = new Point(pos.x + w, pos.y + h);
        setPoints();
    }

    Point getCenter() {
        Point p = new Point(w / 2, h / 2);
        return p.sum(pos);
    }

    void Move(MotionEvent event, Point startPoint, boolean cantMove) {
        Move(event.getX() - startPoint.x, event.getY() - startPoint.y, cantMove);
    }

    private void setPoints(){
        points[0] = left_u;
        points[1] = right_u;
        points[2] = right_d;
        points[3] = left_d;
    }

    public void setPos(Point p) {
        pos = p;
        left_u = new Point(pos.x, pos.y + h);
        left_d = new Point(pos.x, pos.y + h);
        right_u = new Point(pos.x + w, pos.y);
        right_d = new Point(pos.x + w, pos.y + h);
        setPoints();
    }

    public boolean inBrick(Point A) {
        return  A.x >= pos.x && pos.x + w >= A.x &&
                A.y >= pos.y && pos.y + h >= A.y;
    }

    public boolean inBrick(MotionEvent event)
    {
        return inBrick(new Point(event));
    }

    @Override
    public String toString() {
        String c = "State: >";
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 5; j++)
            {    c += ""+state[i][j] + " ";}
        c+= "<\n>";
        }
        c += "\nDestination: ";
        for(int i = 0; i < 4; i++)
            c += dest[i] + " ";
        return "Brick " + (int)pos.x + " " + (int)pos.y + " " + isVisible() + "\n" + c ;
    }

}
