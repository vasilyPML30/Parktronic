package com.ifkbhit.parktronic;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class panel3 extends Panel {

    double scale;
    Texture[] greenInd = new Texture[8];
    Texture[] redInd = new Texture[8];
    Texture[] whiteInd = new Texture[2];
    MyTime blinkTimer = new MyTime();

    panel3(int cnvW, int cnvH, Resources res, boolean isVertical) {
        this.res = res;
        reversible = true;
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
        k = h / panelBitmap.getHeight();
        Bitmap tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sgl1);
        greenInd[0] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[0].setPos(new Point(panel.pos.x  + 50 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sgl2);
        greenInd[1] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[1].setPos(new Point(panel.pos.x  + 125 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sol1);
        greenInd[2] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[2].setPos(new Point(panel.pos.x  + 335 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sol2);
        greenInd[3] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[3].setPos(new Point(panel.pos.x  + 545 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sor2);
        greenInd[4] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[4].setPos(new Point(panel.pos.x  + 755 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sor1);
        greenInd[5] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[5].setPos(new Point(panel.pos.x  + 970 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sgr2);
        greenInd[6] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[6].setPos(new Point(panel.pos.x  + 1180 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.sgr1);
        greenInd[7] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        greenInd[7].setPos(new Point(panel.pos.x  + 1390 * k * 2, panel.pos.y));

        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srl1);
        redInd[0] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[0].setPos(new Point(panel.pos.x  + 50 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srl2);
        redInd[1] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[1].setPos(new Point(panel.pos.x  + 125 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srl3);
        redInd[2] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[2].setPos(new Point(panel.pos.x  + 335 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srl4);
        redInd[3] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[3].setPos(new Point(panel.pos.x  + 545 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srr4);
        redInd[4] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[4].setPos(new Point(panel.pos.x  + 755 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srr3);
        redInd[5] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[5].setPos(new Point(panel.pos.x  + 970 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srr2);
        redInd[6] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[6].setPos(new Point(panel.pos.x  + 1180 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.srr1);
        redInd[7] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        redInd[7].setPos(new Point(panel.pos.x  + 1390 * k * 2, panel.pos.y));

        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.swl);
        whiteInd[0] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        whiteInd[0].setPos(new Point(panel.pos.x  + 50 * k * 2, panel.pos.y));
        tmpBmp = BitmapFactory.decodeResource(res, R.drawable.swr);
        whiteInd[1] = new Texture(Bitmap.createScaledBitmap(tmpBmp, (int)(k * tmpBmp.getWidth()),
                (int)(k * tmpBmp.getHeight()),
                false));
        whiteInd[1].setPos(new Point(panel.pos.x  + 1390 * k * 2, panel.pos.y));



        Bitmap rPanelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_0);
        Bitmap lPanelBitmap = BitmapFactory.decodeResource(res, R.drawable.panel_1_empty);

        k = (isVertical ? (1.0 - Config.CAR_Y_OFFSET_K) * 393.0 / Config.CAR_H : 0.45);

        double lh = cnvH * k;
        double lw = lPanelBitmap.getWidth() * lh / lPanelBitmap.getHeight();

        k = (isVertical ? (1.0 - Config.CAR_Y_OFFSET_K) * 120.0 / Config.CAR_H : 0.2);

        double rh = cnvH * k;
        double rw = rPanelBitmap.getWidth() * rh / rPanelBitmap.getHeight();

        r_panel = new Texture(Bitmap.createScaledBitmap(rPanelBitmap, (int) (rw * 1.3), (int) (rh * 1.3), false));
        l_panel = new Texture(Bitmap.createScaledBitmap(lPanelBitmap, (int) lw, (int) lh, false));

        if (isVertical) {
            l_panel.setPos(new Point(-lw * 0.87, cnvH * 0.425));
            r_panel.setPos(new Point(cnvW - 0.21 * rw, cnvH * (Config.CAR_Y_OFFSET_K / 2) + ((1 - Config.CAR_Y_OFFSET_K) * cnvH) / 2.0 - r_panel.img.getWidth() * 1.3 / 16.0));
        }
        else {
            l_panel.setPos(new Point((-lw - w) / 2, cnvH / 4 - lh / 2));
            r_panel.setPos(new Point(cnvW + (w - rw * 1.3) / 2, cnvH / 4 - rh * 1.3 / 2));
        }
    }

    void drawBars(Canvas canvas) {
        int left = Math.max(state[0], state[1]);
        int right = Math.max(state[2], state[3]);
        if (this.reverse) {
            left += right;
            right = left - right;
            left = left - right;
        }
        for (Texture ind : whiteInd) {
            ind.xPos = panel.xPos;
        }
        for (Texture ind : redInd) {
            ind.xPos = panel.xPos;
        }
        for (Texture ind : greenInd) {
            ind.xPos = panel.xPos;
        }
        if (left > 3) {
            if (left < 5) {
                for (int i = 0; i < 4; ++i) {
                    redInd[i].draw(canvas);
                }
            } else {
                blinkTimer.Refresh();
                int numberOn;
                if (blinkTimer.FromStart % 800 < 400) {
                    numberOn = (int)((blinkTimer.FromStart % 800) / 100);
                } else {
                    numberOn = 2 - (int)((blinkTimer.FromStart % 800 - 400) / 100);
                }
                Log.d("numberOn", String.valueOf(numberOn));
                if (numberOn >= 0) {
                    for (int i = 3; i >= 3 - numberOn; i--) {
                        redInd[i].draw(canvas);
                    }
                }
            }
        } else {
            whiteInd[0].draw(canvas);
            if (left > 0) {
                greenInd[0].draw(canvas);
            }
            if (left > 1) {
                greenInd[1].draw(canvas);
            }
            if (left > 2) {
                greenInd[2].draw(canvas);
                greenInd[3].draw(canvas);
            }
        }
        if (right > 3) {
            if (right < 5) {
                for (int i = 4; i < 8; ++i) {
                    redInd[i].draw(canvas);
                }
            } else {
                blinkTimer.Refresh();
                int numberOn;
                if (blinkTimer.FromStart % 800 < 400) {
                    numberOn = (int)((blinkTimer.FromStart % 800) / 100);
                } else {
                    numberOn = 2 - (int)((blinkTimer.FromStart % 800 - 400) / 100);
                }
                if (numberOn >= 0) {
                    for (int i = 4; i <= 4 + numberOn; i++) {
                        redInd[i].draw(canvas);
                    }
                }
            }
        } else {
            whiteInd[1].draw(canvas);
            if (right > 0) {
                greenInd[7].draw(canvas);
            }
            if (right > 1) {
                greenInd[6].draw(canvas);
            }
            if (right > 2) {
                greenInd[5].draw(canvas);
                greenInd[4].draw(canvas);
            }
        }
    }

    @Override
    void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(panelAngle, (float)panel.getCenter().x, (float)panel.getCenter().y);
        if (reverse) {
            canvas.save();
            canvas.rotate(180, (float) panel.getCenter().x, (float) panel.getCenter().y);
            panel.draw(canvas);
            drawBars(canvas);
            canvas.restore();
        }
        else {
            panel.draw(canvas);
            drawBars(canvas);
        }
        canvas.restore();
    }

    @Override
    int getLevel(double val) {
        if (val < 0) {
            return 0;
        }
        if (val <= 0.21) {
            return 5;
        }
        if (val <= 0.51) {
            return 4;
        }
        if (val <= 0.71) {
            return 3;
        }
        if (val <= 0.91) {
            return 2;
        }
        if (val <= 1.31) {
            return 1;
        }
        return 0;
    }



}