package com.example.bradmobile.testtexture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by BradMobile on 4/23/2016.
 */
public class movieScene {
    private Bitmap background;
    private Bitmap image1;
    private Bitmap image2;
    private Bitmap image3;
    BitmapFactory.Options options = new BitmapFactory.Options();
    private int sceneLength = 0;
    private boolean endScene = false;
    public static final int score = 0;
    private final int screenWidth = Constants.SCREEN_WIDTH;
    private final int screenHeight = Constants.SCREEN_HEIGHT;
    public boolean playing = true;
    private boolean displayingScore = false;

    Paint paint = new Paint();
    int i = 0;

    private RectF whereToDraw = new RectF(0,0,0,0);
    private Rect frameToDraw = new Rect(0,0,0,0);


    public movieScene(Context context, int sceneNum){
        i = 0;
        switch(sceneNum){
            case score:
                sceneLength = 60 * 1;
                break;
        }
        displayingScore = false;

        //paint.setFilterBitmap(true);
        //options.inSampleSize = 6;

       // background = BitmapFactory.decodeResource(context.getResources(),R.drawable.futurecity, options);
       // background = Bitmap.createScaledBitmap(background, 2168, 1080, false);
    }
    public movieScene(Context context, int sceneNo, int score, float time ){
        i = 0;
        displayingScore = true;

    }
    public void draw(Canvas canvas){

        if(background != null) {
            whereToDraw.left = 0;
            whereToDraw.top = 0;
            whereToDraw.right =  screenWidth;
            whereToDraw.bottom = screenHeight;

            frameToDraw.left = 0;
            frameToDraw.top = 0;
            frameToDraw.right = background.getWidth();
            frameToDraw.bottom = background.getHeight();
            canvas.drawBitmap(background, frameToDraw, whereToDraw, paint);
            i += 1;
            if( i >= sceneLength){
                endScene = true;
            }
        }

    }
    public void nullImage(){
        if(background != null){
            background.recycle();
            background = null;
        }
    }
    public boolean isEndScene(){
        return endScene;
    }


}
