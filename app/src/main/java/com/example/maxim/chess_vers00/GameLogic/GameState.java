package com.example.maxim.chess_vers00.GameLogic;

import com.example.maxim.chess_vers00.DataSavingLib.FigureContainer;
import com.example.maxim.chess_vers00.DataSavingLib.GameDataContainer;

import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmList;

public class GameState {
    int Move; // 0 - white are moving; 1 - black are moving;
    Figure GameMatrix[][];

    public GameState setEqual(GameState scd) {
        this.Move = scd.Move;
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x)
                this.GameMatrix[y][x] = scd.GameMatrix[y][x];
        return this;
    }

    public GameState() {
        presetDefaultFields();
        setDefaultGame();
    }

    public void setDefaultGame() {
        for(int y = 0; y < 2; ++y)
            for(int x = 0; x < 8; ++x) {
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

    public boolean inBounds(int x, int y) {
        return (x >= 0) && (y >= 0) && (x < 8) && (y < 8);
    }
    public boolean inBounds(Cell pos) {
        return inBounds(pos.x, pos.y);
    }

    public boolean placeable(Figure figure, int to_x, int to_y) {
        if (inBounds(to_x, to_y))
            return (!figureAt(to_x, to_y).friendly(figure));
        else
            return false;
    }
    boolean placeable(Figure figure, Cell to) {
        return placeable(figure, to.x, to.y);
    }

    public Vector<Cell> getMovableForFigure(Cell Pos, Figure figure) {
        int to_x, to_y;
        Vector <Cell> answer = new Vector<Cell>();
        Vector <Cell> checking = new Vector<Cell>();
        if (figure.type == 2) { // if figure is horse
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

        for (int i = 0; i < checking.size(); ++i) {
            if (placeable(figure, checking.elementAt(i)))
                answer.addElement(checking.elementAt(i));
        }
        return answer;
    }

    public void loadFromSavedGame(GameDataContainer container) {
        clearBoard();
        this.Move = container.getTurnOfSide();
        RealmList<FigureContainer> boardData = container.getBoardData();
        for(int i = 0; i < boardData.size(); ++i) {
            FigureContainer copy = boardData.get(i);
            this.setFigureAt(copy.getFigure(), copy.getX(), copy.getY());
        }
    }

    void presetDefaultFields() {
        Move = 0;
        GameMatrix = new Figure[8][];

        for(int y = 0; y < 8; ++y) {
            GameMatrix[y] = new Figure[8];
            for (int x = 0; x < 8; ++x)
                GameMatrix[y][x] = new Figure();
        }
    }

    public boolean hasFigure(int x, int y) {
        if (inBounds(x, y))
            return ((figureAt(x, y).side != -1) && (figureAt(x, y).type != 0));
        else
            return false;
    }
    public boolean hasFigure(Cell A) {
        return hasFigure(A.x, A.y);
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

    public void setFigureAt(Figure new_figure, int x, int y) {
        GameMatrix[y][x].setEqual(new_figure);
    }
    public void setFigureAt(Figure new_figure, Cell pos) {
        setFigureAt(new_figure, pos.x, pos.y);
    }

    public void removeFigureFrom(int x, int y) {
        GameMatrix[y][x].remove();
    }
    public void removeFigureFrom(Cell pos) {
        removeFigureFrom(pos.x, pos.y);
    }


    public int turnOfSide()
    {
        return Move;
    }

    public void move(int from_x, int from_y, int to_x, int to_y) {
        /*
        TODO: SAFETY CHECK
        this function means, that figure is already present in cell "from",
        and can move to cell "to". Perhaps some checks might be useful.
        */
        GameMatrix[to_y][to_x].setEqual(GameMatrix[from_y][from_x]);
        GameMatrix[from_y][from_x].remove();
    }

    public void move(Cell from, Cell to) {
        move(from.x, from.y, to.x, to.y);
    }
}
