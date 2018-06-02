package com.example.bradmobile.testtexture;

import android.util.Log;

public class CurrentEnemyQueue {
    private EnemyEntity[] eList;
    private EnemyEntity[] privateEList;
    private boolean[] eActive;
    private int[] enemyTypes;
    private float distance = 0;
    private float[][] enemyBounds;


    public CurrentEnemyQueue(EnemyEntity[] enemyList, boolean[] enemyActive) {
        eList = enemyList;
        eActive = enemyActive;

    }

    /**
     *
     * @param enemyTypes
     * @param objectBounds
     * @param distanceX
     */
    public void loadQueue(int[] enemyTypes, float[][] objectBounds, float distanceX ){

        this.distance = distanceX;
        this.enemyTypes = enemyTypes;
        this.enemyBounds = objectBounds;
        privateEList = new EnemyEntity[enemyTypes.length];
        for(int i = 0; i < enemyTypes.length; i ++ ){
            privateEList[i] = getEnemyEntity(enemyTypes[i], objectBounds[i], i);

        }

    }

    public void loadEnemies(){
        for(int i = 0; i < enemyTypes.length; i++){
            for(int k = 0; k <  EnemyContainer.MAX_ENEMIES; k ++ ){

                if(!eActive[k]){
                    eList[k] = privateEList[i];
                    eActive[k] = true;
                    k = EnemyContainer.MAX_ENEMIES;

                }
            }
        }
    }

    private EnemyEntity getEnemyEntity(int enemyType, float[] objectBounds, int index){
        EnemyEntity tempE;
        float offset = ((float)(index) * .1f) + 1.0f;
        switch(enemyType){
            case EnemyContainer.ELECTRIC_SHIP:
                tempE = new FlyingEnemy(distance + offset, .5f, enemyType);
                tempE.InitEnemy( 4, 2, objectBounds);
                break;

            case EnemyContainer.FLYING_SHIP:
                tempE = new FlyingEnemy(distance + offset, .5f, enemyType);
                tempE.InitEnemy(4, 2, objectBounds);
                break;
            default:
                tempE = new EnemyEntity(distance + offset, .5f, enemyType);
                tempE.InitEnemy(4, 2, objectBounds);

        }

        return tempE;
    }
    public float getDistance(){
        return distance;
    }
}
