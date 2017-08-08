package com.ifkbhit.parktronic;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

public class ActivityInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);

        final int sysType = getIntent().getIntExtra("sysType", 0);
        findViewById(R.id.inc_216).setVisibility(sysType == 0 ? View.VISIBLE : View.GONE);
        findViewById(R.id.inc_218).setVisibility(sysType == 1 ? View.VISIBLE : View.GONE);

        setTitle(getString(sysType == 0 ? R.string.info_title_216 : R.string.info_title_218));

        android.widget.Button fab = (android.widget.Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        android.widget.Button more = (android.widget.Button) findViewById(R.id.more_button);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((android.widget.Button)view).getText() != "Скрыть") {
                    ((android.widget.Button)view).setText("Скрыть");
                    if (sysType == 0) {
                        ((TextView) findViewById(R.id.text_216)).setText(getString(R.string.full_216));
                    }
                    else if (sysType == 1) {
                        ((TextView) findViewById(R.id.text_218)).setText(getString(R.string.full_218));
                    }
                }
                else {
                    ((android.widget.Button)view).setText("Подробнее");
                    if (sysType == 0) {
                        ((TextView) findViewById(R.id.text_216)).setText(getString(R.string.short_216));
                    }
                    else if (sysType == 1) {
                        ((TextView) findViewById(R.id.text_218)).setText(getString(R.string.short_218));
                    }
                }
            }
        });
    }

}
