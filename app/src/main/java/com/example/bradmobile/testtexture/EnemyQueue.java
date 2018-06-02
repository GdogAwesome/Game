package com.example.bradmobile.testtexture;

import android.util.Log;

public class EnemyQueue {
    private int maxEnemyTypes = 15;
    private float[] QueueDistances;
    private boolean[] queueActive;
    private int queueNo= 0;
    private int[] enemyTypes = new int[maxEnemyTypes];
    private int totalEnemyTypes = 0;
    private int maxQueues = 15;
    private float[][] enemyBounds = new float[maxEnemyTypes][4];
    private CurrentEnemyQueue[] currentQueue = new CurrentEnemyQueue[maxQueues];

    /**
     *
     * @param enemyList
     * @param enemyActive
     */
    public EnemyQueue(EnemyEntity[] enemyList, boolean[] enemyActive){

        for(int i = 0; i < maxQueues; i ++) {
            currentQueue[i] = new CurrentEnemyQueue(enemyList, enemyActive);
        }

    }

    /**
     *
     * call before loadQueue
     *
     * @param enemyType
     * @param bounds
     */
    public void initEnemies(int enemyType, float[] bounds){

        enemyBounds[enemyType] = bounds;
        this.enemyTypes[enemyType] = enemyType;
        totalEnemyTypes ++;
    }

    /**
     * call after initializing all enemies
     * @param mapNo
     */
    public void initQueue(int mapNo){
        int[] enemyTypes = new int[]{0 ,0,0,0,0};
        queueActive = new boolean[enemyTypes.length];
        float[][] enemyBounds = new float[enemyTypes.length][4];
        for(int i = 0;  i < enemyTypes.length; i ++){
            enemyBounds[i] = this.enemyBounds[enemyTypes[i]];
        }


        currentQueue[0].loadQueue(enemyTypes, enemyBounds,3.0f);
        currentQueue[1].loadQueue(enemyTypes, enemyBounds,6.0f);
        queueActive[0] = true;
        queueActive[1] = true;

    }

    public void checkQueueDistance(float mapX){

        if(mapX > currentQueue[queueNo].getDistance()){
            if(queueActive[queueNo]){
                currentQueue[queueNo].loadEnemies();
                queueActive[queueNo] = false;

            }

            //TODO hardcoded for testing
            if(queueNo < 1 ){
                queueNo ++ ;

            }

        }

    }

    public void updateTimer(){

    }
}
