package com.example.bradmobile.testtexture;


import com.example.bradmobile.testtexture.AnimationUtils.Anim;

public class FlyingEnemy extends EnemyEntity {

	//protected float x;
	//protected float y;
	//Image image;
	private int enemyWidth = Constants.SEGMENT_WIDTH /2;
	private int enemyHeight = Constants.SEGMENT_HEIGHT /2;



	
	/**Creates a hero entity to hold main characters
	 * 
	 * pass the game this belongs to
	 * @param x pos of character cast as an int
	 * @param y pos of character cast as an int
	 */
	public FlyingEnemy (float x, float y ,int enemyType ){

		super(x, y, enemyType);

		/**
		 * setup anims
		 */
		animHandler.setupAnim(Anim.STANDING, 4, 0, 6, false, true, true);
		animHandler.setupAnim(Anim.DYING, 4, 4, 8, false, false, false);

		animHandler.stop();

		this.x = x;
		this.y = y;

		//TODO setup item to report if linked or not, this is just hard coded for now
		shotType = Item.WEAPON_UPGRADE_FLAME;
		this.firingLinked = true;


		
	}


	/**
	 *
	 * @param horizontalAnimFrames
	 * @param verticleAnimFrames
	 */
	@Override
	public void InitEnemy( int horizontalAnimFrames, int verticleAnimFrames){

		horizontalFrames = horizontalAnimFrames;
		verticleFrames = verticleAnimFrames;


	}
    @Override
	public void move(float heroX, float heroY){
		//super.move(heroX, heroY);


				if(!dying) {

					animHandler.stop();
					//updateAnim4();


					if((xView ) > heroX){
						this.x -= moveSpeed;
						facingForward = false;
					}else if((xView ) < heroX){
						this.x += moveSpeed;
						facingForward = true;

					}

					if((this.y ) > heroY){
						this.y -= moveSpeed;
					}else if((this.y ) < heroY){

						this.y += moveSpeed;
					}



					if((this.y - .5f) < (heroY ) && (this.y )> heroY){

						firing = true;
						tryToShoot();
					}else{
						firing = false;
					}


				}else{


					if(justDied){
						animCounterX = 0;
						//sp.play(soundIds[0],1, 1, 1, 0, 1.0f);
						animFrameX = 0;
						animFrameY = 1;


						justDied = false;
						firing = false;
					}
					animHandler.dying();
					animCounterX++;
					if (animCounterX >= 10) {
						if (animFrameX < 3) {
							animFrameX += 1;
							animCounterX = 0;

						}else{
							dead = true;
						}
					}

				}

		
	}
	public void updateAnim4(){

		animCounterX++;
		if (animCounterX >= 10) {
			if (!reverseAnim) {
				if (animFrameX < horizontalFrames -1) {
					animFrameX += 1;
				} else {
					reverseAnim = true;
				}
			} else if (reverseAnim) {
				if (animFrameX > 0) {
					animFrameX -= 1;
				} else {
					reverseAnim = false;
				}
			}
			animCounterX = 0;


		}
	}
	public void updateAnim5(){

		animCounterX++;
		if (animCounterX >= 10) {
			if (!reverseAnim) {
				if (animFrameX < 4) {//used to be 3
					animFrameX += 1;
				} else {
					reverseAnim = true;
				}
			} else if (reverseAnim) {
				if (animFrameX > 0) {
					animFrameX -= 1;
				} else {
					reverseAnim = false;
				}
			}
			animCounterX = 0;


		}
	}
	public void updateAnim6(){

		animCounterX++;
		if (animCounterX >= 10) {
			if (!reverseAnim) {
				if (animFrameX < 5) {
					animFrameX += 1;
				} else {
					reverseAnim = true;
				}
			} else if (reverseAnim) {
				if (animFrameX > 0) {
					animFrameX -= 1;
				} else {
					reverseAnim = false;
				}
			}
			animCounterX = 0;


		}
	}
	public void updateAnimCont(){

		animCounterX++;
		if (animCounterX >= 5) {
				if (animFrameX < 4 && animFrameY < 2) {
					animFrameX += 1;
				} else if(animFrameX < 1 && animFrameY == 2) {
					animFrameX += 1;
				} else if(animFrameX >= 1 && animFrameY == 2) {
					animFrameX = 0;
					animFrameY = 0;
				}else if(animFrameX >= 4 && animFrameY < 2){
					animFrameX = 0;
					animFrameY ++;
				}

			animCounterX = 0;

		}

	}

	@Override
	public void moveDirect(float xDelta, float yDelta){
		updateAnim6();
		//updateAnimCont();
		this.x += xDelta;
		this.y += yDelta;
	}
	@Override
	public void moveDirectWOAnim(float xDelta, float yDelta){
		this.x += xDelta;
		this.y += yDelta;
	}








}
