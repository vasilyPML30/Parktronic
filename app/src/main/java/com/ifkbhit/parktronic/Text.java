package com.ifkbhit.parktronic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

class Text {
    String txt;
    ArrayList<Integer> lines;
    float[] lens;
    Point txt_pos;
    Rect bckgnd, hdr;
    Paint pnt;
    int txt_color, bckgnd_color, hdr_color;

    Text(String text, float text_size, int text_color, Rect background, int background_color, int header_color) {
        txt = text;
        pnt = new Paint();
        pnt.setTextSize(text_size);
        bckgnd = background;
        hdr = new Rect(bckgnd.left, bckgnd.top - (int)text_size, bckgnd.right, bckgnd.top);
        bckgnd_color = background_color;
        hdr_color = header_color;
        txt_color = text_color;
        txt_pos = new Point(bckgnd.left, bckgnd.top + text_size);
        lines = new ArrayList<>();
        int pos = 0;
        int end = txt.length();
        while (pos < end) {
            int len = pnt.breakText(txt, pos, end, true, bckgnd.width() - text_size, null);
            boolean skip = false;
            for (int i = 0; i < len; ++i) {
                if (txt.charAt(pos + i) == '\n' && pos + i < end - 1) {
                    pos += i + 1;
                    lines.add(pos);
                    skip = true;
                    break;
                }
            }
            if (skip) {
                continue;
            }
            while (pos + len < end && txt.charAt(pos + len - 1) != ' ') {
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
        bckgnd.bottom = bckgnd.top + (int)((lines.size() + 1) * text_size * 1.2);
    }

    void draw(Canvas canvas) {
        pnt.setColor(hdr_color);
        canvas.drawRect(hdr, pnt);
        pnt.setColor(bckgnd_color);
        canvas.drawRect(bckgnd, pnt);
        pnt.setColor(txt_color);
        int pos = 0;
        float y_pos = (float)(txt_pos.y + (bckgnd.height() - pnt.getTextSize() * (lines.size() * 1.2)) / 2);
        for (int brk : lines) {
            canvas.drawText(txt, pos, brk, (float)txt_pos.x + pnt.getTextSize() / 2, y_pos, pnt);
            pos = brk;
            y_pos += pnt.getTextSize() * 1.2;
        }
    }
}
