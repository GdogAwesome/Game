package com.example.bradmobile.testtexture.Utils;

import android.util.Log;

import com.example.bradmobile.testtexture.AnimationUtils.Anim;
import com.example.bradmobile.testtexture.Item;

public class Effect {

    private boolean isActive = false;
    private float xPos;
    private float yPos;
    private float viewX;
    private float viewY;
    private float mapOffsetX;
    private float mapOffsetY;
    private int hitStrength = 0;
    private int effectType;
    private int specificEffect;
    private float frameVariance = 1.0f;
    private float scale = 1.0f;
    private float scaleAdvance = .02f;
    private boolean changeScale = false;
    private boolean friendly = true;
    private Anim anim = new Anim();


    public Effect(){

        setupAnim();
    }
    public void startEffect(int effectType, float xPos, float yPos, boolean friendly){
        this.friendly = friendly;
        specificEffect = effectType;
        if (effectType == Item.BLUE_EXPLOSION){
            this.effectType = Item.EXPLOSION;
            anim.Attack1();
        }else if( effectType == Item.YELLOW_EXPLOSION) {
            this.effectType = Item.EXPLOSION;
            hitStrength = 15;
            anim.Attack2();
        }else{
            this.effectType = effectType;
        }
        this.xPos = xPos;
        this.yPos = yPos;
        scale = 1.0f;

        //changeScale = false;

        isActive = true;
    }

    public void advanceEffect(float offsetX, float offsetY, float frameVariance){
        this.mapOffsetX = offsetX;
        this.mapOffsetY = offsetY;
        this.frameVariance = frameVariance;
        scale += scaleAdvance * frameVariance;
        if(!anim.isCurrentDone()) {
            //Log.e("texture", "not active");
            //anim.stop();
        }else{
            isActive = false;
        }
        anim.update(frameVariance);
    }
    private void setupAnim(){
                anim.setupAnim(Anim.ATTACK_1,9,0, 2, false, false,true);
                anim.setupAnim(Anim.ATTACK_2,9, 9, 2, false, false,true);
                anim.setupAnim(Anim.STANDING,9,0, 2, false, false,false);
    }
    public float getScale(){
        return scale;
    }
    public int getHitStrength(){
        return hitStrength;
    }
    public float getX(){
        return xPos - mapOffsetX;
    }
    public float getY(){
        return yPos - mapOffsetY;
    }
    public boolean isActive(){
        return isActive;
    }
    public boolean isFriendly(){return friendly;}
    public int getEffectType(){
        return effectType;
    }
    public int getFrame(){
        return anim.getAnimIndex();
    }
}
