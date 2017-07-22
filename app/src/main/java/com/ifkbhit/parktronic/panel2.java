package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class panel2 extends Panel {

    panel2(int cnvW, int cnvH, Resources res) {
        this.res = res;
        reversable = false;
        Bitmap panelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_1);
        double k = (1.0 - Config.CAR_Y_OFFSET_K) * 120.0 / Config.CAR_H;
        double need_h = cnvH * k;
        h = need_h;
        w = panelBitmap.getWidth() * h / panelBitmap.getHeight();
        panel = new Texture(Bitmap.createScaledBitmap(panelBitmap, (int) w, (int) h, false));
        panel.setPos(new Point((cnvW - panel.img.getWidth()) / 2.0, cnvH * 5 / 10));
        panel.setScaled(3.0);
    }

    void setPanel(double[] info,  boolean isUp) { }

    void draw(Canvas canvas) {
        panel.draw(canvas);
    }

}
