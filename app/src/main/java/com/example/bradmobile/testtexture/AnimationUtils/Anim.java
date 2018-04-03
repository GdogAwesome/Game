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
    private int[] attack1;
    private int[] attack2;
    private int[] attack3;
    private int[] attack4;
    private int[] attack5;
    private int[] attack6;

    private final static int AMOUNT_OF_ANIMS = 13;
    private boolean[] continuous = new boolean[AMOUNT_OF_ANIMS];
    private boolean[] reciprocating = new boolean[AMOUNT_OF_ANIMS];
    private boolean[] interruptible = new boolean[AMOUNT_OF_ANIMS];
    private int[] frameTime = new int[AMOUNT_OF_ANIMS];
    private boolean startJump = true;
    private boolean startRune = true;


    public final static int CONTINUOUS_RUN =0;
    public final static int START_RUN = 1;
    public final static int FIRE = 2;
    public final static int JUMP = 3;
    public final static int STANDING =4;
    public final static int STANDING_UPPER = 5;
    public final static int DYING = 6;
    public final static int ATTACK_1 = 7;
    public final static int ATTACK_2 = 8;
    public final static int ATTACK_3 = 9;
    public final static int ATTACK_4 = 10;
    public final static int ATTACK_5 = 11;
    public final static int ATTACK_6 = 12;
    
    private CurrentAnim currentAnim;

    public Anim(){
        index = new int[AMOUNT_OF_ANIMS];
        currentAnim = new CurrentAnim();

    }

    /**
     *
     * @param animType
     * @param numOfFrames
     * @param startFrame
     * @param frameTime
     * @param continuous
     * @param reciprocating
     * @param interruptible
     */
    public void setupAnim(int animType, int numOfFrames, int startFrame, int frameTime, boolean continuous, boolean reciprocating, boolean interruptible){

        switch(animType){
            case CONTINUOUS_RUN:
                continuousRun = new int[numOfFrames];
                loadAnims(continuousRun, startFrame);
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
            case ATTACK_1:
                attack1 = new int[numOfFrames];
                loadAnims(attack1, startFrame);
                break;
            case ATTACK_2:
                attack2 = new int[numOfFrames];
                loadAnims(attack2, startFrame);
                break;
            case ATTACK_3:
                attack3 = new int[numOfFrames];
                loadAnims(attack3, startFrame);
                break;
            case ATTACK_4:
                attack4 = new int[numOfFrames];
                loadAnims(attack4, startFrame);
                break;
            case ATTACK_5:
                attack5 = new int[numOfFrames];
                loadAnims(attack5, startFrame);
                break;
            case ATTACK_6:
                attack6 = new int[numOfFrames];
                loadAnims(attack6, startFrame);
                break;

        }
        this.frameTime[animType] = frameTime;
        this.continuous[animType] = continuous;
        this.reciprocating[animType] = reciprocating;
        this.interruptible[animType] = interruptible;


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
        currentAnim.runAnim(continuousRun, CONTINUOUS_RUN, frameTime[CONTINUOUS_RUN], reciprocating[CONTINUOUS_RUN], continuous[CONTINUOUS_RUN], interruptible[CONTINUOUS_RUN]);
    }
    public void stop(){
        currentAnim.runAnim(standing, STANDING, frameTime[STANDING], reciprocating[STANDING], continuous[STANDING], interruptible[STANDING]);
    }
    public void dying(){
        currentAnim.runAnim(dying, DYING, frameTime[DYING], reciprocating[DYING], continuous[DYING], interruptible[DYING]);
    }
    public void Attack1(){
        currentAnim.runAnim(attack1,ATTACK_1, frameTime[ATTACK_1], reciprocating[ATTACK_1], continuous[ATTACK_1], interruptible[ATTACK_1]);

    }
    public int getAnimIndex(){
        return currentAnim.getAnimIndex();
    }
    public void update(){
        currentAnim.update();
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
