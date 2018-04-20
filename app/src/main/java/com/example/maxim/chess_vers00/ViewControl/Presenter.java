package com.example.maxim.chess_vers00.ViewControl;

import com.example.maxim.chess_vers00.DataSavingLib.DataManager;
import com.example.maxim.chess_vers00.GameLogic.Cell;
import com.example.maxim.chess_vers00.GameLogic.Figure;
import com.example.maxim.chess_vers00.GameLogic.GameState;

import java.util.Vector;

import io.realm.Realm;

public class Presenter {
    DataManager dataManager;
    private GameState game;
    private Cell Selected;
    private boolean Movable[][];

    // EVIL TRICKS FOR MANIPULATING SELECTED CELL
    Cell getSelected() {
        return Selected;
    }
    boolean isSelected() {
        return Selected.x >= 0 && Selected.y >= 0 && Selected.x < 8 && Selected.y < 8;
    }
    void select(int x, int y) {
        Selected.x = x;
        Selected.y = y;
    }
    void select(Cell pos) {
        select(pos.x, pos.y);
    }
    void deselect() {
        Selected.x = Selected.y = -1;
        resetMovable();
    }

    public Presenter() {
        game = new GameState();
        dataManager = new DataManager(Realm.getDefaultInstance());

        Selected = new Cell(-1, -1);
        Movable = new boolean[8][];
        for(int y = 0; y < 8; ++y) {
            Movable[y] = new boolean[8];
            for (int x = 0; x < 8; ++x)
                Movable[y][x] = false;
        }
    }

    void resetMovable() {
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x)
                Movable[y][x] = false;
    }

    void setMovable(int x, int y) {
        Movable[y][x] = true;
    }
    void setMovable(Cell cell) {
        setMovable(cell.x, cell.y);
    }

    boolean isMovable(int x, int y) {
        return Movable[y][x];
    }
    boolean isMovable(Cell pos) {
        return isMovable(pos.x, pos.y);
    }

    void setMovesForFigure(Cell pos) { // set moveable cell(s) for figure game.Moveable[][] in position Pos
        Figure figure = game.figureAt(pos);
        Vector<Cell> reachable = game.getMovableForFigure(pos, figure);
        for(int i = 0; i < reachable.size(); ++i)
        {
            Cell pos2 = reachable.elementAt(i);
            setMovable(pos2);
        }
    }

    Figure figureAt(int x, int y) {
        return game.figureAt(x, y);
    }
    Figure figureAt(Cell pos) {
        return game.figureAt(pos);
    }

    void selectFigure(Cell NewSelected) {
        this.resetMovable();
        if (game.turnOfSide() == (game.figureAt(NewSelected)).side) {
            setMovesForFigure(NewSelected); // TODO:
            select(NewSelected);
        }
        else {
            deselect();
        }
    }

    public void pressInGame(Cell NewSelected) {
        if (!isSelected()) { // then selecting something new!
            if (game.hasFigure(NewSelected)) {
                selectFigure(NewSelected);
            }
        }
        else {
            if (isMovable(NewSelected)) {
                game.move(Selected, NewSelected);
                game.switchTurn();
                deselect();
                //debugAdd("Making move...");
            }
            else {
                if (Selected.equals(NewSelected)) {
                    //Selected same cell twice. Deselecting.
                    deselect();
                }
                else if (game.hasFigure(NewSelected)) {
                    selectFigure(NewSelected);
                }
                else {
                    //Selected cell has no figures.
                    deselect();
                }
            }
        }
    }

    public void saveGame() {
        dataManager.saveGame(game);
    }

    public boolean tryLoadSavedGame() {
        if (dataManager.tryLoadSaveInto(game) == true)
            return true;
        else
            return false;
    }
}