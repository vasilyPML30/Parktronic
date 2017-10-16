package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class panel3 extends Panel {

    double scale;
    Texture[] indicators = new Texture[11];

    panel3(int cnvW, int cnvH, Resources res, boolean isVertical) {
        this.res = res;
        reversable = false;
        double k = (isVertical ? 0.1 : 0.25);
        Bitmap panelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_2);
        double need_h = cnvH * k;
        h = need_h;
        w = panelBitmap.getWidth() * h / panelBitmap.getHeight();
        panel = new Texture(Bitmap.createScaledBitmap(panelBitmap, (int) w, (int) h, false));
        if (isVertical) {
            panel.setPos(new Point((cnvW - panel.img.getWidth()) / 2.0, cnvH * 0.47));
        }
        else {
            panel.setPos(new Point((cnvW - w) / 2, cnvH / 4 - h / 2));
        }

        Bitmap lPanelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_0);
        Bitmap rPanelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_1_empty);
        k = (1.0 - Config.CAR_Y_OFFSET_K) * 120.0 / Config.CAR_H;

        double lh = (isVertical ? cnvH * k : cnvH / 5);
        double rh = cnvH * k;
        double rw = rPanelBitmap.getWidth() * rh / rPanelBitmap.getHeight();
        double lw = lPanelBitmap.getWidth() * lh / lPanelBitmap.getHeight();

        l_panel = new Texture(Bitmap.createScaledBitmap(lPanelBitmap, (int) (lw * 1.3), (int) (lh * 1.3), false));
        r_panel = new Texture(Bitmap.createScaledBitmap(rPanelBitmap, (int) rw, (int) rh, false));

        if (isVertical) {
            l_panel.setPos(new Point(-lw * 0.75 * 1.3, cnvH * (Config.CAR_Y_OFFSET_K / 2) + ((1 - Config.CAR_Y_OFFSET_K) * cnvH) / 2.0 - l_panel.img.getWidth() * 1.3 / 16.0));
            r_panel.setPos(new Point(cnvW - 0.21 * rw, cnvH * 0.425));
        }
        else {
            l_panel.setPos(new Point(-lw, cnvH / 4 - lh * 1.3 / 2));
            r_panel.setPos(new Point(cnvW + 1, cnvH / 4 - l_panel.h / 2));
        }
    }

    @Override
    void draw(Canvas canvas) {
        panel.draw(canvas);
    }

    @Override
    int getLevel(double val) {
        return 0;
    }


}