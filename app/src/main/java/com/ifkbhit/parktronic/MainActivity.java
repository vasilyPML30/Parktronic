package com.ifkbhit.parktronic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new myGraphics(this));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    class myGraphics extends View {
        boolean             firstStep = true;
        private int         W, H;                            //размеры canvas
        private Brick       brick1, brick2, brick3, brick4;  //сами блоки  и вспомогательные
        private double      minYTape, maxYTape;              //лента смены монитора
        private boolean     onTape = false;
        private boolean[]   onBrickPressed = {false, false}; //информация о блоках
        private Point       movingPoint;                     //точка для передвижения блоков
        private Car         car;
        private Button      invert, info, tap1, tap2;
        private Button[]    demo;
        private int         demo_state = 0;
        Demo                demo_act, demo_act_down, demo_act_c, demo_act_down_c;

        myGraphics(Context context){
            super(context);
            W = 0;
            H = 0;
        }

        int getSBPH() {
            DisplayMetrics usable = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(usable);
            DisplayMetrics real = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(real);
            return Math.abs(usable.heightPixels - real.heightPixels);
        }

        void init() {
            firstStep = false;
            Rect windowRect = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(windowRect);
            windowRect = new Rect(windowRect.left, windowRect.top,
                                  windowRect.right, windowRect.bottom - getSBPH());
            Log.d("window", windowRect.flattenToString());
            H = windowRect.height();
            W = windowRect.width();
            minYTape = H * 9f / 22f;
            maxYTape = H * 7f / 10f;

            /* Машина */

            Bitmap carBitmap[] = new Bitmap[3];
            carBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.car_woz);
            carBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.car_uz);
            carBitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.car_dz);
            double k = (double)H * (1.0 - Config.CAR_Y_OFFSET_K) / ((double)carBitmap[0].getHeight());
            Config.CURRENT_CAR_K = k;
            double cur_car_w = (double)carBitmap[0].getWidth() * k;
            Texture[] carTex = new Texture[3];
            for (int i = 0; i < 3; i++) {
                carBitmap[i] = Bitmap.createScaledBitmap(carBitmap[i], (int) cur_car_w, (int) ((double) carBitmap[i].getHeight() * k), true);
                carTex[i] = new Texture(carBitmap[i], new Point(W / 2 - carBitmap[0].getWidth() / 2, H * (Config.CAR_Y_OFFSET_K) * 0.5), k);
            }
            carTex[2].setPos(new Point(carTex[0].pos.x, carTex[0].pos.y + carTex[0].h - carTex[2].h));
            car = new Car(carTex[0], carTex[1], carTex[2], windowRect, getResources());

            /* Кубики */


            double brick_l = cur_car_w / 10;

            brick1 = new Brick(brick_l, brick_l, new Point(W / 2 - brick_l / 2, H / 8));
            brick1.setBorder(new Point(0, 0), new Point(W, H / 2));
            brick2 = new Brick(brick_l, brick_l, new Point(W / 2 - brick_l / 2, 7 * H / 8));
            brick1.setVisible(false);
            brick2.setVisible(false);
            brick2.setBorder(new Point(0, H / 2), new Point(W, H));
            double supportBrick_l = brick_l * Config.supportBrickScale;
            double deltaSizes = (supportBrick_l - brick_l) / 2;
            brick3 = new Brick(supportBrick_l, supportBrick_l, new Point(W / 2 - brick_l / 2, H / 8));
            brick3.setBorder(new Point(0, 0).sum(new Point(-deltaSizes, -deltaSizes)), new Point(W, H / 2 + brick1.h / 2).sum(new Point(deltaSizes, deltaSizes)));
            brick4 = new Brick(supportBrick_l, supportBrick_l, new Point(W / 2 - brick_l / 2, 7 * H / 8));
            brick4.setBorder(new Point(0, H / 2).sum(new Point(-deltaSizes, -deltaSizes)), new Point(W, H).sum(new Point(deltaSizes, deltaSizes)));

            /* Кнопки */

            invert = new Button(R.drawable.invert, getResources(), windowRect, 0.85, 0.37, 5);
            demo = new Button[] {
                    new Button(R.drawable.button_demo, getResources(), windowRect, 0.15, 0.37, 5),
                    new Button(R.drawable.button_demo_2, getResources(), windowRect, 0.15, 0.37, 5),
                    new Button(R.drawable.button_manual, getResources(), windowRect, 0.15, 0.37, 5)
            };
            info = new Button(R.drawable.info, getResources(), windowRect, -1, -1, 5);

            /* Демо */

            demo_act = new Demo(Demo.TYPE_ELLIPSE, Demo.UPPER_DEMO);
            demo_act_c = new Demo(Demo.TYPE_CIRCLE, Demo.UPPER_DEMO);
            demo_act_down = new Demo(Demo.TYPE_ELLIPSE, Demo.LOWER_DEMO);
            demo_act_down_c = new Demo(Demo.TYPE_CIRCLE, Demo.LOWER_DEMO);
            demo_act.init(car);
            demo_act_down.init(car);
            demo_act_c.init(car);
            demo_act_down_c.init(car);

            /* Области препятствий */

            int tw = (int)(carTex[0].w * 0.25);
            int th = (int)(tw * 1.276);

            Point pos1 = new Point((W - tw) / 2, (carTex[0].pos.y + carTex[0].h * 0.175 - th) / 2);
            Point pos2 = new Point((W - tw) / 2, (H + carTex[0].pos.y + carTex[0].h * 0.77 - th) / 2);

            tap1 = new Button(R.drawable.finger, getResources(), tw, th, pos1);
            tap2 = new Button(R.drawable.finger, getResources(), tw, th, pos2);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (firstStep) {
                init();
            }

            car.draw(canvas);
            if (brick1.isVisible()) {
                if (demo_state != 0) {
                    Point animPoint = null;
                    switch (demo_state) {
                        case 1:
                            animPoint = demo_act.getPos();
                            break;
                        case 2:
                            animPoint = demo_act_c.getPos();
                            break;
                    }
                    brick1.setCenterPos(animPoint);
                }
                car.response(brick1, true, canvas);
                car.curTex = 1;
            }
            if (brick2.isVisible()) {
                car.curTex = 2;
                if (demo_state != 0) {
                    Point animPoint = null;
                    switch (demo_state) {
                        case 1:
                            animPoint = demo_act_down.getPos();
                            break;
                        case 2:
                            animPoint = demo_act_down_c.getPos();
                            break;
                    }
                    brick2.setCenterPos(animPoint);
                }
                car.response(brick2, false, canvas);
            }
            brick1.draw(canvas);
            brick2.draw(canvas);
            invalidate();
            if (car.isPanelReversable()) {
                invert.draw(canvas);
            }
            info.draw(canvas);
            demo[demo_state].draw(canvas);
            double speed = 8;
            if (!brick1.isVisible())
                tap1.animatedDraw(canvas, speed);
            if (!brick2.isVisible())
                tap2.animatedDraw(canvas, speed);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (firstStep) {
                return true;
            }
            double ex = event.getX();
            double ey = event.getY();

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    movingPoint = new Point(event);

                    if (ey >= minYTape && ey <= maxYTape && car.isPanelAvailable()) { //если нажали на ленту, то запомниаем это
                        onTape = true;
                    }
                    else {
                        if (info.onButtonTap(event)) {
                            Intent infoIntent = new Intent(getApplicationContext(), com.ifkbhit.parktronic.ActivityInfo.class);
                            infoIntent.putExtra("sysType", car.cur_panel);
                            startActivity(infoIntent);
                            return true;
                        }
                        if (invert.onButtonTap(event) && car.isPanelAvailable() && car.isPanelReversable()) {
                            car.revertPanel();
                            return true;
                        }
                        if (demo[demo_state].onButtonTap(event)) {
                            if (brick1.isVisible() || brick2.isVisible()) {
                                demo_state = (demo_state + 1) % 3;
                            }
                            return true;
                        }

                        if (new Brick(1, 1, new Point(event)).checkWithLines(car.getSupportLineDown(), false) ||
                             new Brick(1, 1, new Point(event)).checkWithLines(car.getSupportLineUp(), true)) {
                            if (brick1.isVisible() || brick2.isVisible()) {
                                if (brick1.isVisible() && ey > H / 2) {
                                    brick1.hide();
                                    brick2.setPos(new Point(ex - brick2.w / 2, ey - brick2.h / 2));
                                    brick4.setPos(new Point(ex - brick4.w / 2, ey - brick4.h / 2));
                                    onBrickPressed[1] = true;
                                    brick2.show();
                                    return true;
                                }
                                else if (brick2.isVisible() && ey < H / 2) {
                                    brick2.hide();
                                    brick1.setPos(new Point(ex - brick1.w / 2, ey - brick1.h / 2));
                                    brick3.setPos(new Point(ex - brick3.w / 2, ey - brick3.h / 2));
                                    onBrickPressed[0] = true;
                                    brick1.show();
                                    return true;
                                }
                            }
                            else if (ey < H * 11 / 40.0 || ey > H * 29 / 40.0) {
                                boolean tmp = false;
                                double first_floor = H / 2.0 * 0.75;
                                double sec_floor = (5 / 8.0) * H;
                                if (ey < first_floor && !tmp) {
                                    brick1.setVisible(true);
                                }
                                if (event.getY() > sec_floor && !tmp) {
                                    brick2.setVisible(true);
                                }
                            }
                            if (brick1.inBrick(event) || brick3.inBrick(event)) {
                                onBrickPressed[0] = true;
                            }
                            if (brick2.inBrick(event) || brick4.inBrick(event)) {
                                onBrickPressed[1] = true;
                            }
                        }
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (onBrickPressed[0]) {
                        brick1.Move(event, movingPoint, false);
                        brick3.setCenterPos(brick1.getCenter());
                        brick1.checkWithLines(car.getSupportLineUp(), true);
                        Line[] tmp = {new Line(0, H * 7 / 22, W, H * 7 / 22)};
                        brick1.checkWithLines(tmp, true);
                    }

                    if (onBrickPressed[1]) {
                        brick2.Move(event, movingPoint, false);
                        brick4.setCenterPos(brick2.getCenter());
                        brick2.checkWithLines(car.getSupportLineDown(), false);
                        Line[] tmp = {new Line(0, maxYTape, W, maxYTape)};
                        brick2.checkWithLines(tmp, false);
                    }

                    if (onTape) {
                        car.movePanel(ex - movingPoint.x);
                    }

                    movingPoint = new Point(event);
                    break;

                case MotionEvent.ACTION_UP:
                    if (onTape) {
                        car.mvPanel();
                        onTape = false;
                    }
                    else {
                        onBrickPressed[0] = false;
                        onBrickPressed[1] = false;
                    }
                    break;
            }
            return true;
        }
    }
}
