package com.example.bradmobile.testtexture;

import android.util.Log;

public class Shot {
	
	private float xPos;
	private float yPos;
	private float xView;
	private float yView;
	private float mapPosX = 0;
	private float mapPosY = 0;
	private float angle = 0;
	private boolean leader = false;
	private int indexOfLeader= 0;

	private float xAdv = 0;
	private float yAdv = 0;
	private float shotSpeed = 5;
	private float[] draw = new float[5];
	private float size;
	private boolean friendly = false;
	private boolean Dying = false;
	private boolean Impact = false;
	private boolean linked = false;
	private int[] shotAnim = {0 ,50,100};
	private int timerCount = 0;
	private int shotCountX = 0;
	private int shotCountY = 0;
	private int srcOfShot = 0;
	private float shotSize = 0;
	private int shotStrength = 20;
	private int shotObject = Item.DEFAULT_VALUE;
	private boolean Dead = true;
	private double angleRadians = 0;
	private double angleDegrees = 0;
	private int animCounterX = 0;
	private boolean reverseAnim = false;


	private float xDif = 0f;
	private float yDif = 0f;
	private float yDelta = 0f;
	private float xDelta = 0f;
	private float targetXDif = 0f;
	private float targetYDif = 0f;
	private double sinOfDif = 0d;
	private double cosOfDif = 0d;

	private int beginAnimX = 0;
	private int beginAnimY = 0;
	private int endAnimX = 2;
	private int endAnimY = 2;

	
	public Shot( ){
		

	}
	public void updateView(float mapPosX, float mapPosY){
	    this.mapPosX =  mapPosX;
	    this.mapPosY = mapPosY;
		xView = xPos - mapPosX;
		yView = yPos + mapPosY;		//Log.e("map posY", Float.toString(mapPosY));
	}
	public void advanceShot(){

		
		if(!Impact && !Dying){
			xPos += xAdv;
			yPos -= yAdv;


		
		}else if(Impact){
			Dying = true;
			Impact = false;
		}else if(Dying){
			
			timerCount += 2;

			switch(timerCount){
			case 12: shotCountX = 1;
				break;
			case 24: shotCountX = 2;
					
				break;
			case 32:
				Dying = false;
				Dead = true;
				timerCount = 0;
				break;
			
			}
			
		}

	}

	public void fireShot(float x, float y,float drawX, float drawY, float shotSize, int angle,int shotPower, float shotSpeed, boolean friendly, int shotType){
		xPos = x + drawX;
		yPos = y - drawY;

		xView = xPos - drawX;
		yView = yPos + drawY;

		this.shotSize = shotSize;
		this.Dead = false;
		this.Impact = false;
		this.Dead = false;

		this.shotCountX = 0;
		this.shotCountY = 0;
		linked = false;
		angleDegrees = 0;
		reverseAnim = false;


		setShotObject(shotType);
		this.friendly = friendly;
		this.angle = angle;

		this.shotStrength = shotPower;
		this.shotSpeed = shotSpeed;


		if(angle == 0){
			if(shotSpeed < 0 ) {
				xAdv = (shotSpeed * .80f);
				yAdv = -1f *(shotSpeed * .20f);
			}else{
				xAdv = (shotSpeed * .80f);
				yAdv = (shotSpeed * .20f);
			}


		}else if(angle == 1){
			xAdv= shotSpeed;
			yAdv = 0;
		}else if(angle == 4){
			if(shotSpeed < 0 ) {
				xAdv = (shotSpeed * .5f);
				yAdv = (shotSpeed * .5f);
			}else{
				xAdv = (shotSpeed * .5f);
				yAdv = -1f *(shotSpeed * .5f);
			}
		}else if(angle == 3){

			if(shotSpeed < 0 ) {
				xAdv = (shotSpeed * .66f);
				yAdv = (shotSpeed * .34f);
			}else{
				xAdv = (shotSpeed * .66f);
				yAdv = -1f *(shotSpeed * .34f);
			}

		}else if(angle == 2){

			if(shotSpeed < 0 ) {
				xAdv = (shotSpeed * .80f);
				yAdv = (shotSpeed * .20f);
			}else{
				xAdv = (shotSpeed * .80f);
				yAdv = -1f *(shotSpeed * .20f);
			}

		}else if(angle == 5){
			if(shotSpeed < 0 ) {
				xAdv = (shotSpeed * .38f);
				yAdv = (shotSpeed * .62f);
			}else{
				xAdv = (shotSpeed * .38f);
				yAdv = -1f *(shotSpeed * .62f);
			}

		}

	}

	/**
	 *
	 * @param x
	 * @param y
	 * @param shotSize
	 * @param angle
	 * @param shotPower
	 * @param indexOfLeader
	 * @param leader
	 * @param friendly
	 */
	public void fireLinkShot(float x, float y,float drawX, float drawY, float shotSize, int angle,int shotPower, int indexOfLeader,int srcOfShot, boolean leader,boolean flameTip, boolean friendly, int shotType) {
		xPos = x + drawX;
		yPos = y - drawY;
		xView = xPos - drawX;
		yView = yPos + drawY;

		this.shotSize = shotSize;
		this.Dead = false;
		this.Impact = false;
		this.Dead = false;
		reverseAnim = false;
		this.indexOfLeader = indexOfLeader;
		this.leader = leader;
		linked = true;
		this.srcOfShot = srcOfShot;
		angleDegrees = 0;


		if(flameTip){
			beginAnimX = 0;
			beginAnimY = 2;
			endAnimX = 1;
			endAnimY = 3;

		}else if(leader) {
			beginAnimX = 0;
			beginAnimY = 4;
			endAnimX = 1;
			endAnimY = 5;
		}
		else{
			beginAnimX = 0;
			beginAnimY = 0;

			endAnimX = 1;
			endAnimY = 1;
		}
		this.shotCountX = beginAnimX;
		this.shotCountY = beginAnimY;

		setShotObject(shotType);

		this.friendly = friendly;
		this.angle = angle;

		this.shotStrength = shotPower;

	}

	/**
	 *
	 * @param leftLinkX
	 * @param leftLinkY
	 * @param leftLinkAngle
	 */
	public void advanceLink(float leftLinkX, float leftLinkY, double leftLinkAngle) {
		xDif = this.xPos - leftLinkX;
		yDif = this.yPos - leftLinkY;

		updateRelation(leftLinkX, leftLinkY, leftLinkAngle);
		updateRotation(xDif, yDif);
		updateAnim();

	}


	public boolean dying()
	{
		return Dying;
	}
	public void impact(boolean i){

		if(this.shotObject != Item.WEAPON_UPGRADE_FLAME) {
			Impact = i;
		}else {
			Impact = false;
		}
	}
	public boolean dead(){
		return Dead;
	}
	public float[] drawShot(){
		draw[0] = xView;
		draw[1] = yView - (shotSize * .5f);
		draw[2] = shotAnim[shotCountX];
		draw[3] = yView + (shotSize * .5f);
		draw[4] = shotSize;
		
		return draw;
	}
	public int getShotObject(){
		return shotObject;
	}
	public int getFrame(){
		return (((shotCountX + (shotCountY * 2))* 12) * 4);

	}
	private void setShotObject(int upgradeType){
		switch(upgradeType){
			case Item.DEFAULT_VALUE:
				shotObject = Item.DEFAULT_VALUE;
				break;
			case Item.WEAPON_UPGRADE_FLAME:
				shotObject = Item.FLAME_SHOT;
				break;
			case Item.WEAPON_UPGRADE_SPRAY:
				shotObject = Item.DEFAULT_VALUE;
				break;

		}
	}

	public boolean isLinked(){
		return linked;
	}
	public boolean isLeader(){
		return leader;
	}
	public int getIndexOfLeader(){
		return indexOfLeader;
	}
	private void updateRelation(float leftLinkX, float leftLinkY, double angle){

		sinOfDif = Math.sin(angle * Math.PI / 180);
		cosOfDif = Math.cos(angle * Math.PI / 180);
		targetXDif = (.1f * (float)cosOfDif) + leftLinkX;
		targetYDif =  (.12f * (float)sinOfDif) + leftLinkY;
		yDelta = ((targetYDif - this.yPos) *.28f);
		xDelta = ((targetXDif - this.xPos) * .75f);
		this.xPos += xDelta;
		this.yPos += yDelta;



	}
	private void updateRotation(float xDif,float yDif){

		angleRadians = Math.atan2(yDif,xDif );
		angleDegrees = Math.toDegrees(angleRadians);


	}
	public float getRotation(){

		return (float)angleDegrees;

	}
	public void updateAngleDegrees(double deg){
		this.angleDegrees = deg;
	}
	public void updateAngle(int angle){
		this.angle = angle;
	}
	public void updatePosition(float x, float y){
		updateAnim();
		this.xPos = x;
		this.yPos = y;
	}
	private void updateAnim(){
		animCounterX++;
		if (animCounterX >= 6) {
			if (!reverseAnim) {
				if (shotCountX < endAnimX) {
					shotCountX += 1;
				} else {
					if(shotCountY < endAnimY){
						shotCountY +=1;
						shotCountX = beginAnimX;
					}else{
						reverseAnim = true;
					}
				}
			} else if (reverseAnim) {
				if (shotCountX > beginAnimX) {
					shotCountX -= 1;
				} else {
					if(shotCountY > beginAnimY){
						shotCountY -=1;
						shotCountX = endAnimX;
					}else{
						reverseAnim = false;
					}
				}
			}
			animCounterX = 0;


		}
	}
	public float getX(){
		return xPos;
	}
	public float getY(){
		return yPos;
	}
	public float getAbsoluteX(){
	    return xView;
    }
    public float getAbsoluteY(){
	    return yView;
    }
	public int getSrcOfShot(){
		return srcOfShot;
	}

	public int getShotStrength(){
		return shotStrength;
	}
	public boolean isFriendly(){
		return friendly;
	}

}
