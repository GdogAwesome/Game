package com.example.bradmobile.testtexture.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created by Brad on 11/15/2017.
 */

public class TextureUtils {
    public static int[] LoadTexture(Context context, int[] resource, int totalTextures){
        int[] textSize = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE,textSize, 0);

        int[] textureHandle = new int[totalTextures];

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 );

        GLES20.glGenTextures(totalTextures, textureHandle,0  );
        //Log.e("boss image", Integer.toString(textureHandle[0]));
        for(int i = 0; i < totalTextures; i ++) {

            if (textureHandle[i] != 0) {

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;

                /**
                 *
                 * check to see if bitmap is larger than largest texture available in opengl
                 */
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(context.getResources(),resource[i], options);
                if(options.outWidth > textSize[0]){
                    options.inSampleSize = 2;
                }else{
                    options.inSampleSize = 1;
                }
                options.inJustDecodeBounds = false;


                /**
                 *
                 * load Texture
                 */
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resource[i], options);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[i]);
                // GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

                //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                Log.e("texture w", Integer.toString(bitmap.getWidth()));

                // bitmap = null;
                bitmap.recycle();


            }
            if (textureHandle[i] == 0) {
                throw new RuntimeException("Error loading texture");
            }
        }

        return textureHandle;

    }
    public static int[] LoadNormalsTexture(Context context, int[] resource, int totalTextures){
        int[] textSize = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE,textSize, 0);

        int[] textureHandle = new int[totalTextures];

        //GLES20.glActiveTexture(GLES20.GL_TEXTURE0 );
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);

        GLES20.glGenTextures(totalTextures, textureHandle,0  );
        //Log.e("boss image", Integer.toString(textureHandle[0]));
        for(int i = 0; i < totalTextures; i ++) {

            if (textureHandle[i] != 0) {

                final BitmapFactory.Options op = new BitmapFactory.Options();
                op.inScaled = false;

                /**
                 *
                 * check to see if bm is larger than largest texture available in opengl
                 */
                op.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(context.getResources(),resource[i], op);
                if(op.outWidth > textSize[0]){
                    op.inSampleSize = 2;
                }else{
                    op.inSampleSize = 1;
                }
                op.inJustDecodeBounds = false;

                /**
                 *
                 * load Texture
                 */
                op.inPreferredConfig = Bitmap.Config.ARGB_8888;

                final Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resource[i], op);

                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[i]);
                // GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bm, 0);

                //GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
                Log.e("texture w", Integer.toString(bm.getWidth()));

                // bm = null;
                bm.recycle();


            }
            if (textureHandle[i] == 0) {
                throw new RuntimeException("Error loading texture");
            }
        }

        return textureHandle;

    }


}
