package com.example.bradmobile.testtexture.AnimationUtils;

/**
 * Created by b_hul on 3/25/2018.
 */

public class CurrentAnim {
    private float timer = 0.0f;
    private int frameTime = 0;
    private int animLength = 0;
    private boolean reciprocating = false;
    private boolean continuous = false;
    private boolean interruptible = true;
    public boolean animFinished = false;
    private float frameVariance = 1.0f;
    private int[] anim;
    public int animIndex = 0;
    private int animType = -1;
    private boolean reverseAnim = false;

    public CurrentAnim(){

    }

    /**
     *
     * @param animSequence
     * @param animType
     * @param frameTime
     * @param reciprocating
     * @param continuous
     * @param interruptible
     */
    public void runAnim(int[] animSequence,int animType, int frameTime, boolean reciprocating,boolean continuous, boolean interruptible){
        if(this.animType != animType) {
            if(this.interruptible) {

                anim = animSequence;
                this.reciprocating = reciprocating;
                this.interruptible = interruptible;
                this.continuous = continuous;
                this.frameTime = frameTime;
                this.reverseAnim = false;
                timer = 0;
                animIndex = 0;
                animLength = animSequence.length;
               // if(!continuous && !reciprocating){
                    animFinished = false;
               // }
                this.animType = animType;
            }else {
                if(animFinished) {
                    anim = animSequence;
                    this.reciprocating = reciprocating;
                    this.interruptible = interruptible;
                    this.continuous = continuous;
                    this.frameTime = frameTime;
                    this.reverseAnim = false;
                    timer = 0;
                    animIndex = 0;
                    animLength = animSequence.length;
                    // if(!continuous && !reciprocating){
                    animFinished = false;
                    // }
                    this.animType = animType;
                }


            }
        }else{
            /*
            if(this.reciprocating){

                    runReciprocating();
                }else if(this.continuous){
                    runContinuous();
                }else{
                    if(!animFinished) {
                        runOnce();
                    }
                }
                */

        }


    }
    private void runReciprocating(){

        if(timer >= frameTime){
            if(!reverseAnim){
                if(animIndex < animLength -1 ){
                    animIndex ++;
                }else{
                    reverseAnim = true;
                }
            }else{
                if(animIndex > 0){
                    animIndex --;
                }else{
                    reverseAnim = false;
                }
            }
            timer = 0.0f;

        }else{
            timer += frameVariance;
        }

    }

    private void runContinuous(){
        if(timer >= frameTime){
            if(animIndex < animLength -1){
                animIndex ++;
            }else{
                animIndex = 0;
            }

            timer -= frameTime;
        }else{
            timer += frameVariance;
        }

    }
    private void runOnce(){
        if(timer >= frameTime){
            if(animIndex < animLength -1){
                animIndex ++;
            }else{
                //interruptible = true;
                animFinished = true;
            }

            timer -= frameTime;
        }else{
            timer += frameVariance;
        }

    }


    public int getAnimIndex(){
        //
        return (anim[animIndex] * 12 * 4);
    }
    public int getFrame(){
        return animIndex;
    }
    public void update(float frameVariance){
        this.frameVariance = frameVariance;
        if(this.reciprocating){

            runReciprocating();
        }else if(this.continuous){
            runContinuous();
        }else{
            if(!animFinished) {
                runOnce();
            }
        }



    }
    public boolean isDone(){
        return animFinished;
    }
    public void setAnimType(int animType){
        this.animType = animType;

    }
}
