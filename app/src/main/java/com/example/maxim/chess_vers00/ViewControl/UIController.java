package com.example.maxim.chess_vers00.ViewControl;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.example.maxim.chess_vers00.GameLogic.Cell;
import com.example.maxim.chess_vers00.GameLogic.Figure;
import com.example.maxim.chess_vers00.GameLogic.GameState;
import com.example.maxim.chess_vers00.MainMenu;
import com.example.maxim.chess_vers00.R;

import java.util.Vector;

public class UIController {
    private Cell Selected;
    private boolean Movable[][];
    private int ZeroViewId;
    private Resources res;

    // EVIL TRICKS FOR MANIPULATING SELECTED CELL
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

    public UIController(MainMenu menu, GameState game) {
        ZeroViewId = R.id.imageViewCell00;
        Selected = new Cell(-1, -1);
        Movable = new boolean[8][];
        res = menu.getResources();
        for(int y = 0; y < 8; ++y) {
            Movable[y] = new boolean[8];
            for (int x = 0; x < 8; ++x)
                Movable[y][x] = false;
        }
        redrawBoard(menu, game);
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

    void setMovesForFigure(Cell pos, GameState game) // set moveable cell(s) for figure game.Moveable[][] in position Pos
    {
        Figure figure = game.figureAt(pos);
        Vector<Cell> reachable = game.getMovableForFigure(pos, figure);
        for(int i = 0; i < reachable.size(); ++i)
        {
            Cell pos2 = reachable.elementAt(i);
            setMovable(pos2);
        }
    }

    boolean movable(int x, int y) {
        return Movable[y][x];
    }
    boolean movable(Cell pos) {
        return movable(pos.x, pos.y);
    }

    // ID EVIL MANIPULATING TRICKS
    int viewIDofCell(int x, int y) {
        return ZeroViewId+x+8*y;
    }
    int viewIDofCell(Cell A) {
        return viewIDofCell(A.x, A.y);
    }

    // FUNCTION FOR GETTING IMAGE OF FIGURE
    Drawable imageOfFigure(Figure figure) {

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

    Cell coordinatesOf(View ImageCell) {
        int CellNum = ImageCell.getId()-ZeroViewId;
        return (new Cell(CellNum%8, CellNum/8));
    }

    void resetColors(MainMenu menu) {
        int color[] = new int[2];
        color[0] = res.getColor(R.color.colorWhiteCell);
        color[1] = res.getColor(R.color.colorBlackCell);
        int CID = 1; // CID is ColorID;
        for(int y = 0; y < 8; ++y) {
            CID ^= 1;
            for (int x = 0; x < 8; ++x) {
                ImageView this_cell = menu.findViewById(viewIDofCell(x, y));
                this_cell.setBackgroundColor(color[CID]);
                if (movable(x, y))
                {
                    this_cell.setBackgroundColor(res.getColor(R.color.colorMovableCell));
                }
                CID ^= 1;
            }
        }
        if (isSelected()) {
            ImageView selected_cell = menu.findViewById(viewIDofCell(Selected));
            int ColorOfSelected = res.getColor(R.color.colorSelectedCell);
            selected_cell.setBackgroundColor(ColorOfSelected);
        }
    }

    void redrawBoard(MainMenu menu, GameState game) {
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x)
            {
                ImageView viewedCell = menu.findViewById(viewIDofCell(x, y));
                viewedCell.setImageDrawable(imageOfFigure(game.figureAt(x, y)));
            }
        resetColors(menu);
    }

    void selectFigure(Cell NewSelected, GameState game) {
        this.resetMovable();
        if (game.turnOfSide() == (game.figureAt(NewSelected)).side) {
            setMovesForFigure(NewSelected, game); // TODO:
            select(NewSelected);
        }
        else {
            deselect();
        }
    }

    public void pressInGame(View image_cell, GameState game, MainMenu menu) {
        ImageView SelectedView = (ImageView)image_cell;
        Cell NewSelected = coordinatesOf(SelectedView);
        if (!isSelected()) { // then selecting something new!
            if (game.hasFigure(NewSelected)) {
                selectFigure(NewSelected, game);
            }
        }
        else {
            if (movable(NewSelected)) {
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
                    selectFigure(NewSelected, game);
                }
                else {
                    //Selected cell has no figures.
                    deselect();
                }
            }
        }
        redrawBoard(menu, game);
    }
}