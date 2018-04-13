package com.example.maxim.chess_vers00.DataSavingLib;

import com.example.maxim.chess_vers00.GameLogic.Figure;
import com.example.maxim.chess_vers00.GameLogic.GameState;

import io.realm.RealmList;
import io.realm.RealmObject;

public class GameDataContainer extends RealmObject {
    RealmList<FigureContainer> boardData;
    int Move;

    public GameDataContainer() {
        boardData = new RealmList<>();
        //GameState game = new GameState();
        //copyDataFromGame(game);
    }

    public GameDataContainer(GameState game) {
        boardData = new RealmList<>();
        copyDataFromGame(game);
    }

    public GameDataContainer(GameDataContainer scd) {
        boardData = new RealmList<>();
        this.setEqual(scd);
    }

    public void copyDataFromGame(GameState game) {
        this.Move = game.turnOfSide();
        if (!boardData.isEmpty()) {
            boardData.deleteAllFromRealm();
        }
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x) {
                Figure copy = game.figureAt(x, y);
                if (!copy.isEmpty()) {
                    FigureContainer savable_copy = new FigureContainer(x, y, copy);
                    boardData.add(savable_copy);
                }
            }
    }

    public void setEqual(GameDataContainer scd) {
        this.boardData.addAll(scd.boardData);
        this.Move = scd.Move;
    }

    public int getTurnOfSide() {
        return this.Move;
    }

    public RealmList<FigureContainer> getBoardData() {
        /*
        RealmList<FigureContainer> copyOfBoardData = new RealmList<>();
        copyOfBoardData.addAll(this.boardData);
        return copyOfBoardData;
        */
        return this.boardData;
    }
}
