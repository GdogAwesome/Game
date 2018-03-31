package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by BradMobile on 1/2/2017.
 */

public class BossEntity {
    protected Context context;
    protected Bitmap bossImage;
    protected Paint paint = new Paint();
    public int bossSize = 6;
    protected long counter = 0;
    protected int bossHeight = 400;
    protected int bossWidth = 200;

    public final static int FIRST_BOSS = 1;
    public final static int NO_BOSS = 0;
    protected boolean isActive = false;
    protected boolean isDead = false;
    protected boolean playingIntro = false;
    protected int bossScript = 0;

    protected EnemyEntity[] enemyList;
    protected boolean[] enemyActive;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void BossEntity(){

    }
    public void initBoss(Context context, EnemyEntity[] e, boolean[] ea, float xPos){
        this.enemyList = e;
        this.enemyActive = ea;

        isActive = true;
        playingIntro = true;

        /*
        switch(bType){
            case BossEntity.FIRST_BOSS:
                this = new FirstBoss();


        }
        */

    }
    public EnemyEntity[] getEnemies(){
        return enemyList;
    }
    public boolean[] getEnemyActive(){
        return enemyActive;
    }

    public void updateBoss(float HX, float HY, float MPosX){

    }
    public int getBossSize(){
        return bossSize;
    }
    public boolean isActive(){

        return isActive;
    }
    public boolean isDead(){
        return isDead;
    }
    public void draw(Canvas canvas){


    }
    public void hit(int sectionHit, int hitStrength){

    }
    public int getBossText(){
        return bossScript;
    }
}
