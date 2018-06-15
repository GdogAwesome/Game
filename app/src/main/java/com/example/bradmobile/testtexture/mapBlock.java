package com.example.bradmobile.testtexture;



import android.util.Log;

import java.util.ArrayList;


public class mapBlock {
	
	public float posX;
	public float posY;
	private int spritePosX;
	private int spritePosY;
	private int objects;

	public float[] drawInfo = new float[4];
	public boolean[] animList = new boolean[4];
	float blockWidth = (400f / 1600f) * 2f ;
	float blockHeight = 300f / Constants.TOTAL_MAP_HEIGHT;
	
	BlockSegment segment;
	public BlockSegment[] segments= new BlockSegment[4];
	public ArrayList<int[]> drawArrays = new ArrayList<int[]>();
	public float[][] obstacles = new float[4][4];
	private boolean[] hasO = new boolean[4];
	public int[] tempObstacle = new int[4];
	public float[][] tempRender = new float[4][2];
	public float[][] tempFrame = new float[4][4];



	
	
	//obstacle parameters

	private int[] rectObject = new int[4];

	/**
	 * 
	 * @param x
	 * @param y
	 * @param obstacles
	 */

	
	public mapBlock(float x, float y, int obstacles ){
		
		posX = x;
		posY = y;

		this.objects = obstacles;

		

		
	}
	public void initSegments(int[] XPositions, int[] YPositions, int[] DX, int[] DY, float[][] obstacleList, boolean[] hasO, boolean[] hasAnim){


		for(int i = 0; i< 4; i++){
			
				segment = new BlockSegment(XPositions[i],YPositions[i] , DX[i], DY[i]);
				segment.setObstacleRect(hasO[i],obstacleList[i][0], obstacleList[i][1], obstacleList[i][2], obstacleList[i][3]);
				segment.setAnim(hasAnim[i]);
				segments[i] = segment;
			
		}
		for(int i = 0; i < 4; i++){
			tempRender[i] = segments[i].renderSegment();
		}
		for(int i  = 0; i<4; i++){
			animList[i] = segments[i].getAnim();
		}
		
	}


	
	public float[][] renderMapBlock(){

		return tempRender;

		

	}
	public float[][] frameMapBlock(){
		for(int i = 0; i < 4; i++){
			tempFrame[i] = segments[i].whereToDraw();
		}
		return tempFrame;

	}
	public boolean[] getAnim(){

		return animList;
	}
	
	public float[][] getObstacleRect(int index){

		float[] tempObstacle ;
		for (int i = 0; i < 4; i++){
			tempObstacle = segments[i].getObstacleRect();
			obstacles[i][0] = tempObstacle[0]+((blockWidth * (float)index))  ;
			obstacles[i][1] = tempObstacle[1];
			obstacles[i][2] = tempObstacle[2]+((blockWidth * (float)index));
			obstacles[i][3] = tempObstacle[3];


		}


		return obstacles;
		
	}
	public boolean[] getHasO(){
		for(int i = 0; i < 4; i++){
			hasO[i] = segments[i].hasObstacle;
		}
		return hasO;
	}
	public void updateBlock(int x, int y){
		
		this.posX = x;
		this.posY = y;
		int counter = 0;
		int Xcounter = 0;
		int Ycounter =0;

		for(int i = 0; i< 2; i++){
			for(int k = 0; k < 2; k++){

				//segments[counter].updateSegment(this.posX + (k * SEGMENT_WIDTH), this.posY + (i * SEGMENT_HEIGHT));
				counter ++;
			}
		}

		
		
	}
	/*
	public void addSegment(String shape, boolean obstacle,boolean anim, int xPos, int yPos, float[] bounds, int relativeX, int relativeY){
		
		for(int i = 0; i < 4; i++){
			if(relativeX > segments[i].posX && 
					relativeX < (segments[i].posX + segmentWidth) &&
					relativeY > segments[i].posY &&
					relativeY < (segments[i].posY + segmentHeight)){
				
						segments[i].updateDrawInfo(xPos, yPos);	
						segments[i].setObstacleRect(obstacle, bounds[0], bounds[1], bounds[2], bounds[3] );
						segments[i].setAnim(anim);
						//System.out.println("Draw x  "+xPos+ " Draw y "+yPos + " Segment Pos X " +segments[i].posX +" Segment Pos Y "+segments[i].posY);
						
					
				}
		}
		
	}
	*/
	public BlockSegment[] getSegments(){
		return segments;
	}


}
