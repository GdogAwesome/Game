package com.example.bradmobile.testtexture.ShaderLibrary;


import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.example.bradmobile.testtexture.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Shader {

    // setup handles for regular draw
    private static int mMVPMatrixHandle;
    private static int mMVMatrixHandle;
    private static int mPositionHandle;
    private static  int mTextureUniformHandle;
    private static  int mTextureCoordinateHandle;

    //setup handle for normal map draw
    private static  int mNormalTextureUniformHandle;
    private static  int mMVPNormalMatrixHandle;
    private static  int mMVNormalMatrixHandle;
    private static  int mNormalPositionHandle;
    private static  int mNormalTextureCoordinateHandle;
    private static  int mNormalTextureHandle;
    private static  int mLightPosHandle;
    private static  int mNormalPerVertexHandle;
    private static  int mNormalAnimFrame;
    private static int mNormalHasAnim;
    private String vShader;
    private String fShader;

    private int vertexShaderHandle;
    private int fragmentShaderHandle;
    private int normalVertexShaderHandle;
    private int normalFragmentShaderHandle;

    private int mPerVertexProgramHandle;


    Context context;

    public Shader(Context context){

        //TODO make this shader class more dynamic

        this.context = context;

    }
    public void LoadShaders(){
        setupNormalsShader();
        setupRegularShaders();
    }

    private String getVertexShader(int res){

        String shader = "";

        InputStream is = context.getResources().openRawResource(res);

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
    private String getFragmentShader(int res){

        String shader = "";

        InputStream is = context.getResources().openRawResource(res);

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
    private void setupNormalsShader(){
        vShader = getVertexShader(R.raw.v_shader_map);


        fShader = getFragmentShader(R.raw.f_shader_map);

        normalFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fShader);
        normalVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vShader);

        mNormalPerVertexHandle = createAndLinkProgram(normalVertexShaderHandle, normalFragmentShaderHandle,
                new String[] {"a_Position", "a_TexCoordinate, a_HasAnim"});


        mMVNormalMatrixHandle = GLES20.glGetUniformLocation(mNormalPerVertexHandle,"u_MVMatrix");
        mMVPNormalMatrixHandle = GLES20.glGetUniformLocation(mNormalPerVertexHandle,"u_MVPMatrix");
        mNormalPositionHandle = GLES20.glGetAttribLocation(mNormalPerVertexHandle,"a_Position");
        mLightPosHandle = GLES20.glGetUniformLocation(mNormalPerVertexHandle, "u_LightPos");
        mNormalAnimFrame = GLES20.glGetUniformLocation(mNormalPerVertexHandle, "u_AnimFrame");
        mNormalHasAnim = GLES20.glGetAttribLocation(mNormalPerVertexHandle, "a_HasAnim");

        mNormalTextureCoordinateHandle = GLES20.glGetAttribLocation(mNormalPerVertexHandle, "a_TexCoordinate");
        mNormalTextureUniformHandle = GLES20.glGetUniformLocation(mNormalPerVertexHandle, "u_Texture");
        mNormalTextureHandle = GLES20.glGetUniformLocation(mNormalPerVertexHandle, "u_NormalMap");

        Log.d("normal Texture Handle", Integer.toString(mNormalTextureHandle));



    }
    private void setupRegularShaders(){
        vShader = getVertexShader(R.raw.v_shader);


        fShader = getFragmentShader(R.raw.f_shader);

        fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fShader);
        vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vShader);

        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[] {"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});


        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle,"u_MVMatrix");
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle,"u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle,"a_Position");


        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_TexCoordinate");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_Texture");

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
    public void useNormalProgram(){
        GLES20.glUseProgram(mNormalPerVertexHandle);
    }
    public void useRegularProgram(){
        GLES20.glUseProgram(mPerVertexProgramHandle);


    }
    public static int getmMVPMatrixHandle() {
        return mMVPMatrixHandle;
    }

    public static int getmMVMatrixHandle() {
        return mMVMatrixHandle;
    }

    public static int getmPositionHandle() {
        return mPositionHandle;
    }

    public static int getmTextureUniformHandle() {
        return mTextureUniformHandle;
    }

    public static int getmTextureCoordinateHandle() {
        return mTextureCoordinateHandle;
    }

    public static int getmNormalTextureUniformHandle() {
        return mNormalTextureUniformHandle;
    }

    public static int getmMVPNormalMatrixHandle() {
        return mMVPNormalMatrixHandle;
    }

    public static int getmMVNormalMatrixHandle() {
        return mMVNormalMatrixHandle;
    }

    public static int getmNormalPositionHandle() {
        return mNormalPositionHandle;
    }

    public static int getmNormalTextureCoordinateHandle() {
        return mNormalTextureCoordinateHandle;
    }

    public static int getmNormalTextureHandle() {
        return mNormalTextureHandle;
    }

    public static int getmLightPosHandle() {
        return mLightPosHandle;
    }

    public static int getmNormalPerVertexHandle() {
        return mNormalPerVertexHandle;
    }

    public static int getmNormalAnimFrame() {
        return mNormalAnimFrame;
    }

    public static int getmNormalHasAnim() {
        return mNormalHasAnim;
    }

}
