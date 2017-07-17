package com.ifkbhit.parktronic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
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
        private double      minYTape = 0, maxYTape = 0;      //TODO: область ленты, пока 0
        private boolean     onTape = false;
        private boolean[]   onBrickPressed = {false, false}; //информация о блоках
        private Point       movingPoint;                     //точка для передвижения блоков
        private Car         car;
        private Button      invert, info, tap1, tap2;
        private Button[]    demo;
        private int         demo_state = 0;
        Demo                demo_act, demo_act_down, demo_act_c, demo_act_down_c;
        boolean             isDemoActive = false;

        myGraphics(Context context){
            super(context);
            W = 0;
            H = 0;
        }

        void init(Canvas canvas) {
            firstStep = false;
            H = canvas.getHeight();
            W = canvas.getWidth();

            /* Машина */

            Bitmap carBitmap[] = new Bitmap[3];
            carBitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.car_woz);
            carBitmap[1] = BitmapFactory.decodeResource(getResources(), R.drawable.car_uz);
            carBitmap[2] = BitmapFactory.decodeResource(getResources(), R.drawable.car_dz);
            double k = (double)H * (1.0 - Config.CAR_Y_OFFSET_K) / ((double)carBitmap[0].getHeight());
            Config.CURRENT_CAR_K = k;
            double cur_car_w = (double)carBitmap[0].getWidth() * k;
            Texture[] carTex = new Texture[3];
            for(int i = 0; i < 3; i++) {
                carBitmap[i] = Bitmap.createScaledBitmap(carBitmap[i], (int) cur_car_w, (int) ((double) carBitmap[i].getHeight() * k), true);
                carTex[i] = new Texture(carBitmap[i], new Point(W / 2 - carBitmap[0].getWidth() / 2, H * (Config.CAR_Y_OFFSET_K) * 0.5), k);
            }
            car = new Car(carTex, canvas, getResources());

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

            invert = new Button(R.drawable.invert, getResources(), canvas, 0.85, 0.4, 5);
            demo = new Button[] {
                    new Button(R.drawable.button_demo, getResources(), canvas, 0.15, 0.4, 5),
                    new Button(R.drawable.button_demo_2, getResources(), canvas, 0.15, 0.4, 5),
                    new Button(R.drawable.button_manual, getResources(), canvas, 0.15, 0.4, 5)
            };
            info = new Button(R.drawable.info, getResources(), canvas, -1, -1, 5);

            /* Демо */

            double tw = 200;
            double th = 50;
            Line one = car.getSupportLineUp()[2];
            Line two = car.getSupportLineDown()[2];
            Point pos1, pos2;
            demo_act = new Demo(Demo.TYPE_ELLIPSE, Demo.UPPER_DEMO);
            demo_act_c = new Demo(Demo.TYPE_CIRCLE, Demo.UPPER_DEMO);
            demo_act_down = new Demo(Demo.TYPE_ELLIPSE, Demo.LOWER_DEMO);
            demo_act_down_c = new Demo(Demo.TYPE_CIRCLE, Demo.LOWER_DEMO);
            demo_act.init(car);
            demo_act_down.init(car);
            demo_act_c.init(car);
            demo_act_down_c.init(car);

            if (one.getPointA().y > one.getPointB().y) {
                pos1 = new Point(one.getPointA().sum(new Point(100 - one.getPointA().x, 0)), -3 * tw / 4, -th);
            }
            else
                pos1 = new Point(one.getPointB().sum(new Point(100 - one.getPointB().x, 0)), -3 * tw / 4, -th);

            if (two.getPointA().y < two.getPointB().y) {
                pos2 = new Point(two.getPointA().sum(new Point(100 - one.getPointA().x, 0)), -3 * tw / 4, 0);
            }
            else
                pos2 = new Point(two.getPointB().sum(new Point(100 - one.getPointB().x, 0)), -3 * tw / 4, 0);

            /* Области препятствий */

            tap1 = new Button(R.drawable.strelka, getResources(), canvas, 200, (int)th, pos1);
            tap2 = new Button(R.drawable.strelka, getResources(), canvas, 200, (int)th, pos2);

        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (firstStep) {
                init(canvas);
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
            brick1.Draw(canvas);
            brick2.Draw(canvas);
            invalidate();
            invert.draw(canvas);
            info.draw(canvas);
            demo[demo_state].draw(canvas);
            double speed = 11;
            if (!brick1.isVisible())
                tap1.animationScaledDraw(canvas, speed);
            if (!brick2.isVisible())
                tap2.animationScaledDraw(canvas, speed);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            double ex = event.getX();
            double ey = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    movingPoint = new Point(event);

                    if (ey >= minYTape && ey <= maxYTape) { //если нажали на ленту, то запомниаем это
                        onTape = true;
                    }
                    else {
                        if (info.onButtonTap(event)) {
                            startActivity(new Intent(getApplicationContext(), com.ifkbhit.parktronic.ActivityInfo.class));
                            return true;
                        }
                        if (invert.onButtonTap(event)) {
                            car.changePanel();
                            return true;
                        }
                        if (demo[demo_state].onButtonTap(event)) {
                            if (brick1.isVisible() || brick2.isVisible()) {
                                demo_state = (demo_state + 1) % 3;
                            }
                            return true;
                        }
                        if (demo_state != 0) {
                            return true;
                        }
                        if ((event.getY() < 7 * H / 20 || event.getY() > 2 * H / 3) &&
                            (new Brick(1, 1, new Point(event)).checkWithLines(car.getSupportLineDown(), false) ||
                             new Brick(1, 1, new Point(event)).checkWithLines(car.getSupportLineUp(), true))) {
                            if (brick1.isVisible() || brick2.isVisible()) {
                                if (brick1.isVisible() && ey > H / 2) {
                                    brick1.hide();
                                    brick2.setPos(new Point(ex - brick2.w / 2, ey - brick2.h / 2));
                                    brick4.setPos(new Point(ex - brick4.w / 2, ey - brick4.h / 2));
                                    onBrickPressed[1] = true;
                                    brick2.show();
                                }
                                else if (brick2.isVisible() && ey < H / 2) {
                                    brick2.hide();
                                    brick1.setPos(new Point(ex - brick1.w / 2, ey - brick1.h / 2));
                                    brick3.setPos(new Point(ex - brick3.w / 2, ey - brick3.h / 2));
                                    onBrickPressed[0] = true;
                                    brick1.show();
                                }
                                else {
                                    if (brick1.inBrick(event) || brick3.inBrick(event)) {
                                        onBrickPressed[0] = true;
                                    }
                                    if (brick2.inBrick(event) || brick4.inBrick(event)) {
                                        onBrickPressed[1] = true;
                                    }
                                }
                            }
                            else {
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
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isDemoActive) {
                        return true;
                    }

                    if (onBrickPressed[0]) {
                        brick1.Move(event, movingPoint, false);
                        brick3.setCenterPos(brick1.getCenter());
                        boolean response = brick1.checkWithLines(car.getSupportLineUp(), true);
                        car.setMovingResponse(response);
                    }
                    else if (onBrickPressed[1]) {
                        brick2.Move(event, movingPoint, false);
                        brick4.setCenterPos(brick2.getCenter());
                        boolean response = brick2.checkWithLines(car.getSupportLineDown(), false);
                        car.setMovingResponse(response);
                    }

                    if (!brick3.inBrick(event)) {
                        if (onBrickPressed[0])
                        onBrickPressed[0] = false;
                    }

                    if (!brick4.inBrick(event)) {
                        onBrickPressed[1] = false;
                    }

                    movingPoint = new Point(event);
                    break;
                case MotionEvent.ACTION_UP:
                    if (onTape) { //если нажатие было на ленте
                        Point actionUpPoint = new Point(event);
                        //double Length  = destinationAB(actionDownPoint, actionUpPoint, true);
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
