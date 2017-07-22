package com.ifkbhit.parktronic;

/**
 * Created by vasyoid on 22.07.17.
 */

public class MyTime {
    long Start, Cur, Last, Delta, FromStart, NanoToMilli = (long)1e6;
    float StartS, CurS, LastS, DeltaS, FromStartS, MilliToSec = 1e3f;
    MyTime() {
        Restart();
    }
    void Refresh() {
        Cur = System.nanoTime() / NanoToMilli;
        CurS = Cur / MilliToSec;
        Delta = Cur - Last;
        DeltaS = Delta / MilliToSec;
        FromStart = Cur - Start;
        FromStartS = FromStart / MilliToSec;
        Last = Cur;
        LastS = Last / MilliToSec;
    }
    void Restart() {
        Start = Last = System.nanoTime() / NanoToMilli;
        StartS = LastS = Start / MilliToSec;
        FromStart = 0;
        FromStartS = 0f;
    }
}
