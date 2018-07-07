package com.example.bradmobile.testtexture;

import android.util.Log;

public class Constants {


	public static int SCREEN_WIDTH = 1366;
	public static  int SCREEN_HEIGHT = 768;
	public static int HERO_IN_SAMPLE_SIZE = 1;
	public static int HERO_SAMPLE_TORSO_HEIGHT = 200 / HERO_IN_SAMPLE_SIZE;
	public static int HERO_SAMPLE_LEG_HEIGHT = 250 / HERO_IN_SAMPLE_SIZE;
	public static int HERO_SAMPLE_WIDTH = 300 / HERO_IN_SAMPLE_SIZE;
	public static int HERO_TORSO_START = HERO_SAMPLE_LEG_HEIGHT * 5;
	public static float TEST_SHOT_SPEED;
	public static float TOTAL_MAP_MOVE;
	public static float TEST_RUN_SPEED;
	public static int tuOffset = 1;

	public static  int MIDDLE_SPACE = SCREEN_WIDTH - (int)(SCREEN_WIDTH * .27);
	public static  int GROUND_LEVEL = SCREEN_HEIGHT - ((int)(SCREEN_HEIGHT * .15));
	public static  int CONTROL_HEIGHT = SCREEN_HEIGHT - ((int)(SCREEN_HEIGHT * .35));
	public static  int ARROW_HEIGHT = SCREEN_HEIGHT / 10;
	public static  int ARROW_WIDTH = SCREEN_WIDTH / 10;
	public static  int CIRCLE_DIAMETER = SCREEN_HEIGHT / 5;
	public static  int BLOCK_HEIGHT = 300 ;
	public static  int BLOCK_WIDTH = 400;
	public static  int SEGMENT_WIDTH = (BLOCK_WIDTH / 2);
	public static  int SEGMENT_HEIGHT = (BLOCK_HEIGHT / 2);
	public static  int RUN_SPEED = SCREEN_WIDTH / 256;
	public static  float CHARACTER_WIDTH = BLOCK_WIDTH;
	public static  float CHARACTER_HEIGHT = (int)(CHARACTER_WIDTH * .8);
	public static  int START_X = SCREEN_WIDTH / 6;
	public static  int MAIN_CHAR_WIDTH = 300;
	public static  int MAIN_CHAR_HEIGHT = 250;
	public static  float scale = 1;
	public static boolean music =false;
	public static boolean sfx = true;
	public static  float FRAME_VARIANCE = 0;
	public static float CHARACTER_LEG_HEIGHT;
    public static float CHARACTER_TORSO_HEIGHT;
    public static float TOTAL_MAP_WIDTH = 2800;
    public static float TOTAL_MAP_HEIGHT = 1400;
	public static  int FALL_SPEED = 90;
	public static  float SHOT_SPEED = 0;
	public static float RIGHT_BORDER = .50f;
	public static float LEFT_BORDER = -.5f;
	public static int H_BAR_WIDTH ;
	public static int H_BAR_HEIGHT;
	
	public static void setScreen(int w, int h){
		SCREEN_WIDTH = w;
		 SCREEN_HEIGHT = h;
		 Log.e("Screen width", Integer.toString(w));
		Log.e("Screen height", Integer.toString(h));

		if(SCREEN_WIDTH >= 1600) {
			scale = 1f + (1 - (1600f / SCREEN_WIDTH));
		}else{
			scale = ( SCREEN_WIDTH / 1600f );
		}
		//Log.d("Scale", Float.toString(scale));
		//Log.d("screen width", Integer.toString(SCREEN_WIDTH));
		 MIDDLE_SPACE = SCREEN_WIDTH - (int)(SCREEN_WIDTH * .17);
		 GROUND_LEVEL = SCREEN_HEIGHT - ((int)(SCREEN_HEIGHT * .15));
		 CONTROL_HEIGHT = SCREEN_HEIGHT - ((int)(SCREEN_HEIGHT * .35));
		 ARROW_HEIGHT = SCREEN_HEIGHT / 8;
		 ARROW_WIDTH = SCREEN_WIDTH / 8;
		 CIRCLE_DIAMETER = SCREEN_HEIGHT / 5;
		 BLOCK_HEIGHT =300;// SCREEN_HEIGHT/ 3 ;
		 BLOCK_WIDTH = 400;// SCREEN_WIDTH / 4 ;
		 SEGMENT_WIDTH = (BLOCK_WIDTH / 2);
		 SEGMENT_HEIGHT = (BLOCK_HEIGHT / 2);
		H_BAR_WIDTH = (int)( SCREEN_WIDTH * .2f);
		H_BAR_HEIGHT = (int)(H_BAR_WIDTH * .125f);
		LEFT_BORDER = -.5f;
		RIGHT_BORDER = .5f;



		 CHARACTER_WIDTH = 2f/7f;//BLOCK_WIDTH;

		 CHARACTER_HEIGHT = CHARACTER_WIDTH * 2.5f ;
		CHARACTER_LEG_HEIGHT = CHARACTER_HEIGHT * .555555f;
        CHARACTER_TORSO_HEIGHT = CHARACTER_HEIGHT * .44444444f;
		 START_X = SCREEN_WIDTH / 6;
		 MAIN_CHAR_WIDTH = 300;
		 MAIN_CHAR_HEIGHT = 250;
		 FRAME_VARIANCE = 2.5f;
		TEST_RUN_SPEED = ((((float)RUN_SPEED / (float)SCREEN_WIDTH) * 1.5f) * 3.0f) * scale;
		TOTAL_MAP_MOVE = ((1f -(8f / 10f)) * 2.5f);


		 FALL_SPEED = 90;
		 SHOT_SPEED = TEST_RUN_SPEED * 2.7f;
		TEST_SHOT_SPEED = TEST_RUN_SPEED * 1.5f;
	}
	public static boolean getMusicState(){
		return music;
	}
	public static boolean getSfxState(){
		return sfx;
	}

}

