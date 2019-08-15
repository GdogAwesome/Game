package com.example.bradmobile.testtexture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartScreen extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle SavedInstance){

        super.onCreate(SavedInstance);
        setContentView(R.layout.start_layout);

        View newButton = findViewById(R.id.btn_new);
        newButton.setOnClickListener(this);
        View contButton = findViewById(R.id.btn_cont);
        contButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.btn_exit);
        exitButton.setOnClickListener(this);
    }
    public void onClick(View v){

        Intent i;
        switch(v.getId()){
            case R.id.btn_new:
                i =  new Intent(this, Game.class);
                i.putExtra("selection", "new");
                startActivity(i);
                break;
            case R.id.btn_cont:
                i = new Intent(this, LevelSelect.class);
                i.putExtra("selection", "cont");
                startActivity(i);
                break;
            case R.id.btn_exit:
                finish();
                break;

        }

    }
    @Override
    public void onPause(){
        super.onPause();
       onTrimMemory(TRIM_MEMORY_UI_HIDDEN);
    }
}
