package com.ifkbhit.parktronic;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import android.widget.Button;

public class ActivityInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_info2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getActionBar();
        toolbar.setTitleTextColor(Color.BLACK);

        android.widget.Button fab = (android.widget.Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ImageView site = (ImageView)findViewById(R.id.site_image);
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.aviline.ru");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        android.widget.Button more = (android.widget.Button) findViewById(R.id.more_button);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((android.widget.Button) view).getText() != "Скрыть") {
                    ((android.widget.Button) view).setText("Скрыть");
                    ((TextView)findViewById(R.id.info_text)).setText(getString(R.string.full_216));
                }
                else {
                    ((android.widget.Button) view).setText("Подробнее");
                    ((TextView)findViewById(R.id.info_text)).setText(getString(R.string.short_216));
                }
            }
        });
    }

}
