package com.example.maxim.chess_vers00.GameLogic;

import io.realm.RealmObject;

public class Figure extends RealmObject {
    public int type;
    public int side;
        /*
        TYPES:
            Nothing = 0
            Pawn = 1
            Knight = 2
            Bishop = 3
            Rook = 4
            Queen = 5
            King = 6

        SIDES (for cells):
            -1 = empty cell;
            0 = white occupied!
            1 = black occupied!
        */

    public void remove()
    {
        this.type = 0;
        this.side = -1;
    }

    public Figure()
    {
        remove();
    }

    public Figure(int type, int side)
    {
        this.type = type;
        this.side = side;
    }

    public Figure setEqual(Figure scd) {
        this.type = scd.type;
        this.side = scd.side;
        return this;
    }

    public Figure set(int type, int side) {
        this.type = type;
        this.side = side;
        return this;
    }

    public Figure setType(int type) {
        this.type = type;
        return this;
    }

    public Figure setSide(int side) {
        this.side = side;
        return this;
    }

    public boolean friendly(Figure scd) {
        return (this.side == scd.side);
    }

    public boolean isEmpty() {
        return ((this.side == -1) && (this.type == 0));
    }
}
