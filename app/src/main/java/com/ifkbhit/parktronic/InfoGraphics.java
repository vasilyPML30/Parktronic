package com.ifkbhit.parktronic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toolbar;

public class InfoGraphics extends View {

    int W, H;
    Paint paint;
    boolean inited = false;
    Tutorial tutorial;
    int cur_tutorial;
    MyTime timer;

    public InfoGraphics(Context context) {
        super(context);
    }
    public InfoGraphics(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public InfoGraphics(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    int getSBPH() {
        DisplayMetrics usable = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(usable);
        DisplayMetrics real = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(real);
        return Math.abs(usable.heightPixels - real.heightPixels);
    }

    void init() {
        Rect windowRect = new Rect();
        ((Activity)getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(windowRect);
        windowRect = new Rect(windowRect.left, windowRect.top,
                windowRect.right, windowRect.bottom - getSBPH());
        H = windowRect.height();
        W = windowRect.width();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        tutorial = Config.tutorial;
        inited = true;
        timer = new MyTime();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!inited) {
            invalidate();
            return;
        }
        if (timer != null) {
            timer.Refresh();
            if (cur_tutorial == 13 && timer.FromStartS > 1.0) {
                timer = null;
                ++cur_tutorial;
                View tb = ((Activity)getContext()).findViewById(R.id.toolbar);
                View logo = ((Activity)getContext()).findViewById(R.id.site_image_1);
                ((Activity)getContext()).findViewById(R.id.more_button).setEnabled(true);
                ((Activity)getContext()).findViewById(R.id.inc_216).setAlpha(0.25f);
                ((Activity)getContext()).findViewById(R.id.inc_218).setAlpha(0.25f);
                View more_button = ((Activity)getContext()).findViewById(R.id.more_button);
                tutorial.pointers[14] = new Arrow(
                        new Rect(
                                (int)more_button.getX(),
                                (int)(more_button.getY() - W / 19 - more_button.getWidth() * 1.6),
                                (int)(more_button.getX() + more_button.getWidth()),
                                (int)(more_button.getY() - W / 19)
                        ),
                        Arrow.VERTICAL,
                        W / 20);
                View back_button = ((Activity)getContext()).findViewById(R.id.fab);
                tutorial.pointers[15] = new Arrow(
                        new Rect(
                                (int)back_button.getX(),
                                (int)(back_button.getY() - W / 19 - back_button.getWidth() * 1.6),
                                (int)(back_button.getX() + back_button.getWidth()),
                                (int)(back_button.getY() - W / 19)
                        ),
                        Arrow.VERTICAL,
                        W / 20);
                tutorial.texts[14] = new Text("Нажмите на кнопку \"подробнее\" для более полного описания системы помощи при парковке",
                        (int)(H * 0.04), Color.BLACK,
                        new Rect((int)(W * 0.05), (int)(tb.getHeight() + H * 0.04 + 10),
                                (int)(W * 0.95), (int)(H * 0.2)),
                        Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));

                tutorial.texts[15] = new Text("Вернитесь на демонстрационный экран",
                        (int)(H * 0.04), Color.BLACK,
                        new Rect((int)(W * 0.05), (int)(logo.getY() + logo.getHeight() + H * 0.04 + 10),
                                (int)(W * 0.95), (int)(H * 0.3)),
                        Color.rgb(240, 240, 240), Color.rgb(255, 200, 0));


            }
        }

        if (cur_tutorial >= 0) {
            tutorial.draw(canvas, cur_tutorial);
            //if (cur_tutorial >= 14) {
            //    close.draw(canvas);
            //}
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*if (event.getAction() == MotionEvent.ACTION_DOWN && close.onButtonTap(event)) {
            cur_tutorial = -1;
            ((Activity)getContext()).findViewById(R.id.more_button).setEnabled(true);
            ((Activity)getContext()).findViewById(R.id.fab).setEnabled(true);
            ((Activity)getContext()).findViewById(R.id.inc_216).setAlpha(1f);
            ((Activity)getContext()).findViewById(R.id.inc_218).setAlpha(1f);
        }
        */
        return super.onTouchEvent(event);
    }
}
