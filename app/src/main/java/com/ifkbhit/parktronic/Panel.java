package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class Panel {
    public Point pos;
    public double w, h;
    private Texture line;
    private Bitmap[]  digits = new Bitmap[10];
    private Bitmap panelBitmap;
    private Resources res;
    private Texture[][] texPanels = new Texture[4][4];
    private Texture[] texDig = new Texture[10];
    private Texture panel;
    private int[] state = {0,0,0,0};
    private int cur_l, cur_r;
    private boolean empty = true, isInvert = false;
    private boolean reverse = false;



Bitmap getBitmap(int id)
{
    return BitmapFactory.decodeResource(res, id);
}


    void setEmpty(boolean b)
    {
        empty = b;
    }

    Texture getTexture(int id){
        return new Texture(getBitmap(id));
    }
    Panel(Canvas c, Resources res, boolean isReverse)
    {
        this.res = res;
        reverse = isReverse;
        if(!reverse)
            panelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_0);
        else
            panelBitmap = BitmapFactory.decodeResource(res, R.drawable.reverse_panel_0);
        double k =  (1.0-Config.CAR_Y_OFFSET_K) * 120.0 / Config.CAR_H;
        double need_w = k * c.getWidth();
        double need_h = c.getHeight() * k;
        k = need_w / panelBitmap.getWidth();
        h = need_h;
        w = panelBitmap.getWidth() * h / panelBitmap.getHeight();
        Log.d("PANEL", "Converted panel with k "+k+" w: " + (int)w + " h: " + (int)h);
        panel = new Texture(Bitmap.createScaledBitmap(panelBitmap, (int)w, (int)h, false));
        panel.setPos(new Point((c.getWidth() - panel.img.getWidth()) / 2.0, c.getHeight() * (Config.CAR_Y_OFFSET_K / 2) + ((1 - Config.CAR_Y_OFFSET_K) * c.getHeight()) / 2.0 - panel.img.getWidth() / 16.0));
        panel.setScaled(1.3);






        digits[0] = getBitmap(R.drawable.dig_0);
        digits[1] = getBitmap(R.drawable.dig_1);
        digits[2] = getBitmap(R.drawable.dig_2);
        digits[3] = getBitmap(R.drawable.dig_3);
        digits[4] = getBitmap(R.drawable.dig_4);
        digits[5] = getBitmap(R.drawable.dig_5);
        digits[6] = getBitmap(R.drawable.dig_6);
        digits[7] = getBitmap(R.drawable.dig_7);
        digits[8] = getBitmap(R.drawable.dig_8);
        digits[9] = getBitmap(R.drawable.dig_9);



        for(int i = 0; i < 10; i++)
        {
            double kh = 0.9 * 98.0 / 174.0;
            double kw = 0.9 * 65.0 / 600.0;
            double w = panel.img.getWidth() * kw;
            double h = panel.img.getHeight() * kh;
            if(line == null)
                line = new Texture(Bitmap.createScaledBitmap(getBitmap(R.drawable.line), (int)w, (int)h, true));
            texDig[i] = new Texture(Bitmap.createScaledBitmap(digits[i], (int)w, (int)h, true));
        }

        k = panel.img.getHeight() / 174.0;
        int i = 0;
        double dNum = 39;
        Point position = new Point(84 * k, 68 * k).sum(panel.pos);
        if(reverse)
            position.y = dNum * k + panel.pos.y;

        texPanels[0][i++] = new Texture(getScaledBitmap(R.drawable.left_small_0, 71 * k,(70 * k)), position, k);
        texPanels[0][i++] = new Texture(getScaledBitmap(R.drawable.left_small_1, 71 * k,(70 * k)), position, k);
        texPanels[0][i++] = new Texture(getScaledBitmap(R.drawable.left_small_2, 71 * k,(70 * k)), position, k);
        texPanels[0][i++] = new Texture(getScaledBitmap(R.drawable.left_small_3, 71 * k, (70 * k)),position, k);
        i = 0;
        position = new Point(157 * k, 50 * k).sum(panel.pos);
        if(reverse)
            position.y = dNum * k + panel.pos.y;
        texPanels[1][i++] = new Texture(getScaledBitmap(R.drawable.left_big_0, 72 * k,(88 * k)),position , k);
        texPanels[1][i++] = new Texture(getScaledBitmap(R.drawable.left_big_1, 72 * k,(88 * k)), position, k);
        texPanels[1][i++] = new Texture(getScaledBitmap(R.drawable.left_big_2, 72 * k,(88 * k)), position, k);
        texPanels[1][i++] = new Texture(getScaledBitmap(R.drawable.left_big_3, 72 * k, (88 * k)),position, k);
        i = 0;
        position = new Point(370 * k, 50 * k).sum(panel.pos);
        if(reverse)
            position.y = dNum * k + panel.pos.y;
        texPanels[2][i++] = new Texture(getScaledBitmap(R.drawable.right_big_0, 72 * k,(88 * k)),position , k);
        texPanels[2][i++] = new Texture(getScaledBitmap(R.drawable.right_big_1, 72 * k, (88 * k)),position, k);
        texPanels[2][i++] = new Texture(getScaledBitmap(R.drawable.right_big_2, 72 * k,(88 * k)), position, k);
        texPanels[2][i++] = new Texture(getScaledBitmap(R.drawable.right_big_3, 72 * k,(88 * k)), position, k);
        i = 0;
        position = new Point(445 * k, 68 * k).sum(panel.pos);
        if(reverse)
            position.y = dNum * k + panel.pos.y;
        texPanels[3][i++] = new Texture(getScaledBitmap(R.drawable.right_small_0, 71 * k,(70 * k)), position, k);
        texPanels[3][i++] = new Texture(getScaledBitmap(R.drawable.right_small_1, 71 * k,(70 * k)), position, k);
        texPanels[3][i++] = new Texture(getScaledBitmap(R.drawable.right_small_2, 71 * k,(70 * k)), position, k);
        texPanels[3][i++] = new Texture(getScaledBitmap(R.drawable.right_small_3, 71 * k,(70 * k)), position, k);

        //invert();




    }




    private Bitmap getScaledBitmap(int id, double w, double h)
    {
        Bitmap b = Bitmap.createScaledBitmap(getBitmap(id), (int) w, (int) h, false);
        if(reverse)
            b =Config.reverseBitmap(b);
        return b;
    }


    private boolean between(double x, double a, double b)
    {

        return x >= a && x < b;
    }

    private int getLevel(double val, boolean isUp)
    {
        if(isUp) {
            double MAX = 0.9;
            if (between(val, 0, MAX / 4.0))
                return 4;
            if(between(val, MAX / 4,MAX /2))
                return 3;
            if(between(val, MAX /2, MAX * 0.75))
                return 2;
            if(between(val, MAX * 0.75, MAX))
                return 1;
        }else{
            Double MAX = 1.1;
            if (between(val, 0, MAX / 4.0))
                return 4;
            if(between(val, MAX / 4,MAX /2))
                return 3;
            if(between(val, MAX /2, MAX * 0.75))
                return 2;
            if(between(val, MAX * 0.75, MAX))
                return 1;
        }
        return 0;
    }

    public void clear(){
        for(int i = 0; i < 4; i++)
            state[i] = 0;
        empty = true;
    }


    void setPanel(double[] info,  boolean isUp)
    {
        double min_l = 100;
        for(int i = 0; i < 4; i++)
        {
            if(info[i] == -1)
                clear();
            if(info[i] == -2)
            {
                state[i] = 0;
            }
            else{
                state[i] = getLevel(info[i], isUp);
                if(min_l > info[i])
                    min_l = info[i];

            }
        }

        if(min_l == 100) {
            cur_l = -1;
            cur_r =-1;
        }
        else{
            if(isUp)
            {
                double MAX = 0.9;
                if (min_l < 0)
                    min_l = 100;
                if(min_l < MAX / 4.0)
                {
                    cur_l = 0;
                    double tmp_max = MAX / 4.0;
                    if(tmp_max * 0.5 < min_l)
                    {
                        cur_r = 3;
                    }
                    else
                        cur_r = 0;
                }
                else if(min_l < MAX / 2.0)
                {
                    cur_l = 0;
                    double tmp_max = MAX / 2.0;
                    if(min_l > 0.75 * tmp_max)
                        cur_r = 5;
                    else
                        cur_r = 4;
                }
                else if(MAX * 0.75 > min_l)
                {
                    cur_l = 0;
                    if(MAX / 2.0 + MAX / 8.0 < min_l)
                        cur_r = 7;
                    else
                        cur_r = 6;
                }
                else if(min_l <= MAX){
                    cur_l = 0;
                    if(MAX * 7.0 / 8.0 < min_l)
                        cur_r = 9;
                    else
                        cur_r = 8;
                }
                else
                {cur_l = -1;
                    cur_r = -1;}
               // Log.d("SET_PANEL", min_l + " " + "MAX " + MAX);

            }
            else{
                double MAX = 1.1;
                if (min_l < 0)
                    min_l = 100;
                if(min_l < MAX / 4.0)
                {
                    cur_l = 0;
                    double tmp_max = MAX / 4.0;
                    if(tmp_max * 0.75 < min_l)
                    {
                        cur_r = 5;
                    }
                    else if(tmp_max * 0.5 < min_l)
                    {
                        cur_r = 4;
                    }
                    else if(tmp_max * 0.25 < min_l)
                        cur_r = 3;
                    else
                        cur_r = 0;
                }
                else if(min_l < MAX / 2.0)
                {
                    cur_l = 0;
                    double tmp_max = MAX / 2.0;
                    if(min_l > 0.75 * tmp_max)
                        cur_r = 7;
                    else
                        cur_r = 6;
                }
                else if(MAX * 0.75 > min_l)
                {
                    cur_l = 0;
                    if(MAX / 2.0 + MAX / 8.0 < min_l)
                        cur_r = 9;
                    else
                        cur_r = 8;
                }
                else if(min_l <= MAX){
                    cur_l = 1;
                    if(MAX * 7.0 / 8.0 < min_l)
                        cur_r = 1;
                    else
                        cur_r = 0;
                }
                else
                {cur_l = -1;
                cur_r = -1;}
            }
            empty = false;
        }


    }

    void drawNum(Canvas canvas)
    {
        Log.d("DRAW_NUM", empty? "empty": "wtf");
        if(empty)
            return;
        Texture left, right;
        if(cur_l == -1 || cur_r == -1) {
            left = line;
            right = line;
        }
        else {
            left = texDig[cur_l];
            right = texDig[cur_r];
        }
        Point dotCenter = panel.getCenter();//new Point(panel.img.getWidth() * 294.0 / 600.0, panel.img.getHeight() * 122.0 / 174.0);
        Point posLeft = new Point(dotCenter.x - left.img.getWidth() * 1.15, (dotCenter.y -  (left.img.getHeight()) / 2));
        left.setPos(posLeft);
        left.draw(canvas);
        Point posRight = new Point(dotCenter.x + left.img.getWidth() * 0.15, (dotCenter.y - (left.img.getHeight()) / 2));
        right.setPos(posRight);
        right.draw(canvas);
        for(int i = 0; i < state.length; i++) {
            int lvl = state[i];
            if (lvl != 0)
            {
                texPanels[i][4 - lvl].draw(canvas);
            }
        }
    }


    void invert()
    {


    }



    void draw(Canvas canvas)
    {


            panel.draw(canvas);
            drawNum(canvas);
    }

}
