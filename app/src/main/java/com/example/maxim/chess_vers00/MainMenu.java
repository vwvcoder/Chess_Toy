package com.example.maxim.chess_vers00;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// import android.content.Intent;
// import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxim.chess_vers00.DataSavingLib.GameDataContainer;
import com.example.maxim.chess_vers00.GameLogic.Cell;
import com.example.maxim.chess_vers00.GameLogic.Figure;
import com.example.maxim.chess_vers00.GameLogic.GameState;

import java.util.Vector; // Vector

import io.realm.Realm;

public class MainMenu extends AppCompatActivity {

    GameState game;
    Cell NOSELECTED;
    int ZeroID;

    TextView DebugShower;
    Resources res; //##DEBUG

    void debugShow(CharSequence message)
    {
        DebugShower.setText(message);
    }

    void debugAdd(CharSequence message)
    {
        DebugShower.setText(String.valueOf(DebugShower.getText())+String.valueOf(message));
    }

    public Drawable imageOf(Figure figure) {

        Drawable image = res.getDrawable(R.drawable.s_empty); // empty by default;
        if (figure.side == 0) { // if figure is white
            if (figure.type == 1) // is pawn
                image = res.getDrawable(R.drawable.w_pawn);
            else if (figure.type == 2) // is knight
                image = res.getDrawable(R.drawable.w_knight);
            else if (figure.type == 3) // is bishop
                image = res.getDrawable(R.drawable.w_bishop);
            else if (figure.type == 4) // is rook
                image = res.getDrawable(R.drawable.w_rook);
            else if (figure.type == 5) // is queen
                image = res.getDrawable(R.drawable.w_queen);
            else if (figure.type == 6) // is king
                image = res.getDrawable(R.drawable.w_king);
        }
        else { // figure is black
            if (figure.type == 1) // is pawn
                image = res.getDrawable(R.drawable.b_pawn);
            else if (figure.type == 2) // is knight
                image = res.getDrawable(R.drawable.b_knight);
            else if (figure.type == 3) // is bishop
                image = res.getDrawable(R.drawable.b_bishop);
            else if (figure.type == 4) // is rook
                image = res.getDrawable(R.drawable.b_rook);
            else if (figure.type == 5) // is queen
                image = res.getDrawable(R.drawable.b_queen);
            else if (figure.type == 6) // is king
                image = res.getDrawable(R.drawable.b_king);
        }
        return image;
    }

    int viewID(int x, int y) {
        return ZeroID+x+8*y;
    }
    int viewID(Cell A) {
        return viewID(A.x, A.y);
    }

    Cell coordinatesOf(View ImageCell) {
        int CellNum = ImageCell.getId()-ZeroID;
        return (new Cell(CellNum%8, CellNum/8));
    }

    boolean inBounds(int x, int y)
    {
        return (x >= 0 && y >= 0 && x < 8 && y < 8);
    }
    boolean inBounds(Cell A)
    {
        return inBounds(A.x, A.y);
    }

    //checks whether figure can be placed at cell (if it can move anyhow)
    boolean placeable(Figure figure, int to_x, int to_y) {
        if (inBounds(to_x, to_y))
            return (!game.figureAt(to_x, to_y).friendly(figure));
        else
            return false;
    }

    //checks whether figure can be placed at cell (if it can move anyhow)
    boolean placeable(Figure figure, Cell to) {
        return placeable(figure, to.x, to.y);
    }

    Vector <Cell> getMoveable(Cell Pos) {
        Figure figure = game.figureAt(Pos);
        int to_x, to_y;
        Vector <Cell> answer = new Vector<Cell>();
        Vector <Cell> checking = new Vector<Cell>();
        if (figure.type == 2) { // if figure is horse
            debugAdd("Selected figure is horse.\n"); //##DEBUG
            checking.addElement(new Cell(Pos.x-1, Pos.y-2));
            checking.addElement(new Cell(Pos.x+1, Pos.y-2));
            checking.addElement(new Cell(Pos.x+2, Pos.y-1));
            checking.addElement(new Cell(Pos.x+2, Pos.y+1));
            checking.addElement(new Cell(Pos.x+1, Pos.y+2));
            checking.addElement(new Cell(Pos.x-1, Pos.y+2));
            checking.addElement(new Cell(Pos.x-2, Pos.y+1));
            checking.addElement(new Cell(Pos.x-2, Pos.y-1));
        }
        else if (figure.type == 4) { // if figure is rook
            for(int x = Pos.x+1, y = Pos.y; inBounds(x, y); ++x) {
                Cell to_check = new Cell(x, y);
                checking.addElement(to_check);
                if (hasFigure(to_check)) {
                    break;
                }
            }
            for(int x = Pos.x-1, y = Pos.y; inBounds(x, y); --x) {
                Cell to_check = new Cell(x, y);
                checking.addElement(to_check);
                if (hasFigure(to_check)) {
                    break;
                }
            }
            for(int x = Pos.x, y = Pos.y+1; inBounds(x, y); ++y) {
                Cell to_check = new Cell(x, y);
                checking.addElement(to_check);
                if (hasFigure(to_check)) {
                    break;
                }
            }
            for(int x = Pos.x, y = Pos.y-1; inBounds(x, y); --y) {
                Cell to_check = new Cell(x, y);
                checking.addElement(to_check);
                if (hasFigure(to_check)) {
                    break;
                }
            }
        }
        // TODO: OTHER FIGURES
        else //##DEBUG
            debugAdd("Selected figure is not horse.\n"); //##DEBUG

        debugAdd("Possible moves: ");//##DEBUG
        for (int i = 0; i < checking.size(); ++i) {
            Cell D = checking.elementAt(i); //##DEBUG
            if (placeable(figure, checking.elementAt(i))) { //##DEBUG
                answer.addElement(checking.elementAt(i));
                debugAdd(" ("+String.valueOf(D.x)+", "+String.valueOf(D.y)+")"); //##DEBUG
            } //##DEBUG
        }
        debugAdd("\n");
        return answer;
    }

    void setMoveable(Cell pos) // set moveable cell(s) for figure game.Moveable[][] in position Pos
    {
        Vector <Cell> reachable = getMoveable(pos);
        for(int i = 0; i < reachable.size(); ++i)
        {
            Cell pos2 = reachable.elementAt(i);
            game.setMovable(pos2);
        }
    }

    boolean moveable(int x, int y) {
        if (inBounds(x, y))
            return game.isMovable(x, y);
        else {
            DebugShower.setText("In function moveable: trying check inexisting cell."); //##DEBUG
            return false;
        }
    }
    boolean moveable(Cell A)
    {
        return moveable(A.x, A.y);
    }

    /*
    #########################
    ## GRAPHICAL FUNCTIONS ##
    #########################
    */
    public void resetColors() {
        int color[] = new int[2];
        color[0] = res.getColor(R.color.colorWhiteCell);
        color[1] = res.getColor(R.color.colorBlackCell);
        int CID = 1; // CID is ColorID;
        for(int y = 0; y < 8; ++y) {
            CID ^= 1;
            for (int x = 0; x < 8; ++x) {
                ImageView this_cell = findViewById(viewID(x, y));
                this_cell.setBackgroundColor(color[CID]);
                if (game.isMovable(x, y))
                {
                    this_cell.setBackgroundColor(res.getColor(R.color.colorMovableCell));
                }
                CID ^= 1;
            }
        }
        if (inBounds(game.selectedCell())) {
            ImageView selected_cell = findViewById(viewID(game.selectedCell()));
            int ColorOfSelected = res.getColor(R.color.colorSelectedCell);
            selected_cell.setBackgroundColor(ColorOfSelected);
        }
    }

    public void redrawBoard()
    {
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x)
            {
                ImageView viewedCell = findViewById(viewID(x, y));
                viewedCell.setImageDrawable(imageOf(game.figureAt(x, y)));
            }
        resetColors();
    }

    boolean hasFigure(int x, int y) {
        if (inBounds(x, y))
            return ((game.figureAt(x, y).side != -1) && (game.figureAt(x, y).type != 0));
        else
            return false;
    }

    boolean hasFigure(Cell A) {
        return hasFigure(A.x, A.y);
    }

    void selectFigure(Cell NewSelected) {
        game.resetMovable();
        if (game.turnOfSide() == (game.figureAt(NewSelected)).side) {
            setMoveable(NewSelected);
            game.select(NewSelected);

            Figure s = game.figureAt(game.selectedCell());//##DEBUG
            debugAdd("Figure selection succeeded. Figure type/side: "+String.valueOf(s.type)+"/"+String.valueOf(s.side)); //##DEBUG
        }
        else { //##DEBUG
            game.deselect();
            debugAdd("Hey, dude! It's not your move! :P"); //##DEBUG
        } //##DEBUG
    }

    void press(View image_cell) {
        ImageView SelectedView = (ImageView)image_cell;
        Cell NewSelected = coordinatesOf(SelectedView);
        if (game.selectedCell().equals(NOSELECTED)) { // then selecting something new!
            if (hasFigure(NewSelected)) {
                selectFigure(NewSelected);
            }
            else //##DEBUG
                debugAdd("Selected cell has no figures."); //##DEBUG
        }
        else {
            if (moveable(NewSelected)) {
                game.move(game.selectedCell(), NewSelected);
                game.deselect();
                game.switchTurn();
                debugAdd("Making move..."); //##DEBUG
            }
            else {
                if (game.selectedCell().equals(NewSelected)) {
                    debugAdd("Selected same cell twice. Deselecting."); //##DEBUG
                    game.deselect();
                }
                else if (hasFigure(NewSelected)) {
                    selectFigure(NewSelected);
                }
                else { //##DEBUG
                    debugAdd("Selected cell has no figures."); //##DEBUG
                    game.deselect();
                } //##DEBUG
            }
        }
        redrawBoard();
    }

    public void setGame() {
        ZeroID = R.id.imageViewCell00;
        NOSELECTED = new Cell(-1, -1);
        game = new GameState();
    }
    /*
    ######################
    ## SYSTEM FUNCTIONS ##
    ######################
    */

    boolean saved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        res = this.getResources();
        DebugShower = findViewById(R.id.textViewDebugger);
        setGame();

        Realm.init(this);

        Realm realm = Realm.getDefaultInstance();
        // GAME ID DEBUG TEST
        GameID last_game_id = new GameID();
        try {
            last_game_id = realm.where(GameID.class).findFirst();
        } finally {
            if (last_game_id != null) {
                saved = true;
            } else {
                saved = false;
                realm.beginTransaction();
                GameID new_default = realm.createObject(GameID.class);
                new_default.setId(0);
                realm.commitTransaction();
            }

            if (saved) {
                debugAdd("\nSaving works! Last load id is " + String.valueOf(last_game_id.getId()) + "\n");
            }
        }

        GameDataContainer lastGameData = new GameDataContainer();
        try {
            lastGameData = realm.where(GameDataContainer.class).findFirst();
        } finally {
            if (lastGameData != null) { // We have a saved game, loading!
                this.game.loadFromSavedGame(lastGameData);

                debugAdd("\nLoaded saved game successfully.\n");
            }
            else { // We're playing it at the first time... Meh. What a bore.
                debugAdd("\nWe're playing chess at the first time. But why there are only horses?..\n");
            }
        }


        /*
        Realm realm = Realm.getDefaultInstance();
        GameState savedGame = realm.where(GameState.class).equalTo("id", 1000).findFirst();
        if(savedGame != null) {
            game.setEqual(savedGame);
        }
        else {
            setGame();
        }*/
        redrawBoard();
    }


    public void GameOnClick(View image_cell) {
        ImageView selected_cell = (ImageView)image_cell;
        Cell NewSelection = coordinatesOf(selected_cell);
        debugShow("Clicked cell with coordinates: ("+String.valueOf(NewSelection.x)+", "+String.valueOf(NewSelection.y)+")\n");
        press(image_cell);

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

        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction(); // BEGINNING OF TRANSACTION

        // SAVING GAME ID
        GameID last_game_id = realm.where(GameID.class).findFirst();
        last_game_id.setId(last_game_id.getId()+1);
        realm.insertOrUpdate(last_game_id);

        //SAVING GAME DATA
        GameDataContainer lastGameData = realm.where(GameDataContainer.class).findFirst();
        if (lastGameData == null) { // Woohoo! We have a first game save!
            lastGameData = realm.createObject(GameDataContainer.class);
        }
        lastGameData.copyDataFromGame(this.game);

        realm.commitTransaction();  // ENDING OF TRANSACTION
        realm.close();


        /*Realm realm = null;
        try {
            final GameID last_game_id = realm.where(GameID.class).findFirst();
            //GameID last_game_id = realm.where(GameID.class).equalTo("id", 1000).findFirst();
            realm = Realm.getDefaultInstance(); `
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    GameID defaultGame = new GameID();
                    defaultGame.setId(last_game_id.getId()+1);
                    realm.insertOrUpdate(defaultGame);

                }
            });
        } finally {
            if(realm != null) {
                realm.close();
            }
        }*/
    }
}
