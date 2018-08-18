package com.example.bradmobile.testtexture.TextLib;

import android.opengl.GLES20;
import android.util.Log;

import com.example.bradmobile.testtexture.Entity;
import com.example.bradmobile.testtexture.Utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by b_hul on 3/16/2018.
 */

public class TextManager  {
    private int textSize = 0;
    private float[] totalWidth ;
    private float[] totalHeight;

    private float[][] FCharIndices;
    private float[][] FCharUVs;
    private int[] indicesVBOHandles;
    private int[] UVVBOHandles;
    private int[] totalCharCount;
    private float[] lineWidths;

    private FloatBuffer[] FBCharIndices;
    private FloatBuffer[] FBCharUVs;

    private static final int INDICES_PER_BOX = 18;
    private static final int UVS_PER_BOX = 12;
    private static final float UV_BOX_WIDTH = 64f / 512f;
    private int totalMessages = 0;
    private float textHeightPoly = .1f;
    private float textWidthPoly = .5625f *textHeightPoly;
    private float scaleWidthOffLetter = 1f;

    private int uvIndex = 0;
    private int charCounter = 0;
    private int polyIndex = 0;

    private int UVCharSize = 64;
    private String[] values;
    private char[] currentLnChar;


    public static int[] l_size = {36,29,30,34,25,25,34,33,
            11,20,31,24,48,35,39,29,
            42,31,27,31,34,35,55,35,
            31,27,30,26,28,26,31,28,
            28,28,29,29,14,24,30,18,
            26,14,14,14,25,28,31,0,
            0,38,39,12,36,34,0,0,
            0,38,0,0,0,32,0,32};



    public TextManager(){

    }

    public void LoadText(String[] text){


        float tempHeight = 0f;
        float tempWidth;
        totalMessages = text.length;
        int[] lineCount = new int[totalMessages];

        totalWidth = new float[totalMessages];
        totalHeight = new float[totalMessages];
        totalCharCount = new int[totalMessages];

        /**
         * get size of message
         */
        int totalChars = 0;
        for(int tm = 0; tm < totalMessages; tm ++) {
            values = text[tm].split("/n");
            lineCount[tm] = 0;

            for (int i = 0; i < values.length; i++) {

                        currentLnChar = values[i].toCharArray();
                        charCounter += currentLnChar.length;
                        lineCount[tm] ++;

            }
        }


        /**
         *
         * load Chars into float
         *
         */
        // prepare VBO arrays
        indicesVBOHandles = new int[totalMessages];
        UVVBOHandles = new int[totalMessages];
        // prepare float arrays for loading chars
        FCharIndices = new float[totalMessages][charCounter * INDICES_PER_BOX];
        FCharUVs = new float[totalMessages][charCounter * UVS_PER_BOX];

        FBCharUVs = new FloatBuffer[totalMessages];
        FBCharIndices = new FloatBuffer[totalMessages];

        /**
         *
         * load values from chars into corresponding array and get height and width values
         */
        for(int tm = 0; tm < totalMessages; tm ++) {
            values = text[tm].split("/n");
            totalWidth[tm]= 0f;
            totalHeight[tm] = 0f;
            charCounter = 0;
            totalCharCount[tm] = 0;
            int lineCounter = 0;
            tempWidth = 0f;

            for (int i = (values.length - 1); i > -1; i--) {

                        currentLnChar = values[i].toCharArray();
                        tempWidth = convertCharArrayToPolyCoords(currentLnChar, tm, lineCounter);


                if(tempWidth > totalWidth[tm]){
                    totalWidth[tm] = tempWidth;
                    Log.e("totalWidth", Float.toString(totalWidth[tm]));
                }
                tempHeight = (i +1) * textHeightPoly;
                if(tempHeight > totalHeight[tm]){
                    totalHeight[tm] = tempHeight;
                }
                lineCounter ++;
            }
            /**
             *
             * center text
             */
            /*
            for(int i = 0; i< tempWidth.length; i++){
                if(tempWidth[i] < totalWidth[tm]){
                    float sizeDif = (totalWidth[tm] - tempWidth[i])/ 2;
                    for(int k = 0; k < FCharIndices[tm].length; k+=3){
                        FCharIndices[tm][k] += sizeDif;
                    }
                }
            }
            */
        }
        createFloatBuffers();

    }

    private float convertCharArrayToPolyCoords(char[] lineChrArray, int messageIndex, int lineIndex){

        float uPos1;
        float vPos1 ;
        float uPos2;
        float vPos2 ;
        int uvIndex;
        int polyIndex;
        float lineWidth = 0f;
        int asciiVal;
        int charIndex = 0;
        float scaleOfCharWidth = 1f;
        float polyXPos1 = lineWidth;
        float polyYPos1 = (lineIndex *  textHeightPoly);
        float polyXPos2 = polyXPos1 + textWidthPoly;
        float polyYPos2 = (polyYPos1 + textHeightPoly);

        for(int i =0; i < lineChrArray.length; i++){
            asciiVal = (int)lineChrArray[i];
           charIndex =  convertCharToIndex( asciiVal);
           if(charIndex == -1){
               charIndex = 61;
           }
           scaleOfCharWidth = ((float)l_size[charIndex] / (float)UVCharSize);

           int row = charIndex / 8;
           int col = charIndex % 8;

           uvIndex = charCounter * UVS_PER_BOX;
           polyIndex = charCounter * INDICES_PER_BOX;

           vPos1 = row * UV_BOX_WIDTH ;
           vPos2 = vPos1 + UV_BOX_WIDTH;
           uPos1 = col * UV_BOX_WIDTH;
           uPos2 = uPos1 + (UV_BOX_WIDTH * scaleOfCharWidth);

           polyXPos1 = lineWidth;
           polyXPos2 = polyXPos1 + (scaleOfCharWidth * textWidthPoly);
           /*
            Log.e("v pos 1", Float.toString(vPos1));
            Log.e("v pos 2", Float.toString(vPos2));
            Log.e("u pos 1", Float.toString(uPos1));
            Log.e("u pos 2", Float.toString(uPos2));
            */
            /**
             *
             * setup uv coordinates
             */
            FCharUVs[messageIndex][uvIndex] = uPos1;
            FCharUVs[messageIndex][uvIndex + 1] = vPos1;
            FCharUVs[messageIndex][uvIndex + 2] = uPos1;
            FCharUVs[messageIndex][uvIndex + 3] = vPos2;
            FCharUVs[messageIndex][uvIndex + 4] = uPos2;
            FCharUVs[messageIndex][uvIndex + 5] = vPos1;
            FCharUVs[messageIndex][uvIndex + 6] = uPos2;
            FCharUVs[messageIndex][uvIndex + 7] = vPos1;
            FCharUVs[messageIndex][uvIndex + 8] = uPos1;
            FCharUVs[messageIndex][uvIndex + 9] = vPos2;
            FCharUVs[messageIndex][uvIndex + 10] = uPos2;
            FCharUVs[messageIndex][uvIndex + 11] = vPos2;

            /**
             *
             * setup polly coordinates
             */
            lineWidth += polyXPos2 - polyXPos1;
            FCharIndices[messageIndex][polyIndex] = polyXPos1;
            FCharIndices[messageIndex][polyIndex + 1] = polyYPos2;
            FCharIndices[messageIndex][polyIndex + 2] = 1.0f;
            FCharIndices[messageIndex][polyIndex + 3] = polyXPos1;
            FCharIndices[messageIndex][polyIndex + 4] = polyYPos1;
            FCharIndices[messageIndex][polyIndex + 5] = 1.0f;
            FCharIndices[messageIndex][polyIndex + 6] = polyXPos2;
            FCharIndices[messageIndex][polyIndex + 7] = polyYPos2;
            FCharIndices[messageIndex][polyIndex + 8] = 1.0f;
            FCharIndices[messageIndex][polyIndex + 9] = polyXPos2;
            FCharIndices[messageIndex][polyIndex + 10] = polyYPos2;
            FCharIndices[messageIndex][polyIndex + 11] = 1.0f;
            FCharIndices[messageIndex][polyIndex + 12] = polyXPos1;
            FCharIndices[messageIndex][polyIndex + 13] = polyYPos1;
            FCharIndices[messageIndex][polyIndex + 14] = 1.0f;
            FCharIndices[messageIndex][polyIndex + 15] = polyXPos2;
            FCharIndices[messageIndex][polyIndex + 16] = polyYPos1;
            FCharIndices[messageIndex][polyIndex + 17] = 1.0f;


            charCounter++;
            totalCharCount[messageIndex]++;

        }


        return (lineWidth );

    }
    private int convertCharToIndex(int c_val)
    {
        int indx = -1;

        // Retrieve the index
        if(c_val>64&&c_val<91) // A-Z
            indx = c_val - 65;
        else if(c_val>96&&c_val<123) // a-z
            indx = c_val - 97;
        else if(c_val>47&&c_val<58) // 0-9
            indx = c_val - 48 + 26;
        else if(c_val==43) // +
            indx = 38;
        else if(c_val==45) // -
            indx = 39;
        else if(c_val==33) // !
            indx = 36;
        else if(c_val==63) // ?
            indx = 37;
        else if(c_val==61) // =
            indx = 40;
        else if(c_val==58) // :
            indx = 41;
        else if(c_val==46) // .
            indx = 42;
        else if(c_val==44) // ,
            indx = 43;
        else if(c_val==42) // *
            indx = 44;
        else if(c_val==36) // $
            indx = 45;

        return indx;
    }

    public void createFloatBuffers(){
        for( int i = 0 ; i <  totalMessages; i++) {
            this.FBCharIndices[i] = BufferUtils.getFloatBuffer(FCharIndices[i], 4);

            this.FBCharUVs[i] = BufferUtils.getFloatBuffer(FCharUVs[i], 4);

        }
    }
    public void loadVBOs(){

        for( int i = 0 ; i <  totalMessages; i++) {
            this.indicesVBOHandles[i] = getPositionVBO(FBCharIndices[i])[0];
            this.UVVBOHandles[i] = getPositionVBO(FBCharUVs[i])[0];
        }


    }
    public int getUVVbo(int i){
        return UVVBOHandles[i];
    }
    public int getIndicesVBO(int i){
        return indicesVBOHandles[i];
    }
    public int getTotalPolyDraw(int i){
        return (totalCharCount[i] * 6);
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
    public float getTextHeightPoly(int i){
        return totalHeight[i];
    }
    public float getTextWidthPoly(int i){
        return totalWidth[i];
    }
}
