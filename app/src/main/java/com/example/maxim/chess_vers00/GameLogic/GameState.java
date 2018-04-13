package com.example.maxim.chess_vers00.GameLogic;

import com.example.maxim.chess_vers00.DataSavingLib.FigureContainer;
import com.example.maxim.chess_vers00.DataSavingLib.GameDataContainer;

import io.realm.Realm;
import io.realm.RealmList;

public class GameState {
    int Move; // 0 - white are moving; 1 - black are moving;
    Figure GameMatrix[][];
    boolean Moveable[][];
    Cell Selected;

    public GameState setEqual(GameState scd) {
        this.Move = scd.Move;
        this.Selected.setEqual(scd.Selected);
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x) {
                this.GameMatrix[y][x] = scd.GameMatrix[y][x];
                this.Moveable[y][x] = scd.Moveable[y][x];
            }
        return this;
    }

    public GameState()
    {
        presetDefaultFields();
        setDefaultGame();
    }

    public void setDefaultGame() {
        for(int y = 0; y < 2; ++y)
            for(int x = 0; x < 8; ++x)
            {
                /*
                GameMatrix[y][x].side = 0;
                GameMatrix[y][x].type = 2;
                */
                GameMatrix[y][x].set(4, 0);
            }
        for(int y = 6; y < 8; ++y)
            for(int x = 0; x < 8; ++x)
            {
                /*
                GameMatrix[y][x].side = 1;
                GameMatrix[y][x].type = 2;*/
                GameMatrix[y][x].set(2, 1);
            }
    }

    public void clearBoard() {
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x) {
                GameMatrix[y][x].set(0, -1);
            }

    }

    /*public GameState(GameDataContainer container) {
        presetDefaultFields();
        this.loadFromSavedGame(container);
    }*/

    public void loadFromSavedGame(GameDataContainer container) {
        clearBoard();
        this.Move = container.getTurnOfSide();
        RealmList<FigureContainer> boardData = container.getBoardData();
        for(int i = 0; i < boardData.size(); ++i) {
            FigureContainer copy = boardData.get(i);
            this.setFigureAt(copy.getFigure(), copy.getX(), copy.getY());
        }

        /*
        while(!boardData().isEmpty()) {
            FigureContainer copy = boardData.last();
            this.setFigureAt(copy.getFigure(), copy.getX(), copy.getY());
            boardData.deleteLastFromRealm();
        }
        */
    }

    void presetDefaultFields() {
        Selected = new Cell();
        Move = 0;
        GameMatrix = new Figure[8][];
        Moveable = new boolean[8][];

        for(int y = 0; y < 8; ++y) {
            Moveable[y] = new boolean[8];
            GameMatrix[y] = new Figure[8];
            for (int x = 0; x < 8; ++x) {
                Moveable[y][x] = false;
                GameMatrix[y][x] = new Figure();
            }
        }
    }

    public Figure figureAt(int x, int y)
    {
        return GameMatrix[y][x];
    }

    public Figure figureAt(Cell pos)
    {
        return figureAt(pos.x, pos.y);
    }

    public void switchTurn()
    {
        Move ^= 1;
    }

    public void setMovable(int x, int y)
    {
        Moveable[y][x] = true;
    }

    public void setMovable(Cell cell)
    {
        setMovable(cell.x, cell.y);
    }

    public void resetMovable()
    {
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x)
                Moveable[y][x] = false;
    }

    public boolean isMovable(int x, int y)
    {
        return Moveable[y][x];
    }

    public boolean isMovable(Cell cell)
    {
        return isMovable(cell.x, cell.y);
    }

    public Cell selectedCell()
    {
        return Selected;
    }

    public void setFigureAt(Figure new_figure, int x, int y)
    {
        GameMatrix[y][x].setEqual(new_figure);
    }

    public void setFigureAt(Figure new_figure, Cell pos)
    {
        setFigureAt(new_figure, pos.x, pos.y);
    }

    public void removeFigureFrom(int x, int y)
    {
        GameMatrix[y][x].remove();
    }

    public void removeFigureFrom(Cell pos)
    {
        removeFigureFrom(pos.x, pos.y);
    }

    public void select(Cell pos)
    {
        Selected.setEqual(pos);
    }

    public int turnOfSide()
    {
        return Move;
    }

    void resetSelected()
    {
        Selected.x = Selected.y = -1;
    }

    public void deselect()
    {
        resetSelected();
        resetMovable();
    }

    public void move(int from_x, int from_y, int to_x, int to_y)
    {
        /*
        TODO: SAFETY CHECK
        this function means, that figure is already present in cell "from",
        and can move to cell "to". Perhaps some checks might be useful.
        */
        GameMatrix[to_y][to_x].setEqual(GameMatrix[from_y][from_x]);
        GameMatrix[from_y][from_x].remove();
        resetMovable();
    }

    public void move(Cell from, Cell to)
    {
        move(from.x, from.y, to.x, to.y);
    }
}
