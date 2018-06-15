package com.example.bradmobile.testtexture.Utils;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by BradMobile on 10/18/2016.
 */

public class BufferUtils {



    public static FloatBuffer getFloatBuffer(float[] input, int BPF){

        FloatBuffer temp = ByteBuffer.allocateDirect(input.length * BPF).order(ByteOrder.nativeOrder()).asFloatBuffer();
        temp.put(input);
        temp.position(0);
        //output = temp.duplicate();
        //temp = null;


        return temp;



    }
    public static ShortBuffer getShortBuffer(short[] input, int BPF){

        ShortBuffer temp = ByteBuffer.allocateDirect(input.length * BPF).order(ByteOrder.nativeOrder()).asShortBuffer();
        temp.put(input);
        temp.position(0);
        //output = temp.duplicate();
        //temp = null;


        return temp;



    }
    public static IntBuffer getIntBuffer(int[] input, int BPI){

        IntBuffer temp = ByteBuffer.allocateDirect(input.length * BPI).order(ByteOrder.nativeOrder()).asIntBuffer();
        temp.put(input);
        temp.position(0);
        //output = temp.duplicate();
        //temp = null;


        return temp;



    }
    public static void clearBuffers(){

       //output.clear();

    }


}
