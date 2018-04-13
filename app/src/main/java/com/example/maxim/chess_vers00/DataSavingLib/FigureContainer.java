package com.example.maxim.chess_vers00.DataSavingLib;

import com.example.maxim.chess_vers00.GameLogic.Cell;
import com.example.maxim.chess_vers00.GameLogic.Figure;

import io.realm.RealmObject;

public class FigureContainer extends RealmObject {
    int x, y;
    Figure figure;

    public FigureContainer() {
        this.x = this.y = -1;
        this.figure = new Figure(0, -1);
    }

    public FigureContainer(int x, int y, Figure figure) {
        this.figure = new Figure();
        saveFigure(x, y, figure);
    }

    public FigureContainer(Cell pos, Figure figure) {
        this.figure = new Figure();
        saveFigure(pos.x, pos.y, figure);
    }

    public void saveFigure(int x, int y, Figure figure) {
        this.x = x;
        this.y = y;
        this.figure.setEqual(figure);
    }

    public void saveFigure(Cell pos, Figure figure) {
        saveFigure(pos.x, pos.y, figure);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell getPos() {
        return (new Cell(this.x, this.y));
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPos(Cell pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public void setFigure(Figure figure) {
        this.figure.setEqual(figure);
    }

    public Figure getFigure() {
        return this.figure;
    }
}
