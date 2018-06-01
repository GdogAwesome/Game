package com.example.bradmobile.testtexture;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import com.example.bradmobile.testtexture.AnimationUtils.*;

public class HeroEntity extends Entity {
    private Anim animHandler;
	protected float x;
	protected float y;


	protected int xPosCounter = 0;
    protected int yPosCounter = 0;
    protected int xTorsoCounter = 0;
    protected int yTorsoCounter = 0;
	private long lastHit = 0;
	int shootTime = 175;

	
	int counter = 0;
	int jumpCounter = 0;
    int pastShotHeight = 0;
	int shotAngle = 90;
    float lastRotation = 0;
	float torsoAngle = 0;
	int r = 0;
	int b = 0;
	protected float[] fireStats = new float[6];
	public Long lastShot = 0L;
	public boolean canShoot = false;
	protected boolean continuousFire = false;
	private long timer = 0;
	private float currentJumpPos = -12.2F * Constants.scale;
	static float FRAME_VARIANCE = Constants.FRAME_VARIANCE ;
	private static int FALL_SPEED = Constants.FALL_SPEED;
	private static float SHOT_SPEED= Constants.SHOT_SPEED;
	private float lastCount = FRAME_VARIANCE;
	private float lastJumpCount = FRAME_VARIANCE;
	protected boolean doneStart = false;
	protected boolean firing = false;
	private boolean tempWeapon = false;
	private int upgradeTime = 0;
	protected boolean running = false;
	private float currentJumpHeight = 0;
	protected boolean firstRun = true;
	public int shotType = Item.DEFAULT_VALUE;
	private float beginningPos = 0;
	private boolean firingLinked = false;
	MessageBox mBox ;
	/**
	 * set up dying flags
	 */
	public boolean dying = false;
	private boolean dead = false;
	private boolean justDied = false;
	private boolean paused = false;
	private boolean cueToPause = false;
	private boolean returnToStart = false;
	private float returnX = -1f;

	private int shotPower = 20;
	private float shotSpeed = 0;
    private int health = 100;
	private float heightDifference = 0;
	protected boolean contJump = false;
	protected boolean startFalling = true;
	protected boolean falling = false;
	private float fallMomentum = 0;
	private float momentum =0;
	private float heightCheck = 0;
	public boolean facingForward = true;
	private boolean canMoveRight =false;
	private boolean canMoveLeft =false;
	private boolean drawHero = true;
	private float CHARACTER_WIDTH = Constants.CHARACTER_WIDTH;
	private float CHARACTER_HEIGHT = Constants.CHARACTER_HEIGHT;
	private float CHARACTER_LEG_HEIGHT = Constants.CHARACTER_LEG_HEIGHT;
	private float CHARACTER_TORSO_HEIGHT = Constants.CHARACTER_TORSO_HEIGHT;

	/**
	 *
	 * openGl objects
	 */
	private int heroImage;

	private int[] currentFrame = new int[2];
	private float[][] mModelMatrix = new float[2][16];

	private float ground = -0.8f;
	static float RUN_SPEED = Constants.TEST_RUN_SPEED;
    private int lives = 3;

	private float[] hitBox = new float[4];
	private float matrixToFootingOffset = (.18F * CHARACTER_HEIGHT);
	private float torsoMatrixOffset = (.3666f * CHARACTER_TORSO_HEIGHT);

	//TODO fix sound
	//private SoundPool sp;
	//private int[] soundIds = new int[10];
	private long flickerTimer = 0;

	private boolean invincible = false;
	private boolean drawCharacter = true;
	private int height = 0;
	/**
	 * initialize character at starting pos x,  Image urly
	 *
	 * character width and height
	 * @param x
	 * @param y
	 */
	public HeroEntity(Context context, int x, int y){
		super();

		this.x = 0;
		this.y = 0;
		mBox = new MessageBox();
		animHandler = new Anim();

		heroImage =  R.drawable.pos4;

		initHero(context);
		setupModelMatrix();
	}

	
	/**Initializes the entity, loading all of its images into an array from spritesheet that is passed to it.
	 * 
	 * @param source, pass the location of source image
	 */
	public void InitHero(String source, int width, int height){

	
	}
	
	public void draw() {


		/**
		 *
		 * if message exists, draw it
		 */

		if(mBox.activeMessage){

			if(mBox.drawText) {
				for (int i = 0; i < mBox.getText().length; i++) {
					//canvas.drawText(mBox.getText()[i], mBox.getSX(), mBox.getSY(), mBox.getPaint());
					//mBox.drawTY += mBox.getPaint().descent() - mBox.getPaint().ascent();
				}
			}
		}else{
			mBox.activeMessage = false;
		}
	}


	
	/**
	 * 
	 * @param command, pass the command by way of int. 0 = not moving, 1 = moving right, 2 = moving left.
	 */
	public void move(int command, boolean canMoveLeft, boolean canMoveRight){

		this.canMoveLeft = canMoveLeft;
		this.canMoveRight = canMoveRight;

		if(returnToStart){

			if(mModelMatrix[0][12] > returnX){
				command = 2;
			}else {
				command = 1;
				returnToStart = false;
				cueToPause = true;
			}
		}
        if(!dying && !paused ) {
            if (command == 1 && canMoveRight) {
                facingForward = true;
                if(!invincible) {
					mModelMatrix[0][0] = 1;
				}
                running();
                running = true;
            } else if (command == 2 && canMoveLeft) {

				if(!invincible) {
					mModelMatrix[0][0] = -1;
				}
                running();
                facingForward = false;
                running = true;

            } else if (command == 0 || !canMoveLeft || !canMoveRight) {

                Stop();
                running = false;

            }/* else if (contJump) {
                if (!doneStart) {

                }

            }*/
        }else if(paused){
        	running = false;
        	Stop();
		}

		if(cueToPause){
        	paused = true;
        	cueToPause = false;
		}

		updateShotLocation();
	}
	public void jump(){
		

	if(!falling && !paused){
		if (firstRun){
			beginningPos = mModelMatrix[0][13]+(matrixToFootingOffset);
			firstRun = false;
			lastJumpCount = FRAME_VARIANCE;
			currentJumpPos = -.894F;
			contJump = true;
			momentum = 0;
			xPosCounter = 0;
			yPosCounter = 3;


		}else if(!firstRun){
			
			jumpCounter ++;

			if(jumpCounter == 8){
				xPosCounter = 1;
			}else if(jumpCounter == 18){
				xPosCounter = 2;
			}else if(jumpCounter == 30){
				xPosCounter = 3;
			}

			if(jumpCounter >= lastJumpCount){
					
					if(contJump){
						
							heightCheck = .775f;// heightDifference;
						
							heightDifference = (( -1  * (currentJumpPos * currentJumpPos)) + .8f);
							
	
							mModelMatrix[0][13] =  beginningPos + heightDifference;
							currentJumpPos += .12f;
							
	
							if(heightDifference >= heightCheck  ){//y >= beginningPos ){

								contJump = false;

								heightDifference = 0;
								jumpCounter = 0;
								
								
								firstRun = true;

							}

					}
					lastJumpCount += FRAME_VARIANCE;
				}
			}
		}
		
	}
	
	
	/**
	 * control what happens while use is running forward
	 */
	public void running(){
		//TODO fix torso anim to use new method of animation
		if( doneStart && !contJump){
		counter += FRAME_VARIANCE;
		if( momentum < 10){
			momentum ++;
		}
		
			if(counter >= 5){
			
				xPosCounter++;
					counter = 0;
			
				if(xPosCounter >= 4){
					yPosCounter ++;
					xPosCounter = 0;
					
					if(yPosCounter >= 4 ){
						yPosCounter = 0;
					}
			    }
			    if(!firing) {
					xTorsoCounter = yPosCounter;
					yTorsoCounter = 1;
				}

			}
		}else if ( !doneStart && !contJump)
		{
			counter += FRAME_VARIANCE;
			
			
			if(counter >= 5){
				xPosCounter ++;
				counter = 0;
				if(xPosCounter >= 4){
	
					xPosCounter = 0;
					yPosCounter = 0;

					lastCount = FRAME_VARIANCE;
					doneStart = true;
				}
					lastCount += FRAME_VARIANCE;

		
			}

	    }

	    animHandler.run();
		
	}

	
	/**
	 * What is done while player is not moving character
	 */
	private void Stop(){
		if(!contJump && !falling) {
			yPosCounter = 4;
			xPosCounter = 0;

		}
		counter = 0;
		lastCount = FRAME_VARIANCE;
		doneStart = false;
		animHandler.stop();
	}
	public void move(float mapPosX, boolean mR,boolean mL){

        this.canMoveLeft = mL;
        this.canMoveRight = mR;

		if(!dying && !dead) {


			if (facingForward && getRight() <= Constants.RIGHT_BORDER && canMoveRight) {
				mModelMatrix[0][12] += RUN_SPEED;

			} else if (!facingForward && getLeft() >= Constants.LEFT_BORDER && canMoveLeft) {

				mModelMatrix[0][12] -= RUN_SPEED;

			}
		}
		updateShotLocation();

	}
	
	public void falling(){
		if(!contJump){
		
			if(startFalling){


				fallMomentum = .000001F;
				startFalling = false;	
				falling = true;
			}else if(!startFalling){
				if(fallMomentum >= -.0008){
					fallMomentum +=.01;
					mModelMatrix[0][13] -= fallMomentum;
				}

					if( mModelMatrix[0][13] - (matrixToFootingOffset)  <= ground  ){
						
						mModelMatrix[0][13] = ground + ( matrixToFootingOffset) ;
						startFalling = true;
						falling = false;
					}
			}
		
		}
		updateShotLocation();
		
	}
	public void calcFooting(){
		if( mModelMatrix[0][13] - ( matrixToFootingOffset)  > ground && !contJump){
			falling();
		}

	}
	
	
	/**
	 * Return status of jump
	 * @return
	 */
	public void setGround(float ground){
		this.ground = ground;
	}

	public void updateMessage(float mapPosX){
		if(mBox.activeMessage){mBox.updateMessage(this.x - mapPosX, y);}
	}

	public float[] getHitBox(){

		hitBox[0] = mModelMatrix[0][12] - ((CHARACTER_WIDTH) * .2f);
		hitBox[1] = mModelMatrix[1][13] +((CHARACTER_TORSO_HEIGHT) * .25f);
		hitBox[2] = mModelMatrix[0][12] + ((CHARACTER_WIDTH) * .25f);
		hitBox[3] = mModelMatrix[0][13] - (matrixToFootingOffset);

		return hitBox;
	}
    public void setShotHeight(int height){
		this.height = height;

		updateShotLocation();



    }
    private void updateShotLocation(){
		if(firing) {
			if (height == 1) {
				this.shotAngle = 1;

				if (facingForward) {
					torsoAngle = 0f;
					fireStats[0] = mModelMatrix[1][12] + (((CHARACTER_WIDTH * .46666666f)));
					fireStats[1] = mModelMatrix[1][13] + (CHARACTER_TORSO_HEIGHT * .02f);
				} else {
					torsoAngle = 180f;
					fireStats[0] = (mModelMatrix[1][12] - (CHARACTER_WIDTH * .46666666f));
					fireStats[1] = mModelMatrix[1][13] + (CHARACTER_TORSO_HEIGHT * .02f);

				}

			} else if (height == 4) {
				this.shotAngle = 4;

				if (facingForward) {
					torsoAngle = 35f;

					fireStats[0] = mModelMatrix[1][12] + ((CHARACTER_WIDTH * .333333f));
					fireStats[1] = mModelMatrix[1][13] + ((CHARACTER_TORSO_HEIGHT * .55f));
				} else {
					torsoAngle = 145f;
					fireStats[0] = (mModelMatrix[1][12] - (CHARACTER_WIDTH * .3333333f));
					fireStats[1] = mModelMatrix[1][13] + ((CHARACTER_TORSO_HEIGHT * .55f));

				}
			} else if (height == 3) {
				shotAngle = 3;

				if (facingForward) {
					torsoAngle = 22f;
					fireStats[0] = mModelMatrix[1][12] + ((CHARACTER_WIDTH * .466666f));
					fireStats[1] = (mModelMatrix[1][13] + ((CHARACTER_TORSO_HEIGHT * .4f)));
				} else {
					torsoAngle = 158f;
					fireStats[0] = (mModelMatrix[1][12] - (CHARACTER_WIDTH * .4666f));
					fireStats[1] = mModelMatrix[1][13] + ((CHARACTER_TORSO_HEIGHT * .4f));

				}
			} else if (height == 2) {
				shotAngle = 2;

				if (facingForward) {
					torsoAngle = 15f;

					fireStats[0] = mModelMatrix[1][12] + ((CHARACTER_WIDTH * .46666f));
					fireStats[1] = mModelMatrix[1][13] + ((CHARACTER_TORSO_HEIGHT * .2f));
				} else {
					torsoAngle = 165f;
					fireStats[0] = (mModelMatrix[1][12] - (CHARACTER_WIDTH * .46666f));
					fireStats[1] = mModelMatrix[1][13] + ((CHARACTER_TORSO_HEIGHT * .2f));

				}

			}
			yTorsoCounter = 0;
			if (lastRotation != torsoAngle) {
				//rotateTorso(torsoAngle);
				lastRotation = torsoAngle;
				xTorsoCounter = shotAngle - 1;
			}
		}
	}
    public float getFireStatsX(){

		return fireStats[0];

	}
	public float getFireStatsY(){

		return fireStats[1];

	}
	public float[] fireStats(){
		if(facingForward){

			shotSpeed = SHOT_SPEED;

		}else{
            //shotAngle = shotAngle * -1;

			shotSpeed = -1 * (SHOT_SPEED);
		}

		if(tempWeapon){
			if(upgradeTime >0){
				upgradeTime --;
			}else{
				setDefaultFireState();

			}

		}
		
		

		fireStats[2] = shotSpeed;
		fireStats[3] = shotPower;
		fireStats[4] = shotAngle;
		fireStats[5] = shotType;

		return fireStats;
	}
	public void displayMessage(int num){


				mBox.showGlobalMessage(num -1, 8,false);


	}





	private void setupModelMatrix(){
		float toGround = ground + (matrixToFootingOffset);
		Matrix.setIdentityM(mModelMatrix[0],0);
		Matrix.translateM(mModelMatrix[0], 0, 0 , 0, -2.5003f);

		Matrix.setIdentityM(mModelMatrix[1],0);
		Matrix.translateM(mModelMatrix[1], 0, 0 , 0, -2.5003f);


	}

	private void initHero(Context context){
		this.initEntity(context,0, 0, R.drawable.pos4,300, 250, 4, 5, CHARACTER_WIDTH, CHARACTER_LEG_HEIGHT,0, false);
		this.initEntity(context, 0, 1250, R.drawable.pos4, 300, 200, 4, 2, CHARACTER_WIDTH, CHARACTER_TORSO_HEIGHT, 1, false );
		animHandler.setupAnim(Anim.CONTINUOUS_RUN, 16, 0, 2, true, false, true);
		animHandler.setupAnim(Anim.STANDING, 1, 17, 5, false, true, true);
		animHandler.run();
		mBox.initMBox(context);


	}


	public float getLeft(){
		return mModelMatrix[0][12] - (CHARACTER_WIDTH *.2f);
	}
	public float getRight(){
		return mModelMatrix[0][12] + (CHARACTER_WIDTH * .2f);
	}
	public float getCenter(){return mModelMatrix[0][12];}
	public float getFooting(){
		return mModelMatrix[0][13] - (matrixToFootingOffset);
	}

	public boolean contJump(){
		return this.contJump;
	}
	public boolean isRunning(){
		return this.running;
	}
	public void setFiring( boolean f){
		this.firing = f;
	}
	public boolean isFalling(){
		return this.falling;
	}

	public boolean isInvincible(){
		return invincible;
	}
	public int hit(int hitStrength){


        if(!dying) {
			if(!invincible && hitStrength != 0) {
				health -= hitStrength;
				invincible = true;
				flickerTimer = timer;

				if (health <= 0) {
					dying = true;
					justDied = true;

				}
				lastHit = timer;
			}
        }
		return health;

	}
    public void reset(){
		setDefaultFireState();
        this.dead = false;
        this.health = 100;
        this.facingForward = true;
        continuousFire = false;
        this.x -= CHARACTER_WIDTH;
        this.y = 50;
        restoreBounds();
		Stop();
    }

	public void setUpgrade(Item item){

		switch(item.getUType()){
			case Item.WEAPON_UPGRADE_SPRAY:
				tempWeapon = true;
				firingLinked = false;
				upgradeTime = item.getUTime();
				shotType = item.getUType();

				break;
			case Item.WEAPON_UPGRADE_FLAME:
				tempWeapon = true;
				firingLinked = true;
				upgradeTime = item.getUTime();
				shotType = item.getUType();
				break;
			case Item.HEALTH_UPGRADE:
				if(health <= 100){
					health += 20;

					if(health > 100){
						health = 100;
					}
				}
				break;


		}
		continuousFire = false;
	}
	public void tryToShoot(){

		if(!paused && !dying) {
			if ((System.currentTimeMillis() - lastShot) > shootTime) {

				canShoot = true;

				// shots.ShotFired(fireStats(), false);
				lastShot = System.currentTimeMillis();


			}
		}
	}
	public float getHeight(){
		return (CHARACTER_TORSO_HEIGHT + CHARACTER_LEG_HEIGHT);
	}
	public float getWidth(){
		return CHARACTER_WIDTH;
	}
	public void updateTimer(){
		timer ++;

		if(invincible ){
			if((timer -lastHit) > 160) {
				invincible = false;
			}

			if((timer - flickerTimer) > 10){

				drawCharacter = !drawCharacter;
				if(drawCharacter){
					if(facingForward) {
						mModelMatrix[0][0] = 1;
						mModelMatrix[1][0] = 1;
					}else{
						mModelMatrix[0][0] = -1;
						mModelMatrix[1][0] = -1;
					}
				}else {
					mModelMatrix[0][0] = 0;
					mModelMatrix[1][0] = 0;
				}
				flickerTimer = timer;
			}
		}else{
			if(!drawCharacter){
				drawCharacter = true;
				if(facingForward) {
					mModelMatrix[0][0] = 1;
					mModelMatrix[1][0] = 1;
				}else{
					mModelMatrix[0][0] = -1;
					mModelMatrix[1][0] = -1;
				}
			}
		}



	}


    public boolean getDead(){
        return dead;
    }
    public boolean isFiringLinked(){
    	return firingLinked;
	}
    public int getLives(){
        return lives;
    }
    public boolean getFiring(){
    	return firing;
	}

    @Override
	public void draw(int mTextureUniformHandle,int mTextureCoordinateHandle, int mPositionHandle,int mMVMatrixHandle, int mMVPMatrixHandle,float[] mProjectionMatrix,  float[] mMVPMatrix, float[] mViewMatrix){

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0 );
		// Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);

		for(int i = 0; i < 16; i++){
			mModelMatrix[1][i] = mModelMatrix[0][i];
		}

		mModelMatrix[1][13] = mModelMatrix[0][13] + torsoMatrixOffset +(.5f * CHARACTER_LEG_HEIGHT) ;

		//Log.e("VBO", Integer.toString(frameVBO));
		// Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
		GLES20.glUniform1i(mTextureUniformHandle, 0);

		 currentFrame[0] = animHandler.getAnimIndex();// ((xPosCounter + ((yPosCounter ) * 4)) * 12) * 4;
		 currentFrame[1] = ((xTorsoCounter + (yTorsoCounter * 4)) * 12) * 4;



		/**
		 *
		 * draw Torso
		 */


		for(int i = 0; i < 2; i++) {
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[i]);
			GLES20.glEnableVertexAttribArray(mPositionHandle);
			GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
			// GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

			// Pass in the texture coordinate information -- currently not using vbo

			GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[i]);

			GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
					0, currentFrame[i]);


			//Log.e("advance", Float.toString(mModelmatrix[12]));
			Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix[i], 0);
			GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

			Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

			GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


			GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

		}

		mBox.draw(mTextureUniformHandle, mTextureCoordinateHandle, mPositionHandle,mMVMatrixHandle,mMVPMatrixHandle,mProjectionMatrix,mMVPMatrix,mViewMatrix);


		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


	}
	public float getTorsoAngle(){
		return torsoAngle;
	}
	public void setContinuousFire(boolean cont){
		this.continuousFire = cont;
	}
	public boolean getContinuousFire(){
		return continuousFire;
	}
	private void setDefaultFireState(){
		firingLinked = false;
		tempWeapon = false;
		continuousFire = false;
		shotType = Item.DEFAULT_VALUE;
	}

	/**
	 *
	 * forward these method call to message Box
	 */
	@Override
	public void loadVBOs(){
		super.loadVBOs();
		mBox.loadVBOs();


	}
	@Override
	public void setTextureHandle(){
		super.setTextureHandle();
		mBox.setTextureHandle();
	}
	@Override
	public void nullImage(){
		super.nullImage();
		mBox.nullImage();
	}
	public void expandBounds(){
		Constants.RIGHT_BORDER = 1f;
		Constants.LEFT_BORDER = -1f;

	}

	public boolean messageActive(){
		return mBox.activeMessage;
	}
	public void restoreBounds(){
		Constants.RIGHT_BORDER = .5f;
		Constants.LEFT_BORDER = -.5f;

	}
	public void returnTo(float returnX){
		this.returnX = returnX;
		returnToStart = true;
	}
	public void setPaused(boolean p){
		this.paused = p;
	}
	public void update(){
		animHandler.update();
	}


}
