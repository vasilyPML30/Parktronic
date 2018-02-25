package com.ifkbhit.parktronic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class LaunchActivity extends Activity {


    Animation.AnimationListener listener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            try {
                Thread.sleep(500);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        setContentView(R.layout.activity_launch);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        } else {
            if (getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
                setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
            }
            else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        Animation animationTitle = AnimationUtils.loadAnimation(this, R.anim.intro_image_anim);
        Animation animationSlogan = AnimationUtils.loadAnimation(this, R.anim.intro_slogan_anim);
        animationSlogan.setAnimationListener(listener);
        ImageView intro = (ImageView)findViewById(R.id.intro_image);
        ImageView slogan = (ImageView)findViewById(R.id.intro_slogan);
        if (Config.DEBUG_MOD) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        intro.startAnimation(animationTitle);
        slogan.startAnimation(animationSlogan);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}
