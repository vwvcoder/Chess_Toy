package com.example.maxim.chess_vers00;

import io.realm.RealmObject;

public class GameID extends RealmObject {
    private long id;

    public GameID() {
        id = 0;
    }

    public GameID setId(long id) {
        this.id = id;
        return this;
    }

    public long getId() {
        return this.id;
    }
}
