package com.example.bradmobile.testtexture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.SoundPool;
import android.util.Log;

import com.example.bradmobile.testtexture.AnimationUtils.Anim;

import java.util.ArrayList;

public class EnemyEntity {
	protected float x;
	protected float y;
	protected Anim animHandler;

	protected int xPosCounter = 0;
	protected int yPosCounter = 0;
	int shootTime = 300;
	private int itemType = -1;
	private int health = 100;
	public int dSound = 0;
	private float[] bounds = new float[4];
	private float[] relativeBounds = new float[4];

    int counter = 0;
	float shotSpeed = Constants.TEST_SHOT_SPEED;
	int shotPower = 10;
	int jumpCounter = 0;
	int r = 0;
	int b = 0;
	float xView = 0f ;
	int yView = 0;
	float moveSpeed = .002f;
	int intShotAngle = 1;
	float currentJumpPos = -12.2F;
	static int FRAME_VARIANCE = 2;
	float lastCount = FRAME_VARIANCE;
	float lastJumpCount = FRAME_VARIANCE;
	double angleRadians = 0;
	double angleDegrees = 0;
	protected boolean doneStart = false;
	protected boolean startJumping = true;
	protected boolean running = false;
	protected float[] fireStats = new float[6];
	boolean willShoot = false;
	Long lastShot = 0L;


	private boolean hasItem = false;
	boolean canShoot = true;
	float  currentJumpHeight = 0;
	protected boolean firstRun = true;
	protected boolean firing = false;
	float beginningPos = 0;
	float heightDifference = 0;
	protected boolean contJump = false;
	protected boolean startFalling = true;
	protected boolean falling = false;
	float fallMomentum = 0;
	float momentum =0;
	float shotTime = 600f;
	float heightCheck = 0;
	public int groundLevel;
	public int footLevel;
	public int hitPoints = 100;
	public boolean facingForward = true;
	protected int CHARACTER_WIDTH = (int)Constants.CHARACTER_WIDTH;
	protected int CHARACTER_HEIGHT = (int) Constants.CHARACTER_HEIGHT;
	private static int FALL_SPEED = 120;
	public int animCounterX = 0;
	public int animCounterY = 0;
	public int animFrameX = 0;
	public int animFrameY = 0;
	protected  int horizontalFrames = 0;
	protected int verticleFrames = 0;
	protected boolean continuousFire = false;
	protected float shotAngle = 180f;
	public boolean justDied = true;
    public float sampleFrameW = 0;
    public float sampleFrameH =0;

	public boolean dying = false;
	public boolean dead = false;
	protected boolean firingLinked = false;
	public int shotType = Item.DEFAULT_VALUE;
	public boolean reverseAnim = false;
	public int[] soundIds = new int[10];
	public SoundPool sp;


	protected int enemyType = 0;

	long delta = 0;

	Bitmap enemyImage;

	/**
	 * initialize character at starting pos x, y
	 * Image url
	 * character width and height
	 * @param x
	 * @param y
	 */
	public EnemyEntity(float x, float y, int enemyType){

		this.enemyType = enemyType;

		animHandler = new Anim();

		this.x = x;
		this.y = y;
		
	}

	/**
	 *

	 * @param horizontalAnimFrames
	 * @param verticleAnimFrames
	 * @param _bounds
	 */
	public void InitEnemy( int horizontalAnimFrames, int verticleAnimFrames, float[] _bounds ) {

		horizontalFrames = horizontalAnimFrames;
		verticleFrames = verticleAnimFrames;

		this.bounds = _bounds;


    }


	public void tryToShoot(){

		if((System.currentTimeMillis() - lastShot)> shootTime){
			
			canShoot = true;

           // shots.ShotFired(fireStats(), false);
			lastShot = System.currentTimeMillis();
			

		}	
	}
	


	public void jump(){
		

	if(!falling){
		if (firstRun){
			beginningPos = y;
			firstRun = false;
			lastJumpCount = FRAME_VARIANCE;
			currentJumpPos = -12.2F;
			contJump = true;
			momentum = 0;

	
		}else if(!firstRun){
			
			jumpCounter ++;

			if(jumpCounter >= lastJumpCount){
					
					if(contJump){
						
							heightCheck = heightDifference;
						
							heightDifference = ((-1* (currentJumpPos * currentJumpPos)) + CHARACTER_HEIGHT);			

							currentJumpPos ++;
							
	
							if(heightDifference <= heightCheck  ){//y >= beginningPos ){

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
		if( doneStart && !contJump){
		counter++;
		if( momentum < 10){
			momentum ++;
		}
		
			if(counter >= lastCount){
			
				xPosCounter++;
				lastCount += FRAME_VARIANCE;
				if( xPosCounter >= 4 && yPosCounter >= 4){
					
					counter = 0;
					lastCount = FRAME_VARIANCE;
					
				}
			
				if(xPosCounter >= 4){
					yPosCounter ++;
					xPosCounter = 0;
					
					if(yPosCounter >= 4 ){
						yPosCounter = 0;
					}
			    }
			}
		}else if ( !doneStart && !contJump)
		{
			counter ++;
			
			
			if(counter >= lastCount){
				xPosCounter ++;
				
				if(xPosCounter >= 4){
	
					xPosCounter = 0;
					yPosCounter = 0;
					counter = 0;
					lastCount = FRAME_VARIANCE;
					doneStart = true;
				}
					lastCount += FRAME_VARIANCE;
		
			}	
	    }
		
	}
	

	
	
	/**
	 * What is done while player is not moving character
	 */
	private void Stop(){
		
		yPosCounter = 4;
		xPosCounter = 0;
		counter = 0;
		lastCount = FRAME_VARIANCE;
		doneStart = false;
	}
	
	public void falling(){
		if(!contJump){
		
			if(startFalling){
			
				fallMomentum = .02F;
				startFalling = false;	
				falling = true;
			}else if(!startFalling){
				if(fallMomentum <= 1){
					fallMomentum +=.01;
			}
				this.y += (FALL_SPEED * fallMomentum);
					if((this.y + CHARACTER_HEIGHT) >= groundLevel){
						
						this.y = groundLevel - CHARACTER_HEIGHT ;
						startFalling = true;
						falling = false;
					}
			}
		
		}	
		
	}
	
	
	/**
	 * Return status of jump
	 * @return
	 */
	public void setGround(int ground){
		this.groundLevel = ground;
	}
	public void calcFooting(){
		if((this.y + CHARACTER_HEIGHT) < groundLevel && !contJump){
			falling();
		}
	
	}
	public int dyingSound(){
		return dSound;
	}
	public float[] fireStats(){
		
		if(facingForward){
			shotSpeed = Constants.SHOT_SPEED;
		}else{
			shotSpeed = -1 *(Constants.SHOT_SPEED);
		}
		
		fireStats[0] = xView;
		fireStats[1] = this.y ;
		fireStats[2] = shotSpeed;
		fireStats[3] = shotPower;
        fireStats[4] = intShotAngle;
		fireStats[5] = shotType;
		return fireStats;
	}
	
	public boolean willShoot(){

		
		return canShoot;

		
		
	}
	public void move(float heroX, float heroY){
		animHandler.update();

	}
	public void updateView(float x){
	    animHandler.update();
		
		xView = this.x - x;


	}

	public boolean hasItem() {
		return hasItem;
	}
	public int getItemType(){
		return itemType;
	}

	public void setHasItem(boolean hasItem, int itemType) {
		this.itemType = itemType;
		this.hasItem = hasItem;
	}


	public boolean contJump(){
		return this.contJump;
	}
	public boolean isRunning(){
		return this.running;
	}
	public boolean isFalling(){
		return this.falling;
	}
	public boolean isDead() {return this.dead;}
	public boolean isDying() {return this.dying;}

	public float getY(){
		return y;
	}
	public float getAbsoluteX(){
		return xView;
	}
	public float getWidth(){
		return .25f;
	}
	public float getHeight(){
		return .25f;
	}
	public float getX(){
		return x;
	}
	public int hit(int hitStrength){

		this.health -= hitStrength;

		if(health <= 0 ){
			dying = true;
		}
		//Log.d("enemy health", Integer.toString(health));

		return hitPoints;
	}

	public void moveDirect(float xDelta, float yDelta){
		this.x += xDelta;
		this.y += yDelta;
		//updateAnimCont();
		animHandler.stop();
		animHandler.update();

	}

	public void moveDirectWOAnim(float xDelta, float yDelta){
		this.x += xDelta;
		this.y += yDelta;
		//animHandler.stop(false);
	}
	public void dropImage(){
		enemyImage = null;
	}
	public void setContinuousFire(boolean cont){
		this.continuousFire = cont;

	}
	public boolean getContinuousFire(){
		return continuousFire;
	}
	public void update(){
	    animHandler.update();
    }


	public int getAnimFrameOffsetBytes(){

		return animHandler.getAnimIndex();//(((animFrameX + (animFrameY * 4))* 12) * 4);
	}
	public int getEnemyType(){
		return enemyType;
	}

	public void updateRotation(float leftX,float leftY){
		angleRadians = Math.atan2((double)leftY - this.y, (double)leftX - this.x);
		angleDegrees = Math.toDegrees(angleRadians);


	}
	public boolean getFiring(){
		return firing;
	}
	public float getShotAngle(){
		if (facingForward) {
			shotAngle = 0f;
		}else{
			shotAngle = 180f;
		}
		return shotAngle;
	}
	public void setOutOfBounds(){
		firing = false;
		continuousFire = false;
	}
	public float getRotation(){
		return (float)angleDegrees;
	}
	public float[] getBounds(){
		return bounds;
	}
	public float[] getRelativeBounds(){
		relativeBounds[0] = xView + bounds[0];
		relativeBounds[1] = y + bounds[1];
		relativeBounds[2] = xView + bounds[2];
		relativeBounds[3] = y + bounds[3];
		return relativeBounds;

	}
	public boolean isFiringLinked(){
		return firingLinked;
	}
	public void loadAnims(int[] animTypes, int[] numOfFrames, int[] frameStart, int[] frameTime, boolean[] continuous, boolean[] reciprocating, boolean[] interruptible){

		for(int i = 0; i < animTypes.length; i++){
			animHandler.setupAnim(animTypes[i], numOfFrames[i], frameStart[i], frameTime[i], continuous[i], reciprocating[i], interruptible[i]);
		}
	}

}
