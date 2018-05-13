package com.example.bradmobile.testtexture;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by BradMobile on 4/23/2016.
 */
public class Scene {

    Paint paint = new Paint();
    int i = 0;

    private RectF whereToDraw = new RectF(0,0,0,0);
    private Rect frameToDraw = new Rect(0,0,0,0);

    public void Scene(int sceneNum){

       // background = BitmapFactory.decodeResource( Resources.getSystem(),R.drawable.map_background, null);
       //background = Bitmap.createScaledBitmap(background, 2168, 1080, false);
    }
    public void draw(){
        whereToDraw.left = i;
        whereToDraw.top = 0;
        whereToDraw.right =  i+ 1920 ;
        whereToDraw.bottom = 1080;

        frameToDraw.left = 0;
        frameToDraw.top = 0;
        frameToDraw.right = 2168;
        frameToDraw.bottom = 1080;

        i -=1;

    }

}
