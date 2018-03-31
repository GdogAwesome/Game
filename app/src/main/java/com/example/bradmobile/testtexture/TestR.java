package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.example.bradmobile.testtexture.Utils.TextureUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by BradMobile on 11/25/2016.
 */

public class TestR implements GLSurfaceView.Renderer {



    private int mColorOffset = 0;
    private int mPositionOffset =0;
    private int mBytesPerFloat = 4;
    private int mColorSize = 4;
    private int mNormalSize = 3;
    private int mTextureCoordSize = 2;
    private ShotEntity shots;
    private EnemyContainer enemies;
    private Controller controller;
    private boolean hasGameController;
    public int textureLocation = 0;


    private int[] VBO = new int[4];

    private float ratio = 1;

    private Map1 map;

    private int mPositionSize = 3;
    private int mColorStrideBytes = mColorSize * mBytesPerFloat;
    private int mPositionStrideBytes = mPositionSize * mBytesPerFloat;

    //this matrix is used as the camera
    private float[] mViewMatrix = new float[16];

    //this matrix is used to store the final matrix to be sent to the shader
    private float[] mMVPMatrix = new float[16];

    //this matrix transforms everything to 2d screen space
    private float[] mProjectionMatrix = new float[16];
    /**
     * Stores a copy of the model matrix specifically for the light position.
     */
    private float[] mLightModelMatrix = new float[16];

    /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     *  we multiply this by our transformation matrices. */
    private final float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};

    /** Used to hold the current position of the light in world space (after transformation via model matrix). */
    private final float[] mLightPosInWorldSpace = new float[4];

    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
    private final float[] mLightPosInEyeSpace = new float[4];

    private final int POSTITION_DATA_SIZE =  3;

    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mColorHandle;
    private int mPositionHandle;
    private int mLightPosHandle;
    private int mNormalHandle;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;

//Map objects
    private int[] mapTextures = new int[2];
   private FloatBuffer[] mapVertexCoords;
    private int mapTextureCoordVBO = 0;
    private int[] mapVertexCoordVBO = new int[2];
    private int[] mapTextureHandle = new int[2];
    private FloatBuffer[] mapTextureCoords;
    private float[][] mMapModelMatrix = new float[2][16];

    //hero objects

    private FloatBuffer heroTextureCoords;
    private float[] mHeroModelMatrix = new float[16];

    //control objects
    private int controlTexture = 0;
    private FloatBuffer controlTextureCoords;
    private FloatBuffer controlVertexCoords;
    private FloatBuffer rightControlTextureCoords;
    private FloatBuffer rightControlVertexCoords;
    private int controlTextureHandle = 0;
    private float[] mIdentityMatrix = new float[16];
    private boolean surfaceActive = false;



    private FloatBuffer mCubePosition;
    private FloatBuffer mCubeColors;
    private FloatBuffer mCubeNormals;


    private String vShader;
    private String fShader;

    private int vertexShaderHandle;
    private int fragmentShaderHandle;

    private int mPerVertexProgramHandle;
    private int mPointProgramHandle;

    private double frameTime = 1000000000/60D;
    private boolean running = true;
    HeroEntity hero;



    private Context context;


    TestR(Context context){
        this.context = context;




    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        // Use culling to remove back faces.
       //GLES20.glEnable(GLES20.GL_CULL_FACE);
        //GLES20.glDisable(GLES20.GL_POLYGON_OFFSET_FILL);

        // disable depth testing
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_ALWAYS);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        int[] textSize = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE,textSize, 0);

        Log.e("max texture size", Integer.toString(textSize[0]));

        //GLES20.glEnable(GLES20.GL_ALPHA);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = -0.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -1.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);


        vShader = getVertexShader();


        fShader = getFragmentShader();

        fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fShader);
        vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vShader);

        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});



        Log.e("surface", "created");


        setupObjects();


        Matrix.setIdentityM(mIdentityMatrix,0);
        Matrix.translateM(mIdentityMatrix, 0, 0.0f, 0.0f, -2.51f);

        /*

        get cube Handles
         */

        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle,"u_MVMatrix");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle,"u_MVPMatrix");
        mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");
        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle,"a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
        mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");


        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_TexCoordinate");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_Texture");

        // Set the active texture unit to texture unit 0.
       // GLES20.glActiveTexture(GLES20.GL_TEXTURE0);



        String pVShader = getPointVertexShader();
        String pFShader = getPointFragmentShader();
        final int pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, pVShader);
        final int pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, pFShader);

        mPointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,new String[] {"a_Position"});


        surfaceActive = true;
       



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);




        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -1;//-ratio;
        final float right = 1;// ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;
        Log.e("left", Float.toString(left));
        Log.e("right", Float.toString(right));

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);



        //Log.d("shader", fShader);

    }
    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);



        /**
         *
         *
         * drawCube

         */
        GLES20.glUseProgram(mPerVertexProgramHandle);

        drawMap();


        enemies.draw(mTextureUniformHandle, mTextureCoordinateHandle, mPositionHandle,mMVMatrixHandle,mMVPMatrixHandle,mProjectionMatrix,mMVPMatrix,mViewMatrix);
        shots.draw(mTextureUniformHandle, mTextureCoordinateHandle, mPositionHandle,mMVMatrixHandle,mMVPMatrixHandle,mProjectionMatrix,mMVPMatrix,mViewMatrix);
        hero.draw(mTextureUniformHandle, mTextureCoordinateHandle, mPositionHandle,mMVMatrixHandle,mMVPMatrixHandle,mProjectionMatrix,mMVPMatrix,mViewMatrix);
        if(!hasGameController) {
            controller.draw(mTextureUniformHandle, mTextureCoordinateHandle, mPositionHandle, mMVMatrixHandle, mMVPMatrixHandle, mProjectionMatrix, mMVPMatrix, mViewMatrix);
        }

    }


    private String getPointFragmentShader(){
        String shader = "";

        InputStream is = context.getResources().openRawResource(R.raw.point_f_shader);

        InputStreamReader sr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(sr);
        String temp = null;



        try {


            if(reader != null){
                while((temp = reader.readLine()) != null) {

                    shader += temp +"\n";
                }

            }

            is.close();
            sr.close();
            reader.close();

        }catch(IOException e){
            Log.e("shader not loaded", "", e);
        }finally{

        }
        return shader;

    }
    private String getPointVertexShader(){
        String shader = "";

        InputStream is = context.getResources().openRawResource(R.raw.point_v_shader);

        InputStreamReader sr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(sr);
        String temp = null;



        try {


            if(reader != null){
                while((temp = reader.readLine()) != null) {

                    shader += temp +"\n";
                }

            }

            is.close();
            sr.close();
            reader.close();

        }catch(IOException e){
            Log.e("shader not loaded", "", e);
        }finally{

        }
        return shader;



    }
    private String getVertexShader(){

        String shader = "";

        InputStream is = context.getResources().openRawResource(R.raw.v_shader);

        InputStreamReader sr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(sr);
        String temp = null;



        try {


        if(reader != null){
            while((temp = reader.readLine()) != null) {

                shader += temp +"\n";
            }

        }

            is.close();
            sr.close();
            reader.close();

        }catch(IOException e){
         Log.e("shader not loader", "", e);
        }finally{

        }
        return shader;


    }
    private String getFragmentShader(){

        String shader = "";

        InputStream is = context.getResources().openRawResource(R.raw.f_shader);

        InputStreamReader sr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(sr);
        String temp = null;

        try {


            if(reader != null){
                while((temp = reader.readLine()) != null) {

                    shader += temp +"\n";
                }

            }

            is.close();
            sr.close();
            reader.close();

        }catch(IOException e){
            Log.e("shader not loaded", "", e);
        }finally{

        }
        return shader;
    }

    private int compileShader(int shaderType, String shaderSource){
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if(shaderHandle != 0){

            GLES20.glShaderSource(shaderHandle,shaderSource);

            GLES20.glCompileShader(shaderHandle);


        }


        return shaderHandle;
    }

    private int createAndLinkProgram(int vHandle, int fHandle,String[] attributes){
        int programHandle = GLES20.glCreateProgram();

        if(programHandle != 0){

            GLES20.glAttachShader(programHandle,vHandle);

            GLES20.glAttachShader(programHandle, fHandle);

            if(attributes != null){

                final int size = attributes.length;
                for(int i =0; i < size; i++){

                    GLES20.glBindAttribLocation(programHandle,i, attributes[i]);
                }
            }

            GLES20.glLinkProgram(programHandle);


        }
        return programHandle;
    }

    public void setMapVertexCoord( FloatBuffer[] v){

        mapVertexCoords = v;

    }
    public void setMapTexture (int[] t){
        mapTextures = t;
        //Log.e("texture handle", Integer.toString(mapTextureHandle));

    }

    public void setmMapModelMatrix (float[][] m){
        mMapModelMatrix = m;
        for(int i= 0; i<16; i++){
            Log.e("map Matrix Layout " + i +"", Float.toString(mMapModelMatrix[0][i]));
        }
    }
    public void setMapTextureCoords(FloatBuffer[] t){
        mapTextureCoords = t;
    }

    public void setShots(ShotEntity s ){
        this.shots =s;
    }
    public void setEnemies(EnemyContainer enemies){
        this.enemies = enemies;
    }
    public void setHero(HeroEntity hero){
        this.hero = hero;

    }
    public void setController(Controller c){
        this.controller = c;
    }


    private void drawMap(){

        /**
         *
         *
         * draw map background
         */

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mapTextureHandle[1]);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mapVertexCoordVBO[1]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize,GLES20.GL_FLOAT,false,0,0);
        // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);

        // Pass in the texture coordinate information -- currently not using vbo
        mapTextureCoords[1].position(0);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                0, mapTextureCoords[1]);


        Matrix.multiplyMM(mMVPMatrix ,0,mViewMatrix, 0, mMapModelMatrix[1],0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mMVPMatrix,0);



        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,12);

        /**
         * Draw Map
         *
         *
         */


        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mapTextureHandle[0]);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mapVertexCoordVBO[0]);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionSize,GLES20.GL_FLOAT,false,0,0);
       // GLES20.glDrawElements(GLES20.GL_LINES, 360,GLES20.GL_FLOAT, mapVertexCoordVBO[0] );

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);

        // Pass in the texture coordinate information -- currently not using vbo
        mapTextureCoords[0].position(0);
        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordSize, GLES20.GL_FLOAT, false,
                0, mapTextureCoords[0]);


        Matrix.multiplyMM(mMVPMatrix ,0,mViewMatrix, 0, mMapModelMatrix[0],0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mMVPMatrix,0);



        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,360);
        //GLES20.glDrawArrays(GLES20.GL_LINES, 0, 360);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

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

    public boolean surfaceActive(){
        return surfaceActive;
    }
    public void clearMap(){


        for(int i = 0; i < 2; i ++){
            Log.e("map Textur id for index" + i +"", Integer.toString(mapTextureHandle[i]));
        }
        GLES20.glDeleteTextures(2,mapTextureHandle,0);

    }
    public void sceneChange(){

        hero.nullImage();
        //Map.nullImage();
        clearMap();
        enemies.nullImage();
        shots.nullImage();
        controller.nullImage();

    }
    public void setupObjects(){




        hero.loadVBOs();
        hero.setTextureHandle();
        mapTextureHandle = TextureUtils.LoadTexture(context,mapTextures,2);
        mapVertexCoordVBO[0] = getPositionVBO(mapVertexCoords[0])[0];
        mapVertexCoordVBO[1] = getPositionVBO(mapVertexCoords[1])[0];



        shots.loadVBOs();
        shots.setTextureHandle();

        enemies.loadVBOs();
        enemies.setTextureHandle();

        controller.loadVBOs();
        controller.setTextureHandle();
    }
    public void setHasGameController(boolean c){
        this.hasGameController = c;
    }
}
