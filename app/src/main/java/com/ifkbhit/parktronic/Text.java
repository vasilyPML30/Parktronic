package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by vasyoid on 28.07.17.
 */

public class Text {
    String txt;
    ArrayList<Integer> lines;
    float[] lens;
    Point txt_pos;
    Rect bckgnd;
    Paint pnt;
    int txt_color, bckgnd_color;

    Text(String text, float text_size, int text_color, Rect background, int background_color) {
        txt = text;
        pnt = new Paint();
        pnt.setTextSize(text_size);
        bckgnd = background;
        bckgnd_color = background_color;
        txt_color = text_color;
        txt_pos = new Point(bckgnd.left, bckgnd.top + text_size);
        lines = new ArrayList<Integer>();
        int pos = 0;
        int end = text.length();
        while (pos < end) {
            int len = pnt.breakText(txt, pos, end, true, bckgnd.width(), null);
            while (pos + len != txt.length() && txt.charAt(pos + len - 1) != ' ') {
                --len;
            }
            pos += len;
            lines.add(pos);
        }
        lens = new float[lines.size()];
        pos = 0;
        int cur_line = 0;
        for (int brk : lines) {
            lens[cur_line++] = bckgnd.width() - pnt.measureText(txt, pos, brk);
            pos = brk;
        }
    }

    void draw(Canvas canvas) {
        pnt.setColor(bckgnd_color);
        canvas.drawRect(bckgnd, pnt);
        pnt.setColor(txt_color);
        int pos = 0;
        float y_pos = (float)txt_pos.y;
        int cur_line = 0;
        for (int brk : lines) {
            canvas.drawText(txt, pos, brk, (float)txt_pos.x + lens[cur_line++] / 2, y_pos, pnt);
            pos = brk;
            y_pos += pnt.getTextSize() * 1.2;
        }
    }
}
