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
    public int bossSize = 6;
    protected long counter = 0;
    protected int bossHeight = 400;
    protected int bossWidth = 200;
    protected HeroEntity hero;
    public final static int FIRST_BOSS = 1;
    public final static int NO_BOSS = 0;
    protected float frameVariance = 1.0f;
    protected boolean isActive = false;
    protected boolean isDead = false;
    protected boolean playingIntro = false;
    protected boolean startDeathAnim = true;
    protected int bossScript = 0;
    protected boolean isDying = false;

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
    /**
     *
     * @param context
     * @param e
     * @param ea
     * @param xPos
     * @param yPos
     * @param bodyBounds
     */

    public void initBoss(HeroEntity hero, Context context, EnemyEntity[] e, boolean[] ea, float xPos, float yPos, int objectCount, float[][] bodyBounds){
        this.enemyList = e;
        this.enemyActive = ea;
        this.hero = hero;

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

    public void updateBoss(float HX, float HY, float _viewX, float _viewY, float frameVariance){
        this.frameVariance = frameVariance;

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
    public boolean isDying(){
        return isDying;
    }
    public void draw(Canvas canvas){


    }
    public void playDeath(float viewX, float viewY){



    }
    public void hit(int sectionHit, int hitStrength){

    }
    public int getBossText(){
        return bossScript;
    }
    public boolean isStartDeathAnim(){
        return startDeathAnim;
    }
}
