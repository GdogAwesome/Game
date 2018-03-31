package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import com.example.bradmobile.testtexture.AnimationUtils.*;

/**
 * Created by BradMobile on 1/6/2017.
 */



public class FirstBoss extends BossEntity {

    private float viewX;
    private float xDistance = 0;// -50f * Constants.scale;
    private boolean swingingDown = true;
    private boolean swinging = true;
    private boolean movingLeft = true;
    private int scaledBossHeight = 0;

    private long lastMoveChange = 0;
    private float moveDistance = .01f;
    private long lastSwing = 0;
    private float swingSpeed = .01f;


    private int linkWidth = (int)(192 * Constants.scale);// used to be 200
    FirstBoss(){
        super();


        playingIntro = true;
        scaledBossHeight = (int)(bossHeight * Constants.scale);// (int)(Constants.SCREEN_HEIGHT * .75);
        bossWidth = (int)(bossHeight * .75);
        bossScript = 3;

    }

    @Override
    public void updateBoss(float HX, float HY, float MPosX){


        if(!enemyList[4].isDying()) {


            //swing counter
            counter++;
            if (counter  >= 200) {
                lastSwing = counter;
                swinging = true;
                counter = 0;

            }
            //move left and right based on position
            if (enemyList[4].getAbsoluteX() <= .5f) {
                lastMoveChange = counter;
                movingLeft = false;
                if(playingIntro){
                    playingIntro = false;
                }
            }else if(enemyList[4].getAbsoluteX() >= 1.0f){
                movingLeft = true;
            }

            if (swinging) {
                if (swingingDown) {
                    //if(enemyList[0].getHeight())
                    enemyList[0].moveDirect(linkX(enemyList[4].getX(), enemyList[0].getWidth(), enemyList[0].getX()), -(swingSpeed * 6));
                    if ((enemyList[0].getY() + enemyList[0].getHeight()) <= -.8f) {
                        swingingDown = false;

                    }

                } else if (!swingingDown) {
                    enemyList[0].moveDirect(linkX(enemyList[4].getX(), enemyList[0].getWidth(), enemyList[0].getX()), swingSpeed * 2);
                    if ((enemyList[0].getY()) >= enemyList[4].getY() + .8f) {
                        swingingDown = true;
                        swinging = false;

                    }
                }
            } else {
                enemyList[0].moveDirect(linkX(enemyList[4].getX(), enemyList[0].getWidth(), enemyList[0].getX()), 0);
            }

            if (movingLeft) {
                enemyList[4].moveDirectWOAnim(-moveDistance, 0);
            } else {
                enemyList[4].moveDirectWOAnim(moveDistance, 0);
            }

            enemyList[0].updateView(viewX);
            enemyList[4].updateView(viewX);
            for (int i = 1; i < 4; i++) {//enemyActive.length; i++){

                if (enemyActive[i]) {
                    enemyList[i].updateView(viewX);
                    enemyList[i].updateRotation(enemyList[i - 1].getX(), enemyList[i - 1].getY());
                    enemyList[i].moveDirect(linkX(enemyList[i - 1].getX(), (enemyList[i].getWidth() * .75f), enemyList[i].getX()), followY(enemyList[i - 1].getY(), enemyList[i - 1].getHeight(), enemyList[i].getY(), enemyList[i].getHeight()));

                }
            }

            for (int i = 0; i < bossSize; i++) {

                if (enemyActive[i]) {

                    //if((enemyList[i].getX() + ))
                }
            }
        }else if(enemyList[4].isDying()){

            //TODO implement dying animation here
            isActive = false;
            isDead = true;
            Log.e("boss ", "is dead");


        }

    }


    @Override
    public void initBoss(Context context, EnemyEntity[] e, boolean[] ea, float xPos){
        bossSize = 5;
        //Log.e("Scale", Float.toString(Constants.scale));
        this.viewX = xPos;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        //bossImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.test_electricity, options);

        this.enemyList = e;
        this.enemyActive = ea;


        enemyList[4] = new EnemyEntity(xPos + 1.5f, -.4f, EnemyContainer.BOSS_ONE_BODY);
        enemyList[4].InitEnemy(1, 1);
        enemyList[4].loadAnims(new int[]{Anim.STANDING}, new int[]{1}, new int[]{0});

        enemyList[0] = new EnemyEntity(0f, 0f, EnemyContainer.BOSS_ONE_ARM);
        enemyList[0].InitEnemy(5,3);
        enemyList[0].loadAnims(new int[]{Anim.STANDING}, new int[]{12}, new int[]{0});


        for(int i = 1; i < 4; i ++){// enemyActive.length ; i++){
            //if(enemyActive[i]){
                enemyList[i] = new EnemyEntity(0f, 0f, EnemyContainer.BOSS_ONE_ARM);
                enemyList[i].InitEnemy(5,3);
                enemyList[i].loadAnims(new int[]{Anim.STANDING}, new int[]{12}, new int[]{0});
            //}
        }
        for(int i = 0; i< enemyList.length; i++){
            if(i < 5) {
                enemyActive[i] = true;
            }else if(i >= 5){
                enemyActive[i] = false;
            }

        }


    }
    public float followY(float yPos, float height, float yPosFollow, float heightFollow){
        float halfLead = yPos + (height / 2);
        float halfFollow = yPosFollow + (heightFollow / 2);
        float halfDistance = ( -1*(( halfFollow - halfLead) * .1f));// .075
/*
        if( (halfFollow - halfLead) > 4 ){
            return (-swingSpeed);
        }else if((halfFollow - halfLead) < 4 ){
            return  (swingSpeed );
        }else{
            return 0;
        }
        */
        return halfDistance;


    }

    /**
     *
     * @param xLead
     * @param followW
     * @return
     */
    public float linkX(float xLead, float followW, float followX){

        float dif = xLead - (followX +followW + xDistance);
        return dif;
    }

    @Override
    public void hit(int sectionHit, int hitStrength){
        enemyList[sectionHit].hit(hitStrength);

    }
}
