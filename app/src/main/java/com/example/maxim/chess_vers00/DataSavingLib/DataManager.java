package com.example.maxim.chess_vers00.DataSavingLib;

import com.example.maxim.chess_vers00.GameLogic.GameState;

import io.realm.Realm;

public class DataManager {
    Realm realm;

    public DataManager(Realm realm) {
        this.realm = realm;
    }

    public boolean tryLoadSaveInto(GameState game) {
        boolean succeeded;
        GameDataContainer lastGameData = new GameDataContainer();
        try {
            lastGameData = realm.where(GameDataContainer.class).findFirst();
        } finally {
            if (lastGameData != null) { // We have a saved game, loading!
                game.loadFromSavedGame(lastGameData);
                succeeded = true;
            } else { // We're playing it at the first time... Meh. What a bore.
                succeeded = false;
            }
        }
        return succeeded;
    }

    public void saveGame(GameState game) {
        realm.beginTransaction();
        GameDataContainer lastGameData = realm.where(GameDataContainer.class).findFirst();
        if (lastGameData == null) { // Woohoo! We have a first game save!
            lastGameData = realm.createObject(GameDataContainer.class);
        }
        lastGameData.copyDataFromGame(game);
        realm.commitTransaction();
    }
}
