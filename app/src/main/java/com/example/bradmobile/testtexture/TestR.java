package com.example.bradmobile.testtexture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.bradmobile.testtexture.Utils.BufferUtils;
import com.example.bradmobile.testtexture.Utils.TextureUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import com.example.bradmobile.testtexture.ShaderLibrary.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by BradMobile on 11/25/2016.
 */

public class TestR implements GLSurfaceView.Renderer {

    private int mTextureCoordSize = 2;

    private ShotEntity shots;
    private float frameVariance;
    private GLView glView;
    private EnemyContainer enemies;
    private Controller controller;
    private Shader shader;
    private Map1 map;
    private boolean playing = false;
    private boolean paused = false;
    private boolean hasGameController;
    private int mPositionSize = 3;
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
    private final float[] mLightPosInModelSpace = new float[] {-1.0f, 1.0f, 1.0f, 1.0f};

    /** Used to hold the current position of the light in world space (after transformation via model matrix). */
    private final float[] mLightPosInWorldSpace = new float[4];

    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
    private final float[] mLightPosInEyeSpace = new float[4];

    private final int POSTITION_DATA_SIZE =  3;


    private int[] normalMapTexture = new int[1];

//Map objects
    private int[] mapTextures = new int[3];
    private int[] testIndices = new int[240];
    private IntBuffer testIndicesBuffer;
   private FloatBuffer[] mapVertexCoords;
   private ShortBuffer mapAnimBuffer;
    private ShortBuffer drawIndices;
    private int[] mapVertexCoordVBO = new int[4];
    private int[] mapTextureHandle = new int[2];
    private FloatBuffer[] mapTextureCoords;
    private float[][] mMapModelMatrix = new float[3][16];

    //hero objects

    private FloatBuffer heroTextureCoords;
    private float[] mHeroModelMatrix = new float[16];


    private float[] mIdentityMatrix = new float[16];

    private boolean surfaceActive = false;


    HeroEntity hero;



    private Context context;


    TestR(Context context, GLView glview){
        this.glView = glview;
        this.context = context;
        shader = new Shader(context);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        //Log.e("texture compression", gl.glGetString(gl.GL_EXTENSIONS));
        // Use culling to remove back faces.
        // disable depth testing
        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDepthFunc(GLES20.GL_ALWAYS);

        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glEnable(GLES20.GL_BLEND);

        int[] textSize = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE,textSize, 0);

        Log.e("max texture size", Integer.toString(textSize[0]));

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

        shader.LoadShaders();
        setupObjects();


        Matrix.setIdentityM(mIdentityMatrix,0);
        Matrix.translateM(mIdentityMatrix, 0, 0.0f, 0.0f, -2.5f);


        surfaceActive = true;
       



    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);
        String extensions = gl.glGetString(GL10.GL_EXTENSIONS);
        Log.e("extensions", extensions);





        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -1;//-ratio;
        final float right = 1;// ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;//1.0f
        final float far = 3.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);



        //Log.d("shader", fShader);

    }
    @Override
    public void onDrawFrame(GL10 glUnused) {

        /**
         *
         *
         * drawCube

         */
        if(playing) {

            // TODO move this below drawMap after setting up map shader
            shader.useRegularProgram();


            drawMap();

            shader.useRegularProgram();
            enemies.draw(shader.getmTextureUniformHandle(), shader.getmTextureCoordinateHandle(), shader.getmPositionHandle(), shader.getmMVMatrixHandle(), shader.getmMVPMatrixHandle(), mProjectionMatrix, mMVPMatrix, mViewMatrix);
            hero.draw(shader.getmTextureUniformHandle(), shader.getmTextureCoordinateHandle(), shader.getmPositionHandle(), shader.getmMVMatrixHandle(), shader.getmMVPMatrixHandle(), mProjectionMatrix, mMVPMatrix, mViewMatrix);
            shots.draw(shader.getmTextureUniformHandle(), shader.getmTextureCoordinateHandle(), shader.getmPositionHandle(), shader.getmMVMatrixHandle(), shader.getmMVPMatrixHandle(), mProjectionMatrix, mMVPMatrix, mViewMatrix);
            if (!hasGameController) {
                controller.draw(shader.getmTextureUniformHandle(), shader.getmTextureCoordinateHandle(), shader.getmPositionHandle(), shader.getmMVMatrixHandle(), shader.getmMVPMatrixHandle(), mProjectionMatrix, mMVPMatrix, mViewMatrix);
            }
            frameVariance = Constants.getFrameVariance(System.currentTimeMillis());
            if(!paused) {
                glView.tick(frameVariance);
            }

        }

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

        //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT );


        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(shader.getmTextureUniformHandle(), 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mapVertexCoordVBO[1]);
        GLES20.glEnableVertexAttribArray(shader.getmPositionHandle());
        GLES20.glVertexAttribPointer(shader.getmPositionHandle(), mPositionSize,GLES20.GL_FLOAT,false,0,0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);

        // Pass in the texture coordinate information -- currently not using vbo
        mapTextureCoords[1].rewind();//.position(0);
        GLES20.glEnableVertexAttribArray(shader.getmTextureCoordinateHandle());
        GLES20.glVertexAttribPointer(shader.getmTextureCoordinateHandle(), mTextureCoordSize, GLES20.GL_FLOAT, false,
                0, mapTextureCoords[1]);


        //Draw background 1
        Matrix.multiplyMM(mMVPMatrix ,0,mViewMatrix, 0, mMapModelMatrix[1],0);
        GLES20.glUniformMatrix4fv(shader.getmMVMatrixHandle(), 1, false, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(shader.getmMVPMatrixHandle(),1,false,mMVPMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,6);


        //Draw background 2
        Matrix.multiplyMM(mMVPMatrix ,0,mViewMatrix, 0, mMapModelMatrix[2],0);
        GLES20.glUniformMatrix4fv(shader.getmMVMatrixHandle(), 1, false, mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(shader.getmMVPMatrixHandle(),1,false,mMVPMatrix,0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0,6);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        /**
         * Draw Map
         *
         *
         */

        shader.useNormalProgram();

        GLES20.glUniform1i(shader.getmNormalTextureHandle(), 1);
        GLES20.glUniform1i(shader.getmNormalTextureUniformHandle(), 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, normalMapTexture[0]);


        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mapTextureHandle[0]);



        //setup up light position
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.5f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);

        GLES20.glUniform3f(shader.getmLightPosHandle(), mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        GLES20.glUniform3f(shader.getmNormalLightColorHandle(), map.getAmbientLightColor()[0], map.getAmbientLightColor()[1], map.getAmbientLightColor()[2]);

        //pass in vertex coords
        GLES20.glEnableVertexAttribArray(shader.getmNormalPositionHandle());
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mapVertexCoordVBO[0]);
        //GLES20.glVertexAttribPointer(shader.getmNormalPositionHandle(), mPositionSize,GLES20.GL_FLOAT,false,0, 0);
        GLES20.glVertexAttribPointer(shader.getmNormalPositionHandle(), mPositionSize,GLES20.GL_FLOAT,false,0, 0);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);

        // pass in animframe coords
        //mapAnimBuffer.position(map.getAnimBufferOffset());//10 * 6 * 4 );
        GLES20.glEnableVertexAttribArray(shader.getmNormalHasAnim());
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mapVertexCoordVBO[2]);
        GLES20.glVertexAttribPointer(shader.getmNormalHasAnim(), 1, GLES20.GL_SHORT, false,  0, map.getAnimBufferOffset());

        // pass in anim frame pos
        GLES20.glUniform1i(shader.getmNormalAnimFrame(), (map.getAnimFrame()));


        // Pass in the texture coordinate information -- currently not using vbo

        //mapTextureCoords[0].position(map.getTextureBufferOffset());// 6 * 4 * 2);
        GLES20.glEnableVertexAttribArray(shader.getmNormalTextureCoordinateHandle());
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mapVertexCoordVBO[3]);
        GLES20.glVertexAttribPointer(shader.getmNormalTextureCoordinateHandle(), mTextureCoordSize, GLES20.GL_FLOAT, false,
                0,  map.getTextureBufferOffset());


        Matrix.multiplyMM(mMVPMatrix ,0,mViewMatrix, 0, mMapModelMatrix[0],0);
        GLES20.glUniformMatrix4fv(shader.getmMVNormalMatrixHandle(), 1, false, mMVPMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(shader.getmMVPNormalMatrixHandle(),1,false,mMVPMatrix,0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, map.getPrimativesToDraw(), GLES20.GL_UNSIGNED_SHORT, drawIndices);


        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

    }

    public void setMapVertexCoord( FloatBuffer[] v){

        mapVertexCoords = v;

    }
    public void setMapDrawIndices(ShortBuffer d){
        this.drawIndices = d;
    }
    public void setMapTexture (int[] t){
        mapTextures = t;
        //Log.e("texture handle", Integer.toString(mapTextureHandle));

    }
    public void setMapAnimBuffer(ShortBuffer a){
        this.mapAnimBuffer = a;
    }

    public void setmMapModelMatrix (float[][] m){
        mMapModelMatrix = m;

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
    public void setMap(Map1 map){
        this.map = map;
    }
    public void setController(Controller c){
        this.controller = c;
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
    public static int[] getPositionVBOShort(ShortBuffer mPositionData){
        int[] tempBuffer = new int[1];
        mPositionData.rewind();
        //set up buffers
        GLES20.glGenBuffers(1,tempBuffer,0);
        //bind buffers
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, tempBuffer[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mPositionData.capacity() * 2, mPositionData, GLES20.GL_STATIC_DRAW);
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
        GLES20.glDeleteTextures(1, normalMapTexture, 0);

    }
    public void sceneChange(){

        hero.nullImage();
        //Map.nullImage();
        clearMap();
        enemies.nullImage();
        shots.nullImage();
        controller.nullImage();

    }
    public void setRender(boolean p){
        this.playing = p;
    }
    public void setPaused(boolean p){
        this.paused = p;
    }
    public void setupObjects(){


        hero.loadVBOs();
        hero.setTextureHandle();
        int[] temp = new int[1];
        int[] temp2 = new int[2];
        temp[0] = mapTextures[2];
        temp2[0] = mapTextures[0];
        temp2[1] = mapTextures[1];

        mapTextureHandle = TextureUtils.LoadTexture(context,temp2,2);
        //Log.e("map texture handle", Integer.toString(mapTextureHandle[1]));
        normalMapTexture = TextureUtils.LoadNormalsTexture(context,temp,1);
        mapVertexCoordVBO[0] = getPositionVBO(mapVertexCoords[0])[0];
        mapVertexCoordVBO[1] = getPositionVBO(mapVertexCoords[1])[0];
        mapVertexCoordVBO[2] = getPositionVBOShort(mapAnimBuffer)[0];
        mapVertexCoordVBO[3] = getPositionVBO(mapTextureCoords[0])[0];
        mapVertexCoords[0] = null;
        mapVertexCoords[1] = null;
        mapAnimBuffer = null;
        mapTextureCoords[0] = null;

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
