package com.example.bradmobile.testtexture;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.opengl.Matrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import com.example.bradmobile.testtexture.Utils.*;


public class Map1 {
	public float MapFinalPosX;
	public float MapFinalPosY;
	public float ViewX;
	public float prevX;
	public float viewX;
	public float prevY;
	public float deltaX;
	public float deltaY;
	public float ViewY;
	private movieScene currentScene = null;
	public boolean endMap = false;
	public boolean endLevel = false;
	private boolean hasBoss = false;
	private boolean bossActivated = false;
	private boolean bossActive = false;
	private boolean lockScreen = false;
	private int bossType = 0;
	private boolean displayFM = true;
	private boolean displaySM = true;
	private int startX = Constants.START_X;
	private boolean reverseAnim = false;
	private boolean[][] bottomAnim  = new boolean[5][4];
	private boolean[][] topAnim = new boolean[5][4];
	private boolean[][] skyAnim = new boolean[5][4];
	private boolean hasScene = true;
	private boolean startPoints = false;// true;
	private boolean showingPoints = false;
	private int backgroundWidth = Constants.SCREEN_WIDTH;


	/*

	map opengl handles
	 */
	private int texture = 0;
	private int BgTexture = 0;
	private int layerIndexOffset = 0;
	private float[] mModelMatrix = new float[16];
	private float[] mBackgroundMatrix = new float[16];
	private float[] mTextureCoordinateData = new float[720];
	private float[] mBgTextureCoordinateData; // = new float[24];
	private FloatBuffer TextureCoordinateBuffer;
	private FloatBuffer BgTextureCoordinateBuffer;
	private FloatBuffer PositionFloatBuffer;
	private FloatBuffer BgPositionFloatBuffer;
	private float[] mPositionData = new float[1080];
	private float[] mBgPositionData; // = new float[36];
	private float totalMoveSpace = 0f;
	private float BgMoveSpeed = Constants.TEST_RUN_SPEED * .5f;
	private float BgTotalMoveSpace = 0f;
	private double absoluteMoveSpace = 0f;
    private boolean mapNeedsRenderUpdate = false;


	//keep track of update time
	private int updateTime = 4;
	private long timeDiff;
	private long beginTime;

	/**
	 *
	 * position info
	 */

	private float pos0 = 0;
	private float pos1 = 0;
	private float pos2 = 0;
	private float pos3 = 0;

    private HeroEntity hero;
	Context context;
	private int timer = 0;

	private int framePos = 0;

	
	float dif =  (((float)Constants.SCREEN_WIDTH - 1366) / Constants.SCREEN_WIDTH) ;
	
	public int[] bottomX = new int[]{ 0, 1, 2, 3, 4 };

	float totalWidth = 2800f;
	float totalHeight = 1400f;

	float tileWidth = (200f / totalWidth);// + .0005f;
	float tileHeight = 150f / totalHeight;


	int bgH = 0;
	int bgW = 0;
	
	
	public float currentData;
	public int[] currentBottom = new int[100];
	public int[] currentTop = new int[100];
	
	

	private Bitmap mapImage;
	private Bitmap mapBackground;
    private RectF whereToDraw = new RectF(0,0,0,0);
    private Rect frameToDraw = new Rect(0,0,0,0);

	public String ref;
	private int mapLength = 0;
	private Canvas canvas;
	public final static int BLOCK_WIDTH = Constants.BLOCK_WIDTH;
	public final static int BLOCK_HEIGHT = Constants.BLOCK_HEIGHT;
	public final static int SEGMENT_WIDTH = Constants.SEGMENT_WIDTH ;
	public final static int SEGMENT_HEIGHT = Constants.SEGMENT_HEIGHT ;
    public final static int drawWidth = Constants.SEGMENT_WIDTH+ 1;
    public final static int drawHeight= Constants.SEGMENT_HEIGHT + 1;
	public final static int SCREEN_WIDTH = Constants.SCREEN_WIDTH;
	public final static int SCREEN_HEIGHT = Constants.SCREEN_HEIGHT;
	public final static float RIGHT_BORDER = Constants.RIGHT_BORDER;
	public final static float LEFT_BORDER = Constants.LEFT_BORDER;
	public final static int RUN_SPEED = Constants.RUN_SPEED;

	public boolean cutScene = false;
	public mapBlock[] bottomBlocks ;
	public mapBlock[] topBlocks ;
	public mapBlock[] skyBlocks;
	public float[][][] bottomSegmentList = new float[5][4][2];
	public float[][][] topSegmentList = new float[5][4][2];
	public float[][][] skySegmentList = new float[5][4][2];
	public float[][][] bottomFrame = new float[5][4][4];
	public float[][][] topFrame = new float[5][4][4];
	public float[][][] skyFrame = new float[5][4][4];
	public boolean[][][] bHasO = new boolean[5][4][1];
	public boolean[][][] tHasO = new boolean[5][4][1];
	public boolean[][][] sHasO = new boolean[5][4][1];

	public boolean canMoveLeft = true;
	public boolean canMoveRight = true;
	private boolean mapUpdateLeft = true;
	private boolean mapReadyToDraw = false;

	public int[] readObstacle = new int[4];
	public float[][] topObstacleArray = new float[4][4];
	public float[][] bottomObstacleArray = new float[4][4];
	public float[][] skyObstacleArray = new float[4][4];
	BitmapFactory.Options options = new BitmapFactory.Options();
	public InputStream is;
    //private Context context;
    private Paint paint;

	int totalPos = 0;
	int counter = 0;



	public float[] drawBottom = new float[2];
	public float[] drawTop = new float[2];
	public float[] drawSky = new float[2];

	public float[] frameBottom = new float[4];
	public float[] frameTop = new float[4];
	public float[] frameSky = new float[4];

	
	private mapBlock bottomBlock;
	private mapBlock topBlock;
	private mapBlock skyBlock;

	private boolean leftMapEdge = false;
	private boolean rightMapEdge = false;

	/**
	 * 

	 * @param x
	 * @param y 
	 */
	
	public Map1( float x, float y){



		options.inSampleSize = 2;

		this.MapFinalPosX = x;
		this.MapFinalPosY = y;

	}
	public void updateAnim(){
		timer += 1;
		
		if(timer >= 6){
			if(!reverseAnim){
				if(framePos < 3){
					framePos += 1;
					updateAnimBlocks();
				}else{
					reverseAnim = true;

				}
			}else if(reverseAnim){
				if(framePos > 0){
					framePos -=1;
					updateAnimBlocks();
				}else{
					reverseAnim = false;
					//framePos += 1;
				}
			}
			timer = 0;
			


		}
	}

	
	public void initMap(Context context,int mapNo){
		this.context = context;
		

		/**
		 * 
		 * 
		 * Set up Map Image
		 * 
		 * 
		 */
	
		

			/**
			 * decide what map to load
			 */

           options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inDither = true;

			switch(mapNo){

                case 1:
					is = context.getResources().openRawResource(R.raw.test9);
                    options.inSampleSize = 1;
					//this.mapImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.cyber_map, options);
					hasBoss = true; //true;
					bossType = BossEntity.FIRST_BOSS;
					texture = R.drawable.cyber_map;
					BgTexture = R.drawable.city_background;
					//mTextureDataHandle = TextureUtil.LoadTexture(context,R.drawable.cyber_map);
					// mapImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.test_map3);
					//mapImage = Bitmap.createScaledBitmap(mapImage, 2400, 2400, false);
					hasScene = false;

					options.inSampleSize = 1;

					//mapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.city_background, options);

					//mapBackground = Bitmap.createScaledBitmap(mapBackground, 2168, 1080, false);

					break;
				case 2:
                    options.inSampleSize = 1;
                    is = context.getResources().openRawResource(R.raw.test6_1);
					//this.mapImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.test_map, options);
					// mapImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.test_map3);
					//mapImage = Bitmap.createScaledBitmap(mapImage, 2400, 2400, false);
					texture = R.drawable.test_map;
					BgTexture = R.drawable.overgrown_city;
					hasBoss = false;
					bossType = BossEntity.NO_BOSS;

					hasScene = false;
					options.inSampleSize = 1;
					//mapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.overgrown_city, options);
					//mapBackground = Bitmap.createScaledBitmap(mapBackground, 2168, 1080, false);

					break;



			}

			loadMapFromFile();

            setupModelMatrix();


			setupPolyCoords();

			setupBgTextureCoords();
			createFloatBuffers();
			setupTextureCoords();
			updateSegments();
			//positionBuffer = setUpPrimitiveBuffer();




		/**
		 * 
		 * 
		 * Set up Map Blocks
		 * 
		 * 
		 */
		
		for(int i=0; i < mapLength; i ++){
			
			currentTop[i] = i * BLOCK_WIDTH;
	
			currentBottom[i] = i * BLOCK_WIDTH;
			
		}


       // updateSegments();

		
		
	}



	public float moveMap(float heroLeft, float heroRight, float heroy, boolean canMoveLeft, boolean canMoveRight, int playerCommand){//}, float viewX){



		beginTime = System.currentTimeMillis();
			if (absoluteMoveSpace >= .25f && displayFM){
                hero.displayMessage(1);
				displayFM = false;
			}else if(absoluteMoveSpace >= 10.0f && displaySM){
				hero.displayMessage(2);
				displaySM = false;
			}
		if(!lockScreen) {
			if (canMoveRight && heroRight >= RIGHT_BORDER && playerCommand == 1 ) {


				if(totalMoveSpace >= (-1f *(Constants.TOTAL_MAP_MOVE ))){

					if (BgTotalMoveSpace > (-2.0f * 1.02f)) {
						BgTotalMoveSpace -= BgMoveSpeed;
						mBackgroundMatrix[12] -= BgMoveSpeed;

					} else if (BgTotalMoveSpace <= (-2.0f * 1.02f)) {
						BgTotalMoveSpace -= BgTotalMoveSpace;
						mBackgroundMatrix[12] -= mBackgroundMatrix[12];

					}

					totalMoveSpace -= Constants.TEST_RUN_SPEED;
					Matrix.translateM(mModelMatrix, 0, (-1f * (Constants.TEST_RUN_SPEED)), 0.0f, 0.0f);


					absoluteMoveSpace += Constants.TEST_RUN_SPEED;

				} else {


					if(bottomX[4] < mapLength -1 ) {
						bottomX[0] += 1;
						bottomX[1] += 1;
						bottomX[2] += 1;
						bottomX[3] += 1;
						bottomX[4] += 1;
						rightMapEdge = false;
					}else{
						setEndMap(true);
						rightMapEdge = true;
					}

					if(!rightMapEdge) {
						Matrix.translateM(mModelMatrix, 0, (-1f * totalMoveSpace), 0.0f, 0.0f);
						totalMoveSpace -= totalMoveSpace;//Constants.TOTAL_MAP_MOVE;
						totalMoveSpace -= Constants.TEST_RUN_SPEED;
						Matrix.translateM(mModelMatrix, 0, (-1f * (Constants.TEST_RUN_SPEED)), 0.0f, 0.0f);


						absoluteMoveSpace += Constants.TEST_RUN_SPEED;
					}




					updateSegments();

                    mapNeedsRenderUpdate = true;
					updateTextureCoords();

				}

				//Log.e("total move space", Double.toString((absoluteMoveSpace + (mModelMatrix[12]))));
			} else if (canMoveLeft && heroLeft <= LEFT_BORDER &&  playerCommand == 2) {


				if ((totalMoveSpace +(Constants.TOTAL_MAP_MOVE ))<= (Constants.TOTAL_MAP_MOVE )) {
					if ((BgTotalMoveSpace + 1f) < 1.02f) {
						BgTotalMoveSpace += BgMoveSpeed;
						mBackgroundMatrix[12] += BgMoveSpeed;
					} else if ((BgTotalMoveSpace + 1f) >= 1.02f) {
						BgTotalMoveSpace -= 2.0f;
						mBackgroundMatrix[12] -= 2.0f;

					}
					prevX = viewX;
					//viewX = (heroX - LEFT_BORDER);

					deltaX = prevX - viewX;

					Matrix.translateM(mModelMatrix, 0, (Constants.TEST_RUN_SPEED), 0.0f, 0.0f);
					totalMoveSpace += Constants.TEST_RUN_SPEED;
					absoluteMoveSpace -= Constants.TEST_RUN_SPEED;


				} else {

					if (bottomX[0] > 0) {


						bottomX[0] -= 1;
						bottomX[1] -= 1;
						bottomX[2] -= 1;
						bottomX[3] -= 1;
						bottomX[4] -= 1;
						mapUpdateLeft = true;
						leftMapEdge = false;
					}else{
						mapUpdateLeft = false;
						leftMapEdge = true;
					}

					if(!leftMapEdge) {

						Matrix.translateM(mModelMatrix, 0, (-1f * Constants.TOTAL_MAP_MOVE) + totalMoveSpace, 0.0f, 0.0f);
						totalMoveSpace -= Constants.TOTAL_MAP_MOVE + (-1f * totalMoveSpace);
						Matrix.translateM(mModelMatrix, 0, (Constants.TEST_RUN_SPEED), 0.0f, 0.0f);
						totalMoveSpace += Constants.TEST_RUN_SPEED;
						absoluteMoveSpace -= Constants.TEST_RUN_SPEED;
					}



					updateSegments();

					mapNeedsRenderUpdate = true;
					updateTextureCoords();


				}
			}
			//Log.e("total move space", Double.toString((absoluteMoveSpace + (mModelMatrix[12]))));
		}
		timeDiff = (System.currentTimeMillis() - beginTime);
			updateTime = (int)timeDiff;


			ViewX = viewX;
			return viewX;
		
		
	}



	public boolean CutScene(){
		return cutScene;
	}
	public void setEndMap(boolean finish){
		this.endMap = finish;


		if(hasBoss){
			lockScreen = true;
			bossActive = true;
			bossActivated = true;

		}else if(startPoints && !bossActive) {
			//nullImage();
			//initScene(movieScene.score);
			startPoints = false;
			cutScene =true;

		}else if(hasScene && !bossActive){


			//nullImage();
			hasScene = false;
			currentScene.nullImage();
			initScene(1);
			cutScene =true;


		}
		else{
			Log.e("end", "level");


			endLevel = true;
		}
	}


	private void updateSegments(){
		for(int i = 0; i < 5; i++) {
			//bottomSegmentList[i] = bottomBlocks[bottomX[i]].renderMapBlock();
			//topSegmentList[i] = topBlocks[bottomX[i]].renderMapBlock();
			//skySegmentList[i] = skyBlocks[bottomX[i]].renderMapBlock();

			//bottomFrame[i] = bottomBlocks[bottomX[i]].frameMapBlock();
			//topFrame[i] = topBlocks[bottomX[i]].frameMapBlock();
			//skyFrame[i] = skyBlocks[bottomX[i]].frameMapBlock();

			bottomAnim[i] = bottomBlocks[bottomX[i]].getAnim();
			topAnim[i] = topBlocks[bottomX[i]].getAnim();
			skyAnim[i] = skyBlocks[bottomX[i]].getAnim();
		}
	}
	private void updateAnimBlocks(){
		int currentPos = 0;
		int frameCount = 0;

		int indexAdd = 0;



		for(int i = 0; i < 5; i++) {
			currentPos =  frameCount * 24;
				for(int b = 0; b < 2; b++){
					if(skyAnim[i][b]){
						switch(b){
							case 0: indexAdd = 0;
							break;
							case 1: indexAdd = 12;
							break;
						}
						updateTextureBox(currentPos,indexAdd,i,b, 0);

					}
				}
			//Log.d("bottom X", Integer.toString(bottomX[i]));
			frameCount++;
		}
		for(int i = 0; i < 5; i++) {
			currentPos =  frameCount * 24;
			for(int b = 2; b < 4; b++){
				if(skyAnim[i][b]){
					switch(b){
						case 2: indexAdd = 0;
							break;
						case 3: indexAdd = 12;
							break;
					}
					updateTextureBox(currentPos,indexAdd,i,b, 0);

				}
			}


			//Log.d("bottom X", Integer.toString(bottomX[i]));
			frameCount++;
		}
 		//mapNeedsRenderUpdate = true;



	}



	public FloatBuffer[] getDrawCoords(){

		TextureCoordinateBuffer.rewind();
		TextureCoordinateBuffer.put(mTextureCoordinateData);
		FloatBuffer[] temp =new FloatBuffer[2];
		temp[0] = TextureCoordinateBuffer;
		temp[1] = BgTextureCoordinateBuffer;

        return temp;
    }
    public void updateTextures(){
		TextureCoordinateBuffer.rewind();
		TextureCoordinateBuffer.put(mTextureCoordinateData);

	}

    private void createFloatBuffers(){
		
		TextureCoordinateBuffer = BufferUtils.getFloatBuffer(mTextureCoordinateData, 4);
		PositionFloatBuffer = BufferUtils.getFloatBuffer(mPositionData, 4);
		BgTextureCoordinateBuffer = BufferUtils.getFloatBuffer(mBgTextureCoordinateData, 4);
		BgPositionFloatBuffer = BufferUtils.getFloatBuffer(mBgPositionData, 4 );

	}
	public FloatBuffer[] getPositionFloatBuffer(){
    	FloatBuffer[] temp = new FloatBuffer[2];

    	temp[0] = PositionFloatBuffer;
    	temp[1] = BgPositionFloatBuffer;
		return temp ;

	}
	public int[] getTexture(){
		int[] temp = new int[2];
		temp[0] = texture;
		temp[1] = BgTexture;
		return temp;
	}
	public float getMapOffset(){
		return  mModelMatrix[12];
	}
	public float getAbsoluteMove(){
		return (float)absoluteMoveSpace;
	}
    public float[][] getmModelMatrix(){
		float[][] temp = new float[2][16];

		temp[0] = mModelMatrix;
		temp[1] = mBackgroundMatrix;
        return temp;
    }
    private void setupModelMatrix(){
        Matrix.setIdentityM(mModelMatrix, 0 );
        Matrix.scaleM(mModelMatrix, 0 , 1.02f,1.03f, 1.0f); // x= 1.02 y = 1.o3
        Matrix.translateM(mModelMatrix, 0, 0.0f, -0.02f, -2.51f );


        Matrix.setIdentityM(mBackgroundMatrix, 0 );
		Matrix.scaleM(mBackgroundMatrix, 0 , 1.02f,1.03f, 1.0f);
        Matrix.translateM(mBackgroundMatrix, 0 , 0.0f, 0.0f, -2.505f);



    }

	public int getUpdateTime(){
    	return updateTime;
	}
    private void updateTextureBox(int currentPos, int indexAdd, int blockIndex, int segmentIndex, int layer){

		layerIndexOffset = 0 ;

		if(layer == 0) {
			pos0 = skyBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][0];
			pos1 = skyBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][1];
			pos2 = skyBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][2];
			pos3 = skyBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][3];
			if (skyBlocks[bottomX[blockIndex]].getAnim()[segmentIndex]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}
		}else if(layer ==1){
			pos0 = topBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][0];
			pos1 = topBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][1];
			pos2 = topBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][2];
			pos3 = topBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][3];
			if (topBlocks[bottomX[blockIndex]].getAnim()[segmentIndex]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}
		}else if(layer ==2){
			pos0 = bottomBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][0];
			pos1 = bottomBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][1];
			pos2 = bottomBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][2];
			pos3 = bottomBlocks[bottomX[blockIndex]].renderMapBlock()[segmentIndex][3];
			if (bottomBlocks[bottomX[blockIndex]].getAnim()[segmentIndex]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}
		}

		//pos0 += (tileWidth * (float)(framePos));
		//pos2 += (tileWidth * (float)(framePos));
		//test

		/*
		if(framePos == 1) {
			Log.e("origin", Float.toString(pos0));
			Log.e("added anim", Float.toString(pos0 + (tileWidth * (float) (framePos))));
		}
		*/
		
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd), pos0);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 1), pos3);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 2), pos0);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 3), pos1);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 4), pos2);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 5), pos3);

		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 6), pos0);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 7), pos1);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 8), pos2);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 9), pos1);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 10), pos2);
		TextureCoordinateBuffer.put((layerIndexOffset + currentPos + indexAdd + 11), pos3);


	}
	public void updateTextureCoords(){
		int currentPos = 0;
		int frameCount = 0;

		int indexAdd = 0;


		for(int l = 0; l < 3; l++) {
			//frameCount = 0;

			for (int i = 0; i < 5; i++) {
				currentPos = frameCount * 24;
				for (int b = 0; b < 2; b++) {
						switch (b) {
							case 0:
								indexAdd = 0;
								break;
							case 1:
								indexAdd = 12;
								break;
						}
						updateTextureBox(currentPos, indexAdd, i, b, l);


				}
				//Log.d("bottom X", Integer.toString(bottomX[i]));
				frameCount++;
			}
			for (int i = 0; i < 5; i++) {
				currentPos = frameCount * 24;
				for (int b = 2; b < 4; b++) {
						switch (b) {
							case 2:
								indexAdd = 0;
								break;
							case 3:
								indexAdd = 12;
								break;
						}
						updateTextureBox(currentPos, indexAdd, i, b, l);


				}


				//Log.d("bottom X", Integer.toString(bottomX[i]));
				frameCount++;
			}
		}

	}
    public void setupTextureCoords(){

		int frameCount = 0;
		int currentPos = 0;
		float pos0;
		float pos1;
		float pos2;
		float pos3;

		/**
		 *
		 *
		 * set up sky blocks
		 */


		for(int i = 0; i < 5; i++) {
			currentPos =  frameCount * 24;


			/**
			 *
			 * setup texture Coords
			 */

			//todo interleave first and second segments
			/**
			 *
			 * set up first sky
			 */
			pos0 = skyBlocks[bottomX[i]].renderMapBlock()[0][0];
			pos1 = skyBlocks[bottomX[i]].renderMapBlock()[0][1];
			pos2 = skyBlocks[bottomX[i]].renderMapBlock()[0][2];
			pos3 = skyBlocks[bottomX[i]].renderMapBlock()[0][3];

			if (skyBlocks[bottomX[i]].getAnim()[0]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos] = pos0;
			mTextureCoordinateData[currentPos + 1] = pos3 ;
			mTextureCoordinateData[currentPos + 2] = pos0;
			mTextureCoordinateData[currentPos + 3] = pos1;
			mTextureCoordinateData[currentPos + 4] = pos2;
			mTextureCoordinateData[currentPos + 5] = pos3 ;



			mTextureCoordinateData[currentPos + 6] = pos0;
			mTextureCoordinateData[currentPos + 7] = pos1;
			mTextureCoordinateData[currentPos + 8] = pos2;
			mTextureCoordinateData[currentPos + 9] = pos1;
			mTextureCoordinateData[currentPos + 10] = pos2;
			mTextureCoordinateData[currentPos + 11] = pos3 ;

			/**
			 *
			 * set up second sky
			 */

			pos0 = skyBlocks[bottomX[i]].renderMapBlock()[1][0];
			pos1 = skyBlocks[bottomX[i]].renderMapBlock()[1][1];
			pos2 = skyBlocks[bottomX[i]].renderMapBlock()[1][2];
			pos3 = skyBlocks[bottomX[i]].renderMapBlock()[1][3];
			if (skyBlocks[bottomX[i]].getAnim()[1]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos + 12] = pos0;
			mTextureCoordinateData[currentPos + 13] = pos3;
			mTextureCoordinateData[currentPos + 14] = pos0;
			mTextureCoordinateData[currentPos + 15] = pos1;
			mTextureCoordinateData[currentPos + 16] = pos2;
			mTextureCoordinateData[currentPos + 17] = pos3;

			mTextureCoordinateData[currentPos + 18] = pos0;
			mTextureCoordinateData[currentPos + 19] = pos1;
			mTextureCoordinateData[currentPos + 20] = pos2;
			mTextureCoordinateData[currentPos + 21] = pos1;
			mTextureCoordinateData[currentPos + 22] = pos2;
			mTextureCoordinateData[currentPos + 23] = pos3;


			//Log.d("bottom X", Integer.toString(bottomX[i]));
			frameCount++;
		}
		for(int i = 0; i < 5; i++){

			/**
			 *
			 * set up third sky
			 */
			currentPos =  frameCount * 24;
			pos0 = skyBlocks[bottomX[i]].renderMapBlock()[2][0];
			pos1 = skyBlocks[bottomX[i]].renderMapBlock()[2][1];
			pos2 = skyBlocks[bottomX[i]].renderMapBlock()[2][2];
			pos3 = skyBlocks[bottomX[i]].renderMapBlock()[2][3];

			if (skyBlocks[bottomX[i]].getAnim()[2]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos] = pos0;
			mTextureCoordinateData[currentPos + 1] = pos3;
			mTextureCoordinateData[currentPos + 2] = pos0;
			mTextureCoordinateData[currentPos + 3] = pos1;
			mTextureCoordinateData[currentPos + 4] = pos2;
			mTextureCoordinateData[currentPos + 5] = pos3;



			mTextureCoordinateData[currentPos + 6] = pos0;
			mTextureCoordinateData[currentPos + 7] = pos1;
			mTextureCoordinateData[currentPos + 8] = pos2;
			mTextureCoordinateData[currentPos + 9] = pos1;
			mTextureCoordinateData[currentPos + 10] = pos2;
			mTextureCoordinateData[currentPos + 11] = pos3;

			/**
			 *
			 * set up fourth sky
			 */
			pos0 = skyBlocks[bottomX[i]].renderMapBlock()[3][0];
			pos1 = skyBlocks[bottomX[i]].renderMapBlock()[3][1];
			pos2 = skyBlocks[bottomX[i]].renderMapBlock()[3][2];
			pos3 = skyBlocks[bottomX[i]].renderMapBlock()[3][3];
			if (skyBlocks[bottomX[i]].getAnim()[3]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos + 12] = pos0;
			mTextureCoordinateData[currentPos + 13] = pos3;
			mTextureCoordinateData[currentPos + 14] = pos0;
			mTextureCoordinateData[currentPos + 15] = pos1;
			mTextureCoordinateData[currentPos + 16] = pos2;
			mTextureCoordinateData[currentPos + 17] = pos3;

			mTextureCoordinateData[currentPos + 18] = pos0;
			mTextureCoordinateData[currentPos + 19] = pos1;
			mTextureCoordinateData[currentPos + 20] = pos2;
			mTextureCoordinateData[currentPos + 21] = pos1;
			mTextureCoordinateData[currentPos + 22] = pos2;
			mTextureCoordinateData[currentPos + 23] = pos3;

			frameCount++;
		}


		/**
		 *
		 * set up top Blocks
		 */
		for(int i = 0; i < 5; i++) {

			//todo interleave first and second segments
			/**
			 *
			 * set up first sky
			 */
			currentPos =  frameCount * 24;

			pos0 = topBlocks[bottomX[i]].renderMapBlock()[0][0];
			pos1 = topBlocks[bottomX[i]].renderMapBlock()[0][1];
			pos2 = topBlocks[bottomX[i]].renderMapBlock()[0][2];
			pos3 = topBlocks[bottomX[i]].renderMapBlock()[0][3];

			if (topBlocks[bottomX[i]].getAnim()[0]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}
			
			mTextureCoordinateData[currentPos] = pos0;
			mTextureCoordinateData[currentPos + 1] = pos3;
			mTextureCoordinateData[currentPos + 2] = pos0;
			mTextureCoordinateData[currentPos + 3] = pos1;
			mTextureCoordinateData[currentPos + 4] = pos2;
			mTextureCoordinateData[currentPos + 5] = pos3;



			mTextureCoordinateData[currentPos + 6] = pos0;
			mTextureCoordinateData[currentPos + 7] = pos1;
			mTextureCoordinateData[currentPos + 8] = pos2;
			mTextureCoordinateData[currentPos + 9] = pos1;
			mTextureCoordinateData[currentPos + 10] = pos2;
			mTextureCoordinateData[currentPos + 11] = pos3;

			/**
			 *
			 * set up second sky
			 */
			pos0 = topBlocks[bottomX[i]].renderMapBlock()[1][0];
			pos1 = topBlocks[bottomX[i]].renderMapBlock()[1][1];
			pos2 = topBlocks[bottomX[i]].renderMapBlock()[1][2];
			pos3 = topBlocks[bottomX[i]].renderMapBlock()[1][3];

			if (topBlocks[bottomX[i]].getAnim()[1]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos + 12] = pos0;
			mTextureCoordinateData[currentPos + 13] = pos3;
			mTextureCoordinateData[currentPos + 14] = pos0;
			mTextureCoordinateData[currentPos + 15] = pos1;
			mTextureCoordinateData[currentPos + 16] = pos2;
			mTextureCoordinateData[currentPos + 17] = pos3;

			mTextureCoordinateData[currentPos + 18] = pos0;
			mTextureCoordinateData[currentPos + 19] = pos1;
			mTextureCoordinateData[currentPos + 20] = pos2;
			mTextureCoordinateData[currentPos + 21] = pos1;
			mTextureCoordinateData[currentPos + 22] = pos2;
			mTextureCoordinateData[currentPos + 23] = pos3;


			//Log.d("bottom X", Integer.toString(bottomX[i]));
			frameCount++;
		}
		for(int i = 0; i < 5; i++){

			/**
			 *
			 * set up third sky
			 */
			currentPos =  frameCount * 24;
			pos0 = topBlocks[bottomX[i]].renderMapBlock()[2][0];
			pos1 = topBlocks[bottomX[i]].renderMapBlock()[2][1];
			pos2 = topBlocks[bottomX[i]].renderMapBlock()[2][2];
			pos3 = topBlocks[bottomX[i]].renderMapBlock()[2][3];
			if (topBlocks[bottomX[i]].getAnim()[2]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos] = pos0;
			mTextureCoordinateData[currentPos + 1] = pos3;
			mTextureCoordinateData[currentPos + 2] = pos0;
			mTextureCoordinateData[currentPos + 3] = pos1;
			mTextureCoordinateData[currentPos + 4] = pos2;
			mTextureCoordinateData[currentPos + 5] = pos3;



			mTextureCoordinateData[currentPos + 6] = pos0;
			mTextureCoordinateData[currentPos + 7] = pos1;
			mTextureCoordinateData[currentPos + 8] = pos2;
			mTextureCoordinateData[currentPos + 9] = pos1;
			mTextureCoordinateData[currentPos + 10] = pos2;
			mTextureCoordinateData[currentPos + 11] = pos3;

			/**
			 *
			 * set up fourth sky
			 */
			pos0 = topBlocks[bottomX[i]].renderMapBlock()[3][0];
			pos1 = topBlocks[bottomX[i]].renderMapBlock()[3][1];
			pos2 = topBlocks[bottomX[i]].renderMapBlock()[3][2];
			pos3 = topBlocks[bottomX[i]].renderMapBlock()[3][3];
			if (topBlocks[bottomX[i]].getAnim()[3]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos + 12] = pos0;
			mTextureCoordinateData[currentPos + 13] = pos3;
			mTextureCoordinateData[currentPos + 14] = pos0;
			mTextureCoordinateData[currentPos + 15] = pos1;
			mTextureCoordinateData[currentPos + 16] = pos2;
			mTextureCoordinateData[currentPos + 17] = pos3;

			mTextureCoordinateData[currentPos + 18] = pos0;
			mTextureCoordinateData[currentPos + 19] = pos1;
			mTextureCoordinateData[currentPos + 20] = pos2;
			mTextureCoordinateData[currentPos + 21] = pos1;
			mTextureCoordinateData[currentPos + 22] = pos2;
			mTextureCoordinateData[currentPos + 23] = pos3;
			frameCount++;
		}

		/**
		 *
		 * set up bottom blocks
		 */

		for(int i = 0; i < 5; i++) {

			//todo interleave first and second segments
			/**
			 *
			 * set up first sky
			 */
			currentPos =  frameCount * 24;
			pos0 = bottomBlocks[bottomX[i]].renderMapBlock()[0][0];
			pos1 = bottomBlocks[bottomX[i]].renderMapBlock()[0][1];
			pos2 = bottomBlocks[bottomX[i]].renderMapBlock()[0][2];
			pos3 = bottomBlocks[bottomX[i]].renderMapBlock()[0][3];
			if (bottomBlocks[bottomX[i]].getAnim()[0]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos] = pos0;
			mTextureCoordinateData[currentPos + 1] = pos3 ;
			mTextureCoordinateData[currentPos + 2] = pos0;
			mTextureCoordinateData[currentPos + 3] = pos1;
			mTextureCoordinateData[currentPos + 4] = pos2;
			mTextureCoordinateData[currentPos + 5] = pos3 ;



			mTextureCoordinateData[currentPos + 6] = pos0;
			mTextureCoordinateData[currentPos + 7] = pos1;
			mTextureCoordinateData[currentPos + 8] = pos2;
			mTextureCoordinateData[currentPos + 9] = pos1;
			mTextureCoordinateData[currentPos + 10] = pos2;
			mTextureCoordinateData[currentPos + 11] = pos3 ;

			/**
			 *
			 * set up second sky
			 */

			pos0 = bottomBlocks[bottomX[i]].renderMapBlock()[1][0];
			pos1 = bottomBlocks[bottomX[i]].renderMapBlock()[1][1];
			pos2 = bottomBlocks[bottomX[i]].renderMapBlock()[1][2];
			pos3 = bottomBlocks[bottomX[i]].renderMapBlock()[1][3];
			if (bottomBlocks[bottomX[i]].getAnim()[1]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos + 12] = pos0;
			mTextureCoordinateData[currentPos + 13] = pos3;
			mTextureCoordinateData[currentPos + 14] = pos0;
			mTextureCoordinateData[currentPos + 15] = pos1;
			mTextureCoordinateData[currentPos + 16] = pos2;
			mTextureCoordinateData[currentPos + 17] = pos3;

			mTextureCoordinateData[currentPos + 18] = pos0;
			mTextureCoordinateData[currentPos + 19] = pos1;
			mTextureCoordinateData[currentPos + 20] = pos2;
			mTextureCoordinateData[currentPos + 21] = pos1;
			mTextureCoordinateData[currentPos + 22] = pos2;
			mTextureCoordinateData[currentPos + 23] = pos3;
			


			//Log.d("bottom X", Integer.toString(bottomX[i]));
			frameCount++;
		}
		for(int i = 0; i < 5; i++){

			/**
			 *
			 * set up third sky
			 */
			currentPos =  frameCount * 24;
			pos0 = bottomBlocks[bottomX[i]].renderMapBlock()[2][0];
			pos1 = bottomBlocks[bottomX[i]].renderMapBlock()[2][1];
			pos2 = bottomBlocks[bottomX[i]].renderMapBlock()[2][2];
			pos3 = bottomBlocks[bottomX[i]].renderMapBlock()[2][3];

			if (bottomBlocks[bottomX[i]].getAnim()[2]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos] = pos0;
			mTextureCoordinateData[currentPos + 1] = pos3;
			mTextureCoordinateData[currentPos + 2] = pos0;
			mTextureCoordinateData[currentPos + 3] = pos1;
			mTextureCoordinateData[currentPos + 4] = pos2;
			mTextureCoordinateData[currentPos + 5] = pos3;



			mTextureCoordinateData[currentPos + 6] = pos0;
			mTextureCoordinateData[currentPos + 7] = pos1;
			mTextureCoordinateData[currentPos + 8] = pos2;
			mTextureCoordinateData[currentPos + 9] = pos1;
			mTextureCoordinateData[currentPos + 10] = pos2;
			mTextureCoordinateData[currentPos + 11] = pos3;

			/**
			 *
			 * set up fourth sky
			 */
			pos0 = bottomBlocks[bottomX[i]].renderMapBlock()[3][0];
			pos1 = bottomBlocks[bottomX[i]].renderMapBlock()[3][1];
			pos2 = bottomBlocks[bottomX[i]].renderMapBlock()[3][2];
			pos3 = bottomBlocks[bottomX[i]].renderMapBlock()[3][3];

			if (bottomBlocks[bottomX[i]].getAnim()[3]) {
				pos0 += (tileWidth * (float)(framePos));
				pos2 += (tileWidth * (float)(framePos));

			}

			mTextureCoordinateData[currentPos + 12] = pos0;
			mTextureCoordinateData[currentPos + 13] = pos3;
			mTextureCoordinateData[currentPos + 14] = pos0;
			mTextureCoordinateData[currentPos + 15] = pos1;
			mTextureCoordinateData[currentPos + 16] = pos2;
			mTextureCoordinateData[currentPos + 17] = pos3;

			mTextureCoordinateData[currentPos + 18] = pos0;
			mTextureCoordinateData[currentPos + 19] = pos1;
			mTextureCoordinateData[currentPos + 20] = pos2;
			mTextureCoordinateData[currentPos + 21] = pos1;
			mTextureCoordinateData[currentPos + 22] = pos2;
			mTextureCoordinateData[currentPos + 23] = pos3;

			frameCount++;
		}


	}
	public void setupBgTextureCoords(){

    	mBgTextureCoordinateData = new float[]{
			0.0f, 0.0f,
			0.0f, 1.0f,
			1.0f, 0.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f,
				0.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 0.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f
		};
	}
	public void setupPolyCoords(){
		float segW =  2f / 8f; //(2f / 4f)/ 2f;
		float segH = 2f / 6f;
		float hSegW = segW /2f ;
		float hSegH = segH / 2f;

		int squareCount = 0;
		int lastSquare = 0;

		for(float h = (1f - hSegH); h  > -1.0f; h -= segH ){
			for(float w =( -1.0f + hSegW); w < 1.4f; w += segW){


				//first triangle
				mPositionData[(squareCount * 18) ] = w - hSegW;
				mPositionData[(squareCount * 18) + 1] = h + hSegH;
				mPositionData[(squareCount * 18) + 2] = 1.0f;
				mPositionData[(squareCount * 18) + 3] = w - hSegW;
				mPositionData[(squareCount * 18) + 4] = h - hSegH;
				mPositionData[(squareCount * 18) + 5] = 1.0f;
				mPositionData[(squareCount * 18) + 6 ] = w + hSegW;
				mPositionData[(squareCount * 18) + 7] = h + hSegH;
				mPositionData[(squareCount * 18) + 8] = 1.0f;
				//second triangle
				mPositionData[(squareCount * 18) + 9] = w - hSegW;
				mPositionData[(squareCount * 18) + 10] = h - hSegH;
				mPositionData[(squareCount * 18) + 11] = 1.0f;
				mPositionData[(squareCount * 18) + 12] = w + hSegW;
				mPositionData[(squareCount * 18) + 13] = h - hSegH;
				mPositionData[(squareCount * 18) + 14] = 1.0f;
				mPositionData[(squareCount * 18) + 15] = w + hSegW;
				mPositionData[(squareCount * 18) + 16] = h + hSegH;
				mPositionData[(squareCount * 18) + 17] = 1.0f;

				squareCount++;
			}

		}

		mBgPositionData = new float[] {
				-1.0f , 1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				1.0f, 1.0f, 1.0f,
				-1.0f, -1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,

				1.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				3.0f, 1.0f, 1.0f,
				3.0f, 1.0f, 1.0f,
				1.0f, -1.0f, 1.0f,
				3.0f, -1.0f, 1.0f

		};

		
		
	}
	public boolean getMapNeedsRenderUpdate(){
        return mapNeedsRenderUpdate;
    }
    public void setMapNeedsRenderUpdate(boolean b){
        mapNeedsRenderUpdate = b;
    }



	/**
	 *
	 *
	 * Draw scene playing
	 */
	public void drawScene(Canvas canvas){

		if(currentScene.playing) {
			//currentScene.draw(canvas);

		}else{

			endLevel = true;
		}
	}
	public void endScene(){
		//currentScene.nullImage();
		endLevel = true;

	}
	public void setHasBoss( boolean b){
		hasBoss = b;
	}
	public void setBossActive( boolean b){

		bossActive = b;
	}
	public boolean isSceneOver(){
		return currentScene.isEndScene();
	}
	public void initScene(int sceneNumber){

		currentScene = new movieScene( context, sceneNumber);
		Log.d("Scene Number", Integer.toString(sceneNumber));
	}



	public void loadMapFromFile(){
		InputStreamReader ir = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(ir);

		try{
		this.ref =  reader.readLine();

		mapLength = Integer.parseInt(reader.readLine());


		bottomBlocks = new mapBlock[mapLength];
		topBlocks =  new mapBlock[mapLength];
		skyBlocks = new mapBlock[mapLength];


		//System.out.println(mapLength);
		int[] xPosSegments = new int[4];
		int[] yPosSegments = new int[4];
		int[] DrawSegmentsX = new int[4];
		int[] DrawSegmentsY = new int[4];
		boolean[] hasO = new boolean[4];
		boolean[] hasAnim = new boolean[4];

		int sectionCounter = 0;
			int sectionMultiplyer = 0;
			int sectionSubtraction = 0;
			int sectionOffset = 0;
			int blockCount = 0;




		for(int i = 0; i< mapLength; i++){
			reader.readLine();
			reader.readLine();

			if(sectionCounter > blockCount){
				sectionMultiplyer++;
				sectionSubtraction = (i * BLOCK_WIDTH);
				sectionCounter = 0;

			}


			skyBlock = new mapBlock(BLOCK_WIDTH * i, 0, 0 );




			for(int y = 0; y < 2 ; y++){
				for(int x = 0; x < 2; x++){
					totalPos = x + y;
					reader.readLine();
					reader.readLine();

					sectionOffset = (i* BLOCK_WIDTH) - sectionSubtraction;
					/*
					xPosSegments[k] = Integer.parseInt(reader.readLine());
					yPosSegments[k] = Integer.parseInt(reader.readLine());
					*/
					xPosSegments[counter] = ((x * SEGMENT_WIDTH) );

					yPosSegments[counter] = ((y * SEGMENT_HEIGHT) );

					DrawSegmentsX[counter] = Integer.parseInt(reader.readLine());
					DrawSegmentsY[counter] = Integer.parseInt(reader.readLine());
					hasO[counter] = Boolean.parseBoolean(reader.readLine());
					hasAnim[counter] = Boolean.parseBoolean(reader.readLine());
					for(int j = 0; j < 4; j++){

						//System.out.println("Obstacle: "+ k + " Obstacle #"+ j+ " Obstacle: "+readObstacle[j]);
						topObstacleArray[counter][j]= Float.parseFloat(reader.readLine());

					}
					counter ++;
				}



			}
			counter = 0;
			sectionCounter++;

			skyBlock.initSegments(xPosSegments, yPosSegments, DrawSegmentsX, DrawSegmentsY, topObstacleArray,hasO, hasAnim);
			skyBlocks[i] = skyBlock;
		}

			 sectionCounter = 0;
			 sectionMultiplyer = 0;
			 sectionSubtraction = 0;
			 sectionOffset = 0;


			for(int i = 0; i< mapLength; i++){
			reader.readLine();
			reader.readLine();

			topBlock = new mapBlock(BLOCK_WIDTH * i, BLOCK_HEIGHT, 0 );





			for(int y = 0; y < 2 ; y++){
				for(int x = 0; x < 2; x++){
					totalPos = x + y;
					reader.readLine();
					reader.readLine();

					sectionOffset = (i* BLOCK_WIDTH) - sectionSubtraction;
					/*
					xPosSegments[k] = Integer.parseInt(reader.readLine());
					yPosSegments[k] = Integer.parseInt(reader.readLine());
					*/
					xPosSegments[counter] = ((x * SEGMENT_WIDTH) );

					yPosSegments[counter] = ((y * SEGMENT_HEIGHT)+ BLOCK_HEIGHT );

					DrawSegmentsX[counter] = Integer.parseInt(reader.readLine());
					DrawSegmentsY[counter] = Integer.parseInt(reader.readLine());
					hasO[counter] = Boolean.parseBoolean(reader.readLine());
					hasAnim[counter] = Boolean.parseBoolean(reader.readLine());
					for(int j = 0; j < 4; j++){

						//System.out.println("Obstacle: "+ k + " Obstacle #"+ j+ " Obstacle: "+readObstacle[j]);
						topObstacleArray[counter][j]= Float.parseFloat(reader.readLine());

					}
					counter ++;
				}



			}
				if(sectionCounter > blockCount){
					sectionMultiplyer++;
					sectionSubtraction = (i * BLOCK_WIDTH);
					sectionCounter = 0;

				}

			counter = 0;
				sectionCounter++;
				/*
				for(int j = 0; j < 4; j++){
					for(int q = 0; q < 4; q++){

					System.out.println("Obstacle: "+ j + " Obstacle #"+ q+ " Obstacle: "+ bottomObstacleArray[j][q] );
					}
				}*/
			topBlock.initSegments(xPosSegments, yPosSegments, DrawSegmentsX, DrawSegmentsY, topObstacleArray,hasO, hasAnim);
			topBlocks[i] = topBlock;
		}

			sectionCounter = 0;
			sectionMultiplyer = 0;
			sectionSubtraction = 0;
			sectionOffset = 0;
		for(int i = 0; i< mapLength; i++){
			reader.readLine();
			reader.readLine();

			bottomBlock = new mapBlock(BLOCK_WIDTH * i,( 2 *BLOCK_HEIGHT), 0 );



			if(sectionCounter > blockCount){
				sectionMultiplyer++;
				sectionSubtraction = (i * BLOCK_WIDTH);
				sectionCounter = 0;

			}

			for(int y = 0; y < 2 ; y++){
				for(int x = 0; x < 2; x++){

					totalPos = x + y;
					reader.readLine();
					reader.readLine();

					sectionOffset = (i* BLOCK_WIDTH) - sectionSubtraction;
					/*
					xPosSegments[k] = Integer.parseInt(reader.readLine());
					yPosSegments[k] = Integer.parseInt(reader.readLine());
					*/
					xPosSegments[counter] = ( (x * SEGMENT_WIDTH) );

					//System.out.println("xPos: "+ xPosSegments[counter]);
					yPosSegments[counter] = ((y * SEGMENT_HEIGHT)+ (BLOCK_HEIGHT * 2));

					DrawSegmentsX[counter] = Integer.parseInt(reader.readLine());
					DrawSegmentsY[counter] = Integer.parseInt(reader.readLine());
					hasO[counter] = Boolean.parseBoolean(reader.readLine());
					hasAnim[counter] = Boolean.parseBoolean(reader.readLine());
					for(int j = 0; j < 4; j++){

						//System.out.println("Obstacle: "+ k + " Obstacle #"+ j+ " Obstacle: "+readObstacle[j]);
						bottomObstacleArray[counter][j]= Float.parseFloat(reader.readLine());

					}
					counter ++;
				}



			}
			counter = 0;
			sectionCounter++;
			bottomBlock.initSegments(xPosSegments, yPosSegments, DrawSegmentsX, DrawSegmentsY, bottomObstacleArray, hasO, hasAnim);
			bottomBlocks[i] = bottomBlock;
		}
		ir.close();
		reader.close();

		} catch (IOException e1) {

			e1.printStackTrace();
		}

	}

	public void dropAllButBackground(){
		if(mapImage != null) {
			mapImage.recycle();
			mapImage = null;

		}

	}
	public boolean isBossActive(){
		return bossActive;
	}
	public boolean isShowingPoints(){return showingPoints;}
	public boolean end(){
		return endMap;
	}
	public boolean isBossActivated(){
		return bossActivated;
	}
	public void setBossActivated(boolean b){
		this.bossActivated = b;

	}
	public void resetPosX(){

		viewX = 0;
	}

	public int getMapLength(){
		return mapLength;
	}
	public boolean isHasScene(){
		return hasScene;
	}
	public int getBossType(){
		return bossType;
	}
	public boolean[] getCenterThasO(){ return topBlocks[bottomX[2]].getHasO(); }
	public boolean[] getCenterBhasO(){ return bottomBlocks[bottomX[2]].getHasO(); }
	public boolean[] getLeftThasO(){ return topBlocks[bottomX[1]].getHasO(); }
	public boolean[] getLeftBhasO(){ return bottomBlocks[bottomX[1]].getHasO(); }
	public boolean[] getRightThasO(){ return topBlocks[bottomX[3]].getHasO(); }
	public boolean[] getRightBhasO(){ return bottomBlocks[bottomX[3]].getHasO(); }

	public boolean[] getFarRightThasO(){ return topBlocks[bottomX[4]].getHasO();}
	public boolean[] getFarRightBhasO(){ return bottomBlocks[bottomX[4]].getHasO();}

	public float[][] getFarRightTopObstacle(){
		return topBlocks[bottomX[4]].getObstacleRect(4);
	}
	public float[][] getFarRightBottomObstacle(){
		return bottomBlocks[bottomX[4]].getObstacleRect(4);
	}

	public float[][] getCenterBottomObstacle(){
		return bottomBlocks[bottomX[2]].getObstacleRect( 2 );
	}
	public void addHero(HeroEntity hero){
		this.hero = hero;
	}
	public float[][] getCenterTopObstacle(){
		return topBlocks[bottomX[2]].getObstacleRect(2 );
	}
	public float[][] getRightBottomObstacle(){
		return bottomBlocks[bottomX[3]].getObstacleRect(3);
	}
	public float[][] getRightTopObstacle(){
		return topBlocks[bottomX[3]].getObstacleRect(3);
	}
	public float[][] getLeftBottomObstacle(){
		return bottomBlocks[bottomX[1]].getObstacleRect(1);
	}
	public float[][] getLeftTopObstacle(){
		return topBlocks[bottomX[1]].getObstacleRect(1);
	}
	public float[][] getCenterSkyObstacle(){
		return skyBlocks[bottomX[1]].getObstacleRect(1);
	}
	public float[][] getRightSkyObstacle(){
		return skyBlocks[bottomX[2]].getObstacleRect(2);
	}

	

}
