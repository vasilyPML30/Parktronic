package com.ifkbhit.parktronic;

import android.graphics.Canvas;

/**
 * Created by vasyoid on 28.07.17.
 */

public class Tutorial {

    Text text;
    Texture arrow;

    Tutorial(Text text, Texture arrow) {
        this.text = text;
        this.arrow = arrow;
    }

    void draw(Canvas canvas) {
        canvas.drawARGB(200, 255, 255, 255);
        text.draw(canvas);
        arrow.draw(canvas);
    }
}
