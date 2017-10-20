package com.ifkbhit.parktronic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends Activity {

    View view;
    int orientation;

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private Rect getwindowRect() {
        Rect windowRect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(windowRect);
        windowRect = new Rect(windowRect.left, windowRect.top,
                windowRect.right, windowRect.bottom - getStatusBarHeight());
        return windowRect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new myGraphics(this);
        view.setId(R.id.my_main);
        orientation = getResources().getConfiguration().orientation;
        if (orientation == ORIENTATION_PORTRAIT) {
            view = new myGraphics(this);
        }
        else {
            view = new myLandscape(this);
        }
        view.setId(R.id.my_main);
        setContentView(view);
    }

    class myGraphics extends View {
        private int         W, H;                            //размеры canvas
        private Brick       brick1, brick2, brick3, brick4;  //сами блоки  и вспомогательные
        private double      minYTape, maxYTape;              //лента смены монитора
        private boolean     onTape = false;
        private boolean[]   onBrickPressed = {false, false}; //информация о блоках
        private Point       movingPoint;                     //точка для передвижения блоков
        private Car         car;
        private Button      invert, info, tap1, tap2, help, close;
        private Button[]    demo;
        private int         demo_state = 0;
        private Demo        demo_act, demo_act_down, demo_act_c, demo_act_down_c;
        private Tutorial    tutorial;
        int                 cur_tutorial = -1, move_dist = 0;
        private MyTime      timer;

        myGraphics(Context context) {
            super(context);
            init();
        }

        void init() {
            timer = null;
            Rect windowRect = getwindowRect();
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
            Config.BRICK_L = (int)brick_l;

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
            help = new Button(R.drawable.help, getResources(), windowRect, -2, -1, 5);
            close = new Button(R.drawable.close, getResources(), windowRect, 0.9, 0.055, 8);

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

            /* Туториалы */

            tutorial = new Tutorial(windowRect);
            Config.tutorial = tutorial;
            SharedPreferences pref = getPreferences(MODE_PRIVATE);
            cur_tutorial = (pref.getBoolean("FirstLaunch", true) ? 0 : cur_tutorial);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("FirstLaunch", false);
            editor.apply();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (timer != null) {
                timer.Refresh();
                if (cur_tutorial == 2 && timer.FromStartS > 3.0) {
                    ++cur_tutorial;
                    onBrickPressed[0] = onBrickPressed[1] = false;
                    timer = null;
                }
                else if (cur_tutorial == 5 && timer.FromStartS > 1.0) {
                    ++cur_tutorial;
                    timer = null;
                }
                else if ((cur_tutorial == 7 || cur_tutorial == 9) && timer.FromStartS > 4.0) {
                    ++cur_tutorial;
                    timer = null;
                }
                else if (cur_tutorial == 11 && timer.FromStartS > 2.0) {
                    ++cur_tutorial;
                    onBrickPressed[0] = onBrickPressed[1] = false;
                    timer = null;
                }
                else if (cur_tutorial == 16 && timer.FromStartS > 4.0) {
                    cur_tutorial = -1;
                    timer = null;
                }
            }

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
                car.response(brick1, true);
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
                car.response(brick2, false);
            }
            invalidate();
            car.draw(canvas);
            brick1.draw(canvas);
            brick2.draw(canvas);
            if (car.isPanelReversable()) {
                invert.draw(canvas);
            }
            info.draw(canvas);
            demo[demo_state].draw(canvas);
            if (cur_tutorial < 0) {
                help.draw(canvas);
                if (!brick1.isVisible())
                    tap1.animatedDraw(canvas, 8);
                if (!brick2.isVisible())
                    tap2.animatedDraw(canvas, 8);
            }
            else {
                canvas.drawARGB(200, 255, 255, 255);
                switch (cur_tutorial) {
                    case 0:
                        tap2.animatedDraw(canvas, 8);
                        break;
                    case 1:
                    case 2:
                        car.lower_net.draw(canvas);
                        car.drawPanel(canvas);
                        brick2.draw(canvas);
                        break;
                    case 3:
                    case 4:
                    case 5:
                        car.drawPanel(canvas);
                        car.drawNextPanel(canvas);
                        break;
                    case 7:
                    case 9:
                    case 11:
                        car.texture.draw(canvas);
                        car.lower_net.draw(canvas);
                        car.drawPanel(canvas);
                        brick2.draw(canvas);
                    case 6:
                    case 8:
                    case 10:
                        demo[demo_state].draw(canvas);
                        break;
                    case 12:
                        info.draw(canvas);
                        break;
                    case 16:
                        help.draw(canvas);
                }
                tutorial.draw(canvas, cur_tutorial);
                if (cur_tutorial < 16) {
                    close.draw(canvas);
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            double ex = event.getX();
            double ey = event.getY();

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    movingPoint = new Point(event);

                    if (ey >= minYTape && ey <= maxYTape && car.isPanelAvailable()) { //если нажали на ленту, то запомниаем это
                        if (cur_tutorial < 0 || cur_tutorial == 3 || cur_tutorial == 4) {
                            onTape = true;
                            if (cur_tutorial == 3) {
                                ++cur_tutorial;
                            }
                        }
                    }
                    else {
                        if ((cur_tutorial < 0 || cur_tutorial == 12) &&
                            info.onButtonTap(event)) {
                            Intent infoIntent = new Intent(getApplicationContext(), InfoActivity.class);
                            infoIntent.putExtra("sysType", car.cur_panel);
                            infoIntent.putExtra("curTutorial", cur_tutorial);
                            startActivityForResult(infoIntent, 0);

                            return true;
                        }
                        if (cur_tutorial < 0 &&
                            help.onButtonTap(event)) {
                            brick1.setVisible(false);
                            brick2.setVisible(false);
                            demo_state = 0;
                            car.curTex = 0;
                            cur_tutorial = 0;
                            return true;
                        }
                        if (cur_tutorial >= 0 && cur_tutorial < 16
                            && close.onButtonTap(event)) {
                            cur_tutorial = -1;
                            return true;
                        }
                        if (cur_tutorial < 0 &&
                            invert.onButtonTap(event) && car.isPanelAvailable() && car.isPanelReversable()) {
                            car.revertPanel();
                            return true;
                        }
                        if ((cur_tutorial < 0 || cur_tutorial == 6 ||
                                cur_tutorial == 8 || cur_tutorial == 10) &&
                            demo[demo_state].onButtonTap(event)) {
                            if (brick1.isVisible() || brick2.isVisible()) {
                                demo_state = (demo_state + 1) % 3;
                            }
                            if (cur_tutorial == 6 || cur_tutorial == 8 || cur_tutorial == 10) {
                                ++cur_tutorial;
                                timer = new MyTime();
                            }
                            return true;
                        }
                        if (new Brick(1, 1, new Point(event)).checkWithLines(car.getSupportLineDown(), false) ||
                             new Brick(1, 1, new Point(event)).checkWithLines(car.getSupportLineUp(), true)) {
                            if (brick1.isVisible() || brick2.isVisible()) {
                                if ((cur_tutorial < 0 || cur_tutorial == 0) &&
                                        brick1.isVisible() && ey > H / 2) {
                                    brick1.hide();
                                    brick2.setPos(new Point(ex - brick2.w / 2, ey - brick2.h / 2));
                                    brick4.setPos(new Point(ex - brick4.w / 2, ey - brick4.h / 2));
                                    onBrickPressed[1] = true;
                                    brick2.show();
                                    return true;
                                }
                                else if (cur_tutorial < 0 &&
                                        brick2.isVisible() && ey < H / 2) {
                                    brick2.hide();
                                    brick1.setPos(new Point(ex - brick1.w / 2, ey - brick1.h / 2));
                                    brick3.setPos(new Point(ex - brick3.w / 2, ey - brick3.h / 2));
                                    onBrickPressed[0] = true;
                                    brick1.show();
                                    return true;
                                }
                            }
                            else if ((cur_tutorial < 0 || cur_tutorial == 0) &&
                                     ((cur_tutorial < 0 && ey < H * 11 / 40.0) || ey > H * 29 / 40.0)) {
                                double first_floor = H / 2.0 * 0.75;
                                double sec_floor = (5 / 8.0) * H;
                                if (ey < first_floor) {
                                    brick1.setVisible(true);
                                }
                                if (event.getY() > sec_floor) {
                                    brick2.setVisible(true);
                                }
                                if (cur_tutorial == 0) {
                                    ++cur_tutorial;
                                    brick2.setPos(new Point((int)(W * 0.35) - brick2.w / 2, (int)(H * 0.88) - brick2.h / 2));
                                    brick4.setPos(new Point((int)(W * 0.35) - brick4.w / 2, (int)(H * 0.88) - brick4.h / 2));
                                    return true;
                                }
                            }
                            if (cur_tutorial < 0 &&
                                (brick1.inBrick(event) || brick3.inBrick(event))) {
                                onBrickPressed[0] = true;
                            }
                            if ((cur_tutorial < 0 || cur_tutorial == 1 ||
                                    cur_tutorial == 2 || cur_tutorial == 11) &&
                                (brick2.inBrick(event) || brick4.inBrick(event))) {
                                if (cur_tutorial == 1) {
                                    ++cur_tutorial;
                                    move_dist = 0;
                                }
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
                        move_dist += movingPoint.dist(new Point(event));
                        if (move_dist > W / 3 && cur_tutorial == 2 && timer == null) {
                            timer = new MyTime();
                        }
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
                        if (car.mvPanel() && cur_tutorial == 4) {
                            cur_tutorial = 5;
                            timer = new MyTime();
                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (orientation == ORIENTATION_PORTRAIT) {
            if (resultCode == 15) {
                ((myGraphics) view).cur_tutorial = 16;
                ((myGraphics) view).timer = new MyTime();
            } else {
                ((myGraphics) view).cur_tutorial = -1;
            }
        }
    }

    class myLandscape extends View {
        int H, W;
        Texture car;
        Button info, help, invert, tap1, tap2;
        Button[] demo;
        Panel panel;
        Obstacle[] obstacles;
        int cur_obstacle = 0, cur_demo = 0, cur_panel = 0;
        boolean obstacle_set = false, panel_captured = false;
        Point lastTouch;

        myLandscape(Context context) {
            super(context);
            init();
        }

        void init() {

            /* инициализация */

            Rect windowRect = getwindowRect();
            H = windowRect.height();
            W = windowRect.width();

            /* машина */

            Bitmap carBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.car_land);
            carBitmap = Bitmap.createScaledBitmap(carBitmap, (int)(W * 0.6), (int)(H * 0.4), true);
            car = new Texture(carBitmap, new Point((W - carBitmap.getWidth()) / 2, H * 5 / 9), 1);

            /* панель */

            panel = new panel1(W, H, W / 2, H / 4, H / 5, getResources(), false, false);

            /* кнопки */

            info = new Button(R.drawable.info, getResources(), windowRect, -1, -1, 9);
            help = new Button(R.drawable.help, getResources(), windowRect, -2, -1, 9);
            help.setActive(false);
            demo = new Button[2];
            demo[0] = new Button(R.drawable.button_demo, getResources(), windowRect,
                    -1, info.texture.h / H * 1.25, 9);
            demo[1] = new Button(R.drawable.button_manual, getResources(), windowRect,
                    -1, info.texture.h / H * 1.25, 9);
            invert = new Button(R.drawable.invert, getResources(), windowRect,
                    -2, help.texture.h / H * 1.25, 9);

            /* препятствия */

            Obstacle.setBounds((W - carBitmap.getWidth()) / 2,
                                (W + carBitmap.getWidth()) / 2, W);
            obstacles = new Obstacle[9];
            Bitmap[] bitmap = new Bitmap[1];
            Bitmap[] bitmap2 = new Bitmap[2];
            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.cart);
            obstacles[0] = new Obstacle(bitmap,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{true, true, false, false});
            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.human);
            obstacles[1] = new Obstacle(bitmap,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{false, true, true, false});
            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fence);
            obstacles[2] = new Obstacle(bitmap,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{true, true, true, true});
            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.ghost);
            obstacles[3] = new Obstacle(bitmap  ,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{false, false, false, false});
            bitmap2[0] = BitmapFactory.decodeResource(getResources(), R.drawable.car_l);
            bitmap2[1] = BitmapFactory.decodeResource(getResources(), R.drawable.car_r);
            obstacles[4] = new Obstacle(bitmap2,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{true, true, true, true});
            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bike);
            obstacles[5] = new Obstacle(bitmap,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{false, false, false, true});
            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.cow);
            obstacles[6] = new Obstacle(bitmap,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{true, true, true, true});
            bitmap2[0] = BitmapFactory.decodeResource(getResources(), R.drawable.child_l);
            bitmap2[1] = BitmapFactory.decodeResource(getResources(), R.drawable.child_r);
            obstacles[7] = new Obstacle(bitmap2,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{false, true, false, false});
            bitmap[0] = BitmapFactory.decodeResource(getResources(), R.drawable.dog);
            obstacles[8] = new Obstacle(bitmap,
                    new Point(0, H * 0.6), (int)(H * 0.33), new boolean[]{false, true, true, false});

            int th = (int)(H * 0.25);
            int tw = (int)(th / 1.276);

            tap1 = new Button(R.drawable.finger, getResources(), tw, th,
                    new Point((Obstacle.leftBound - tw) / 2, H * 0.7));
            tap2 = new Button(R.drawable.finger, getResources(), tw, th,
                    new Point((W + Obstacle.rightBound - tw) / 2, H * 0.7));
        }

        @Override
        public void onDraw(Canvas canvas) {
            panel.invert();
            panel.move();
            if (panel.getInvalidFlag()) {
                panel.setInvalidFlag(false);
                panel.switchReverse();
            }
            if (obstacle_set) {
                obstacles[cur_obstacle].animate(cur_obstacle == 7);
                obstacles[cur_obstacle].draw(canvas);
                panel.setPanel(obstacles[cur_obstacle].getDists(), Obstacle.xPos < W / 2);
            }
            if (!obstacle_set || Obstacle.xPos < Obstacle.rightBound) {
                tap2.animatedDraw(canvas, 8);
            }
            if (!obstacle_set || Obstacle.xPos > Obstacle.leftBound) {
                tap1.animatedDraw(canvas, 8);
            }
            car.draw(canvas);
            panel.draw(canvas);
            panel.drawNext(canvas);
            info.draw(canvas);
            help.draw(canvas);
            demo[cur_demo].draw(canvas);
            invert.draw(canvas);
            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Point curTouch = new Point(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (info.onButtonTap(event)) {
                        Intent infoIntent = new Intent(getApplicationContext(), InfoActivity.class);
                        infoIntent.putExtra("sysType", cur_panel);
                        infoIntent.putExtra("curTutorial", -1);
                        startActivity(infoIntent);
                    }
                    else if (invert.onButtonTap(event)) {
                        panel.setInvertFlag(true);
                    }
                    else if (obstacle_set && demo[cur_demo].onButtonTap(event)) {
                        cur_demo = (cur_demo + 1) % demo.length;
                        Obstacle.direction = cur_demo;
                    }
                    else if (obstacle_set && obstacles[cur_obstacle].onTap(event)) {
                        cur_obstacle = (cur_obstacle + 1) % obstacles.length;
                        obstacles[cur_obstacle].move(0);
                        if (cur_demo == 0) {
                            obstacles[cur_obstacle].setCaptured(true);
                        }
                    }
                    else if (curTouch.y > H / 2) {
                        if (curTouch.x < Obstacle.leftBound && (Obstacle.xPos > Obstacle.leftBound || !obstacle_set)) {
                        if (obstacle_set) {
                            obstacles[4].switchImage();
                        }
                            obstacle_set = true;
                            Obstacle.xPos = Obstacle.leftBound;
                            obstacles[cur_obstacle].move(0);
                            if (cur_demo == 0) {
                                obstacles[cur_obstacle].setCaptured(true);
                            }
                        }
                        else if (curTouch.x > Obstacle.rightBound && (Obstacle.xPos < Obstacle.rightBound || !obstacle_set)) {
                            obstacles[4].switchImage();
                            obstacle_set = true;
                            Obstacle.xPos = Obstacle.rightBound;
                            obstacles[cur_obstacle].move(0);
                            if (cur_demo == 0) {
                                obstacles[cur_obstacle].setCaptured(true);
                            }
                        }
                    }
                    else {
                        if (!panel.getInvertFlag() && !panel.getMoveFlag()) {
                            panel_captured = true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (obstacles[cur_obstacle].getCaptured()) {
                        obstacles[cur_obstacle].move(curTouch.x - lastTouch.x);
                    }
                    else if (panel_captured) {
                        panel.moveX(curTouch.x - lastTouch.x);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    obstacles[cur_obstacle].setCaptured(false);
                    panel_captured = false;
                    double xPos = panel.panel.xPos;
                    double tmpPos = (xPos < 0 ? panel.r_panel : panel.l_panel).pos.x + xPos;
                    if (Math.abs(panel.panel.xPos) > W / 3) {
                        if (panel.panel.xPos < 0) {
                            cur_panel = (cur_panel + 1) % 3;
                        } else {
                            cur_panel = (cur_panel + 2) % 3;
                        }
                        switch (cur_panel) {
                            case 0:
                                panel = new panel1(W, H, W / 2, H / 4, H / 5, getResources(), false, false);
                                break;
                            case 1:
                                panel = new panel2(W, H, getResources(), false);
                                break;
                            case 2:
                                panel = new panel3(W, H, getResources(), false);
                        }
                        panel.panel.xPos = tmpPos - panel.panel.pos.x;
                        invert.setActive(panel.reversible);
                    }
                    panel.setMoveFlag(true);
                    break;
                default:
                    break;
            }
            lastTouch = curTouch;
            return true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}
