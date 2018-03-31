package com.example.bradmobile.testtexture;


import android.util.Log;

public class BlockSegment {
	
	public double posX;
	public double posY;
	private double spritePosX;
	private double spritePosY;
	private int objects;
	public boolean hasObstacle = false;
	int segmentWidth = 200;
	int segmentHeight = 150;
	public final static int BLOCK_WIDTH = Constants.BLOCK_WIDTH;
	public final static int BLOCK_HEIGHT = Constants.BLOCK_HEIGHT;
	public final static int SEGMENT_WIDTH = Constants.SEGMENT_WIDTH;
	public final static int SEGMENT_HEIGHT = Constants.SEGMENT_HEIGHT;
	public float[] drawInfo = new float[4];
	public float[] whereToDraw = new float[4];
	private boolean Anim = false;
	int ViewX;
	int ViewY;
	private double calcX = 0;
	private double calcY = 0;
	double totalWidth = 2800f;
	double totalHeight = 1400f;

	double tileWidth = (200.0000f / totalWidth);// - .0007f;
	double tileHeight = 150.0000f / totalHeight;

	float obstacleTileWidth = (200f / 1600f) * 2f;
	float obstacleTileHeight = (150f / 900f) * 2f;

	
	
	//obstacle parameters

	private double[] rectObject = new double[4];
	private float[] finalRectObject = new float[4];


	/**
	 * 
	 * @param x
	 * @param y
	 * @param spritePosX
	 * @param spritePosY

	 */

	
	public BlockSegment(float x, float y, double spritePosX, double spritePosY){
		
		posX = x / totalWidth;
		posY = y / totalHeight;

		calcX = x;
		 calcY = y;




		//Log.e("x", Double.toString(x));


		if(spritePosX <= totalWidth - 200) {
			this.spritePosX = spritePosX;
		}else{
			this.spritePosX = totalWidth - 200;
		}

		if(spritePosY <= totalHeight - 200) {
			this.spritePosY = spritePosY;
		}else{
			this.spritePosY = totalHeight - 200;
		}






		drawInfo[0] = (float)(this.spritePosX / totalWidth) + .0001f;// offset rounding error ?
		drawInfo[3] = (float)(this.spritePosY / totalHeight) ;
		drawInfo[2] = (float)((this.spritePosX + 200)/ totalWidth);
		drawInfo[1] = (float)((this.spritePosY + 150) / totalHeight);
		/*
		drawInfo[2] = drawInfo[0] + tileWidth;
		drawInfo[1] = drawInfo[3] + tileHeight;
		*/


		//Log.e("yPos ", Float.toString(drawInfo[1]));



	
		
	}

	
	public float[] renderSegment(){
		//sprite.draw(g,  posX, posY, posX + BLOCK_WIDTH ,posY + BLOCK_HEIGHT, 0, 0, BLOCK_WIDTH, BLOCK_HEIGHT);


		return drawInfo;
		
		
	}
	public float[] whereToDraw(){


		return whereToDraw;

	}
	
	
	/**
	 * 
	 * @param hasO 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void setObstacleRect(boolean hasO, float x1, float y1, float x2, float y2){
		
		hasObstacle = hasO;


		double halfX = 1600 * .5;
		double halfY = 900 * .5;
		double heightDiff = 900 - calcY;
		//Log.e("test X", Double.toString( ((200 - halfX )/ 1600) * 2));
		
		
		if(hasO == true){
			//System.out.println("hosO is true");
			rectObject[0] = x1;
			rectObject[1] = y1;
			rectObject[2] = x2;
			rectObject[3] = y2;
			
		}else if(hasO == false){
			rectObject[0] = 0;
			rectObject[1] = 0;
			rectObject[2] = 0;
			rectObject[3] = 0;
		}
		finalRectObject[0] = (float)((((calcX - halfX) / 1600) * 2) + (rectObject[0] * obstacleTileWidth));
		finalRectObject[1] = (float)((((heightDiff - halfY) / 900) * 2) - (rectObject[1] * obstacleTileHeight));
		finalRectObject[2] = (float)((((calcX  - halfX) / 1600) * 2) + ( rectObject[2] * obstacleTileWidth));
		finalRectObject[3] = (float)((((heightDiff - halfY) / 900) * 2) - (rectObject[3] * obstacleTileHeight));



	}
	public void updateDrawInfo(int DrawX, int DrawY ){
		
		spritePosX = DrawX;
		spritePosY = DrawY;
		

		//System.out.println("Segment X pos "+this.posX+ " Segment Y pos "+this.posY);
		//System.out.println("Draw x  "+DrawX+ " Draw y "+DrawY);
	}
	public void setAnim(boolean a){
		this.Anim = a;
	}
	public boolean getAnim(){
		return Anim;
	}
	public void updateObstacleHorizontal(){
	
		finalRectObject[0] =(int)( this.posX + (rectObject[0] * SEGMENT_WIDTH));
		finalRectObject[1] = (int)(this.posY + (rectObject[1] * SEGMENT_HEIGHT));
		finalRectObject[2] = (int)(this.posX + ( rectObject[2] * SEGMENT_WIDTH));
		finalRectObject[3] = (int)(this.posY + (rectObject[3] * SEGMENT_WIDTH));
		
		
		
	}
	public float[] getObstacleRect(){
		//System.out.println("this segment PosY: " + posY);

		return finalRectObject;
		
	}

	/*public void updateSegment(int x, int y){
		
		this.posX = x;
		this.posY = y;
		
		updateObstacleHorizontal();

		
		
	}*/


}