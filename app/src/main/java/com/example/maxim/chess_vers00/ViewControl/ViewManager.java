package com.example.maxim.chess_vers00.ViewControl;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maxim.chess_vers00.GameLogic.Cell;
import com.example.maxim.chess_vers00.GameLogic.Figure;
import com.example.maxim.chess_vers00.GameLogic.GameState;
import com.example.maxim.chess_vers00.MainMenu;
import com.example.maxim.chess_vers00.R;

public class ViewManager {
    Presenter presenter;
    private int ZeroViewId;
    private Resources res;

    TextView DebugShower;

    /*
    ##########################
    ## KIND DEBUG FUNCTIONS ##
    ##########################
    */
    void debugShow(CharSequence message) {
        DebugShower.setText(message);
    }
    void debugAdd(CharSequence message) {
        DebugShower.setText(String.valueOf(DebugShower.getText())+String.valueOf(message));
    }

    public ViewManager(MainMenu menu) {
        DebugShower = menu.findViewById(R.id.textViewDebugger); //##DEBUG

        res = menu.getResources();
        ZeroViewId = R.id.imageViewCell00;
        presenter = new Presenter();

        tryLoadSavedGame();
        redrawBoard(menu);
    }

    // view -- Cell coordinate communication
    int viewIDofCell(int x, int y) {
        return ZeroViewId+x+8*y;
    }
    int viewIDofCell(Cell A) {
        return viewIDofCell(A.x, A.y);
    }
    Cell coordinatesOf(View ImageCell) {
        int CellNum = ImageCell.getId()-ZeroViewId;
        return (new Cell(CellNum%8, CellNum/8));
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
                if (presenter.isMovable(x, y))
                {
                    this_cell.setBackgroundColor(res.getColor(R.color.colorMovableCell));
                }
                CID ^= 1;
            }
        }
        if (presenter.isSelected()) {
            ImageView selected_cell = menu.findViewById(viewIDofCell(presenter.getSelected()));
            selected_cell.setBackgroundColor(res.getColor(R.color.colorSelectedCell));
        }
    }

    void redrawBoard(MainMenu menu) {
        for(int y = 0; y < 8; ++y)
            for(int x = 0; x < 8; ++x)
            {
                ImageView viewedCell = menu.findViewById(viewIDofCell(x, y));
                viewedCell.setImageDrawable(imageOfFigure(presenter.figureAt(x, y)));
            }
        resetColors(menu);
    }

    public void pressInGame(View imageCell, MainMenu menu) {
        Cell NewSelected = coordinatesOf(imageCell);
        presenter.pressInGame(NewSelected);
        redrawBoard(menu);
    }

    private boolean tryLoadSavedGame() {
        boolean succeeded = presenter.tryLoadSavedGame();
        if (succeeded)
            debugAdd("\nLoaded saved game successfully.\n");
        else
            debugAdd("\nWe're playing chess at the first time.\n");
        return succeeded;
    }

    public void saveGame() {
        presenter.saveGame();
    }
}
