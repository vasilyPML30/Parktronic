package com.ifkbhit.parktronic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        setContentView(R.layout.activity_launch);
        Animation animationTitle = AnimationUtils.loadAnimation(this, R.anim.intro_text_anim);
        Animation animationSlogan = AnimationUtils.loadAnimation(this, R.anim.intro_slogan_anim);
        animationSlogan.setAnimationListener(listener);
        TextView intro = (TextView)findViewById(R.id.intro_text),
                slogan = (TextView)findViewById(R.id.intro_slogan);
        if(Config.DEBUG_MOD) {
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
