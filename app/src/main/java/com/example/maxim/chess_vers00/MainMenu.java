package com.example.maxim.chess_vers00;

// import android.content.SharedPreferences;
// import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// import android.content.Intent;
// import android.util.Log;
import android.view.View;
        import android.widget.TextView;

import com.example.maxim.chess_vers00.DataSavingLib.DataManager;
        import com.example.maxim.chess_vers00.GameLogic.GameState;
import com.example.maxim.chess_vers00.ViewControl.Presenter;
import com.example.maxim.chess_vers00.ViewControl.ViewManager;

import io.realm.Realm;

public class MainMenu extends AppCompatActivity {
    ViewManager viewManager;

    /*
    ######################
    ## SYSTEM FUNCTIONS ##
    ######################
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Realm.init(this);
        viewManager = new ViewManager(this);


        /*
        Realm realm = Realm.getDefaultInstance();
        // GAME ID DEBUG TEST
        GameID last_game_id = new GameID();
        try {
            last_game_id = realm.where(GameID.class).findFirst();
        } finally {
            if (last_game_id != null) {
                debugAdd("\nSaving works! Last load id is " + String.valueOf(last_game_id.getId()) + "\n");
            } else {
                realm.beginTransaction();
                GameID new_default = realm.createObject(GameID.class);
                new_default.setId(0);
                realm.commitTransaction();
            }
        }
        */


    }


    public void GameOnClick(View image_cell) {
        viewManager.pressInGame(image_cell, this);

        //Drawable myImage = res.getDrawable(R.drawable.b_pawn);
        //selected_cell.setImageDrawable(myImage);
        /*
        int color = res.getColor(R.color.colorSelectedCell);
        selected_cell.setBackgroundColor(color);
        */
        /*
        TextView DebugShower = findViewById(R.id.textViewDebugger);
        Cell A = coordinatesOf(ImageCell);
        DebugShower.setText("Eating cell with coordinates:"+String.valueOf(A.x)+" "+String.valueOf(A.y)+"\n");
        ImageCell.setVisibility(View.GONE);
        ImageView Cell = (ImageView)ImageCell;
        */
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewManager.saveGame();
        /*
        // SAVING GAME ID
        realm.beginTransaction();
        GameID last_game_id = realm.where(GameID.class).findFirst();
        last_game_id.setId(last_game_id.getId()+1);
        realm.insertOrUpdate(last_game_id);
        realm.commitTransaction();
        */

        //SAVING GAME DATA
    }
}
