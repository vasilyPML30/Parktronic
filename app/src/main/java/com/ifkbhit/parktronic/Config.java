package com.ifkbhit.parktronic;


import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Config {

    public static boolean DEBUG_MOD = false;
    public static final double CAR_W = 612, CAR_H = 1296;
    public static final double PANEL_W = 600, PANEL_H = 178;
    public static final double CAR_Y_OFFSET_K = 0.1;
    public static double CURRENT_CAR_K = 1;
    private static int i = 0;
    public static int BRICK_L;

    public static final Point[][] UPPER_POINTS = {
        {new Point(35, 102), new Point(159, 28), new Point(299, 6), new Point(438, 27), new Point(563, 102)},
        {new Point(74, 156), new Point(182, 100), new Point(299, 83), new Point(413, 100), new Point(523, 165)},
        {new Point(118, 214), new Point(205, 172), new Point(299, 157), new Point(389, 171), new Point(479, 214)},
        {new Point(163, 273), new Point(225, 235), new Point(299, 226), new Point(369, 231), new Point(435, 273)}
    };

    public static final Point[][] LOWER_POINTS = {
        {new Point(6, 1157), new Point(141, 1261), new Point(305, 1286), new Point(469, 1261), new Point(603, 1157)},
        {new Point(40, 1110), new Point(165, 1186), new Point(305, 1208), new Point(444, 1186), new Point(569, 1110)},
        {new Point(80, 1054), new Point(188, 1111), new Point(305, 1129), new Point(418, 1111), new Point(529, 1055)},
        {new Point(123, 995), new Point(210, 1039), new Point(305, 1054), new Point(395, 1044), new Point(485, 996)},
        {new Point(149, 960), new Point(226, 987), new Point(305, 992), new Point(377, 986), new Point(457, 957)}
    };
    public static double supportBrickScale = 2.5;

    static double destinationAB(Point A, Point B, boolean isVector) {
        return ((A.x > B.x) && isVector? -1: 1) *  Math.sqrt((A.x - B. x) * (A.x - B. x) + (A.y - B.y)*(A.y - B.y));
    }

    public static double roundResult(double d, int n) {
        return new BigDecimal(d).setScale(n, RoundingMode.UP).doubleValue();
    }

    public static Bitmap reverseBitmap(Bitmap b) {
        Matrix m = new Matrix();
        m.preScale(1, -1);
        Bitmap tmp = Bitmap.createBitmap(b, 0,0, b.getWidth(), b.getHeight(), m, true);
        return tmp;
    }
}
