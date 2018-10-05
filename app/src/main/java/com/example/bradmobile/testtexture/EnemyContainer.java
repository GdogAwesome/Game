package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;


/**
 * Created by BradMobile on 8/7/2016.
 */
public class EnemyContainer extends Entity {
    private FlyingEnemy enemy;
    private EnemyQueue eQueue;
    ShotEntity shots;
    BossEntity boss;
    Bitmap enemyImage;
    boolean bossActive = false;
    private boolean isBossDead = false;
    private float mapPosX = 0f;
    HeroEntity hero;

    public static final int MAX_ENEMIES = 50;
    private EnemyEntity[] enemyList = new EnemyEntity[MAX_ENEMIES];
    private boolean[] enemyActive = new boolean[MAX_ENEMIES];
    public final static int FLYING_SHIP = 0;
    public final static int ELECTRIC_SHIP = 1;
    public final static int BOSS_ONE_BODY = 2;
    public final static int BOSS_ONE_ARM = 3;
    float remainder = 0.0f;

    private float screenWidth = 1.2f;

    public EnemyContainer(Context context, HeroEntity hero, int bossType, int mapNo){

        super();

        this.hero = hero;


        //enemyImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship, options);
        //enemyImage = Bitmap.createScaledBitmap(enemyImage, 800, 800, false);

        float flyingEnemyHeight =  .8f;

        eQueue = new EnemyQueue(enemyList, enemyActive);
        initEnemyTypes(context, bossType);
        eQueue.initQueue(mapNo);



    }

    public void updateEnemies(float heroX, float heroY, float mapPosX, float mapPosY){
                    /*
            Check for active enemies

             */
                    this.mapPosX = mapPosX;
        if(!bossActive) {
            eQueue.checkQueueDistance(mapPosX);
            for (int e = 0; e < MAX_ENEMIES; e++) {

                if (enemyList[e] != null) {
                    //Log.d("Active Enemy ", Integer.toString(e));
                    if ((enemyList[e].x - mapPosX) > - screenWidth * 2.0f && !enemyList[e].isDead()) {
                        //enemyActive[e] = true;
                        //enemyList[e].setContinuousFire(false);
                    } else {
                        enemyActive[e] = false;
                        enemyList[e].setOutOfBounds();

                    }
                }
            }


        for(int i = 0; i< MAX_ENEMIES; i ++){
            if(enemyActive[i] ) {
                enemyList[i].move(heroX, heroY);
                enemyList[i].updateView(mapPosX, mapPosY);
                if (enemyList[i].willShoot() ) {

                    shots.ShotFired(enemyList[i].fireStats(), i, false);

                    enemyList[i].canShoot = false;

                }
            }

        }
        }else{
            //Log.e("boss", "active");
            boss.updateBoss(heroX, heroY, mapPosX, mapPosY);
            if(!boss.playingIntro){
                hero.setPaused(false);
            }
        }



    }
    @Override
    public void draw(int mTextureUniformHandle,int mTextureCoordinateHandle, int mPositionHandle,int mMVMatrixHandle, int mMVPMatrixHandle,float[] mProjectionMatrix,  float[] mMVPMatrix, float[] mViewMatrix){

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);


        int currentEnemyType = polyVBOHandle.length;
        int currentTextureHandle = -1;
        //Log.e("VBO", Integer.toString(frameVBO));
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);
        //textCoordsFB.position(12);
        for(int i = 0; i< MAX_ENEMIES; i++) {
            if(enemyActive[i]) {
                if(currentTextureHandle != getObjectTextureHandle(enemyList[i].getEnemyType()) ){

                    currentTextureHandle = getObjectTextureHandle(enemyList[i].getEnemyType());

                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentTextureHandle);
                    //GLES20.glUniform1i(mTextureUniformHandle, 0);

                }

                if(currentEnemyType != enemyList[i].getEnemyType()){
                    currentEnemyType = enemyList[i].getEnemyType();
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[currentEnemyType]);
                GLES20.glEnableVertexAttribArray(mPositionHandle);
                GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
                // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

                // Pass in the texture coordinate information -- currently not using vbo

                GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[currentEnemyType]);
                }
                GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                        0, enemyList[i].getAnimFrameOffsetBytes());

                Matrix.setIdentityM(mModelMatrix,0);
                Matrix.translateM(mModelMatrix,0, (enemyList[i].getAbsoluteX()), (enemyList[i].getAbsoluteY()), -2.51f);
                if(enemyList[i].isFacingForward()){
                    mModelMatrix[0] = 1;
                }else{
                    mModelMatrix[0] = -1;
                }
                Matrix.rotateM(mModelMatrix,0,  enemyList[i].getRotation(), 0f,0f, 1f);

                //Log.e("advance", Float.toString(mModelmatrix[12]));
                Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
                GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

                Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

                GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
            }
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


        /**
         *
         *
         * drawScore
         */
        //scoreString = Integer.toString(totalScore);
    }

    public void initBoss(Context context, int b, boolean bossActive, float posX, float posY){



        switch(b){
            case BossEntity.FIRST_BOSS:
                //initBossEntities(context,BossEntity.FIRST_BOSS);
                 boss = new FirstBoss();
                boss.initBoss(hero, context, enemyList, enemyActive, posX,  posY, 2, new float[][]{this.getObjectBounds(BOSS_ONE_BODY) , this.getObjectBounds(BOSS_ONE_ARM)});

                break;
        }
        this.bossActive = bossActive;



          hero.displayMessage(boss.getBossText());
          hero.returnTo(-.8f);


        //shots.setBossEntities(boss.getEnemies(), boss.getEnemyActive());
       // shots.addEnemies(boss.getEnemies(),boss.getEnemyActive());

    }
    public BossEntity getBoss(){
        return boss;
    }
    public void addShots(ShotEntity shots){
        this.shots = shots;
        shots.addEnemies(enemyList, enemyActive);
    }
    public boolean isBossDead(){
        if(boss != null){
            isBossDead = boss.isDead();
        }
        return isBossDead;
    }

    public void setBossActive(boolean active){
        this.bossActive = active;
    }

    private void initEnemyTypes(Context context, int bossType){
        this.initEntity(context, 0, 300, R.drawable.ship,220, 200, 6, 2, .25f, .25f, FLYING_SHIP,false );
        this.initEntity(context, 0, 200, R.drawable.ship, 192, 100, 6, 1, .25f, .25f, ELECTRIC_SHIP, false );

        eQueue.initEnemies(FLYING_SHIP, this.getObjectBounds(FLYING_SHIP));
        eQueue.initEnemies(ELECTRIC_SHIP, this.getObjectBounds(ELECTRIC_SHIP));
        setupBossEntity(bossType);
    }
    private void setupBossEntity(int bossType){
        switch(bossType){
            case BossEntity.FIRST_BOSS:
                this.initEntity(context,0, 0, R.drawable.boss_one, 114, 111, 5, 3, .25f, .25f, BOSS_ONE_ARM, false  );
                this.initEntity(context, 0, 340, R.drawable.boss_one, 208, 355, 9, 2, (Constants.CHARACTER_WIDTH * .75f), (Constants.CHARACTER_HEIGHT * .9f), BOSS_ONE_BODY, false );
                break;
            case BossEntity.NO_BOSS:
                break;
            default:
                break;


        }

    }

    /**
     *
     * @param enemyType
     * @param index
     * @param enemyX
     * @param enemyY
     */
    private void initEnemy(int enemyType, int index, float enemyX, float enemyY){
        switch(enemyType){
            case FLYING_SHIP:
                enemy = new FlyingEnemy( enemyX, enemyY, enemyType);
                break;
            case ELECTRIC_SHIP:
                enemy = new FlyingEnemy( enemyX,enemyY, enemyType);
                break;
        }
        enemy.InitEnemy(4,2, this.getObjectBounds(enemyType));



        enemyList[index] = enemy;
        enemyActive[index] = false;

    }
}
