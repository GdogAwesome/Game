package com.example.bradmobile.testtexture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LevelSelect extends AppCompatActivity implements View.OnClickListener {

    public Button[] keys = new Button[11];
    public int totalLevels = 99;
    public String selection = "first";
    public boolean[] unlocked = new boolean[totalLevels];
    public int[] currentLevels = new int[9];
    public int[] allLevels = new int[totalLevels];
    public Drawable img = null;
    public Bitmap background = null;
    LinearLayout layout;

    public Bitmap bri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpBackground();
        int v = Build.VERSION.SDK_INT;
/*
        layout = findViewById(R.id.level_select);
         if (v >= 16) {
         layout.setBackground(img);
          } else {
        layout.setBackgroundResource(R.drawable.futurecity);

        }
        */


        setContentView(R.layout.activity_level_select);
        unlocked[0] = true;
        unlocked[1] = true;
        unlocked[2] = true;
        img = this.getResources().getDrawable(R.drawable.lock);
        img.setBounds(0, 0, 60, 60);
        findKeys();
        setUpLevels();
        updateCurrentLevels(selection);
    }

    public void setUpLevels() {
        for (int i = 0; i < totalLevels; i++) {
            allLevels[i] = i + 1;

        }
    }

    public void updateCurrentLevels(String selection) {
        switch (selection) {
            case "first":
                for (int i = 0; i < 9; i++) {
                    currentLevels[i] = i;

                }
                updateKeys();
                break;
            case "next":
                currentLevels[0] += 9;
                for (int i = 1; i < 9; i++) {
                    currentLevels[i] = currentLevels[i - 1] + 1;
                }
                updateKeys();
                break;
            case "prev":
                currentLevels[0] -= 9;
                for (int i = 1; i < 9; i++) {
                    currentLevels[i] = currentLevels[i - 1] + 1;
                }
                updateKeys();
                break;


        }
        if (currentLevels[0] <= allLevels[0]) {
            keys[9].setClickable(false);
        } else {
            keys[9].setClickable(true);
        }
        if (currentLevels[8] >= allLevels[totalLevels - 1]) {
            keys[10].setClickable(false);
        } else {
            keys[10].setClickable(true);
        }

    }

    public void updateKeys() {
        for (int i = 0; i < 9; i++) {
            if (unlocked[currentLevels[i]]) {
                keys[i].setText(Integer.toString(allLevels[currentLevels[i]]));
                keys[i].setCompoundDrawables(null, null, null, null);
                keys[i].setClickable(true);

            } else {

                keys[i].setText(Integer.toString(allLevels[currentLevels[i]]));
                keys[i].setCompoundDrawables(img, null, null, null);
                keys[i].setClickable(false);
            }
        }
    }


    public void findKeys() {
        /*
        for(int i = 0; i < 11;  i++){

        }
        */
        keys[0] = findViewById(R.id.btn_1);
        keys[1] = findViewById(R.id.btn_2);
        keys[2] = findViewById(R.id.btn_3);
        keys[3] = findViewById(R.id.btn_4);
        keys[4] = findViewById(R.id.btn_5);
        keys[5] = findViewById(R.id.btn_6);
        keys[6] = findViewById(R.id.btn_7);
        keys[7] = findViewById(R.id.btn_8);
        keys[8] = findViewById(R.id.btn_9);
        keys[9] = findViewById(R.id.btn_prev);
        keys[10] = findViewById(R.id.btn_next);
        for (int i = 0; i < 11; i++) {
            keys[i].setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                updateCurrentLevels("next");
                break;
            case R.id.btn_prev:
                updateCurrentLevels("prev");
                break;
            case R.id.btn_1:
                queueNewLevel(allLevels[currentLevels[0]]);
                break;
            case R.id.btn_2:
                queueNewLevel(allLevels[currentLevels[1]]);
                break;
            case R.id.btn_3:
                queueNewLevel(allLevels[currentLevels[2]]);
                break;


        }

    }

    public void setUpBackground() {

    }

    public void queueNewLevel(int level) {
        Intent i = new Intent(this, Game.class);
        i.putExtra("selection", "cont");
        i.putExtra("mapNo", level);
        startActivity(i);
        finish();


    }

    public void onResume() {
        super.onResume();
        selection = "first";
        unlocked[0] = true;
        unlocked[1] = true;
        findKeys();
        setUpLevels();
        updateCurrentLevels(selection);

       // background = BitmapFactory.decodeResource(this.getResources(), R.drawable.futurecity);


    }

    @Override
    public void onPause() {
        super.onPause();
        // onTrimMemory(TRIM_MEMORY_UI_HIDDEN);


    }
}
