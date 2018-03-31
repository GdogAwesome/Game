package com.example.bradmobile.testtexture.AnimationUtils;

/**
 * Created by b_hul on 3/25/2018.
 */

public class CurrentAnim {
    private int timer = 0;
    private int frameTime = 0;
    private int animLength = 0;
    private boolean reciprocating = false;
    private boolean interuptable = true;
    private int[] anim;
    private int animIndex = 0;
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
     * @param interuptable
     */
    public void runAnim(int[] animSequence,int animType, int frameTime, boolean reciprocating, boolean interuptable){
        if(this.animType != animType) {
            if(this.interuptable) {

                anim = animSequence;
                this.reciprocating = reciprocating;
                this.interuptable = interuptable;
                this.frameTime = frameTime;
                this.reverseAnim = false;
                timer = 0;
                animIndex = 0;
                animLength = animSequence.length;
                this.animType = animType;
            }else{

                if(reciprocating){
                    runReciprocating();
                }else{
                    runContinuous();
                }
            }
        }else{
            if(this.reciprocating){
                runReciprocating();
            }else{
                runContinuous();
            }

        }

    }
    private void runReciprocating(){

        if(timer >= frameTime){
            if(!reverseAnim){
                if(animIndex < animLength -1){
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
            timer = 0;

        }else{
            timer ++;
        }

    }

    private void runContinuous(){
        if(timer >= frameTime){
            if(animIndex < animLength -1){
                animIndex ++;
            }else{
                animIndex = 0;
            }

            timer = 0;
        }else{
            timer++;
        }

    }


    public int getAnimIndex(){
        //
        return (anim[animIndex] * 12 * 4);
    }
}
