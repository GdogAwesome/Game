package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.example.bradmobile.testtexture.Utils.*;

import java.nio.FloatBuffer;

/**
 * Created by BradMobile on 1/1/2018.
 */

public class Entity {

    protected float[] polyCoordsF;
    protected float[] textCoordsF;

    Bitmap temp;

    protected FloatBuffer[] polyCoordsFB = new FloatBuffer[12];
    protected FloatBuffer[] textCoordsFB = new FloatBuffer[12];

    protected int horizontalAnimFrames;
    protected int verticleAnimFrames;

    protected float textureFrameWidth;
    protected float textureFrameHeight;

    protected int objectCount = 0;
    protected int totalObjects = 0;
    protected  int tuOffset = 0;

    private int maxObjects = 16;
    
    protected int[] polyVBOHandle = new int[maxObjects];
    protected int[] textVBOHandle = new int [maxObjects];
    protected int[] objectList = new int [maxObjects];
    protected float[][] objectBounds = new float[maxObjects][4];

    protected int[] textureHandles = new int[maxObjects];
    protected int[] textures = new int[maxObjects];
    protected int[] objectTexture = new int[maxObjects];
    protected int totalTextures = 0;
    protected int lastVBO = 0;

    protected int mTextureCoordSize = 2;
    protected int mPositionSize = 3;

    protected int animState;
    Context context;

    protected float[] mModelMatrix = new float[16];

    public Entity(){

    }

    /**
     * @param context
     * @param texture
     * @param tilePixelWidth
     * @param tilePixelHeight
     * @param horizontalAnimFrames
     * @param verticleAnimFrames
     * @param polyWidth
     * @param polyHeight
     */

    public void initEntity(Context context,int startX, int startY, int texture, int tilePixelWidth, int tilePixelHeight, int horizontalAnimFrames, int verticleAnimFrames, float polyWidth, float polyHeight, int objectCount, boolean leftAlign){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        boolean textureLoaded = false;
        int currentTexture = 0;

        this.objectCount = objectCount;
        objectList[totalObjects] = objectCount;

        for(int i = 0; i <= totalTextures; i++){
            if(texture == textures[i] && textureLoaded == false){
                textureLoaded = true;
                currentTexture = i;

            }

        }

        if(textureLoaded){
            objectTexture[objectList[totalObjects]] = textures[currentTexture];

        }else{
            textures[totalTextures] = texture;
            objectTexture[objectList[totalObjects]] = textures[totalTextures];
            totalTextures++;

        }



        this.context = context;

        BitmapFactory.decodeResource(context.getResources(),texture,options);


        createTextureCoords(startX, startY,options.outWidth, options.outHeight, tilePixelWidth, tilePixelHeight, horizontalAnimFrames,verticleAnimFrames);
        createPolyCoords(polyWidth, polyHeight ,1,1, leftAlign);
        createFloatBuffers();
        //loadVBOs();

        setUpModelMatrix();

        totalObjects ++;

    }



    public void initLinkedPolyEntity(Context context,int startX, int startY, int texture, int tilePixelWidth, int tilePixelHeight, int horizontalAnimFrames, int verticleAnimFrames, float polyWidth, float polyHeight, int objectCount){


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inScaled = false;
        boolean textureLoaded = false;
        int currentTexture = 0;

        this.objectCount = objectCount;
        objectList[totalObjects] = objectCount;

        for(int i = 0; i <= totalTextures; i++){
            if(texture == textures[i] && textureLoaded == false){
                textureLoaded = true;
                currentTexture = i;

            }

        }

        if(textureLoaded){
            objectTexture[objectList[totalObjects]] = textures[currentTexture];

        }else{
            textures[totalTextures] = texture;
            objectTexture[objectList[totalObjects]] = textures[totalTextures];
            totalTextures++;

        }

        this.context = context;

        BitmapFactory.decodeResource(context.getResources(),texture,options);


        createTextureCoords(startX, startY,options.outWidth, options.outHeight, tilePixelWidth, tilePixelHeight, horizontalAnimFrames,verticleAnimFrames);
        createPolyCoords(polyWidth, polyHeight ,horizontalAnimFrames,verticleAnimFrames, false);
        createFloatBuffers();
        //loadVBOs();

        setUpModelMatrix();

        totalObjects ++;

    }

    /**
     *
     *
     * @param textureWidth
     * @param textureHeight
     * @param tilePixelWidth
     * @param tilePixelHeight
     * @param horizontalAnimFrames
     * @param verticleAnimFrames
     */
    protected void createTextureCoords(int startX, int startY,int textureWidth, int textureHeight, int tilePixelWidth, int tilePixelHeight, int horizontalAnimFrames, int verticleAnimFrames){

        textureFrameHeight = (float)tilePixelHeight /  (float)textureHeight ;
        textureFrameWidth = (float)tilePixelWidth / (float)textureWidth;

        float offsetX = 0f;
        float offsetY = 0f;

        if(startX > 0) {
            offsetX = (float)startX / (float)textureWidth;
        }
        if (startY > 0) {
            offsetY = (float)startY / (float)textureHeight;
        }

        
        this.horizontalAnimFrames = horizontalAnimFrames;
        this.verticleAnimFrames = verticleAnimFrames;
        
        int textCoordsPerFrame = 12;
        int counter = 0;
        int index = 0;
        textCoordsF = new float[(horizontalAnimFrames * verticleAnimFrames) * textCoordsPerFrame];


        for(int v = 0; v < verticleAnimFrames; v++){
            for(int h = 0; h < horizontalAnimFrames; h++){
                
                index = counter * textCoordsPerFrame;

                textCoordsF[index] = h * textureFrameWidth + offsetX;
                textCoordsF[index + 1] = v * textureFrameHeight + offsetY;
                textCoordsF[index + 2] = h * textureFrameWidth + offsetX;
                textCoordsF[index + 3] = (v * textureFrameHeight) + textureFrameHeight + offsetY;
                textCoordsF[index + 4] = (h * textureFrameWidth) + textureFrameWidth  + offsetX;
                textCoordsF[index + 5] = v * textureFrameHeight + offsetY;
                textCoordsF[index + 6] = (h * textureFrameWidth) + textureFrameWidth  + offsetX;
                textCoordsF[index + 7] = v * textureFrameHeight + offsetY;
                textCoordsF[index + 8] = h * textureFrameWidth + offsetX;
                textCoordsF[index + 9] = (v * textureFrameHeight) + textureFrameHeight + offsetY;
                textCoordsF[index + 10] = (h * textureFrameWidth) + textureFrameWidth + offsetX;
                textCoordsF[index + 11] = (v * textureFrameHeight) + textureFrameHeight + offsetY;
                
                counter++;

            }
        }

    }

    /**
     *
     * @param _polyWidth
     * @param _polyHeight
     * @param horizontalPolys
     * @param verticlePolys
     */
    protected void createPolyCoords( float _polyWidth,  float _polyHeight, int horizontalPolys, int verticlePolys , boolean leftAlign){

        float polyWidth = _polyWidth / (float)horizontalPolys;
        float polyHeight = _polyHeight / (float)verticlePolys;

        float halfWidth = (float)horizontalPolys /2f;
        float halfHeight = (float)verticlePolys /2f;
        //Log.e("halfWidth", Float.toString(halfWidth));

        int arraySize = horizontalPolys * verticlePolys * 18;

        float halfPolyWidth = polyWidth * .5f;
        float halfPolyHeight = polyHeight * .5f;

        float Left;
        float Right;
        if(!leftAlign) {
             Left = (-1f * (halfWidth * polyWidth));
             Right = (halfWidth * polyWidth);
        }else{
            Left = 0f;
            Right = (polyWidth * ((float)horizontalPolys));
        }


        float Top = (halfHeight *  polyHeight);
        float Bottom = (-1f *(halfHeight * polyHeight));

        objectBounds[objectCount][0] = Left;
        objectBounds[objectCount][1] = Top;
        objectBounds[objectCount][2] = Right;
        objectBounds[objectCount][3] = Bottom;

        polyCoordsF = new float[arraySize];

        int counter = 0;
        int index = 0;

        for(float y = Top; y >= (Bottom + polyHeight) ; y -= polyHeight){
            for(float x = Left; x <= (Right - polyWidth)  ; x += polyWidth){
                index = counter * 18;

                polyCoordsF[index] = x;
                polyCoordsF[index + 1 ] = y;
                polyCoordsF[index + 2 ] = 1.0f;
                polyCoordsF[index + 3] = x;
                polyCoordsF[index + 4 ] = y - polyHeight;
                polyCoordsF[index + 5 ] = 1.0f;
                polyCoordsF[index + 6 ] = x + polyWidth;
                polyCoordsF[index + 7] = y;
                polyCoordsF[index + 8 ] = 1.0f;

                polyCoordsF[index + 9 ] = x + polyWidth;
                polyCoordsF[index + 10] = y;
                polyCoordsF[index + 11] = 1.0f;
                polyCoordsF[index + 12] = x;
                polyCoordsF[index + 13] = y - polyHeight;
                polyCoordsF[index + 14] = 1.0f;
                polyCoordsF[index + 15] = x + polyWidth;
                polyCoordsF[index + 16] = y - polyHeight;
                polyCoordsF[index + 17] = 1.0f;


                counter++;
            }
        }


    }


    /**
     *
     * @param mTextureUniformHandle
     * @param mTextureCoordinateHandle
     * @param mPositionHandle
     * @param mMVMatrixHandle
     * @param mMVPMatrixHandle
     * @param mProjectionMatrix
     * @param mMVPMatrix
     * @param mViewMatrix
     */
    public void draw(int mTextureUniformHandle,int mTextureCoordinateHandle, int mPositionHandle,int mMVMatrixHandle, int mMVPMatrixHandle,float[] mProjectionMatrix,  float[] mMVPMatrix, float[] mViewMatrix){

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandles[0]);


        /**
         *

         */
        //Log.e("VBO", Integer.toString(frameVBO));
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, polyVBOHandle[objectCount]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize,GLES20.GL_FLOAT,false,0,0);
        // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);

        // Pass in the texture coordinate information -- currently not using vbo

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        textCoordsFB[0].position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                        0, textCoordsFB[0]);


                //Log.e("advance", Float.toString(mModelmatrix[12]));
                Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
                GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

                Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

                GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);


        /**
         *
         *
         * drawScore
         */
        //scoreString = Integer.toString(totalScore);

        // canvas.drawText(scoreString,SCREEN_WIDTH - 200, 0,paint);

    }
    
    

    protected void createFloatBuffers(){

        polyCoordsFB[objectCount] = BufferUtils.getFloatBuffer(polyCoordsF, 4);
        textCoordsFB[objectCount] = BufferUtils.getFloatBuffer(textCoordsF , 4);
    }
    protected void setUpModelMatrix(){
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix,0, 0.0f, 0.0f, -2.51f);
    }



    public void loadVBOs(){

        for( int i = lastVBO ; i <  totalObjects; i++) {
            this.polyVBOHandle[objectList[i]] = getPositionVBO(polyCoordsFB[objectList[i]])[0];

            this.textVBOHandle[objectList[i]] = getPositionVBO(textCoordsFB[objectList[i]])[0];

            lastVBO = i + 1;
        }


    }


    public void setTextureHandle(){
        this.textureHandles = TextureUtils.LoadTexture(context,textures,totalTextures);

        /**
         *
         * set object texture from android integer lookup to texture handle
         */
        for(int i = 0; i < totalObjects; i++){
            for(int k = 0; k< totalTextures; k++){
                if(objectTexture[objectList[i]] == textures[k]){
                    objectTexture[objectList[i]] = textureHandles[k];
                }
            }
        }
    }
    public float[] getObjectBounds(int object){
        return objectBounds[object];
    }
    public int getObjectTextureHandle(int object){
        return objectTexture[object];
    }


    public static int[] getPositionVBO(FloatBuffer mPositionData){
        int[] tempBuffer = new int[1];
        mPositionData.rewind();
        //set up buffers
        GLES20.glGenBuffers(1,tempBuffer,0);
        //bind buffers
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tempBuffer[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mPositionData.capacity() * 4, mPositionData, GLES20.GL_STATIC_DRAW);
        //unbind buffers
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);



        return tempBuffer;
    }
    public void nullImage(){


        for(int i = 0; i < totalTextures; i ++){
            Log.e("Textur id for index" + i +"", Integer.toString(textureHandles[i]));
        }
        GLES20.glDeleteTextures(totalTextures,textureHandles,0);

    }

}
