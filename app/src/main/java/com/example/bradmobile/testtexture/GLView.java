package com.example.bradmobile.testtexture;

import android.content.Context;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.bradmobile.testtexture.Utils.Audio;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/*
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
*/


/**
 * Created by BradMobile on 11/25/2016.
 */

public class GLView extends GLSurfaceView  {


    boolean playAd = true;
    //Bitmap heroImage = null;

    Thread gameThread;

    public boolean playing = false;


    private boolean shoot = false;
    /**
     * True if game logic needs to be applied this loop, normally as a result of a game event
     */

    private HeroEntity hero;
    private EnemyContainer enemies;
    private Controller controller;
    private int finish = 99;
    private boolean hasGameController = false;
    private boolean fromButtonA = false;
    private float frameVariance = 1.0f;


    public Map1 Map;
    private ShotEntity shots;

    private int arrowWidth = Constants.ARROW_WIDTH;
    private int arrowheight = Constants.ARROW_HEIGHT;
    private int circleDiameter = Constants.CIRCLE_DIAMETER;
    private int controlHeight = Constants.CONTROL_HEIGHT;
    private int middleSpace = Constants.MIDDLE_SPACE;
    private boolean canMoveRight = true;
    private boolean canMoveLeft = true;
    private boolean bossJustDied = true;
    private int playerCommand = 0;
    private int groundLevel = Constants.GROUND_LEVEL;
    private float groundUnderHero = -2.0f;
    private boolean startJumping = false;
    private boolean jumping = false;

    private int shotHeight = 0;
    private int screenWidth = Constants.SCREEN_WIDTH;
    private int screenHeight = Constants.SCREEN_HEIGHT;
    private int CHARACTER_WIDTH;
    private int CHARACTER_HEIGHT ;
    public int BLOCK_WIDTH = Constants.BLOCK_WIDTH;
    public int BLOCK_HEIGHT = Constants.BLOCK_HEIGHT;
    private int halfScreen = screenWidth / 2;

    public String map = null;

    private float heroCenter = 0;
    private TestR renderer;
    private float hRight = 0;
    private float hLeft = 0;
    private float hFooting = 0;
    private float hHead = 0.0f;
    Handler mainHandler;

    public float mapPosX = 0f;
    public float mapPosY = 0f;
    public float mapOffset = 0f;
    private float obOffset;
    private boolean interact = false;

    //set up controller
    private ArrayList gameControllerIds ;
    private int currentControllerHandle = 0;

   // InterstitialAd mInterstitialAd;


    public Context context;



    public boolean[][] hasOList = new boolean[10][4];
    public float[][][] obstacleList = new float[10][4][4];
    public boolean twoPointers = false;



    private boolean running = true;
    private final float MAX_FPS = 59.9f;
    //private final float FRAME_PERIOD = 1000.0f/MAX_FPS;
    private final int MAX_FRAME_SKIPS = 0;

    Audio music;


    public GLView(Context context) {
        super(context);
        this.context = context;
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        setEGLContextClientVersion(2);
        setFocusable(true);
        requestFocus();


        renderer = new TestR(context , this);

        setRenderer(renderer);


       // this.setRenderMode(RENDERMODE_WHEN_DIRTY);
        this.setRenderMode(RENDERMODE_CONTINUOUSLY);
        setConstants(context);



        //Thread gameThread = new Thread(this);
        //gameThread.start();



}
/*
    public void run() {


        long beginTime;
        long timeDiff;
        int sleepTime = 0;
        int framesSkipped;
        int frames = 0;

        while(playing){
            beginTime = System.currentTimeMillis();
            framesSkipped = 0;



            tick();
            if(Map.getMapNeedsRenderUpdate()){
                //Map.updateTextures();
                // sleepTime -= Map.getUpdateTime();
                Map.setMapNeedsRenderUpdate(false);
                //Log.d("time diff","" +sleepTime+"");
            }



            //render();
            frames ++;

            timeDiff = System.currentTimeMillis() - beginTime;
            sleepTime = (int)(FRAME_PERIOD - timeDiff);




            if(sleepTime > 0){

                try{
                    Thread.sleep(sleepTime);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
            }



            while(sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS){

                sleepTime += FRAME_PERIOD;
                //Log.d("sleep time","" +sleepTime+"");
                framesSkipped++;

            }



        }

    }
    */
   /* private void render(){
        requestRender();
    }
    */
    public void tick(float frameVariance) {


        this.frameVariance = frameVariance;
        if(Map.isBossActivated() ){


            Log.e("boss", "activated");
            Map.setBossActive(true);
            hero.expandBounds();
            enemies.initBoss(context, Map.getBossType(), Map.isBossActive(), Map.getAbsoluteMoveX(), Map.getAbsoluteMoveY());
            enemies.setBossActive(Map.isBossActive());
            shots.setBossActive(Map.isBossActive());
            shots.addBoss(enemies.getBoss());
            Map.setBossActivated(false);


        }

        if(!Map.end() || Map.isBossActive()) {


            calcLogic();
            Map.updateAnim();
            //playerCommand = 1;
            hero.move(playerCommand, Map.canMoveLeft, Map.canMoveRight);
            if(!enemies.isBossDying()) {
                enemies.updateEnemies(hero.getCenter(), hero.getFooting() + (hero.getObjectBounds(1)[1] * 3f), Map.getAbsoluteMoveX(), Map.getAbsoluteMoveY(), frameVariance);
            }
            if(enemies.isBossDead()){
                shots.setBossActive(false);
                Map.setHasBoss(false);
                Map.setBossActive(false);
                Map.setBossActivated(false);

                //Map.setBossActivated(false);
                Map.setEndMap(true);



            }else if(enemies.isBossDying()){
                if(bossJustDied) {
                    hero.displayMessage(3, 8, false);
                    bossJustDied = false;
                }
                //hero.setPaused(true);
                enemies.finalizeEnemies(Map.getAbsoluteMoveX(), Map.getAbsoluteMoveY());

            }

        }else if(Map.endLevel){
            Log.e("next", "level");
            nextLevel();
        }


    }

    public void setConstants(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        /*
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        int v = Build.VERSION.SDK_INT;
        //TODO Decide lowest api
        if(v >= 13) {
            display.getSize(size);
            Constants.setScreen(Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
            //Constants.setScreen(1366, 768);
            //Constants.setScreen(480, 320);
        }else {
            Constants.setScreen(480, 320);
        }
        */

                final DisplayMetrics metrics = new DisplayMetrics();
        Display display = wm.getDefaultDisplay();
        Method mGetRawH = null,mGetRawW = null;

        try {

            // For JellyBeans and onward
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

                display.getRealMetrics(metrics);
                Constants.setScreen(metrics.widthPixels, metrics.heightPixels);
            } else {
                // Below Jellybeans you can use reflection method

                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                try {
                    Constants.setScreen((Integer) mGetRawW.invoke(display),(Integer) mGetRawH.invoke(display));

                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }catch(Exception e){

        }



        arrowWidth = Constants.ARROW_WIDTH;
        arrowheight = Constants.ARROW_HEIGHT;
        circleDiameter = Constants.CIRCLE_DIAMETER;
        controlHeight = Constants.CONTROL_HEIGHT;
        middleSpace = Constants.MIDDLE_SPACE;
        //RUN_SPEED = Constants.RUN_SPEED;
        screenWidth = Constants.SCREEN_WIDTH;
        screenHeight = Constants.SCREEN_HEIGHT;

        BLOCK_WIDTH = Constants.BLOCK_WIDTH;
        BLOCK_HEIGHT = Constants.BLOCK_HEIGHT;
        halfScreen = screenWidth / 2;



    }
    private void initEntities(Context context, int mapNo) {

        music = new Audio();
        hero = new Hero(context, mapNo);
        heroCenter = (CHARACTER_WIDTH / 2) + (int)hero.x;
        hero.setGround(groundLevel);
        hero.reset();

        //mapPosX = 0;

        Map = new Map1( mapPosX, 0);
        Map.initMap(context, mapNo);
        Map.addHero(hero);
        Map.resetPosX();
        finish = (int)((Map.getMapLength() * Constants.BLOCK_WIDTH) - (.5 * Constants.SCREEN_WIDTH));

        shots = new ShotEntity(context);
        enemies = new EnemyContainer(context, hero, Map.getBossType(), mapNo, Map.mapType);
        enemies.addShots(shots);


        enemies.updateEnemies(hero.x,hero.y, Map.getAbsoluteMoveX(),Map.getAbsoluteMoveY(), frameVariance);
        shots.addEntities(hero);
        shots.updateObstacles(obstacleList, hasOList);

        controller = new Controller();
        controller.initController(context);
        renderer.setHasGameController(hasGameController);

        gameControllerIds = getGameControllerIds();
        if(gameControllerIds.size() != 0) {
            currentControllerHandle = (int) gameControllerIds.get(0);
        }


        // if gl renderer is active I need to queue this event
        if(renderer.surfaceActive()){

            queueEvent(new Runnable() {
                @Override public void run() {
                    renderer.setHasGameController(hasGameController);

                    renderer.setShots(shots);
                    renderer.setEnemies(enemies);
                    renderer.setHero(hero);
                    renderer.setController(controller);
                    renderer.setMap(Map);
                    setupMapRender();
                    //renderer.sceneChange();
                    Log.e("setup Objects","now");
                    renderer.setupObjects();
                }});

        }else{
            renderer.setHasGameController(hasGameController);
            renderer.setShots(shots);
            renderer.setEnemies(enemies);
            renderer.setHero(hero);
            renderer.setController(controller);
            renderer.setMap(Map);
            setupMapRender();
        }
    }

    public void calcLogic() {
        //get controller input
        playerCommand = controller.getPlayerCommand();

        shoot = controller.getShoot();
        boolean obFound = false;
        startJumping = controller.getJump();
        hero.setFiring(controller.getShoot());
        shotHeight = controller.getFireState();

        if (shoot) {
            hero.tryToShoot();
        }
        hLeft = hero.getLeft();//8
        hRight = hero.getRight();//10
        heroCenter = hero.getCenter();
        hFooting = hero.getFooting();
        hHead = hero.getHead();
        mapPosX = Map.getAbsoluteMoveX();
        mapPosY = Map.getAbsoluteMoveY();
        obOffset = Map.getObstacleOffset();
        mapOffset = Map.getMapOffset();
        checkHState();
         Map.moveMap(hero.getLeft(), hero.getRight(), hero.getHeroY(),canMoveLeft, canMoveRight, playerCommand, frameVariance);

        shots.updateShots(mapOffset,obOffset, mapPosX, mapPosY, frameVariance);

        if (hero.canShoot) {

            hero.setShotHeight(shotHeight);
            shots.ShotFired(hero.fireStats(),-1, true);
            hero.canShoot = false;
        }
            /*
            Get map Obstacles

             */
            hasOList[0] = Map.getFarLeftThasO();
            hasOList[5] = Map.getFarLeftBhasO();
        hasOList[1] = Map.getLeftThasO();
        hasOList[6] = Map.getLeftBhasO();
        hasOList[2] = Map.getCenterThasO();
        hasOList[7] = Map.getCenterBhasO();
        hasOList[3] = Map.getRightThasO();
        hasOList[8] = Map.getRightBhasO();
        hasOList[4] = Map.getFarRightThasO();
        hasOList[9] = Map.getFarRightBhasO();

        obstacleList[0] = Map.getFarLeftTopObstacle();
        obstacleList[5] = Map.getFarLeftBottomObstacle();
        obstacleList[1] = Map.getLeftTopObstacle();
        obstacleList[6] = Map.getLeftBottomObstacle();
        //obstacleList[2] = Map.getCenterSkyObstacle();
        obstacleList[2] = Map.getCenterTopObstacle();
        obstacleList[7] = Map.getCenterBottomObstacle();
        //obstacleList[5] = Map.getRightSkyObstacle();
        obstacleList[3] = Map.getRightTopObstacle();
        obstacleList[8] = Map.getRightBottomObstacle();

        obstacleList[4] = Map.getFarRightTopObstacle();
        obstacleList[9] = Map.getFarRightBottomObstacle();


		/*
		 *
		 * scan through obstacles
		 *
		 *
		 */
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                if(hasOList[i][j]) {
                    if (heroCenter >= (obstacleList[i][j][0] + mapOffset) && heroCenter <= (obstacleList[i][j][2] + mapOffset)) {
                            if (((obstacleList[i][j][1] + obOffset ) >= groundUnderHero) && ((obstacleList[i][j][1] + obOffset) <= (hFooting + .03f))  ) {
                                groundUnderHero = obstacleList[i][j][1] + obOffset;


                                if (!((hero.falling || hero.contJump) || hero.contJump)) {
                                    canMoveRight = !(hRight >= (obstacleList[i][j][0] + mapOffset) && (hRight > (obstacleList[i][j][0] + mapOffset)) && (hFooting + .02f) < (obstacleList[i][j][1] + obOffset) && hRight < (obstacleList[i][j][2] + mapOffset));// && hHead >(obstacleList[i][j][3] + obOffset) );

                                    canMoveLeft = !(hLeft <= (obstacleList[i][j][2] + mapOffset) && (hLeft < (obstacleList[i][j][2] + mapOffset)) && (hFooting + .02f) < (obstacleList[i][j][1] + obOffset) && hLeft > (obstacleList[i][j][0] + mapOffset));// && hHead >(obstacleList[i][j][3] + obOffset) );
                                }

                                if((((obstacleList[i][j][1] + obOffset )  + groundUnderHero) < .02f) || (((obstacleList[i][j][1] + obOffset )  + groundUnderHero) > -.02f)) {
                                 hero.setGround(groundUnderHero);
                                 obFound = true;
                             }else{

                             }

                            }

                    }
                }
                if(obFound){
                    i = 10;
                    j = 4;
                    groundUnderHero = -20f;
                    obFound = false;
                }else{
                    if((i >= 9) && (j >= 3)){
                        groundUnderHero = -20f;

                    }

                }


            }
        }
        hero.calcFooting();
        hero.update(0f, Map.getDeltaY());
        hero.updateMessage(mapPosX);
        if (hero.isRunning()) {
            hero.move(mapPosX, canMoveRight,canMoveLeft, frameVariance);
            if(hero.dying) {
                canMoveLeft = !hero.dying;
                canMoveRight = !hero.dying;
            }
        }
        if (startJumping) {
            if(!hero.dying) {
                jumping = true;
            }
        }
        if (jumping) {
            hero.jump();
        }
        jumping = hero.contJump();
    }


    public void checkHState(){
        if(hero.getDead()  ){
            if(hero.getLives() <= 0){
                /**
                 *
                 * code for death
                 */
                /*
                if(playAd) {
                    //requestNewInterstitial();
                    playing = false;
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }

                        }
                    });




                    playAd = false;
                }
                */

            }else{

                hero.reset();
                shots.resetHealth();



            }


        }
    }

    /*public void setAd(InterstitialAd ad){
        this.mInterstitialAd = ad;
    }*/
    public void newMap(Context context,int mapNo){
        //mp.setLooping(true);

        currentStats.currentMap = mapNo;
        initEntities(context,mapNo);
        switch(mapNo){
            case 1:
                music.loadTrack(context, R.raw.new_beginning);
                break;
            case 2:
                music.loadTrack(context, R.raw.new_beginning);
                break;
            case 3:
                music.loadTrack(context, R.raw.neo);
                break;

            default:
                music.loadTrack(context, R.raw.new_beginning);
        }

        music.startMusic();

    }

    public void leavingGame(){
        sceneChange();
        if(Map.CutScene()){
            Map.endScene();
        }
    }
    private void setupMapRender(){
                renderer.setMapTextureCoords(Map.getDrawCoords());
                renderer.setMapTexture(Map.getTexture());
                renderer.setMapVertexCoord(Map.getPositionFloatBuffer());
                renderer.setmMapModelMatrix(Map.getmModelMatrix());
                renderer.setMapDrawIndices(Map.getDrawListBuffer());
                renderer.setMapAnimBuffer(Map.getAnimBuffer());
    }



    public void sceneChange(){

        playing = false;
        music.releaseMP();
        Log.e("scene", "change");

        queueEvent(new Runnable() {
            @Override public void run() {
                renderer.setRender(false);

                renderer.sceneChange();

            }});
    }


    public void pause() {
        playing = false;
        //music.pauseMusic();
        queueEvent(new Runnable() {
            @Override public void run() {
                renderer.setPaused(true);
            }});
    }




    public void resume() {

        music.startMusic();
        queueEvent(new Runnable() {
            @Override public void run() {
                renderer.setPaused(false);
                renderer.setRender(true);

            }});
        playing = true;
    }
    public void startNew(Context context, int mapNo, boolean returned){

        playAd = true;

        if(returned) {
            sceneChange();

        }
        newMap(context,mapNo);
        music.startMusic();

        queueEvent(new Runnable() {
            @Override public void run() {
                renderer.setRender(true);
            }});
    }
    public void nextLevel(){
        sceneChange();
        playAd = true;
        playing = false;
        newMap(context,currentStats.currentMap + 1);
        queueEvent(new Runnable() {
            @Override public void run() {
                renderer.setRender(true);

            }});

        playing = true;
    }

    /**
     * get available controllers
     * @return
     */
    public ArrayList getGameControllerIds() {
        ArrayList gameControllerDeviceIds = new ArrayList();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();
            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    hasGameController = true;
                    Log.e("has game controller", "true");
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    }
    @Override
    public boolean onTouchEvent(MotionEvent me) {
        boolean handled = false;
        if(!hasGameController) {

            int action = MotionEventCompat.getActionMasked(me);
            int pointerId;


            int index;

            //switch (action) {

                if(action == MotionEvent.ACTION_DOWN){
                    interact = true;
                    //playerCommand = 1;
                    //leftPlayerMove(MotionEventCompat.getX(me, index), MotionEventCompat.getY(me, index));
                    //leftPlayerMove(me.getX(), me.getY());
                    pointerId = me.getPointerId(0);

                    index = me.findPointerIndex(pointerId);
                    //x =  (int)me.getX(index);
                    //y = (int)me.getY(index);

                    handled = controller.retControlFromTouch(me.getX(index), me.getY(index), false);
                    if (me.getPointerCount() > 1) {
                        twoPointers = true;
                        pointerId = me.getPointerId(1);
                        index = me.findPointerIndex(pointerId);
                        //x = (int) me.getX(index);
                        //y = (int) me.getY(index);
                        handled = controller.retControlFromTouch(me.getX(index), me.getY(index), false);
                    }
                    }

                if(action ==  MotionEvent.ACTION_UP){
                    //interact = false;
                    playerCommand = 0;
                    // buttonReleaseLeft(MotionEventCompat.getX(me, index), MotionEventCompat.getY(me, index));
                    pointerId = me.getPointerId(0);
                    index = me.findPointerIndex(pointerId);
                    // x =  (int)me.getX(index);
                    // y = (int)me.getY(index);
                    handled = controller.retControlFromTouch(me.getX(index), me.getY(index), true);
                    if (me.getPointerCount() > 1) {
                        twoPointers = true;
                        pointerId = me.getPointerId(1);
                        index = me.findPointerIndex(pointerId);
                        //x = (int) me.getX(index);
                        //y = (int) me.getY(index);
                        handled = controller.retControlFromTouch(me.getX(index), me.getY(index), true);
                    }


                 }
                if(action == MotionEvent.ACTION_MOVE){

                    //leftPlayerMove(MotionEventCompat.getX(me, index), MotionEventCompat.getY(me, index));
                    //leftPlayerMove(me.getX(),me.getY());
                    pointerId = me.getPointerId(0);
                    index = me.findPointerIndex(pointerId);
                    // x =  (int)me.getX(index);
                    // y = (int)me.getY(index);
                    handled = controller.retControlFromTouch(me.getX(index), me.getY(index), false);
                    if (me.getPointerCount() > 1) {
                        twoPointers = true;
                        pointerId = me.getPointerId(1);
                        index = me.findPointerIndex(pointerId);
                        //x = (int) me.getX(index);
                        //y = (int) me.getY(index);
                        handled = controller.retControlFromTouch(me.getX(index), me.getY(index), false);

                    }

                    }

                if(action ==  MotionEvent.ACTION_POINTER_DOWN){
                    interact = true;
                    //playerCommand = 1;
                    //leftPlayerMove(MotionEventCompat.getX(me, index), MotionEventCompat.getY(me, index));
                    //leftPlayerMove(me.getX(), me.getY());
                    pointerId = me.getPointerId(0);
                    index = me.findPointerIndex(pointerId);
                    // x =  (int)me.getX(index);
                    // y = (int)me.getY(index);

                    handled = controller.retControlFromTouch(me.getX(index), me.getY(index), false);
                    if (me.getPointerCount() > 1) {
                        twoPointers = true;
                        pointerId = me.getPointerId(1);
                        index = me.findPointerIndex(pointerId);
                        //x = (int) me.getX(index);
                        //y = (int) me.getY(index);
                        handled = controller.retControlFromTouch(me.getX(index), me.getY(index), false);
                    }
                }
                if(action == MotionEvent.ACTION_POINTER_UP){
                    // interact = false;
                    playerCommand = 0;
                    //buttonReleaseLeft(MotionEventCompat.getX(me, index), MotionEventCompat.getY(me, index));
                    //buttonReleaseLeft(me.getX(), me.getY());
                    //buttonReleaseRight(0, 0);
                    pointerId = me.getPointerId(0);
                    index = me.findPointerIndex(pointerId);
                    // x =  (int)me.getX(index);
                    // y = (int)me.getY(index);

                    handled = controller.retControlFromTouch(me.getX(index), me.getY(index), true);

                    if (me.getPointerCount() > 1) {
                        twoPointers = true;
                        pointerId = me.getPointerId(1);
                        index = me.findPointerIndex(pointerId);
                        //x = (int) me.getX(index);
                        //y = (int) me.getY(index);
                        handled = controller.retControlFromTouch(me.getX(index), me.getY(index), true);
                    }

                }


            /*
            playerCommand = controller.getPlayerCommand();

            shoot = controller.getShoot();
            startJumping = controller.getJump();
            hero.setFiring(controller.getShoot());
            shotHeight = controller.getFireState();
            */
        }


        return handled;

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){


        boolean handled = false;
        handled = controller.handleControllerInput(keyCode, event, true);

        return handled || super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){

        boolean handled = false;
        handled = controller.handleControllerInput(keyCode, event, false);

        return handled || super.onKeyDown(keyCode, event);

    }

    /*
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }*/

}
