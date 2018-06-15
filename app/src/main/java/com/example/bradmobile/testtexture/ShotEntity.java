package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.FloatBuffer;
import java.util.ArrayList;


public class ShotEntity extends Entity{
	private int shotImage;
	private boolean bossActive = false;


	private boolean[] enemyActive;
	private BossEntity boss;

	private float healthPercent = 1;
    private int heroHealth = 100;

    private int hBarFrame = 200;
	private int totalScore = 0;
    private float lowestLevel = 0f;
    private String scoreString = null;

    private float originalHFrame = Constants.H_BAR_WIDTH;
    private float hBarHeight = Constants.H_BAR_HEIGHT;
	private float hBorderWidth = originalHFrame * 1.225f;
	private float barBorderWDiff = (hBorderWidth - originalHFrame) * .5f;
    private float hBorderHeight = hBarHeight * 2;
    private float barBorderHDiff = (hBorderHeight - hBarHeight) * .5f;
    private float hDrawFrame = originalHFrame;
    private float fullHealth = 100f;
	private int maxShots = 200;

	/**
	 *
	 * shot opengl objects
	 */

	private float[] mModelmatrix = new float[16];
	private float[] mHealthHudMatrix = new float[16];
	private float[] mHealthBarMatrix = new float[16];


	private int mPositionSize = 3;
	private int mTextureCoordSize = 2;

	private Shot[] shotArray = new Shot[maxShots];
	private boolean[] bulletState = new boolean[maxShots];
	private boolean[] friendlyState = new boolean[maxShots];
	private EnemyEntity[] enemyList ;
	private Item[] items = new Item[10];
    private Item tempItem;
	private boolean[] itemActive = new boolean[10];
	private float xView = 0;
	private float absoluteX = 0;
    private float hitAreaX;
    private float hitAreaY;
    private int hMarkerTimer = 0;
    private boolean drawHmarker = false;
	private int hitTalley = 0;
    private int lastHit = 0;

	Canvas canvas;
	private float[][][] obstacleList;
	private boolean[][] hasOList;// = new boolean[8][4];

	static float CHARACTER_WIDTH = Constants.CHARACTER_WIDTH;
	private float ShotSize = CHARACTER_WIDTH / 2.5f;
	private float shotWidth = ShotSize * .5625f;
	private RectF whereToDraw;
	private int[] soundIds = new int[10];
	private Rect frameToDraw;
	private float[] hitBox = new float[4];

	/** values for controlling flame shot
	 *
	 *
	 */
	private int leadShotIndex = 0;
	private int maxLinks = 5;
	private int[] flameIndecies = new int[maxLinks];
	private boolean continuousFire = false;
	private int shotsFired = 0;
	private int lastShotIndex = 0;


	
	Shot shot;
	HeroEntity hero;
	public ShotEntity(Context context){


		super();

		whereToDraw = new RectF();
		frameToDraw = new Rect();
		for(int i = 0; i < maxShots; i ++ ){
			shot = new Shot();
			shotArray[i] = shot;
			bulletState[i] = false;
		}
        for(int i = 0; i < items.length; i++){

            tempItem = new Item();
            items[i] = tempItem;
            itemActive[i] = false;
        }

        Matrix.setIdentityM(mModelmatrix,0);
		Matrix.translateM(mModelmatrix,0, 0f, 0f, -2.51f);

		initShotTypes(context);

		Matrix.setIdentityM(mHealthHudMatrix, 0);
		Matrix.translateM(mHealthHudMatrix,0, (-1 - getObjectBounds(Item.HEALTH_HUD)[0]),(1 - getObjectBounds(Item.HEALTH_HUD)[1]), -2.51f);

		Matrix.setIdentityM(mHealthBarMatrix, 0);
		Matrix.translateM(mHealthBarMatrix,0, (-1 - getObjectBounds(Item.HEALTH_BAR)[0]),(1 - (getObjectBounds(Item.HEALTH_BAR)[1] * 2f)), -2.51f);

		//TODO fix sound
		// sp = new SoundPool(15, AudioManager.STREAM_MUSIC, 0);
		//soundIds[0] = sp.load(context,R.raw.laser,1);

	}

	public void ShotFired(float[] shotStats,int _srcOfShot, boolean friendly){

		//activeShots.add(shot);
		if(shotStats[5] == Item.DEFAULT_VALUE) {

			for (int i = 0; i < maxShots; i++) {

				if (!bulletState[i]) {
					//sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);
					shotArray[i].fireShot(shotStats[0], shotStats[1], 0, 0, ShotSize, (int)shotStats[4], (int)shotStats[3], shotStats[2], friendly,Item.DEFAULT_VALUE);
					bulletState[i] = true;
					friendlyState[i] = friendly;
					i = maxShots;

				}

			}
		}else if(shotStats[5] == Item.WEAPON_UPGRADE_SPRAY){
			shotsFired = -1;
			for (int i = 0; i < maxShots; i++) {


				if (!bulletState[i]) {


					shotArray[i].fireShot(shotStats[0], shotStats[1], 0, 0, ShotSize, ((int)shotStats[4] + shotsFired), (int)shotStats[3], shotStats[2], friendly, Item.WEAPON_UPGRADE_SPRAY);
					bulletState[i] = true;
					friendlyState[i] = friendly;
					shotsFired ++;
					if(shotsFired > 1) {
						//sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);
						i = maxShots;
					}

				}

			}


		}else if(shotStats[5] == Item.WEAPON_UPGRADE_FLAME){

			setupLinkedShots(Item.WEAPON_UPGRADE_FLAME, shotStats, _srcOfShot, friendly);



		}
		

		
	}
	public void updateObstacles(float ob[][][], boolean ho[][]){
		this.obstacleList = ob;
		this.hasOList = ho;
	}
	public void addEntities(HeroEntity hero){
		this.hero = hero;
		
	}
	public void addEnemies(EnemyEntity[] enemies, boolean[] activeEnemies ){
		this.enemyList = enemies;
		this.enemyActive = activeEnemies;
	}
	public void updateShots(float mapOffsetX, float absX){
			this.absoluteX = absX;
			this.xView = mapOffsetX;
			hero.updateTimer();
			hitBox = hero.getHitBox();


			for(int i =0; i < maxShots; i++){

				if(bulletState[i]){
					moveShots(i);

					
					if(shotArray[i].dead()){
						
						bulletState[i] = false;
					}else if(shotArray[i].drawShot()[0] > 3.0f || shotArray[i].drawShot()[0]< -3.0f){
						bulletState[i] = false;
						
					}else if(!shotArray[i].isFriendly()){
						
						if(shotArray[i].drawShot()[0] <= hitBox[2] && shotArray[i].drawShot()[0] >= hitBox[0] && shotArray[i].drawShot()[1] <= hitBox[1] && shotArray[i].drawShot()[1] >= hitBox[3]){
							//bulletState[i] = false;
							if(!shotArray[i].dying()){
								if(!hero.isInvincible()) {
									updateHealthBar(shotArray[i].getShotStrength());
									shotArray[i].impact(true);
								}
							}
						}

					}else if(shotArray[i].isFriendly()){

						
						for(int k =0; k < EnemyContainer.MAX_ENEMIES; k++){
							if(enemyActive[k]) {
								if (shotArray[i].drawShot()[0] <= enemyList[k].getRelativeBounds()[2] && shotArray[i].drawShot()[0] >= enemyList[k].getRelativeBounds()[0] && shotArray[i].drawShot()[1] <= enemyList[k].getRelativeBounds()[1] && shotArray[i].drawShot()[1] >= enemyList[k].getRelativeBounds()[3]) {
									//bulletState[i] = false;

									if (!shotArray[i].dying()) {
										if(!enemyList[k].dying) {
											if (!bossActive) {
												lastHit = enemyList[k].hit(shotArray[i].getShotStrength());
											} else {
												boss.hit(k, shotArray[i].getShotStrength());
											}
											totalScore += lastHit;
											shotArray[i].impact(true);
											hitAreaX = shotArray[i].drawShot()[0];
											hitAreaY = shotArray[i].drawShot()[1];
											hMarkerTimer = 0;
											drawHmarker = true;
										}

										if(enemyList[k].justDied && enemyList[k].dying){
											for(int j = 0; j < items.length; j++){

												if(!itemActive[j]){

													itemActive[j] = enemyList[k].hasItem();
													/*

													get ground for obstacle to drop on
													 */
													for(int o = 0; o < 10; o++) {
                                                        for (int p = 0; p < 4; p++) {
															if(hasOList[o][p]) {
																if (enemyList[k].getAbsoluteX() - mapOffsetX >= (obstacleList[o][p][0]) && enemyList[k].getAbsoluteX() - mapOffsetX <= (obstacleList[o][p][2])  && enemyList[k].getY() >= obstacleList[o][p][1]) {
																	if (obstacleList[o][p][1] < lowestLevel) {
																		lowestLevel = obstacleList[o][p][1];
																		o = 10;
																		p =4;
																	}

																}
															}


                                                        }

                                                    }

                                                    items[j].initItem(enemyList[k].getX(), enemyList[k].getY(),enemyList[k].getItemType(), lowestLevel);

                                                    lowestLevel = 2;
                                                    j = items.length;
												}

											}

										}


                                    }
								}
							}
						}
					}
					
					if(!shotArray[i].dying()){
						for(int o = 0; o < 10; o++){
							for(int j = 0; j < 4; j++){
								if (hasOList[o][j]) {

									if ((shotArray[i].drawShot()[0] ) >= (obstacleList[o][j][0] + xView) && shotArray[i].drawShot()[0] <= (obstacleList[o][j][2] + xView)) {


										if ((shotArray[i].drawShot()[1] ) < obstacleList[o][j][1] && shotArray[i].drawShot()[1] > obstacleList[o][j][3]) {

											shotArray[i].impact(true);
											o = 10;
											j = 4;
										}


									}

								}
						}
					}
					
				}

			}
			}

		updateEnemyInteraction();


        if(hMarkerTimer >= 100){
            drawHmarker = false;
        }else{
            hMarkerTimer ++;
        }
		updateItems();

	}

	/**
	 *
	 *
	 * @param mTextureUniformHandle
	 * @param mTextureCoordinateHandle
	 * @param mPositionHandle
	 * @param mProjectionMatrix
	 * @param mMVMatrixHandle
	 * @param mMVPMatrixHandle
	 * @param mMVPMatrix
	 * @param mViewMatrix
	 */
	
	public void draw(int mTextureUniformHandle,int mTextureCoordinateHandle, int mPositionHandle,int mMVMatrixHandle, int mMVPMatrixHandle,float[] mProjectionMatrix,  float[] mMVPMatrix, float[] mViewMatrix){
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);


		int currentShotType = 200;
		int currentTextureHandle = -1;
		// Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		//textCoordsFB.position(12);
		for(int i = 0; i< maxShots; i++) {
			if(bulletState[i]) {

			    // if current shot texture is different bind appropriate texture
				if(currentTextureHandle != getObjectTextureHandle(shotArray[i].getShotObject()) ){

					currentTextureHandle = getObjectTextureHandle(shotArray[i].getShotObject());

					GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentTextureHandle);
				}
                //if current shot vbo's are not the same bind appropriate vbos
				if(currentShotType != shotArray[i].getShotObject()){
					currentShotType = shotArray[i].getShotObject();
					GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[currentShotType]);
					GLES20.glEnableVertexAttribArray(mPositionHandle);
					GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);

					GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

					// Pass in the texture coordinate information

					GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
					GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[currentShotType]);
				}
				GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
						0, shotArray[i].getFrame());

				Matrix.setIdentityM(mModelMatrix,0);
				Matrix.translateM(mModelMatrix,0, shotArray[i].drawShot()[0], shotArray[i].drawShot()[1], -2.509f);
				Matrix.rotateM(mModelMatrix, 0 , shotArray[i].getRotation(), 0, 0, 1f);

				Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
				GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

				Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

				GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


				GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
			}
		}
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


		int currentItemType = -1;
		/**
		 *
		 * draw items
		 */

		for(int i = 0; i< items.length; i++) {
			if(itemActive[i]) {

				if(currentItemType != items[i].getUType()){
					currentItemType = items[i].getUType();
					GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[currentItemType]);
					GLES20.glEnableVertexAttribArray(mPositionHandle);
					GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);

					GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

					GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
					GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[currentItemType]);
				}
				GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
						0, 0);

				Matrix.setIdentityM(mModelMatrix,0);
				Matrix.translateM(mModelMatrix,0, items[i].getDrawStats()[0] , items[i].getDrawStats()[1], -2.5002f);

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
         * HEALTH
         */
        //scoreString = Integer.toString(totalScore);
		drawHealth( mTextureUniformHandle, mTextureCoordinateHandle, mPositionHandle, mMVMatrixHandle, mMVPMatrixHandle, mProjectionMatrix, mMVPMatrix, mViewMatrix);

    }
	private void updateItems(){
		for(int i = 0; i < 10; i++){
			if (itemActive[i]) {
				if(items[i].active) {
					items[i].update(absoluteX);
					if(hero.getRight() >= items[i].getDrawStats()[0] +(this.getObjectBounds(items[i].getUType())[0]) && hero.getLeft() <= (items[i].getDrawStats()[2] + (this.getObjectBounds(items[i].getUType())[2]))
							&& hero.getFooting() <= (items[i].getDrawStats()[1] + (this.getObjectBounds(items[i].getUType())[1])) && hero.getHitBox()[1] >= (items[i].getDrawStats()[3] + (this.getObjectBounds(items[i].getUType())[3])))
					{

						hero.setUpgrade(items[i]);
						if(items[i].getUType() == Item.HEALTH_UPGRADE){
							updateHealthBar(0);
						}
						itemActive[i] = false;
					}
				}else{
					itemActive[i] = false;
				}
			}
		}
	}
	public void resetHealth(){
		this.heroHealth = 100;
		hBarFrame = 200 - (int)((fullHealth - heroHealth) * 2);
		hDrawFrame = originalHFrame - (((fullHealth - heroHealth) / fullHealth)* originalHFrame);
	}

	private void updateHealthBar(int hitStrength){
		heroHealth = hero.hit(hitStrength);

		healthPercent = (float)heroHealth / 100f;
		mHealthBarMatrix[0] = healthPercent;
	}
	private void updateEnemyInteraction(){

		for(int i = 0; i < enemyList.length; i++){

			if(enemyActive[i]){

				if((enemyList[i].getAbsoluteX() > hitBox[0] &&
						enemyList[i].getAbsoluteX() < hitBox[2]) ||
						(enemyList[i].getAbsoluteX() + enemyList[i].getWidth() > hitBox[0]
								&& enemyList[i].getAbsoluteX() + enemyList[i].getWidth() < hitBox[2]) ){

					if((enemyList[i].getY() < hitBox[1] && enemyList[i].getY() > hitBox[3])
							|| (enemyList[i].getY()+ enemyList[i].getHeight() < hitBox[1] && enemyList[i].getY() > hitBox[3])) {
						if(!enemyList[i].dying ) {
							updateHealthBar(5);
						}
					}

				}

			}
		}
	}

	private void initShotTypes(Context context){

		this.initEntity(context, 0, 0, R.drawable.test_shot2, 50, 50,3, 1,shotWidth,ShotSize, Item.DEFAULT_VALUE, false);
		this.initEntity(context, 150, 0, R.drawable.test_shot2, 100, 50,1, 1, ShotSize, ShotSize, Item.WEAPON_UPGRADE_SPRAY, false);
		this.initEntity(context, 0, 100, R.drawable.test_shot2, 125, 50, 1, 1, ShotSize, ShotSize, Item.HEALTH_UPGRADE, false);
		this.initEntity(context, 0, 100, R.drawable.test_shot2, 200, 25, 1, 1,(shotWidth * 3.5f), (ShotSize * .5f), Item.HEALTH_BAR,true);
		this.initEntity(context, 200, 100, R.drawable.test_shot2, 225, 50, 1, 1, (shotWidth * 4f), ShotSize , Item.HEALTH_HUD, false);
		this.initEntity(context, 250, 0, R.drawable.test_shot2, 100, 50, 1, 1,ShotSize,ShotSize, Item.WEAPON_UPGRADE_FLAME, false );
		this.initEntity(context, 0, 150, R.drawable.test_shot2, 300, 150, 2, 6,(ShotSize * 1.25f),(ShotSize * .9f) , Item.FLAME_SHOT, false );
	}


	public void setBossActive(boolean active){
		this.bossActive = active;

	}
	private void setupLinkedShots(int upgradeType, float[] shotStats,int srcOfShot, boolean friendly){
		shotsFired = 0;

		if(srcOfShot == -1){
			continuousFire = hero.getContinuousFire();

		}else{
			continuousFire = enemyList[srcOfShot].getContinuousFire();

		}
		if(!continuousFire){
			for (int i = 0; i < maxShots; i++) {

				if (!bulletState[i]) {


					if (shotsFired == 0) {

						if(srcOfShot == -1) {
							shotArray[i].fireLinkShot(shotStats[0], shotStats[1], ShotSize, (int) shotStats[4], (int) shotStats[3], 0, srcOfShot, true,false, friendly, upgradeType);
							shotArray[i].updateAngleDegrees(hero.getTorsoAngle());
							lastShotIndex = i;
						}else {

							shotArray[i].fireLinkShot(shotStats[0], shotStats[1], ShotSize, (int) shotStats[4], (int) shotStats[3], 0, srcOfShot, true,false, friendly, upgradeType);
							shotArray[i].updateAngleDegrees(enemyList[srcOfShot].getShotAngle());
							lastShotIndex = i;
						}


					} else {
						if(shotsFired == maxLinks -1) {
							shotArray[i].fireLinkShot(shotArray[lastShotIndex].getX(), shotArray[lastShotIndex].getY(), ShotSize, 0, (int) shotStats[3], lastShotIndex, srcOfShot, false, true, friendly, upgradeType);
						}else{
							shotArray[i].fireLinkShot(shotArray[lastShotIndex].getX(), shotArray[lastShotIndex].getY(), ShotSize, 0, (int) shotStats[3], lastShotIndex, srcOfShot, false,false, friendly, upgradeType);
						}

						lastShotIndex = i;
					}

					flameIndecies[shotsFired] = i;

					bulletState[i] = true;
					friendlyState[i] = friendly;
					shotsFired++;
					if (shotsFired > maxLinks - 1) {
						//sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);
						if(srcOfShot == -1){
							 hero.setContinuousFire(true);

						}else{
							enemyList[srcOfShot].setContinuousFire(true);
							//Log.e("set continuous","true");
						}
						i = maxShots;
					}
				}

			}
		}else{

		}
	}
	private void moveShots(int i){



		if(shotArray[i].isLinked() ){
			// -1 indicates the source is from the hero
			if(shotArray[i].getSrcOfShot() == -1) {
				if(!hero.getFiring() && hero.getContinuousFire()){
					hero.setContinuousFire(false);
				}
				if (hero.getContinuousFire() && hero.isFiringLinked()) {
					if (!shotArray[i].isLeader()) {
						shotArray[i].advanceLink(shotArray[shotArray[i].getIndexOfLeader()].getX(), shotArray[shotArray[i].getIndexOfLeader()].getY(), shotArray[shotArray[i].getIndexOfLeader()].getRotation());
					} else {
						shotArray[i].updatePosition(hero.getFireStatsX(), hero.getFireStatsY());
						shotArray[i].updateAngleDegrees(hero.getTorsoAngle());
					}

				} else {
					bulletState[i] = false;
				}
			}else{
				if(enemyActive[shotArray[i].getSrcOfShot()]) {
					if (!enemyList[shotArray[i].getSrcOfShot()].getFiring() && enemyList[shotArray[i].getSrcOfShot()].getContinuousFire()) {
						enemyList[shotArray[i].getSrcOfShot()].setContinuousFire(false);

					}
					if (enemyList[shotArray[i].getSrcOfShot()].getContinuousFire() && enemyList[shotArray[i].getSrcOfShot()].isFiringLinked()) {


						if (!shotArray[i].isLeader()) {
							shotArray[i].advanceLink(shotArray[shotArray[i].getIndexOfLeader()].getX(), shotArray[shotArray[i].getIndexOfLeader()].getY(), shotArray[shotArray[i].getIndexOfLeader()].getRotation());
						} else {
							shotArray[i].updatePosition(enemyList[shotArray[i].getSrcOfShot()].fireStats()[0], enemyList[shotArray[i].getSrcOfShot()].getY());
							shotArray[i].updateAngleDegrees(enemyList[shotArray[i].getSrcOfShot()].getShotAngle());

						}

					} else {
						bulletState[i] = false;
					}
				}else{
					bulletState[i] = false;
				}

			}
		}else {
			shotArray[i].advanceShot();
		}
	}
	public void addBoss(BossEntity b){
		this.boss = b;
	}


	public void drawHealth(int mTextureUniformHandle,int mTextureCoordinateHandle, int mPositionHandle,int mMVMatrixHandle, int mMVPMatrixHandle,float[] mProjectionMatrix,  float[] mMVPMatrix, float[] mViewMatrix) {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);

		/**
		 *
		 * draw hud
		 */

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[Item.HEALTH_HUD]);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
		// GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		// Pass in the texture coordinate information -- currently not using vbo

		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[Item.HEALTH_HUD]);

		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
				0, 0);


		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mHealthHudMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

		/**
		 *
		 * draw health bar
		 */
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[Item.HEALTH_BAR]);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
		// GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

		// Pass in the texture coordinate information -- currently not using vbo

		GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[Item.HEALTH_BAR]);

		GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
				0, 0);


		Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mHealthBarMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);


		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

	}
	

}
