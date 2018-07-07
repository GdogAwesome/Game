package com.example.bradmobile.testtexture;


import com.example.bradmobile.testtexture.AnimationUtils.Anim;

public class FlyingEnemy extends EnemyEntity {

	
	/**
	 *
	 * @param x pos of character cast as an int
	 * @param y pos of character cast as an int
	 */
	public FlyingEnemy (float x, float y ,int enemyType ){

		super(x, y, enemyType);

		/**
		 * setup anims
		 */
		animHandler.setupAnim(Anim.STANDING, 6, 0, 6, false, true, true);
		animHandler.setupAnim(Anim.DYING, 4, 4, 8, false, false, false);

		animHandler.stop();

		this.x = x;
		this.y = y;

		//TODO setup item to report if linked or not, this is just hard coded for now
		shotType = Item.WEAPON_UPGRADE_FLAME;
		this.firingLinked = true;



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



					if((this.y - .5f) <= (heroY ) && (this.y )>= heroY){

						firing = true;
						tryToShoot();
					}else{
						//firing = false;
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


	@Override
	public void moveDirect(float xDelta, float yDelta){
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
