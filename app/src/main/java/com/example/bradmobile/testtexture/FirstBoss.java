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
    private float viewY;

    private float xDistance = 0;// -50f * Constants.scale;
    private boolean swingingDown = true;
    private boolean swinging = true;
    private boolean movingLeft = true;
    private boolean cueAttack = true;

    private int scaledBossHeight = 0;
    private float circleOriginX = 0.5f;
    private float circleOriginY = 0.0f;
    private float LeadX ;
    private float LeadY;
    private float angle = 0.0f;
    private float preAngle = 0.0f;
    private double sinOfCircle;
    private double cosOfCircle;

    private long lastMoveChange = 0;
    private float moveDistance = .01f;
    private long lastSwing = 0;
    private float swingSpeed = .01f;

    FirstBoss(){
        super();


        playingIntro = true;
        scaledBossHeight = (int)(bossHeight * Constants.scale);// (int)(Constants.SCREEN_HEIGHT * .75);
        bossWidth = (int)(bossHeight * .75);
        bossScript = 3;

    }

    @Override
    public void updateBoss(float HX, float HY, float _viewX, float _viewY){

        viewX = _viewX;
        viewY = _viewY;

        if(preAngle < 360.0f){
            preAngle += .06f;
        }else{
            preAngle = 0.0f;
        }
        angle = preAngle - 180.0f;
        //Log.e("angle", Float.toString(angle));

        sinOfCircle = Math.sin(angle);
        cosOfCircle = Math.cos(angle);


        enemyList[0].updateView(viewX,viewY);
        enemyList[4].updateView(viewX,viewY);

        if(!enemyList[4].isDying()) {


            //swing counter
            counter++;
            if (counter  >= 400) {
                lastSwing = counter;
                swinging = true;
                counter = 0;

            }
            //move left and right based on position
            if (enemyList[4].getAbsoluteX() <= .25f) {
                lastMoveChange = counter;
                movingLeft = false;
                if(playingIntro){
                    playingIntro = false;
                }
            }else if(enemyList[4].getAbsoluteX() >= 1.40f){
                movingLeft = true;
            }


            if (swinging) {
                if (swingingDown) {
                    //if(enemyList[0].getHeight())
                    if(cueAttack){
                        LeadX = hero.getCenter() + viewX;
                        LeadY = 1.2f;
                        enemyList[4].animHandler.Attack1();
                        cueAttack = false;
                    }
                    LeadY -= swingSpeed * 4;

                    enemyList[0].followLooseXY(LeadX,LeadY,.1f, .1f);
                    //enemyList[0].moveDirect(linkX(LeadX, enemyList[0].getWidth(), enemyList[0].getX()), -(swingSpeed * 6));
                    if ((enemyList[0].getY() + enemyList[0].getHeight()) <= -.8f) {
                        swingingDown = false;

                    }

                } else if (!swingingDown) {
                    LeadY += swingSpeed * 2;
                    LeadX =  enemyList[4].getAbsoluteX()+((float)cosOfCircle * 0.6f) + _viewX;

                    enemyList[0].followLooseXY(LeadX,LeadY,.1f, .5f);
                    //enemyList[0].moveDirect(linkX(LeadX, enemyList[0].getWidth(), enemyList[0].getX()), swingSpeed * 2);
                    if(!cueAttack){
                        cueAttack = true;

                    }



                    if ((enemyList[0].getY()) >= (enemyList[4].getAbsoluteY()+((float)sinOfCircle * 0.6f) + _viewY)) {
                        swingingDown = true;
                        swinging = false;
                        enemyList[4].animHandler.stop();
                    }
                }
            } else {
                LeadX =  enemyList[4].getAbsoluteX()+((float)cosOfCircle * 0.6f) + _viewX;
                LeadY = enemyList[4].getAbsoluteY()+((float)sinOfCircle * 0.6f) + _viewY;

                enemyList[0].followLooseXY(LeadX,LeadY,.2f, .2f);

            }


                if (movingLeft) {
                    enemyList[4].moveDirectWOAnim(-moveDistance, 0);
                } else {
                    //enemyList[4].moveDirectWOAnim(moveDistance, 0);
                }



            for (int i = 1; i < 4; i++) {//enemyActive.length; i++){

                if (enemyActive[i]) {
                    enemyList[i].updateView(viewX, viewY);
                    enemyList[i].followDirectXY(enemyList[i-1].getX(), enemyList[i-1].getY(), .22f, .22f);

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


    /**
     *
     * @param context
     * @param e
     * @param ea
     * @param xPos
     * @param yPos
     * @param bodyBounds
     */

    @Override
    public void initBoss(HeroEntity hero, Context context, EnemyEntity[] e, boolean[] ea, float xPos, float yPos, int objectCount, float[][] bodyBounds){
        bossSize = 5;
        this.hero = hero;
        this.viewX = xPos;
        this.viewY = yPos;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        bodyBounds[0][1] *= .8f;
        bodyBounds[0][0] *= .25f;
        bodyBounds[0][2] *= .25f;

        bodyBounds[1][1] *= .5f;
        bodyBounds[1][0] *= .3f;
        bodyBounds[1][3] *= .5f;
        bodyBounds[1][2] *= .3f;

        //bossImage = BitmapFactory.decodeResource(context.getResources(),R.drawable.test_electricity, options);

        this.enemyList = e;
        this.enemyActive = ea;


        enemyList[4] = new EnemyEntity(xPos + 1.5f, yPos -.45f, EnemyContainer.BOSS_ONE_BODY);
        enemyList[4].InitEnemy(1, 1, bodyBounds[0]);
        enemyList[4].loadAnims(new int[]{Anim.ATTACK_1,Anim.STANDING}, new int[]{8,4}, new int[]{0,8}, new int[]{4, 7}, new boolean[]{false, false}, new boolean[]{false,true}, new boolean[]{false, true});

        enemyList[0] = new EnemyEntity(enemyList[4].getAbsoluteX(), enemyList[4].getAbsoluteY() + 1.5f, EnemyContainer.BOSS_ONE_ARM);
        enemyList[0].InitEnemy(5,3, bodyBounds[1]);
        enemyList[0].loadAnims(new int[]{Anim.STANDING}, new int[]{12}, new int[]{0}, new int[]{5}, new boolean[]{true}, new boolean[]{false},new boolean[]{true});


        for(int i = 1; i < 4; i ++){// enemyActive.length ; i++){
            //if(enemyActive[i]){
                enemyList[i] = new EnemyEntity(0f, 0f, EnemyContainer.BOSS_ONE_ARM);
                enemyList[i].InitEnemy(5,3,  bodyBounds[1]);
                enemyList[i].loadAnims(new int[]{Anim.STANDING}, new int[]{12}, new int[]{0}, new int[]{5}, new boolean[]{true}, new boolean[]{false},new boolean[]{true});
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
