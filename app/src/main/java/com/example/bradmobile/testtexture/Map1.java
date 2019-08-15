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
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.example.bradmobile.testtexture.Utils.*;


public class Map1 {

	public static final int MAP_TYPE_CITY = 0;
	public static final int MAP_TYPE_JUNGLE = 1;
	public static final int MAP_TYPE_CYBER = 2;
	public int mapType = 0;

	public float MapFinalPosX;
	public float MapFinalPosY = 0f;
	public float ViewX;
	public float prevX;
	public float viewX;
	private float ViewY = 0;
	private float obstacleOffset;
	private float originalObOffset;
	private float deltaY=0;
	private float upperBounds = .80f;
	private float lowerBounds = .00f;
	private float currentDeltaY = 0;
	public float prevY;
	public float deltaX;
	private movieScene currentScene = null;
	public boolean endMap = false;
	public boolean endLevel = false;
	private boolean hasBoss = false;
	private boolean bossActivated = false;
	private boolean bossActive = false;
	private boolean lockScreen = false;
	private boolean justUpdated = false;
	private int bossType = 0;
	private boolean displayFM = true;
	private boolean displaySM = true;
	private int startX = Constants.START_X;
	private boolean reverseAnim = false;

	private boolean hasScene = true;
	private boolean startPoints = false;// true;
	private boolean showingPoints = false;

	/*

	map opengl handles
	 */
	private int normalTexture = 0;
	private int texture = 0;
	private int BgTexture = 0;
	private int layerIndexOffset = 0;
	private float[] mModelMatrix = new float[16];
	private float[] mBackgroundMatrix = new float[16];
	private float[] mBackgroundMatrix2 = new float[16];
	private boolean BGMatrix1Right = false;
	private float[] mTextureCoordinateData;
	private short[] mAnimData;
	private short[] mIndices;
	private float[] mBgTextureCoordinateData = new float[24];
	private FloatBuffer TextureCoordinateBuffer;
	private FloatBuffer BgTextureCoordinateBuffer;
	private FloatBuffer PositionFloatBuffer;
	private FloatBuffer BgPositionFloatBuffer;
	private ShortBuffer drawListBuffer;
	private ShortBuffer animBuffer;
	private float[] lightColor = new float[]{1.0f, 1.0f, 1.0f};
	private int animBufferOffset = 0;
	private int textureBufferOffset = 0;
	private int primativesToDraw = 360;
	private float[] mPositionData ;
	private float[] mBgPositionData;// = new float[36];
	private float totalMoveSpace = 0f;
	private float BgMoveSpeed = Constants.TEST_RUN_SPEED * .5f;
	private float BgTotalMoveSpace = 0f;
	private double absoluteMoveSpace = 0.0f;
    private boolean mapNeedsRenderUpdate = false;
    private int offsetCounter = 0;

	/**
	 *
	 * position info
	 */


    private HeroEntity hero;
	Context context;
	private int timer = 0;

	private int framePos = 0;

	
	float dif =  (((float)Constants.SCREEN_WIDTH - 1366) / Constants.SCREEN_WIDTH) ;
	
	public int[] posX = new int[]{ 0, 1, 2, 3, 4 };
	public int[] posY = new int[]{0, 2, 3, 4};

	public int[] currentBottom = new int[100];
	public int[] currentTop = new int[100];

	public String ref;
	private int mapLength = 0;
	private int mapHeight = 0;
	private Canvas canvas;
	public final static int BLOCK_WIDTH = Constants.BLOCK_WIDTH;
	public final static int BLOCK_HEIGHT = Constants.BLOCK_HEIGHT;
	public final static int SEGMENT_WIDTH = Constants.SEGMENT_WIDTH ;
	public final static int SEGMENT_HEIGHT = Constants.SEGMENT_HEIGHT ;
	public final static float RIGHT_BORDER = Constants.RIGHT_BORDER;
	public final static float LEFT_BORDER = Constants.LEFT_BORDER;


	public boolean cutScene = false;
	private mapBlock[][] mapBlocks;


	public boolean canMoveLeft = true;
	public boolean canMoveRight = true;
	private boolean mapUpdateLeft = true;
	private boolean mapReadyToDraw = false;

	public int[] readObstacle = new int[4];
	public float[][] topObstacleArray = new float[4][4];
	public float[][] bottomObstacleArray = new float[4][4];
	public float[][] skyObstacleArray = new float[4][4];

	public InputStream is;


	int totalPos = 0;
	int counter = 0;

	
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

		this.MapFinalPosX = x;
		this.MapFinalPosY = y;

	}
	public void updateAnim(){
		timer += 1;
		
		if(timer >= 6){
			if(!reverseAnim){
				if(framePos < 3){
					framePos += 1;
					//updateAnimBlocks();
				}else{
					reverseAnim = true;

				}
			}else if(reverseAnim){
				if(framePos > 0){
					framePos -=1;
					//updateAnimBlocks();
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
		setLightColor(mapNo);

			// TODO fix this hardcoded mess before adding new levels!!! meh, maybe not..
			switch(mapNo){

                case 1:
					is = context.getResources().openRawResource(R.raw.test9);
					hasBoss = true;
					bossType = BossEntity.FIRST_BOSS;
					texture = R.drawable.cyber_map;
					normalTexture = R.drawable.cyber_map_norm;
					BgTexture = R.drawable.city_background;
					hasScene = false;
					mapType = MAP_TYPE_CITY;

					break;
				case 2:

                    is = context.getResources().openRawResource(R.raw.test_map);
					texture = R.drawable.test_map;
					BgTexture = R.drawable.overgrown_city;
					normalTexture = R.drawable.test_map_norm;
					hasBoss = false;
					bossType = BossEntity.NO_BOSS;
					mapType = MAP_TYPE_JUNGLE;
					hasScene = false;
					break;
				case 3:
					is = context.getResources().openRawResource(R.raw.test_boss);
					hasBoss = true; //true;
					bossType = BossEntity.FIRST_BOSS;
					texture = R.drawable.cyber_map;
					normalTexture = R.drawable.cyber_map_norm;
					BgTexture = R.drawable.city_background;
					mapType = MAP_TYPE_CITY;
					hasScene = false;
					break;



			}

			loadMapFromFile();

            setupModelMatrix();


			setupPolyCoords();

			setupBgTextureCoords();

			setupTextureCoords();
		createFloatBuffers();

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
	}

	public float moveMap(float heroLeft, float heroRight, float heroy, boolean canMoveLeft, boolean canMoveRight, int playerCommand, float frameVariance){//}, float viewX){


		deltaY = 0.0f;
		//Log.e("frame variance", Float.toString(frameVariance));
		if(heroy > upperBounds){

			//if(posY[1] != 0) {
				deltaY = (heroy - upperBounds) * .25f;
				currentDeltaY += (deltaY * frameVariance);
				Matrix.translateM(mModelMatrix, 0, 0.0f, (frameVariance * -deltaY), 0.0f);
				obstacleOffset -= (frameVariance * deltaY);
				MapFinalPosY -= (frameVariance * deltaY);
			//}
			//deltaY = 0.0f;
			if(currentDeltaY >= .333333f){
				if(posY[1] > 0){
					posY[3] -= 1;
					posY[2] = posY[3] - 1;
					posY[1] = posY[2] - 1;
					posY[0] = posY[1] - 1;
				}
				currentDeltaY -= currentDeltaY;

			}



		}else if(heroy < lowerBounds){

			if(currentDeltaY <= -.33333333f){
				if(posY[3] < mapHeight -1){
					posY[3] += 1;
					posY[2] = posY[3] - 1;
					posY[1] = posY[2] - 1;
					posY[0] = posY[1] - 1;
				}

				currentDeltaY -= currentDeltaY;

			}
			if(obstacleOffset > originalObOffset){
				obstacleOffset = originalObOffset;
				mModelMatrix[13] = 0.0f;// -= mModelMatrix[13];
				MapFinalPosY -= MapFinalPosY;
				deltaY -= (frameVariance * deltaY);


			}else if( obstacleOffset < originalObOffset) {
				deltaY =  (heroy - lowerBounds) * .25f;
				currentDeltaY += (frameVariance * deltaY);
				obstacleOffset -= (frameVariance * deltaY);
				MapFinalPosY -= (frameVariance * deltaY);
				Matrix.translateM(mModelMatrix, 0, 0.0f, (frameVariance * -deltaY), 0.0f);
				//Log.e("too", "low");
			}/*else{
				obstacleOffset = originalObOffset;
				deltaY = 0.0f;
				mModelMatrix[13] = 0.0f;
			}*/
		}
			if (absoluteMoveSpace >= .25f && displayFM){
                hero.displayMessage(1, 8, false);
				displayFM = false;
			}else if(absoluteMoveSpace >= 10.0f && displaySM){
				hero.displayMessage(2, 8, false);
				displaySM = false;
			}
		if(!lockScreen) {
			if (canMoveRight && heroRight >= RIGHT_BORDER && playerCommand == 1 ) {

				if (BgTotalMoveSpace <= (-2.2f )) {

					BgTotalMoveSpace -= BgTotalMoveSpace;
					mBackgroundMatrix[12] -= (frameVariance * BgMoveSpeed);

					if(BGMatrix1Right){
						BGMatrix1Right = false;
						mBackgroundMatrix2[12] = mBackgroundMatrix[12] + 2.2f;
					}else{
						BGMatrix1Right = true;
						mBackgroundMatrix[12] = mBackgroundMatrix2[12] + 2.2f;
					}
					//mBackgroundMatrix2[12] -= BgMoveSpeed;
				}else {
					BgTotalMoveSpace -= (frameVariance * BgMoveSpeed);
					mBackgroundMatrix[12] -= (frameVariance * BgMoveSpeed);
					//mBackgroundMatrix2[12] -= BgMoveSpeed;
                    if(BGMatrix1Right){
                        mBackgroundMatrix2[12] = mBackgroundMatrix[12] - 2.2f;
                    }else{
                        mBackgroundMatrix2[12] = mBackgroundMatrix[12] + 2.2f;

                    }
				}
				if(totalMoveSpace >= (-1f *(Constants.TOTAL_MAP_MOVE ))){

					Matrix.translateM(mModelMatrix, 0, (frameVariance * (-1f * (Constants.TEST_RUN_SPEED))), 0.0f, 0.0f);
					totalMoveSpace -= (frameVariance * Constants.TEST_RUN_SPEED);
					absoluteMoveSpace += (frameVariance * Constants.TEST_RUN_SPEED);

				} else {
					if(posX[4] < mapLength -1 ) {
						posX[0] += 1;
						posX[1] += 1;
						posX[2] += 1;
						posX[3] += 1;
						posX[4] += 1;
						offsetCounter ++;
						rightMapEdge = false;
					}else{
						setEndMap(true);
						rightMapEdge = true;
					}

					if(!rightMapEdge) {
						Matrix.translateM(mModelMatrix, 0, (-1f * totalMoveSpace), 0.0f, 0.0f);
						totalMoveSpace -= totalMoveSpace;//Constants.TOTAL_MAP_MOVE;
						totalMoveSpace -= (frameVariance * Constants.TEST_RUN_SPEED);
						Matrix.translateM(mModelMatrix, 0, (frameVariance * (-1f * (Constants.TEST_RUN_SPEED))), 0.0f, 0.0f);

						absoluteMoveSpace += (frameVariance * Constants.TEST_RUN_SPEED);

					}
					textureBufferOffset = (offsetCounter * 4 * 4 * 2 * mapHeight) * 4;
					animBufferOffset = (offsetCounter * 4 * 4 * mapHeight) * 2;


                    mapNeedsRenderUpdate = true;
					//updateTextureCoords();

				}

			} else if (canMoveLeft && heroLeft <= LEFT_BORDER &&  playerCommand == 2) {

				if ((totalMoveSpace +(Constants.TOTAL_MAP_MOVE ))<= (Constants.TOTAL_MAP_MOVE )) {

                    if (BgTotalMoveSpace > (0.0f )) {

                        BgTotalMoveSpace -= 2.2f;
                        mBackgroundMatrix[12] += (BgMoveSpeed * frameVariance);

                        if(BGMatrix1Right){
                            BGMatrix1Right = false;
                            mBackgroundMatrix[12] = mBackgroundMatrix2[12] - 2.2f;
                        }else{
                            BGMatrix1Right = true;
                            mBackgroundMatrix2[12] = mBackgroundMatrix[12] - 2.2f;
                        }
                        //mBackgroundMatrix2[12] -= BgMoveSpeed;
                    }else {
                        BgTotalMoveSpace += (BgMoveSpeed * frameVariance);
                        mBackgroundMatrix[12] += (BgMoveSpeed * frameVariance);
                        //mBackgroundMatrix2[12] -= BgMoveSpeed;
                        if(BGMatrix1Right){
                            mBackgroundMatrix2[12] = mBackgroundMatrix[12] - 2.2f;
                        }else{
                            mBackgroundMatrix2[12] = mBackgroundMatrix[12] + 2.2f;

                        }
                    }
					prevX = viewX;
					deltaX = prevX - viewX;

					Matrix.translateM(mModelMatrix, 0, (Constants.TEST_RUN_SPEED), 0.0f, 0.0f);
					totalMoveSpace += Constants.TEST_RUN_SPEED;
					absoluteMoveSpace -= Constants.TEST_RUN_SPEED;


				} else {


					if (posX[0] > 0) {


						posX[0] -= 1;
						posX[1] -= 1;
						posX[2] -= 1;
						posX[3] -= 1;
						posX[4] -= 1;
						offsetCounter --;
						mapUpdateLeft = true;
						leftMapEdge = false;


					}else{
						mapUpdateLeft = false;
						leftMapEdge = true;
					}

					if(!leftMapEdge) {

						/*
						if ((BgTotalMoveSpace + 1f) < 1.02f) {
							BgTotalMoveSpace += BgMoveSpeed;
							mBackgroundMatrix[12] += BgMoveSpeed;
						} else if ((BgTotalMoveSpace + 1f) >= 1.02f) {
							BgTotalMoveSpace -= 2.0f;
							mBackgroundMatrix[12] -= 2.0f;

						}*/

						Matrix.translateM(mModelMatrix, 0, (-1f * Constants.TOTAL_MAP_MOVE) + totalMoveSpace, 0.0f, 0.0f);
						totalMoveSpace -= Constants.TOTAL_MAP_MOVE + (-1f * totalMoveSpace);
						Matrix.translateM(mModelMatrix, 0, (Constants.TEST_RUN_SPEED), 0.0f, 0.0f);
						totalMoveSpace += Constants.TEST_RUN_SPEED;
						absoluteMoveSpace -= Constants.TEST_RUN_SPEED;
					}


					textureBufferOffset = (offsetCounter * 4 * 4 * 2 * mapHeight ) * 4;
					animBufferOffset = (offsetCounter * 4 * 4 * mapHeight ) * 2;

					mapNeedsRenderUpdate = true;
					//updateTextureCoords();


				}
			}
		}


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

			hasScene = false;
			currentScene.nullImage();
			initScene(1);
			cutScene =true;


		}
		else{
			endLevel = true;
		}
	}


	public FloatBuffer[] getDrawCoords(){

		//TextureCoordinateBuffer.rewind();
		//TextureCoordinateBuffer.put(mTextureCoordinateData);
		FloatBuffer[] temp =new FloatBuffer[2];
		temp[0] = TextureCoordinateBuffer;
		temp[1] = BgTextureCoordinateBuffer;

        return temp;
    }

    public ShortBuffer getAnimBuffer(){
		return animBuffer;
	}

    private void createFloatBuffers(){
		
		TextureCoordinateBuffer = BufferUtils.getFloatBuffer(mTextureCoordinateData, 4);
		PositionFloatBuffer = BufferUtils.getFloatBuffer(mPositionData, 4);
		BgTextureCoordinateBuffer = BufferUtils.getFloatBuffer(mBgTextureCoordinateData, 4);
		BgPositionFloatBuffer = BufferUtils.getFloatBuffer(mBgPositionData, 4 );
		drawListBuffer = BufferUtils.getShortBuffer(mIndices, 2);
		animBuffer = BufferUtils.getShortBuffer(mAnimData, 2);

	}
	public FloatBuffer[] getPositionFloatBuffer(){
    	FloatBuffer[] temp = new FloatBuffer[2];

    	temp[0] = PositionFloatBuffer;
    	temp[1] = BgPositionFloatBuffer;
		return temp ;

	}
	public int[] getTexture(){
		int[] temp = new int[3];
		temp[0] = texture;
		temp[1] = BgTexture;
		temp[2] = normalTexture;
		return temp;
	}
	public float getMapOffset(){
		return  mModelMatrix[12];
	}
	public float getAbsoluteMoveX(){
		return (float)absoluteMoveSpace;
	}
	public float getAbsoluteMoveY(){
		//Log.e("map pos y", Float.toString(MapFinalPosY));
		return MapFinalPosY;
	}
    public float[][] getmModelMatrix(){
		float[][] temp = new float[3][16];

		temp[0] = mModelMatrix;
		temp[1] = mBackgroundMatrix;
		temp[2] = mBackgroundMatrix2;
        return temp;
    }
    private void setupModelMatrix(){
        Matrix.setIdentityM(mModelMatrix, 0 );
        Matrix.scaleM(mModelMatrix, 0 , 1.02f,1.03f, 1.0f); // x= 1.02 y = 1.03
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.0f, -2.505f );


        Matrix.setIdentityM(mBackgroundMatrix, 0 );
		Matrix.scaleM(mBackgroundMatrix, 0 , 1.0f,1.00f, 1.0f);
        Matrix.translateM(mBackgroundMatrix, 0 , 0.0f, 0.0f, -2.50f);

		Matrix.setIdentityM(mBackgroundMatrix2, 0 );
		Matrix.scaleM(mBackgroundMatrix2, 0 , 1.0f,1.0f, 1.0f);
		Matrix.translateM(mBackgroundMatrix2, 0 , 2.2f, 0.0f, -2.50f);




	}

    public void setupTextureCoords(){

		int frameCount = 0;
		int animCountPos = 0;
		boolean firstRun = true;
		int heightStart = 0;
		int segmentCounter = 0;
		int startSegments = 0;
		int endSegments = 2;
		int frameSegmentOffset = 0;
		int animSegmentOffset = 0;
		int length = mapLength ;
		int remainder = mapLength % 5;
		int addToTiles = 5;
		int currentPos = 0;
		float pos0;
		float pos1;
		float pos2;
		float pos3;
		Log.e("remainder",  Integer.toString(remainder));

		/**
		 *
		 *
		 * set up sky blocks
		 */


		mTextureCoordinateData = new float[ mapLength * 4 * 4 * 2 * mapHeight ];// maplength * segments per block(4) * coordinates per segment(4) * variables per coordinate(2) * mapHeight
		mAnimData = new short[mapLength * 4 * 4 * mapHeight ]; // mapLength * segments per block(4) * coordinates per segment(4) * mapHeight
		primativesToDraw = ((mapHeight * 2) * 6 * 10);

		for(int w = 0 ; w < length; w += 1) {
			for (int h = heightStart; h < mapHeight; h++) {
				for (int s = startSegments; s <= endSegments ; s += 2) {

					if(s == endSegments){

						frameSegmentOffset = 8;
						animSegmentOffset = 4;
					}else if(s == startSegments){
						frameSegmentOffset = 0;
						animSegmentOffset = 0;
					}
					currentPos = (frameCount * 16) + frameSegmentOffset;
					animCountPos = (frameCount * 8) + animSegmentOffset;

					/**
					 *
					 * setup texture Coords
					 */
					//Log.e("s", Integer.toString(s));
					//Log.e("frameOffset" , Integer.toString(frameSegmentOffset));
					pos0 = mapBlocks[w][h].renderMapBlock()[s][0];
					pos1 = mapBlocks[w][h].renderMapBlock()[s][1];
					pos2 = mapBlocks[w][h].renderMapBlock()[s][2];
					pos3 = mapBlocks[w][h].renderMapBlock()[s][3];

					if (mapBlocks[w][h].getAnim()[s]) {
						mAnimData[animCountPos ] = 1;
						mAnimData[animCountPos + 1] = 1;
						mAnimData[animCountPos + 2] = 1;
						mAnimData[animCountPos + 3] = 1;

					} else {
						mAnimData[animCountPos] = 0;
						mAnimData[animCountPos + 1] = 0;
						mAnimData[animCountPos + 2] = 0;
						mAnimData[animCountPos + 3] = 0;
					}

					mTextureCoordinateData[currentPos] = pos0;
					mTextureCoordinateData[currentPos + 1] = pos3;
					mTextureCoordinateData[currentPos + 2] = pos0;
					mTextureCoordinateData[currentPos + 3] = pos1;
					//mTextureCoordinateData[currentPos + 4] = pos2;
					//mTextureCoordinateData[currentPos + 5] = pos3 ;


					//mTextureCoordinateData[currentPos + 6] = pos0;
					//mTextureCoordinateData[currentPos + 7] = pos1;
					mTextureCoordinateData[currentPos + 4] = pos2;
					mTextureCoordinateData[currentPos + 5] = pos1;
					mTextureCoordinateData[currentPos + 6] = pos2;
					mTextureCoordinateData[currentPos + 7] = pos3;
				}

				frameCount ++;


				if(firstRun && h == mapHeight -1 ){
					startSegments = 1;
					endSegments = 3;
					firstRun = false;
					h = -1;
				}else if(!firstRun && h == mapHeight -1 ){
					startSegments = 0;
					endSegments = 2;
					firstRun = true;
				}

			}

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

		};
	}
	public void setupPolyCoords() {
			/**
			 *
			 * setup indices
			 */
			mIndices = new short[(mapHeight * 2) * 60];
			mPositionData = new float[(mapHeight * 2) * 10 * 4 * 3];
			int vertexCount = 0;
			for (int currentIndex = 0; currentIndex < (mIndices.length); currentIndex += 6) {
				mIndices[currentIndex] = (short) (vertexCount);
				mIndices[currentIndex + 1] = (short) (vertexCount + 1);
				mIndices[currentIndex + 2] = (short) (vertexCount + 3);
				mIndices[currentIndex + 3] = (short) (vertexCount + 3);
				mIndices[currentIndex + 4] = (short) (vertexCount + 1);
				mIndices[currentIndex + 5] = (short) (vertexCount + 2);
				vertexCount += 4;

			}

			float segW = 2f / 8f; //(2f / 4f)/ 2f;
			float segH = 2f / 6f;
			float hSegW = segW / 2f;
			float hSegH = segH / 2f;
			int heightDiff = mapHeight - 3;
			float startSegmentPos = ((float)(mapHeight + heightDiff ) *segH) - hSegH;

			int squareCount = 0;
			int lastSquare = 0;


			for (float w = (-1.0f + hSegW); w < 1.4f; w += segW) {
				for (float h = startSegmentPos; h > -1.0f; h -= segH) {
					//for (float h = (1f - hSegH); h > -1.0f; h -= segH) {


					//first triangle

					mPositionData[(squareCount * 12)] = w - hSegW;
					mPositionData[(squareCount * 12) + 1] = h + hSegH;
					mPositionData[(squareCount * 12) + 2] = 1.0f;
					mPositionData[(squareCount * 12) + 3] = w - hSegW;
					mPositionData[(squareCount * 12) + 4] = h - hSegH;
					mPositionData[(squareCount * 12) + 5] = 1.0f;
					//mPositionData[(squareCount * 12) + 6 ] = w + hSegW;
					//mPositionData[(squareCount * 12) + 7] = h + hSegH;
					//mPositionData[(squareCount * 12) + 8] = 1.0f;

					//second triangle

					//mPositionData[(squareCount * 12) + 9] = w - hSegW;
					//mPositionData[(squareCount * 12) + 10] = h - hSegH;
					//mPositionData[(squareCount * 12) + 11] = 1.0f;
					mPositionData[(squareCount * 12) + 6] = w + hSegW;
					mPositionData[(squareCount * 12) + 7] = h - hSegH;
					mPositionData[(squareCount * 12) + 8] = 1.0f;
					mPositionData[(squareCount * 12) + 9] = w + hSegW;
					mPositionData[(squareCount * 12) + 10] = h + hSegH;
					mPositionData[(squareCount * 12) + 11] = 1.0f;

					squareCount++;
				}

			}


		mBgPositionData = new float[] {
				-1.1f , 1.0f, 1.0f,
				-1.1f, -1.0f, 1.0f,
				1.1f, 1.0f, 1.0f,
				1.1f, 1.0f, 1.0f,
				-1.1f, -1.0f, 1.0f,
				1.1f, -1.0f, 1.0f

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

		//TODO fix old draw scene code from old rendering method
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


		try {
			this.ref = reader.readLine();

			mapLength = Integer.parseInt(reader.readLine());
			mapHeight = Integer.parseInt(reader.readLine());

			//setup view in relation to mapHeight
			float viewDiff = (((float)(BLOCK_HEIGHT) *(float)(mapHeight)) - ((float)(BLOCK_HEIGHT) * 3.0f)  );
			if(viewDiff == 0){
				obstacleOffset = 0;
			}else{
				obstacleOffset = (((viewDiff * 2f) / 900f));
			}
			originalObOffset = obstacleOffset;

			ViewY = 0;


			int blockCounter = mapHeight -1;
			posY[3] = mapHeight - 1;
			posY[2] = posY[3] - 1;
			posY[1] = posY[2] - 1;
			posY[0] = posY[1] - 1;

					mapBlocks = new mapBlock[mapLength][mapHeight];

			//TODO Combine all this duplicate code into One function.
			//System.out.println(mapLength);
			int[] xPosSegments = new int[4];
			int[] yPosSegments = new int[4];
			int[] DrawSegmentsX = new int[4];
			int[] DrawSegmentsY = new int[4];
			boolean[] hasO = new boolean[4];
			boolean[] hasAnim = new boolean[4];

			for (int h = 0; h < mapHeight; h++) {
				for (int w = 0; w < mapLength; w++) {
					reader.readLine();
					reader.readLine();

					mapBlocks[w][h] = new mapBlock(BLOCK_WIDTH * w, BLOCK_HEIGHT * h, 0);

					for (int y = 0; y < 2; y++) {
						for (int x = 0; x < 2; x++) {
							totalPos = x + y;
							reader.readLine();
							reader.readLine();

							xPosSegments[counter] = ((x * SEGMENT_WIDTH));

							yPosSegments[counter] = ((y * SEGMENT_HEIGHT) +( h * BLOCK_HEIGHT));

							DrawSegmentsX[counter] = Integer.parseInt(reader.readLine());
							DrawSegmentsY[counter] = Integer.parseInt(reader.readLine());
							hasO[counter] = Boolean.parseBoolean(reader.readLine());
							hasAnim[counter] = Boolean.parseBoolean(reader.readLine());
							for (int j = 0; j < 4; j++) {
								topObstacleArray[counter][j] = Float.parseFloat(reader.readLine());

							}
							counter++;
						}


					}
					counter = 0;

					mapBlocks[w][h].initSegments(xPosSegments, yPosSegments, DrawSegmentsX, DrawSegmentsY, topObstacleArray, hasO, hasAnim);
				}
			}


			ir.close();
			reader.close();


		} catch (IOException e1) {

			e1.printStackTrace();
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
	public float getViewY(){
		return ViewY;
	}
	public float getDeltaY(){
		float tmp = deltaY ;

		return tmp;
	}
	public float getObstacleOffset(){
		return obstacleOffset;
	}
	public void resetPosX(){

		viewX = 0;
	}
	private void setLightColor(int mapNo){

		if(mapNo == 1){
			lightColor[0] = .85f; // .95f;
			lightColor[1] = .4f; //.5f;
			lightColor[2] = .7f; //1.0f;
		}else if(mapNo == 2){
			lightColor[0] = 1.0f;
			lightColor[1] = .87f;
			lightColor[2] = .38f;
		}else{
			lightColor[0] = .95f;
			lightColor[1] = .5f;
			lightColor[2] = 1.0f;
		}
	}
	public int getPrimativesToDraw(){
		return primativesToDraw;
	}
	public ShortBuffer getDrawListBuffer(){
		return drawListBuffer;
	}

	public int getMapLength(){
		return mapLength;
	}
	public int getAnimBufferOffset(){
		return animBufferOffset;
	}
	public int getTextureBufferOffset(){
		return textureBufferOffset;
	}
	public int getAnimFrame(){
		return framePos ;
	}
	public boolean isHasScene(){
		return hasScene;
	}
	public int getBossType(){
		return bossType;
	}
	public boolean[] getCenterThasO(){ return mapBlocks[posX[2]][posY[2]].getHasO(); }
	public boolean[] getCenterBhasO(){ return mapBlocks[posX[2]][posY[3]].getHasO(); }
	public boolean[] getLeftThasO(){ return mapBlocks[posX[1]][posY[2]].getHasO(); }
	public boolean[] getLeftBhasO(){ return mapBlocks[posX[1]][posY[3]].getHasO(); }
	public boolean[] getRightThasO(){ return mapBlocks[posX[3]][posY[2]].getHasO(); }
	public boolean[] getRightBhasO(){ return mapBlocks[posX[3]][posY[3]].getHasO(); }

	public boolean[] getFarRightThasO(){ return mapBlocks[posX[4]][posY[2]].getHasO();}
	public boolean[] getFarRightBhasO(){ return mapBlocks[posX[4]][posY[3]].getHasO();}
	public boolean[] getFarLeftThasO(){return mapBlocks[posX[0]][posY[2]].getHasO();}
	public boolean[] getFarLeftBhasO(){return mapBlocks[posX[0]][posY[3]].getHasO();}

	public float[][] getFarRightTopObstacle(){
		return mapBlocks[posX[4]][posY[2]].getObstacleRect(4);
	}
	public float[][] getFarRightBottomObstacle(){
		return mapBlocks[posX[4]][posY[3]].getObstacleRect(4);
	}

	public float[][] getCenterBottomObstacle(){
		return mapBlocks[posX[2]][posY[3]].getObstacleRect( 2 );
	}
	public void addHero(HeroEntity hero){
		this.hero = hero;
	}
	public float[][] getCenterTopObstacle(){
		return mapBlocks[posX[2]][posY[2]].getObstacleRect(2 );
	}
	public float[][] getRightBottomObstacle(){
		return mapBlocks[posX[3]][posY[3]].getObstacleRect(3);
	}
	public float[][] getRightTopObstacle(){
		return mapBlocks[posX[3]][posY[2]].getObstacleRect(3);
	}
	public float[][] getLeftBottomObstacle(){
		return mapBlocks[posX[1]][posY[3]].getObstacleRect(1);
	}
	public float[][] getLeftTopObstacle(){
		return mapBlocks[posX[1]][posY[2]].getObstacleRect(1);
	}
	public float[] getAmbientLightColor(){
		return lightColor;
	}
	public float[][] getFarLeftTopObstacle(){ return mapBlocks[posX[0]][posY[2]].getObstacleRect(0);}
	public float[][] getFarLeftBottomObstacle(){ return mapBlocks[posX[0]][posY[3]].getObstacleRect(0);}
	public float[][] getCenterSkyObstacle(){
		return mapBlocks[posX[1]][posY[1]].getObstacleRect(1);
	}
	public float[][] getRightSkyObstacle(){
		return mapBlocks[posX[2]][posY[1]].getObstacleRect(2);
	}
	public boolean isBGMatrix1Right(){
		return BGMatrix1Right;
	}

	

}
