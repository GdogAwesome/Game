package com.example.bradmobile.testtexture;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class Game extends AppCompatActivity {

    GLView rd;
    String selection = null;
    boolean fromAd = false;
    public boolean returned = false;
    private boolean backAlreadyPressed = false;
    int mapNo = 1;
    View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selection = getIntent().getExtras().getString("selection");
        rd = new GLView(this);


        setContentView(rd);




    }

    @Override
    protected void onPause(){

        super.onPause();
        rd.pause();
        returned = true;
        selection = "paused";



    }
    public void launchMenu(){


        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("You Have Died");
        alertDialog.setMessage("Do you want to Quit to Menu or Restart the map?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Restart",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  selection = "restart";


                        rd.startNew(getApplicationContext(),mapNo,false);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Quit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.putExtra("selection", "first");
                        startActivity(i);
                    }
                });


        alertDialog.show();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
         rd.pause();

        inflater.inflate(R.menu.cont_menu, menu);


        return true;
    }


    @Override

    protected void onResume(){
        super.onResume();



        removeSystemBars();


        //Log.d("selection",selection);
        if(!fromAd) {
            if (selection.equals("new")) {
                rd.startNew(this, 1, returned);
                //selection = "cont";
            } else if (selection.equals("cont")) {
                mapNo = getIntent().getExtras().getInt("mapNo");
                rd.startNew(this, mapNo, returned);
            } else if (selection.equals("paused")) {

                rd.resume();
            } else if (selection.equals("restart")) {
                rd.startNew(this, mapNo, returned);
            }
        }else{
            fromAd = false;
        }

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        rd.pause();
        selection = "paused";



        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Quit?");
        //alertDialog.setIcon(R.drawable.lock);
        alertDialog.setMessage("Do you really want to quit the game?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Resume",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // selection = "restart";
                        removeSystemBars();
                        rd.resume();

                        //view.startNew(getApplicationContext(),mapNo,false);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Quit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        rd.leavingGame();
                        Game.super.onBackPressed();
                    }
                });


        alertDialog.show();
        backAlreadyPressed = true;




    }
    private void removeSystemBars(){

        int uiOptions;
        if(android.os.Build.VERSION.SDK_INT >= 16) {
             uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

             rd.setSystemUiVisibility(uiOptions);
        }else{
            //TODO this doesn't quite work right but it doesn't mess up the screen touch coordinates

             //uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        }


    }

/*
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }
    */
}
