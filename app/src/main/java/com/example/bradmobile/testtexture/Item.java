package com.example.bradmobile.testtexture;

import android.graphics.Rect;

/**
 * Created by BradMobile on 10/8/2016.
 */


public class Item {

    public static final int HEALTH_BAR = 6;
    public static final int HEALTH_HUD = 5;
    public static final int WEAPON_UPGRADE_SPRAY = 1;
    public static final int WEAPON_UPGRADE_FLAME = 7;
    public static final int FLAME_SHOT = 8;
    public static final int HEALTH_UPGRADE = 2;
    public static final int POINTS_UPGRADE = 3;
    public static final int DEFAULT_VALUE = 4;
    public static final int BLUE_EXPLOSION = 9;
    public static final int YELLOW_EXPLOSION = 10;
    public static final int EXPLOSION = 11;
    public static final int BOMB = 12;

    public boolean droping = true;
    private int upgradeTime = 50;
    private int uType = 0;
    public boolean active = false;
    private int timer = 0;
    private int time = 0;
    private int totalTime = 10;
    private float[] drawStats = new float[4];
    public float posX = 0;
    public float posY = 0;
    public float g = 0;
    private int width = (int)(Constants.CHARACTER_WIDTH / 5);
    private int height = width/2 ;

    public float drawX = 0;
    public float drawY = 0;

    /*

        @Param set upgrade type
     */
    public Item(){

        time = 0;
        droping = true;
        active = true;



    }
    public void initItem(float x, float y, int upgradeType, float ground){

        //TODO set upgrade time and if item is linked here
        this.uType = upgradeType;
        this.g = ground;
        this.posX = x;
        this.posY = y;
        time = 0;
        droping = true;
        active = true;


    }

    public void update(float viewX, float viewY){
        drawX = posX - viewX;
        drawY = posY + viewY;
        if(droping){
            if((posY ) >= g){
                posY -= .044444f;
            }else{
                droping = false;
            }

        }
        timer ++;
        if(timer > 90){
            time ++;
            if(time >= totalTime){
                active = false;
            }
            timer = 0;
        }

    }

    public float[] getDrawStats(){
        drawStats[0] = drawX;
        drawStats[1] = drawY;
        drawStats[2] = drawX + width;
        drawStats[3] = drawY + height;

        return drawStats;
    }


    public int getUType(){
        return uType;

    }
    public int getUTime(){
        return upgradeTime;
    }
}
