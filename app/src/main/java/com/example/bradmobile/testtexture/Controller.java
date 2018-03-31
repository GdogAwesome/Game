package com.example.bradmobile.testtexture;


import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Created by b_hul on 1/29/2018.
 */

public class Controller extends Entity {
    public static final int CONTROL_ARROWS = 0;
    public static final int CONTROL_SHOT = 1;
    public static final int CONTROL_JUMP = 2;
    private int playerCommand = 0;
    private float screenWidth;
    private float screenHeight;
    private float halfScreenWidth;
    private float halfScreenHeight;
    private float edgeBuffer = .1f;
    private float[] mArrowsMatrix = new float[16];
    private float[] mFireMatrix = new float[16];
    private float[] mJumpMatrix = new float[16];

    private float screenSpaceArrowX;
    private float screenSpaceArrowY;
    private float screenSpaceArrowLeft;
    private float screenSpaceArrowRight;
    private float screenSpaceArrowTop;
    private float screenSpaceArrowBottom;
    private int fireState = 0;

    private float[] jumpBox = new float[4];
    private float[] fireBoxHorizontal = new float[2];
    private float[] fireBoxVerticle = new float[5];

    private float leftX = 0f;
    private float leftY = 0f;
    private float rightX = 0f;
    private float rightY = 0f;

    private boolean jump = false;
    private boolean shoot =false;

    public Controller(){

        //TODO finish setting up controller!!!
        super();



    }
    public void initController(Context context){
        this.initLinkedPolyEntity(context, 0, 0,R.drawable.button_layout, 100, 60, 2, 1, .55f, .275f, CONTROL_ARROWS );
        this.initEntity(context, 300, 0, R.drawable.button_layout, 100, 200, 1, 1, .225f, 1.0f, CONTROL_SHOT, false);
        this.initEntity(context, 200, 0, R.drawable.button_layout, 100, 100, 1, 1, .25f, .444444f, CONTROL_JUMP, false);

        screenWidth = Constants.SCREEN_WIDTH;
        screenHeight = Constants.SCREEN_HEIGHT;
        halfScreenWidth = screenWidth * .5f;
        halfScreenHeight = screenHeight * .5f;
        setupMatricies();
        screenSpaceArrowX = (mArrowsMatrix[12] * halfScreenWidth) + halfScreenWidth;
        screenSpaceArrowY = (mArrowsMatrix[13] * halfScreenHeight) + halfScreenHeight;
        screenSpaceArrowLeft = ((mArrowsMatrix[12] + this.getObjectBounds(CONTROL_ARROWS)[0]) * halfScreenWidth) + halfScreenWidth;
        screenSpaceArrowRight = ((mArrowsMatrix[12] + this.getObjectBounds(CONTROL_ARROWS)[2]) * halfScreenWidth) + halfScreenWidth;
        screenSpaceArrowTop = screenHeight -(((mArrowsMatrix[13] + (this.getObjectBounds(CONTROL_ARROWS)[1] )) * halfScreenHeight) + halfScreenHeight);
        screenSpaceArrowBottom = screenHeight -(((mArrowsMatrix[13] + this.getObjectBounds(CONTROL_ARROWS)[3] ) * halfScreenHeight) + halfScreenHeight );

        /**
         *
         * setup jump sense box
         */
        jumpBox[0] = ((mJumpMatrix[12] + this.getObjectBounds(CONTROL_JUMP)[0]) * halfScreenWidth) + halfScreenWidth;
        jumpBox[1] = screenHeight -(((mJumpMatrix[13] + (this.getObjectBounds(CONTROL_JUMP)[1]* 1.5f )) * halfScreenHeight) + halfScreenHeight);
        jumpBox[2] = ((mJumpMatrix[12] + this.getObjectBounds(CONTROL_JUMP)[2]) * halfScreenWidth) + halfScreenWidth;
        jumpBox[3] = screenHeight -(((mJumpMatrix[13] + (this.getObjectBounds(CONTROL_JUMP)[3] * .5f )) * halfScreenHeight) + halfScreenHeight);

        /**
         *
         * setup shot sense box
         */
        float fireDivide =((( this.getObjectBounds(CONTROL_SHOT)[1]* 2f) / 4f) * halfScreenHeight);

        fireBoxHorizontal[0] = ((mFireMatrix[12] + this.getObjectBounds(CONTROL_SHOT)[0]) * halfScreenWidth) + halfScreenWidth;
        fireBoxHorizontal[1] = ((mFireMatrix[12] + this.getObjectBounds(CONTROL_SHOT)[2]) * halfScreenWidth) + halfScreenWidth;


        fireBoxVerticle[0] = screenHeight -(((mFireMatrix[13] + (this.getObjectBounds(CONTROL_SHOT)[1] )) * halfScreenHeight) + halfScreenHeight);
        Log.e("shot Height"+ 0 +"", Float.toString(fireBoxVerticle[0]) );
        for(int i = 1; i < fireBoxVerticle.length; i++){

            fireBoxVerticle[i] = fireBoxVerticle[i -1] + fireDivide;
            Log.e("shot Height"+ i +"", Float.toString(fireBoxVerticle[i]) );
        }



    }
    public int retControlFromTouch(float x, float y, boolean release){



                if(x > (Constants.SCREEN_WIDTH /2)){

                    if(!release) {
                        rightX = x;
                        rightY = y;

                    }else{
                        rightX = 0f;
                        rightY = 0f;

                        buttonReleaseRight(x, y);
                    }

                }else{
                    if(!release) {
                        leftX = x;
                        leftY = y;

                    }else{
                       leftX = 0f;
                        leftY = 0f;
                        buttonReleaseLeft(x, y);
                    }
                }
        rightButtonMove(rightX,rightY);
        leftButtonMove(leftX, leftY);





        return playerCommand;

    }

    private void setupMatricies(){
        Matrix.setIdentityM(mArrowsMatrix, 0);
        Matrix.translateM(mArrowsMatrix,0, (-1f - (this.getObjectBounds(CONTROL_ARROWS)[0] ) + edgeBuffer), ( -1f - (2f *this.getObjectBounds(CONTROL_ARROWS)[3]) + edgeBuffer), -2.5001f);

        Matrix.setIdentityM(mFireMatrix, 0);
        Matrix.translateM(mFireMatrix,0, (1f - (this.getObjectBounds(CONTROL_JUMP)[2] * 4f) + edgeBuffer), ( -1f - (this.getObjectBounds(CONTROL_SHOT)[3]) + edgeBuffer), -2.5001f);

        Matrix.setIdentityM(mJumpMatrix, 0);
        Matrix.translateM(mJumpMatrix,0, (1f - (this.getObjectBounds(CONTROL_JUMP)[2] * 2f) + edgeBuffer), ( -1f - (this.getObjectBounds(CONTROL_JUMP)[3]) + edgeBuffer), -2.5001f);

    }

        public void leftButtonMove(float posX, float posY) {



            if (posX > screenSpaceArrowLeft && posX < screenSpaceArrowX && posY < screenSpaceArrowBottom && posY > screenSpaceArrowTop) {
                playerCommand = 2;

            } else if (posX < screenSpaceArrowRight && posX > screenSpaceArrowX && posY < screenSpaceArrowBottom && posY > screenSpaceArrowTop) {

                playerCommand = 1;
            }else{
                playerCommand = 0;
            }




        }

    private void buttonReleaseLeft(float posX, float posY) {

       // playerCommand = 0;


    }


        public void rightButtonMove(float posX, float posY) {



            if (posX > jumpBox[0] && posX < jumpBox[2] && posY > jumpBox[1] && posY < jumpBox[3]) {
                //Log.e("jumping", "true");
                jump = true;
                shoot = false;
            }  else if (posX > fireBoxHorizontal[0] && posX < fireBoxHorizontal[1]){ //&& posY > controlHeight && posY < controlHeight + circleDiameter) {



                if(posY > fireBoxVerticle[0] && posY < fireBoxVerticle[1]){
                    shoot = true;
                    fireState = 4;

                }else if(posY > fireBoxVerticle[1] && posY < fireBoxVerticle[2]){
                    shoot = true;
                    fireState = 3;
                }else if(posY > fireBoxVerticle[2] && posY < fireBoxVerticle[3]){
                    shoot = true;
                    fireState = 2;
                }else if(posY > fireBoxVerticle[3] && posY < fireBoxVerticle[4]){
                    shoot = true;
                    fireState = 1;
                }else{
                    shoot = false;
                    fireState = 0;
                }
                //shotHeight = (int) posY;



            }else{
                shoot = false;
                jump = false;
                fireState = 0;
                //hero.setFiring(false);
            }


        }



        private void buttonReleaseRight(float posX, float posY){

            jump = false;

            shoot = false;
           /* if (posX > middleSpace && posX < middleSpace + circleDiameter && posY > controlHeight && posY < controlHeight + circleDiameter) {

            } else if (posX > (middleSpace - circleDiameter) && posX < middleSpace){// && posY > controlHeight && posY < controlHeight + circleDiameter) {
                shoot = false;

                hero.setFiring(false);
            } else {
                startJumping = false;
            }
            */

        }

@Override
public void draw(int mTextureUniformHandle,int mTextureCoordinateHandle, int mPositionHandle,int mMVMatrixHandle, int mMVPMatrixHandle,float[] mProjectionMatrix,  float[] mMVPMatrix, float[] mViewMatrix){

    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    // Bind the texture to this unit.
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);


    /**
     *
     * draw Arrows
     */
    // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
    GLES20.glUniform1i(mTextureUniformHandle, 0);



        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[CONTROL_ARROWS]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
        // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Pass in the texture coordinate information -- currently not using vbo

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[CONTROL_ARROWS]);

        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                0, 0);


        //Log.e("advance", Float.toString(mModelmatrix[12]));
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mArrowsMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 12);


    /**
     *
     * draw shot
     */
    // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
   // GLES20.glUniform1i(mTextureUniformHandle, 0);



    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[CONTROL_SHOT]);
    GLES20.glEnableVertexAttribArray(mPositionHandle);
    GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
    // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    // Pass in the texture coordinate information -- currently not using vbo

    GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[CONTROL_SHOT]);

    GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
            0, 0);


    //Log.e("advance", Float.toString(mModelmatrix[12]));
    Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mFireMatrix, 0);
    GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

    Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

    /**
     *
     * draw jump
     */
    // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
    GLES20.glUniform1i(mTextureUniformHandle, 0);



    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[CONTROL_JUMP]);
    GLES20.glEnableVertexAttribArray(mPositionHandle);
    GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
    // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    // Pass in the texture coordinate information -- currently not using vbo

    GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[CONTROL_JUMP]);

    GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
            0, 0);


    //Log.e("advance", Float.toString(mModelmatrix[12]));
    Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mJumpMatrix, 0);
    GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

    Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

    GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);


    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);


}
public boolean getShoot(){

    return shoot;
}
public int getFireState(){

   return fireState;
}
public boolean getJump(){

    return jump;
}
public int getPlayerCommand(){
    return playerCommand;
}
}
