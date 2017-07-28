package com.ifkbhit.parktronic;

import android.graphics.Canvas;

/**
 * Created by vasyoid on 28.07.17.
 */

public class Tutorial {
    Text text;

    Tutorial(Text text) {
        this.text = text;
    }

    void draw(Canvas canvas) {
        canvas.drawARGB(200, 255, 255, 255);
        text.draw(canvas);
    }
}
