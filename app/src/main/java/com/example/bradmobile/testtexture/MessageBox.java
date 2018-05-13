package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;

import com.example.bradmobile.testtexture.TextLib.TextManager;

/**
 * Created by BradMobile on 9/9/2016.
 */
public class MessageBox extends Entity {

    private String Message = null;
    private float textSize;

    public boolean activeMessage = false;
    private int currentMessage = 0;
    int timer =0;
    int length = 0;
    float posX = 0;
    float posY = 0;
    float scale = 1.6f;
    int blockSize = 14;
    private float expandW;
    private float expandH;


    public float drawTX = 0;
    public float drawTY = 0;
    private float targetW = .25f;
    private float targetH = .25f;
    private float barHeight = .05f;
    private float barWidth = barHeight * .5625f;

    private String[] messages;
    TextManager textManager;
    private float[] widths ;
    public boolean expanding = true;
    public boolean drawText = false;
    private String[] values ;
    private int totalWidths = 0;
    private boolean global = false;
    private int textLength = 0;

    /**
     *
     * setup box objects
     *
     */
    public static int UPPER_LEFT_CORNER = 0;
    public static int UPPER_RIGHT_CORNER = 1;
    public static int LOWER_LEFT_CORNER = 2;
    public static int LOWER_RIGHT_CORNER = 3;
    public static int UPPER_CENTER_BAR = 4;
    public static int LOWER_CENTER_BAR = 5;
    public static int LEFT_BAR = 6;
    public static int RIGHT_BAR = 7;
    public static int CENTER_SQUARE = 8;

    private float[] centerBounds = new float[4];


    private float[][] mModelMatrix = new float[9][16];
    private float[] mTextModelMatrix = new float[16];


    int version;


    int cornerWH = 14;

    float[] draw = new float[4];


    float messageH = 0f;
    float messageW = 0f;

     MessageBox(){

         setupMatrix();
         textSize = (28 * scale);
         messages = new String[]{"HELLO! /n Welcome To The Game! /n The Controls are simple, try some things.",
                 "Still Playing? /n This is kind of boring don't you think?",
         "TEST I am first boss lololololol"};
         textManager = new TextManager();
         textManager.LoadText(messages);
        // int paintC ;

         version = Build.VERSION.SDK_INT;

         if (version >= 23) {
            // paintC = context.getResources().getColor(R.color.white, null);
         } else {
            // paintC = context.getResources().getColor(R.color.white);

         }


     }
    public void showMessage(int currentMessage, int time, boolean mutable){

        /**
         *
         *
         * alter string to fit box here
         */

        global = false;
        this.currentMessage = currentMessage;
        setMessageWidth(currentMessage);

        expandW = (targetW / messageW)  * .25f;
        expandH = (targetH / messageH)* .25f;


        timer = 0;
        this.length = time * 60;
        activeMessage = true;

    }
    public void showGlobalMessage(int currentMessage, int time, boolean mutable){
        /**
         *
         *
         * alter string to fit box here
         */

        this.currentMessage = currentMessage;

        global = true;
        drawText = false;
        activeMessage = false;

        setMessageWidth(currentMessage);

        this.posX = 0f;//(Constants.SCREEN_WIDTH /2 ) - (targetW /2 );  //scale * 200; //x - (messageW * .5f);// - messageW;
        this.posY = .5f;//(scale * 100);//(float)(Constants.SCREEN_HEIGHT * .75); //y - messageH - (blockSize * scale);

        //this.Message = Integer.toString(version);


        timer = 0;
        this.length = time * 60;
        activeMessage = true;

    }
    public void updateMessage(float x, float y){
/*
        if(!global) {
            this.posX = x - (targetW * .5f);// - messageW;
            this.posY = y - targetH - (blockSize * scale);

        }
        */

        if(activeMessage) {

            if (drawText) {
                timer++;
                if (timer >= length) {
                    timer = 0;
                    activeMessage = false;
                    drawText = false;
                    textLength = 0;
                    messageH = 0;
                    messageW = 0;
                    targetH = 0;
                    targetH = 0;

                }
            } else {
                if ((mModelMatrix[CENTER_SQUARE][0] * barWidth) <= targetW) {
                    messageW += expandW;
                    mModelMatrix[CENTER_SQUARE][0] = messageW;
                    // Log.e("message width",Float.toString(mModelMatrix[0] * .01f));
                }
                if ((mModelMatrix[CENTER_SQUARE][5] * barHeight) <= targetH) {
                    messageH += expandH;
                    // Matrix.scaleM(mModelMatrix[CENTER_SQUARE],0, 1f, messageH, 1f);
                    mModelMatrix[CENTER_SQUARE][5] = messageH;
                }
                if ((mModelMatrix[CENTER_SQUARE][5] * barHeight) >= targetH && (mModelMatrix[CENTER_SQUARE][0] * barWidth) >= targetW) {

                    drawText = true;

                }
                alignBox();
            }
        }



    }

    private void setUpBitmap(){

        /**
         *
         * set up frame to draw from bitmap
         */

    }

    private void setMessageWidth(int currentMessage){
        int widestLine = 0;
        setupMatrix();
        messageW = barWidth;
        messageH = barHeight;
       targetW = (textManager.getTextWidthPoly(currentMessage));
       targetH = (textManager.getTextHeightPoly(currentMessage) );
        expandW = (targetW / messageW)  * .05f;
        expandH = (targetH / messageH)* .05f;
       // Matrix.scaleM(mModelMatrix[CENTER_SQUARE],0, 1f, 1f, 1f);



        this.posX = 0f;//(Constants.SCREEN_WIDTH /2 ) - (targetW /2 );  //scale * 200; //x - (messageW * .5f);// - messageW;
        this.posY = .5f;//(scale * 100);//(float)(Constants.SCREEN_HEIGHT * .75); //y - messageH - (blockSize * scale);
    }
    public String[] getText(){
        return values;
    }
    public float getSX(){
        return drawTX;
    }
    public float getSY(){
        return drawTY;
    }
    public void initMBox(Context context){
        this.initEntity(context, 1980,1980, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.UPPER_LEFT_CORNER, false);
        this.initEntity(context, 2000,1980, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.UPPER_CENTER_BAR, false);
        this.initEntity(context, 2020,1980, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.UPPER_RIGHT_CORNER, false);
        this.initEntity(context, 1980,2000, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.LEFT_BAR, false);
        this.initEntity(context, 1980,2020, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.LOWER_LEFT_CORNER, false);
        this.initEntity(context, 2000,2020, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.LOWER_CENTER_BAR, false);
        this.initEntity(context, 2020,2020, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.LOWER_RIGHT_CORNER, false);
        this.initEntity(context, 2020,2000, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.RIGHT_BAR, false);
        this.initEntity(context, 2000,2000, R.drawable.pos4,20, 20,1, 1,barWidth, barHeight, MessageBox.CENTER_SQUARE, false);

        //TODO create init method for just textures. I don't need the rest here.
        this.initEntity(context, 2000,2000, R.drawable.font,20, 20,0, 0,barWidth, barHeight, 9, false);
    }
    private void setupMatrix(){
        for(int i = UPPER_LEFT_CORNER ; i <= CENTER_SQUARE; i++) {
            Matrix.setIdentityM(mModelMatrix[i], 0);
            Matrix.translateM(mModelMatrix[i], 0, 0f, .5f, -2.501f);
        }
        Matrix.setIdentityM(mTextModelMatrix, 0);
        Matrix.translateM(mTextModelMatrix, 0, 0f, .5f, -2.501f);

    }

    private void alignBox(){

        //setup center box real bounds
        centerBounds[0] = (mModelMatrix[CENTER_SQUARE][12] + (mModelMatrix[CENTER_SQUARE][0] * this.getObjectBounds(CENTER_SQUARE)[0]));
        centerBounds[1] = (mModelMatrix[CENTER_SQUARE][13] + (mModelMatrix[CENTER_SQUARE][5] * this.getObjectBounds(CENTER_SQUARE)[1]));
        centerBounds[2] = (mModelMatrix[CENTER_SQUARE][12] + (mModelMatrix[CENTER_SQUARE][0] * this.getObjectBounds(CENTER_SQUARE)[2]));
        centerBounds[3] = (mModelMatrix[CENTER_SQUARE][13] + (mModelMatrix[CENTER_SQUARE][5] * this.getObjectBounds(CENTER_SQUARE)[3]));

        //align text
        mTextModelMatrix[12] = centerBounds[0];
        mTextModelMatrix[13] = centerBounds[3];
        //setup upper left corner
        mModelMatrix[UPPER_LEFT_CORNER][12] = (centerBounds[0] + this.getObjectBounds(UPPER_LEFT_CORNER)[0]);
        mModelMatrix[UPPER_LEFT_CORNER][13] = (centerBounds[1] + this.getObjectBounds(UPPER_LEFT_CORNER)[1]);
        //setup upper right corner
        mModelMatrix[UPPER_RIGHT_CORNER][12] = (centerBounds[2] + this.getObjectBounds(UPPER_RIGHT_CORNER)[2]);
        mModelMatrix[UPPER_RIGHT_CORNER][13] = (centerBounds[1] + this.getObjectBounds(UPPER_RIGHT_CORNER)[1]);
        
        //setup lower left corner
        mModelMatrix[LOWER_LEFT_CORNER][12] = (centerBounds[0] + this.getObjectBounds(LOWER_LEFT_CORNER)[0]);
        mModelMatrix[LOWER_LEFT_CORNER][13] = (centerBounds[3] + this.getObjectBounds(LOWER_LEFT_CORNER)[3]);
        //setup lower right corner
        mModelMatrix[LOWER_RIGHT_CORNER][12] = (centerBounds[2] + this.getObjectBounds(LOWER_RIGHT_CORNER)[2]);
        mModelMatrix[LOWER_RIGHT_CORNER][13] = (centerBounds[3] + this.getObjectBounds(LOWER_RIGHT_CORNER)[3]);

        //setup top center bar
        mModelMatrix[UPPER_CENTER_BAR][12] = mModelMatrix[CENTER_SQUARE][12];
        mModelMatrix[UPPER_CENTER_BAR][13] = (centerBounds[1] + this.getObjectBounds(UPPER_CENTER_BAR)[1]);
        mModelMatrix[UPPER_CENTER_BAR][0] = mModelMatrix[CENTER_SQUARE][0];

        //setup bottom center bar
        mModelMatrix[LOWER_CENTER_BAR][12] = mModelMatrix[CENTER_SQUARE][12];
        mModelMatrix[LOWER_CENTER_BAR][13] = (centerBounds[3] + this.getObjectBounds(LOWER_CENTER_BAR)[3]);
        mModelMatrix[LOWER_CENTER_BAR][0] = mModelMatrix[CENTER_SQUARE][0];
        
        //setup left bar
        mModelMatrix[LEFT_BAR][12] = (centerBounds[0] + this.getObjectBounds(LEFT_BAR)[0]);
        mModelMatrix[LEFT_BAR][13] = mModelMatrix[CENTER_SQUARE][13];
        mModelMatrix[LEFT_BAR][5] = mModelMatrix[CENTER_SQUARE][5];
        //setup right bar
        mModelMatrix[RIGHT_BAR][12] = (centerBounds[2] + this.getObjectBounds(RIGHT_BAR)[2]);
        mModelMatrix[RIGHT_BAR][13] = mModelMatrix[CENTER_SQUARE][13];
        mModelMatrix[RIGHT_BAR][5] = mModelMatrix[CENTER_SQUARE][5];



    }
    /*
    public float[] getmModelMatrix(){
        return mModelMatrix;
    }
    */
    public void setSY(float SY){
        drawTY = SY;

    }
    @Override
    public void draw(int mTextureUniformHandle,int mTextureCoordinateHandle, int mPositionHandle,int mMVMatrixHandle, int mMVPMatrixHandle,float[] mProjectionMatrix,  float[] mMVPMatrix, float[] mViewMatrix){
        /**
         *
         * draw message
         */
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 );
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[UPPER_LEFT_CORNER]);

        if(activeMessage){

            for(int i = UPPER_LEFT_CORNER; i <= CENTER_SQUARE; i++ ) {
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[i]);
                GLES20.glEnableVertexAttribArray(mPositionHandle);
                GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
                // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

                // Pass in the texture coordinate information -- currently not using vbo

                GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textVBOHandle[i]);

                GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                        0, 0);


                //Log.e("advance", Float.toString(mModelmatrix[12]));
                Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix[i], 0);
                GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

                Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

                GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
            }
            /**
             *
             * draw Text
             */
            if(drawText) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
                // Bind the texture to this unit.
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[1]);


                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textManager.getIndicesVBO(currentMessage));
                GLES20.glEnableVertexAttribArray(mPositionHandle);
                GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize, GLES20.GL_FLOAT, false, 0, 0);
                // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

                // Pass in the texture coordinate information -- currently not using vbo

                GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, textManager.getUVVbo(currentMessage));

                GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                        0, 0);


                //Log.e("advance", Float.toString(mModelmatrix[12]));
                Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mTextModelMatrix, 0);
                GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

                Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

                GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, textManager.getTotalPolyDraw(currentMessage));
            }

        }



    }
    @Override
    public void loadVBOs(){
        super.loadVBOs();
        textManager.loadVBOs();
    }
}
