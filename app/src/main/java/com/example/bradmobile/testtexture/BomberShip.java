package com.example.bradmobile.testtexture;


import android.util.Log;

import com.example.bradmobile.testtexture.AnimationUtils.Anim;

public class BomberShip extends EnemyEntity {

    private float yMove = -.005f;
    public BomberShip(float x, float y, int enemyType) {

        super(x,y,enemyType);

        animHandler.setupAnim(Anim.STANDING, 15, 0, 6, false, true, true);
        animHandler.setupAnim(Anim.DYING, 4, 4, 8, false, false, false);

        animHandler.stop();

        //TODO setup item to report if linked or not, this is just hard coded for now
        shotType = Item.BOMB;
        this.firingLinked = false;

    }

    @Override
    public void move(float heroX, float heroY){
        animHandler.stop();
        this.y += yMove;

        if(animHandler.getFrame() == 7){
            if(yMove < 0f) {
                yMove *= -1f;
            }
            tryToShoot();
        }else{

        }
        if((animHandler.getFrame() == 0 || animHandler.getFrame() == 14) && yMove >0f){
            yMove*= -1f;
        }


        if(dying){
            dead = true;

        }

        this.x -= (moveSpeed * 4f);


        //animHandler.update();

    }
}

