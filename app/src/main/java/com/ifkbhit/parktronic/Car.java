package com.ifkbhit.parktronic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Car {

    private Resources   res;
    private int         W, H;
    public  Texture     texture, upper_net, lower_net;                                           //текстура машины
    private Line[]      lines_up = new Line[5], lines_down = new Line[5];  //опорные линии
    private Panel       panel = null;                             //панель-индикатор
    private Point[][]   upper_dots = new Point[4][5];                      //верхние опорные точки
    private Point[][]   lower_dots = new Point[5][5];                      //нижние опорные точки(по идее не нужны)
    private Point[]     top_bumper = new Point[4];                         //примерные точки отслеживания на переднем бампере(по идее не нужны)
    private Point[]     down_bumper = new Point[4];                        //примерные точки отслеживания на заднем бампере
    // вот эти помогут
    private Point[]     up_supp_point = new Point[4];
    private Point[]     down_supp_point = new Point[4];
    private Line[]      support_line_up = new Line[4];
    private Line[]      support_line_down = new Line[4];
    public int          curTex = 0;
    int                 cur_panel = 0;
    double              R0_up = 0, R0_down = 0, mid_lu = 0, mid_ld = 0; // среднее расстояния до "центра окружности" сверху и снизу,
                                                                        // а так же средняя длинна отрезка сектора



    public void revertPanel(){
        panel.setInvertFlag(true);
    }

    boolean isPanelReversable() {
        return panel.reversible;
    }

    boolean isPanelAvailable() {
        return !panel.getMoveFlag() && !panel.getInvertFlag();
    }

    void movePanel(double delta) {
        panel.moveX(delta);
    }

    Panel getPanel() {
        return panel;
    }

    void setPanel(int cp) {
        cur_panel = cp;
        if (cur_panel == 0) {
            panel = new panel1(W, H, -1, -1, -1, res, false, true);
        } else if (cur_panel == 1) {
            panel = new panel2(W, H, res, true);
        } else {
            panel = new panel3(W, H, res, true);
        }
    }

    boolean mvPanel() {
        boolean result = false;
        double xPos = panel.panel.xPos;
        double tmpPos = (xPos < 0 ? panel.r_panel : panel.l_panel).pos.x + xPos;
        if (Math.abs(xPos) > W * 3 / 10) {
            result = true;
            if (xPos < 0) {
                cur_panel = (cur_panel + 1) % 3;
            } else {
                cur_panel = (cur_panel + 2) % 3;
            }
            if (cur_panel == 0) {
                panel = new panel1(W, H, -1, -1, -1, res, false, true);
            } else if (cur_panel == 1) {
                panel = new panel2(W, H, res, true);
            } else {
                panel = new panel3(W, H, res, true);
            }
            panel.panel.xPos = tmpPos - panel.panel.pos.x;
        }
        panel.setMoveFlag(true);
        return result;
    }

    void response(Brick b, boolean isUp) {
        b.refreshStates(isUp ? lines_up : lines_down);
        double[] infoForPanel = new double[4];
        Point[] sensors = isUp ? top_bumper : down_bumper;
        double scale = isUp ? 0.9 / (sensors[1].sum(texture.pos).y - upper_net.pos.y) :
                2.0 / (lower_net.pos.y + lower_net.h - sensors[1].sum(texture.pos).y);
        for (int i = 0; i < 4; ++i) {
            infoForPanel[i] = b.states[i] ? sensors[i].sum(texture.pos).dist(b) * scale + 0.1 : -2;
        }
        this.panel.setPanel(infoForPanel, isUp);
    }

    public Point getPos(){
        return texture.pos;
    }

    public Line[] getSupportLineUp() {
        return support_line_up;
    }

    public Line[] getSupportLineDown() {
        return support_line_down;
    }

    Car(Texture t, Texture u_n, Texture l_n, Rect windowRect, Resources res) {
        this.res = res;
        H = windowRect.height();
        W = windowRect.width();
        curTex = 0;
        panel = new panel1(W, H, -1, -1, -1, this.res, false, true);

        Point tmpP;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                tmpP = Config.UPPER_POINTS[i][j].multi(t.img.getHeight() / Config.CAR_H);
                upper_dots[i][j] = tmpP;
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tmpP = Config.LOWER_POINTS[i][j].multi(t.img.getHeight() / Config.CAR_H);
                lower_dots[i][j] = tmpP;
            }
        }
        lines_up = new Line[5];
        lines_down = new Line[5];

        texture = t;
        upper_net = u_n;
        lower_net = l_n;

        for (int i = 0; i < 4; i++) {
            top_bumper[i] = upper_dots[3][i].medium(upper_dots[3][i+1]);
            down_bumper[i] = lower_dots[4][i].medium(lower_dots[4][i+1]);
        }

        for (int i = 0; i < 5; i++) {
            lines_up[i] = new Line(upper_dots[0][i].sum(texture.pos), upper_dots[3][i].sum(texture.pos));
            lines_down[i] = new Line(lower_dots[0][i].sum(texture.pos), lower_dots[4][i].sum(texture.pos));
        }

        for (int i = 0; i < 4; i++) {
            up_supp_point[i] = lines_up[i].intersectionPoint(lines_up[i + 1]);
            down_supp_point[i] = lines_down[i].intersectionPoint(lines_down[i + 1]);
            mid_lu += lines_up[i].getL();
            mid_ld += lines_down[i].getL();
        }

        mid_lu += lines_up[4].getL();
        mid_ld += lines_down[4].getL();
        mid_lu /= 5.0;
        mid_ld /= 5.0;
        for (int i = 0; i < 4; i++) {
            Line maxDown =  new Line(lower_dots[0][i].sum(t.pos), down_supp_point[i]);
            R0_down += maxDown.getL();
            Line maxUp = new Line(upper_dots[0][i].sum(t.pos), up_supp_point[i]);
            R0_up += maxUp.getL();
        }

        R0_down /= 4.0;
        R0_up /= 4.0;

        R0_down -= mid_ld;
        R0_up -= mid_lu;

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                double x = 0;
                Line tmp1 = new Line(lower_dots[4][i].sum(t.pos), lower_dots[4][i + 1].sum(t.pos));
                support_line_down[i] = new Line(new Point(x, tmp1.getY(x)), lower_dots[4][i + 1].sum(t.pos));

                Line tmp2 = new Line(upper_dots[3][i].sum(t.pos), upper_dots[3][i + 1].sum(t.pos));
                support_line_up[i] = new Line(upper_dots[3][i+1].sum(t.pos),new Point(x, tmp2.getY(x)) );

            }
            else if (i == 3) {
                double x = windowRect.width();
                Line tmp1 = new Line(lower_dots[4][i].sum(t.pos), lower_dots[4][i+1].sum(t.pos));
                support_line_down[i] = new Line(new Point(x, tmp1.getY(x)), lower_dots[4][i].sum(t.pos));

                Line tmp2 = new Line(upper_dots[3][i].sum(t.pos), upper_dots[3][i + 1].sum(t.pos));
                support_line_up[i] =new Line(upper_dots[3][i].sum(t.pos),new Point(x, tmp2.getY(x)) );
            }
            else {
                support_line_down[i] = new Line(lower_dots[4][i].sum(t.pos), lower_dots[4][i + 1].sum(t.pos));
                support_line_up[i] = new Line(upper_dots[3][i].sum(t.pos), upper_dots[3][i + 1].sum(t.pos));
            }
        }
    }

    public Point[][] getUpper_dots() {
        return upper_dots;
    }

    public Point[][] getLower_dots() {
        return lower_dots;
    }

    void drawPanel(Canvas canvas) {
        panel.draw(canvas);
    }
    void drawNextPanel(Canvas canvas) {
        panel.drawNext(canvas);
    }

    void draw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        if (curTex == 1) {
            upper_net.draw(canvas);
        }
        else if (curTex == 2) {
            lower_net.draw(canvas);
        }

        texture.draw(canvas);

        panel.invert();
        panel.move();
        if (panel.getInvalidFlag()) {
            panel.setInvalidFlag(false);
            panel.switchReverse();
        }
        drawPanel(canvas);
        drawNextPanel(canvas);
    }
}
