package com.example.bradmobile.testtexture;


import java.util.ArrayList;


public class Enemy {
	protected float x;
	protected float y;

	protected int xPosCounter = 0;
	protected int yPosCounter = 0;
	int shootTime = 200;
	
	int counter = 0;
	int jumpCounter = 0;
	int r = 0;
	int b = 0;
	float currentJumpPos = -12.2F;
	static int FRAME_VARIANCE = 2;
	float lastCount = FRAME_VARIANCE;
	float lastJumpCount = FRAME_VARIANCE;
	protected boolean doneStart = false;
	protected boolean startJumping = true;
	protected boolean running = false;
	float  currentJumpHeight = 0;
	protected boolean firstRun = true;
	float beginningPos = 0;
	float heightDifference = 0;
	protected boolean contJump = false;
	protected boolean startFalling = true;
	protected boolean falling = false;
	float fallMomentum = 0;
	float momentum =0;
	float heightCheck = 0;
	public int groundLevel;
	public int footLevel;
	public boolean facingForward = true;
	static int CHARACTER_WIDTH = (int)Constants.CHARACTER_WIDTH;
	static int CHARACTER_HEIGHT = (int)Constants.CHARACTER_HEIGHT;
	private static int FALL_SPEED = 120;
	protected int[] displayPosX = new int[]{0,300,600, 900, 1200};
	protected int[] displayPosY = new int[]{0,250, 500, 750, 1000, 1250};
	
											

	
	long delta = 0;

	 /**
	 * initialize character at starting pos x, y
	 * Image url
	 * character width and height
	 * @param x
	 * @param y
	 */
	public Enemy(int x, int y){
			
		this.x = x;
		this.y = y;
		groundLevel = y + CHARACTER_HEIGHT;
		//this.footLevel = this.yPosCounter - 350;
		//System.out.println("foot level: "+footLevel);
		
	}
	
	/**
	 * 
	 * @param source
	 * @param width
	 * @param height
	 * 
	 */
	public void InitHero(String source, int width, int height){


		CHARACTER_HEIGHT = height;
		CHARACTER_WIDTH = width;
		//System.out.println("character height " +CHARACTER_HEIGHT);
	
		//this.groundLevel = (int)(this.y -250);
		

	
	}
	
	public void draw()
	{

	}
	
	/**
	 * 
	 * @param command, pass the command by way of int. 0 = not moving, 1 = moving right, 2 = moving left.
	 */
	public void move(int command, boolean canMoveLeft, boolean canMoveRight){
		

		if(command == 1 && canMoveLeft && canMoveRight){
			facingForward = true;
			running();
			running = true;
		}else if( command == 2  && canMoveLeft && canMoveRight){
			
			running();
			facingForward = false;
			running = true;
			
		}else if( command == 0 || !canMoveLeft || !canMoveRight){
			
			Stop();
			running = false;
			
		}else if (contJump){
	    	if(!doneStart){
	    		
	    	}
	    	
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
							
	
							y = beginningPos - heightDifference;
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
		
		//this.y = groundLevel - 250;
		//System.out.println("hero ground level: " + groundLevel);
	}
	public void calcFooting(){
		if((this.y + CHARACTER_HEIGHT) < groundLevel && !contJump){
			falling();
		}
	
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
	public int getY(){
		return (int)y;
	}

}
