package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

public class Tutorial {

    Text[]   texts;
    Pointer[]  pointers;

    Tutorial(Rect windowRect) {
        texts = new Text[17];
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
                         (int)(windowRect.height() * 0.17),
                         (int)(windowRect.width() * 0.95),
                         (int)(windowRect.height() * 0.4)),
                Color.rgb(240, 240, 240),
                Color.rgb(250, 200, 0));
        texts[2] = texts[1];

        texts[3] = new Text("Выберите следующий монитор перелистыванием справа налево",
                (int)(windowRect.height() * 0.04),
                Color.BLACK,
                new Rect((int)(windowRect.width() * 0.05),
                        (int)(windowRect.height() * 0.17),
                        (int)(windowRect.width() * 0.95),
                        (int)(windowRect.height() * 0.4)),
                Color.rgb(240, 240, 240),
                Color.rgb(250, 200, 0));
        texts[4] = texts[3];
        texts[5] = texts[3];

        texts[6] = new Text("Нажмите на эту кнопку для включения режима демонстрации",
                (int)(windowRect.height() * 0.04), Color.BLACK,
                new Rect((int)(windowRect.width() * 0.05), (int)(windowRect.height() * 0.5),
                        (int)(windowRect.width() * 0.95), (int)(windowRect.height() * 0.7)),
                Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));
        texts[7] = null;

        texts[8] = new Text("Нажмите ещё раз для включения второго режима демонстрации",
                (int)(windowRect.height() * 0.04), Color.BLACK,
                new Rect((int)(windowRect.width() * 0.05), (int)(windowRect.height() * 0.5),
                        (int)(windowRect.width() * 0.95), (int)(windowRect.height() * 0.7)),
                Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));
        texts[9] = null;

        texts[10] = new Text("Нажмите ещё раз для перехода в ручной режим",
                (int)(windowRect.height() * 0.04), Color.BLACK,
                new Rect((int)(windowRect.width() * 0.05), (int)(windowRect.height() * 0.5),
                        (int)(windowRect.width() * 0.95), (int)(windowRect.height() * 0.7)),
                Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));
        texts[11] = null;

        texts[12] = new Text("Нажмите на кнопку \"i\" для информации о парковочной системе",
                (int)(windowRect.height() * 0.04), Color.BLACK,
                new Rect((int)(windowRect.width() * 0.05), (int)(windowRect.height() * 0.2),
                        (int)(windowRect.width() * 0.95), (int)(windowRect.height() * 0.4)),
                Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));

        texts[13] = null;
        texts[16] = new Text("Обучение закончено!\n\nВ дальнейшем, если Вы захотите повторить обучение, нажмите на вопросительный знак.",
                (int)(windowRect.height() * 0.04), Color.BLACK,
                new Rect((int)(windowRect.width() * 0.05), (int)(windowRect.height() * 0.33),
                        (int)(windowRect.width() * 0.95), (int)(windowRect.height() * 0.4)),
                Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));


        pointers = new Pointer[17];
        pointers[0] = new BlinkRect(new Rect(
                (int)(windowRect.width() * 0.02),
                (int)(windowRect.height() * 0.7),
                (int)(windowRect.width() * 0.98),
                (int)(windowRect.height() * 0.99)));

        Path path = new Path();
        path.moveTo((int)(windowRect.width() * 0.35), (int)(windowRect.height() * 0.88));
        path.lineTo((int)(windowRect.width() * 0.6), (int)(windowRect.height() * 0.88));
        path.arcTo(new RectF((int)(windowRect.width() * 0.5),
                        (int)(windowRect.height() * 0.88 - windowRect.width() * 0.2),
                        (int)(windowRect.width() * 0.7),
                        (int)(windowRect.height() * 0.88)),
                90, -270);
        path.lineTo((int)(windowRect.width() * 0.5), (int)(windowRect.height() * 0.95));


        pointers[1] = new Bee(path, 6);
        pointers[2] = null;

        path = new Path();
        path.moveTo((int)(windowRect.width() * 0.9), (int)(windowRect.height() * 0.55));
        path.lineTo((int)(windowRect.width() * 0.5), (int)(windowRect.height() * 0.55d));

        pointers[3] = new Bee(path, 4);
        pointers[4] = null;
        pointers[5] = null;

        pointers[6] = new Arrow(
                new Rect(
                    (int)(windowRect.width() * 0.3),
                    (int)(windowRect.height() * 0.315),
                    (int)(windowRect.width() * 0.55),
                    (int)(windowRect.height() * 0.415)
                ),
                Arrow.HORIZONTAL,
                windowRect.width() / 20);
        pointers[7] = null;
        pointers[8] = pointers[6];
        pointers[9] = null;
        pointers[10] = pointers[6];
        pointers[11] = null;
        pointers[12] =  new Arrow(
                new Rect(
                        (int)(windowRect.width() * 0.3),
                        (int)(windowRect.height() * 0.01),
                        (int)(windowRect.width() * 0.55),
                        (int)(windowRect.height() * 0.11)
                ),
                Arrow.HORIZONTAL,
                windowRect.width() / 20);
        pointers[13] = null;
        pointers[16] = null;
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
