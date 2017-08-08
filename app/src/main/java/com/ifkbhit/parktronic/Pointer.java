package com.ifkbhit.parktronic;

import android.graphics.Canvas;

abstract class Pointer {
    abstract void animate();
    abstract void draw(Canvas canvas);
}
