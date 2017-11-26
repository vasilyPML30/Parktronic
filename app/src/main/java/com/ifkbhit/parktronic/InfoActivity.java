package com.ifkbhit.parktronic;

import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import android.widget.Button;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED;
import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;

public class InfoActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private GestureDetectorCompat mDetector;
    private int sysType;
    private View[] layouts;
    private int[] titles = {R.string.info_title_216, R.string.info_title_218, R.string.info_title_277};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setRequestedOrientation(SCREEN_ORIENTATION_LOCKED);
        sysType = getIntent().getIntExtra("sysType", 0);
        layouts = new View[] {
                findViewById(R.id.inc_216),
                findViewById(R.id.inc_218),
                findViewById(R.id.inc_277)
        };
        for (View l : layouts) {
            l.setAlpha(0);
        }
        layouts[sysType].setAlpha(1);
        int cur_tutorial = getIntent().getIntExtra("curTutorial", -1);
        if (cur_tutorial == 12) {
            ++cur_tutorial;
            findViewById(R.id.fab).setEnabled(false);
            findViewById(R.id.more_button).setEnabled(false);
        }
        else {
            findViewById(R.id.close).setVisibility(View.GONE);
        }
        ((InfoGraphics)findViewById(R.id.info_graphics)).cur_tutorial = cur_tutorial;
        setTitle(getString(titles[sysType]));

        android.widget.Button fab = (android.widget.Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("sysType", sysType);
                setResult(((InfoGraphics)findViewById(R.id.info_graphics)).cur_tutorial, intent);
                finish();
            }
        });
        View.OnClickListener link = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.aviline.ru");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        };
        (findViewById(R.id.site_image_1)).setOnClickListener(link);
        (findViewById(R.id.site_image_2)).setOnClickListener(link);
        (findViewById(R.id.site_image_3)).setOnClickListener(link);

        android.widget.Button more = (android.widget.Button) findViewById(R.id.more_button);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((android.widget.Button)view).getText() != "Скрыть") {
                    if (((InfoGraphics)findViewById(R.id.info_graphics)).cur_tutorial == 14) {
                        ++((InfoGraphics)findViewById(R.id.info_graphics)).cur_tutorial;
                        findViewById(R.id.fab).setEnabled(true);
                        findViewById(R.id.more_button).setEnabled(false);
                    }
                    ((android.widget.Button)view).setText("Скрыть");
                    if (sysType == 0) {
                        ((TextView) findViewById(R.id.text_216)).setText(getString(R.string.full_216));
                    } else if (sysType == 1) {
                        ((TextView) findViewById(R.id.text_218)).setText(getString(R.string.full_218));
                    } else {
                        ((TextView) findViewById(R.id.text_277)).setText(getString(R.string.full_277));
                    }
                }
                else {
                    ((android.widget.Button)view).setText("Подробнее");
                    if (sysType == 0) {
                        ((TextView) findViewById(R.id.text_216)).setText(getString(R.string.short_216));
                    } else if (sysType == 1) {
                        ((TextView) findViewById(R.id.text_218)).setText(getString(R.string.short_218));
                    } else {
                        ((TextView) findViewById(R.id.text_277)).setText(getString(R.string.short_277));
                    }
                }
            }
        });
        mDetector = new GestureDetectorCompat(this, this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((InfoGraphics)findViewById(R.id.info_graphics)).init();
    }

    @Override
    public void onBackPressed() {
        if (((InfoGraphics)findViewById(R.id.info_graphics)).cur_tutorial < 0) {
            setResult(-1);
            Intent intent = new Intent();
            intent.putExtra("sysType", sysType);
            setResult(((InfoGraphics)findViewById(R.id.info_graphics)).cur_tutorial, intent);
            super.onBackPressed();
        }
    }
    public void close(View view) {
        ((InfoGraphics)findViewById(R.id.info_graphics)).cur_tutorial = -1;
        findViewById(R.id.fab).setEnabled(true);
        findViewById(R.id.more_button).setEnabled(true);
        findViewById(R.id.inc_216).setAlpha(1.0f);
        findViewById(R.id.inc_218).setAlpha(1.0f);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distX, float distY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        final float distX = motionEvent1.getX() - motionEvent.getX();
        if (Math.abs(distX) <= 300) {
            return false;
        }
        if (((Button)findViewById(R.id.more_button)).getText() == "Скрыть") {
            ((Button)findViewById(R.id.more_button)).performClick();
        }
        final View curLayout = layouts[sysType];
        curLayout.animate().alpha(0).translationX((distX > 300 ? 300 : -300)).withEndAction(new Runnable() {
            @Override
            public void run() {
                curLayout.setX(0);
            }
        });
        sysType = (sysType + (distX > 300 ? 2 : 1)) % 3;
        setTitle(titles[sysType]);
        final View nextLayout = layouts[sysType];
        nextLayout.animate().alpha(1);
        return false;
    }
}
