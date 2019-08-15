package com.example.bradmobile.testtexture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.example.bradmobile.testtexture.Utils.Effect;

public class Effects extends Entity {

    private float viewX;
    private float viewY;
    private float[] hitBox;
    private int maxEffects = 50;
    private Effect[] effects = new Effect[maxEffects];
    private boolean[] activeEffects = new boolean[maxEffects];
    private HeroEntity hero;


    public Effects(Context context, HeroEntity hero) {
        this.hero = hero;
        for(int i = 0; i < maxEffects; i++){
            effects[i] = new Effect();
        }
        this.initEntity(context, 0, 0, R.drawable.effects, 100, 120, 9, 2, .36f, .63999f, Item.EXPLOSION, false);
    }
    // returns possible hits from effects
    public int updateEffects( float viewX, float viewY, float frameVariance) {
        this.viewX = viewX;
        this.viewY = viewY;
        int hitStrength = 0;
        float[] hitbox = hero.getHitBox();
        for (int i = 0; i < maxEffects; i++) {
            if (activeEffects[i]) {
                if (effects[i].isActive()) {
                    effects[i].advanceEffect(viewX, viewY, frameVariance);
                    if(!effects[i].isFriendly()){
                        float[] effectBox = new float[4];
                        float scale = effects[i].getScale();

                        effectBox[0] = effects[i].getX() + (getObjectBounds(Item.EXPLOSION)[0] * scale);
                        effectBox[1] = effects[i].getY() + (getObjectBounds(Item.EXPLOSION)[1] * scale);
                        effectBox[2] = effects[i].getX() + (getObjectBounds(Item.EXPLOSION)[2] * scale);
                        effectBox[3] = effects[i].getY() + (getObjectBounds(Item.EXPLOSION)[3] * scale);

                        if(checkCollision(hitbox, effectBox)){
                           hitStrength = effects[i].getHitStrength();
                        }

                    }
                } else {
                    activeEffects[i] = false;
                }
            }
        }
        return hitStrength;
    }

    public void cueEffect(int effectType, float xPos, float yPos, boolean friendly) {
        for (int i = 0; i < maxEffects; i++) {
            if (!activeEffects[i]) {
                effects[i].startEffect(effectType, xPos, yPos, friendly);
                activeEffects[i] = true;
                i = maxEffects + 1;
            }
        }
    }
    //public void checkHeroCollision(HeroEntity hero){
       // hitBox = hero.getHitBox();

   // }
    public void draw(int mTextureUniformHandle, int mTextureCoordinateHandle, int mPositionHandle, int mMVMatrixHandle, int mMVPMatrixHandle, float[] mProjectionMatrix, float[] mMVPMatrix, float[] mViewMatrix) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);


        int currentEffectType = 200;
        int currentTextureHandle = -1;
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        //textCoordsFB.position(12);
        for (int i = 0; i < maxEffects; i++) {
            if (activeEffects[i]) {

                // if current shot texture is different bind appropriate texture
                if (currentTextureHandle != getObjectTextureHandle(effects[i].getEffectType())) {

                    currentTextureHandle = getObjectTextureHandle(effects[i].getEffectType());

                    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, currentTextureHandle);
                }
                //if current shot vbo's are not the same bind appropriate vbos
                if (currentEffectType != effects[i].getEffectType()) {
                    currentEffectType = effects[i].getEffectType();
                    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[currentEffectType]);
                    GLES20.glEnableVertexAttribArray(mPositionHandle);
                    GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);

                    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

                    // Pass in the texture coordinate information

                    GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
                    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[currentEffectType]);
                }
                GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                        0, effects[i].getFrame());

                Matrix.setIdentityM(mModelMatrix, 0);
                Matrix.translateM(mModelMatrix, 0, effects[i].getX(), effects[i].getY(), -2.509f);
                Matrix.scaleM(mModelMatrix, 0, effects[i].getScale(), effects[i].getScale(), 1.0f);

                Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
                GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

                Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

                GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
            }
        }
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


    }

}