package com.example.maxim.chess_vers00.GameLogic;

import com.example.maxim.chess_vers00.MainMenu;

import io.realm.RealmObject;

public class Cell extends RealmObject {
    public int x, y;

    public Cell() {
        this.x = -1;
        this.y = -1;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Cell Scd)
    {
        return ((this.x == Scd.x) && (this.y == Scd.y));
    }

    public boolean isSelected()
    {
        return ((this.x != -1) && (this.y != -1));
    }

    public Cell setEqual(Cell Scd)
    {
        this.x = Scd.x;
        this.y = Scd.y;
        return this;
    }
}
