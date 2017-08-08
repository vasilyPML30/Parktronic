package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class Tutorial {

    Text[]   texts;
    Pointer[]  pointers;

    Tutorial(Rect windowRect) {
        texts = new Text[9];
        texts[0] = new Text("Нажмите на область за автомобилем, чтобы поставить препятствие",
                (int)(windowRect.height() * 0.04),
                Color.BLACK,
                new Rect(windowRect.width() / 20,
                        (int)(windowRect.height() * 0.4),
                        windowRect.width() * 19 / 20,
                        (int)(windowRect.height() * 0.6)),
                Color.rgb(240, 240, 240),
                Color.rgb(250, 200, 0));
        texts[1] = new Text("Перетаскивайте препятствие и следите за реакцией на мониторе парковочной системы",
                (int)(windowRect.height() * 0.04),
                Color.BLACK,
                new Rect((int)(windowRect.width() * 0.05),
                         (int)(windowRect.height() * 0.2),
                         (int)(windowRect.width() * 0.95),
                         (int)(windowRect.height() * 0.4)),
                Color.rgb(240, 240, 240),
                Color.rgb(250, 200, 0));
        texts[2] = texts[1];
        texts[4] = new Text("Нажмите на кнопку \"i\" для информации о парковочной системе",
                (int)(windowRect.height() * 0.04), Color.BLACK,
                new Rect(windowRect.width() / 9, (int)(windowRect.height() * 0.4),
                         windowRect.width() * 8 / 9, (int)(windowRect.height() * 0.6)),
                Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));

        pointers = new Pointer[9];
        pointers[0] = new BlinkRect(new Rect(
                (int)(windowRect.width() * 0.02),
                (int)(windowRect.height() * 0.7),
                (int)(windowRect.width() * 0.98),
                (int)(windowRect.height() * 0.99)));
        pointers[1] = new Bee(windowRect, Config.BRICK_L / 2);
        pointers[2] = null;
    }

    void draw(Canvas canvas, int mode) {
        if (texts[mode] != null) {
            texts[mode].draw(canvas);
        }
        if (pointers[mode] != null) {
            pointers[mode].animate();
            pointers[mode].draw(canvas);
        }
    }
}
