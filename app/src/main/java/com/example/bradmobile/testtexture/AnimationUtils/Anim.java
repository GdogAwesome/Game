package com.example.bradmobile.testtexture.AnimationUtils;

/**
 * Created by b_hul on 3/25/2018.
 */

public class Anim {
    private int[] continuousRun;
    private int[] startRun;
    private int[] fire;
    private int[] jump;
    private int[] standing;
    private int[] standingUpper;
    private int[] index;
    private int[] dying;

    private final static int AMOUNT_OF_ANIMS = 7;
    private boolean[] continuous = new boolean[AMOUNT_OF_ANIMS];
    private boolean[] reciprocating = new boolean[AMOUNT_OF_ANIMS];
    private boolean startJump = true;
    private boolean startRune = true;


    public final static int CONTINUOUS_RUN =0;
    public final static int START_RUN = 1;
    public final static int FIRE = 2;
    public final static int JUMP = 3;
    public final static int STANDING =4;
    public final static int STANDING_UPPER = 5;
    public final static int DYING = 6;
    
    private CurrentAnim currentAnim;

    public Anim(){
        index = new int[AMOUNT_OF_ANIMS];
        currentAnim = new CurrentAnim();

    }

    public void setupAnim(int animType, int numOfFrames, int startFrame){

        switch(animType){
            case CONTINUOUS_RUN:
                continuousRun = new int[numOfFrames];
                continuousRun = loadAnims(continuousRun, startFrame);
                break;
            case START_RUN:
                startRun = new int[numOfFrames];
                loadAnims(startRun, startFrame);
                break;
            case FIRE:
                fire = new int[numOfFrames];
                loadAnims(fire, startFrame);
                break;
            case JUMP:
                jump = new int[numOfFrames];
                loadAnims(jump, startFrame);
                break;
            case STANDING:
                standing = new int[numOfFrames];
                standing =loadAnims(standing, startFrame);
                break;
            case STANDING_UPPER:
                standingUpper = new int[numOfFrames];
                loadAnims(standingUpper, startFrame);
                break;
            case DYING:
                dying = new int[numOfFrames];
                loadAnims(dying, startFrame);
                break;

        }


    }

    private int[] loadAnims(int[] anim, int startFrame){
        int[] a = anim;
        int currentFrame = startFrame;


        for(int i = 0; i < a.length; i++){

            a[i] = currentFrame;
            currentFrame++;
        }


        return a;
    }
    public void run(){
        currentAnim.runAnim(continuousRun, CONTINUOUS_RUN, 2, false, true);
    }
    public void stop(boolean reciprocating){
        currentAnim.runAnim(standing, STANDING, 5, reciprocating, true);
    }
    public void dying(){
        currentAnim.runAnim(dying, DYING, 8, false, true);
    }
    public int getAnimIndex(){
        return currentAnim.getAnimIndex();
    }

    private void resetIndicies(int[] exclude){
        for(int i = 0; i < AMOUNT_OF_ANIMS; i++){
            for(int k = 0; k < exclude.length; k++) {
                if (i != exclude[k]) {
                    index[i] = 0;
                }
            }

        }
    }





}
