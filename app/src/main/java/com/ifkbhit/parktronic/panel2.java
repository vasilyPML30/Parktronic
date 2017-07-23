package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class panel2 extends Panel {

    double scale;
    Texture[] nums = new Texture[10];

    panel2(int cnvW, int cnvH, Resources res) {
        this.res = res;
        reversable = false;
        Bitmap panelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_1);
        double k = (1.0 - Config.CAR_Y_OFFSET_K) * 360.0 / Config.CAR_H;
        double need_h = cnvH * k;
        h = need_h;
        w = panelBitmap.getWidth() * h / panelBitmap.getHeight();
        panel = new Texture(Bitmap.createScaledBitmap(panelBitmap, (int) w, (int) h, false));
        panel.setPos(new Point((cnvW - panel.img.getWidth()) / 2.0, cnvH * 0.45));

        Bitmap[] numBitmaps = new Bitmap[10];

        numBitmaps[0] = BitmapFactory.decodeResource(res, R.drawable.dig_2_0);
        numBitmaps[1] = BitmapFactory.decodeResource(res, R.drawable.dig_2_1);
        numBitmaps[2] = BitmapFactory.decodeResource(res, R.drawable.dig_2_2);
        numBitmaps[3] = BitmapFactory.decodeResource(res, R.drawable.dig_2_3);
        numBitmaps[4] = BitmapFactory.decodeResource(res, R.drawable.dig_2_4);
        numBitmaps[5] = BitmapFactory.decodeResource(res, R.drawable.dig_2_5);
        numBitmaps[6] = BitmapFactory.decodeResource(res, R.drawable.dig_2_6);
        numBitmaps[7] = BitmapFactory.decodeResource(res, R.drawable.dig_2_7);
        numBitmaps[8] = BitmapFactory.decodeResource(res, R.drawable.dig_2_8);
        numBitmaps[9] = BitmapFactory.decodeResource(res, R.drawable.dig_2_9);


        h = h * numBitmaps[0].getHeight() / panelBitmap.getHeight();
        w = w * numBitmaps[0].getWidth() / panelBitmap.getWidth();
        scale = panel.h / 572;

        for (int i = 0; i < 10; ++i) {
            nums[i] = new Texture(Bitmap.createScaledBitmap(numBitmaps[i], (int) w, (int) h, false));
            nums[i].setPos(new Point(0, panel.pos.y + 245 * scale));
            numBitmaps[i] = null;
        }


    }

    void drawNum(Canvas canvas) {
        if (cur_l < 0 || cur_r < 0)
            return;
        nums[cur_l].pos.x = panel.pos.x + 110 * scale;
        nums[cur_l].xPos = panel.xPos;
        nums[cur_l].draw(canvas);
        nums[cur_r].pos.x = panel.pos.x + 195 * scale;
        nums[cur_r].xPos = panel.xPos;
        nums[cur_r].draw(canvas);
    }

    void draw(Canvas canvas) {
        panel.draw(canvas);
        drawNum(canvas);
        Paint pnt = new Paint();
        pnt.setColor(Color.LTGRAY);
        canvas.drawCircle((float)(panel.pos.x + panel.xPos), (float)panel.pos.y, 4, pnt);
    }

}
