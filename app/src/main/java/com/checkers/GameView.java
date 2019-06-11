package com.checkers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public final class GameView extends View {

    public static final int fieldSize = 130;
    public static final int offset = 20;

    private Board board = new Board();

    private Activity activity;

    private Pawn[][] pawns = new Pawn[8][8];

    Pair<Integer, Integer> selectedPawnCoords;

    private boolean playerWhiteTurn = true;
    private Pawn.PawnColor currentPlayerColor = Pawn.PawnColor.White;
    private Pawn.PawnColor opponentColor = Pawn.PawnColor.Black;

    private int playerWhitePoints = 0;
    private int playerBlackPoints = 0;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        resetGame();
        board.unmarkAll();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawBoard(canvas);
        drawPawns(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) ((event.getX() - offset) / fieldSize);
            int y = (int) ((event.getY() - offset) / fieldSize);

            if (selectedPawnCoords == null) {
                updateSelection(x, y);
                calculatePossibleWays(false);
            } else {
                Pair<Integer, Integer> prev = selectedPawnCoords;
                Pair<Integer, Integer> temp = new Pair<>(x, y);

                if (prev.equals(new Pair<>(x, y))) {
                    pawns[x][y].unmarkSelected();
                    selectedPawnCoords = null;
                    board.unmarkAll();
                } else {
                    updatePawn(x, y);
                }
            }
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void updateSelection(int x, int y) {
        if (coordsCorrect(new Pair<>(x, y))) {
            if (pawns[x][y] != null) {
                if (pawns[x][y].getPawnColor().equals(currentPlayerColor)) {
                    pawns[x][y].markSelected();
                    selectedPawnCoords = new Pair<>(x, y);
                }
            }
        }
    }

    public void updatePawn(int x, int y) {
        Pair<Integer, Integer> temp = new Pair<>(x, y);
        Pair<Integer, Integer> prev = selectedPawnCoords;

        if (coordsCorrect(temp)) {
            if (selectedPawnCoords.equals(temp)) {
                pawns[x][y].unmarkSelected();
                selectedPawnCoords = temp;
                pawns[temp.first][temp.second].markSelected();
                board.unmarkAll();
            } else if (board.isMarked(temp)) {
                movePawn(selectedPawnCoords, new Pair<>(x, y));
                pawns[x][y].unmarkSelected();
                selectedPawnCoords = temp;
                pawns[temp.first][temp.second].markSelected();
                board.unmarkAll();

                for(int i = (temp.first > prev.first ? prev.first + 1 : prev.first -1), j = (temp.second > prev.second ? prev.second + 1 : prev.second - 1) ;
                    (temp.first > prev.first ? (i < temp.first) : i > temp.first) &&
                            (temp.second > prev.second ? (j < temp.second) : j > temp.second);
                )
                {
                    if(pawns[i][j] != null)
                    {
                        if(!pawns[i][j].getPawnColor().equals(currentPlayerColor))
                        {
                            killPawn(new Pair(i,j));
                        }
                    }


                    if(temp.first > prev.first) i++; else i--;
                    if(temp.second > prev.second) j++; else j--;
                }

                if (Math.abs(temp.first - prev.first) > 1 || Math.abs(temp.second - prev.second) > 1) {
                    killPawn(new Pair<>((temp.first + prev.first) / 2, (temp.second + prev.second) / 2));
                }

                if (canHitNextPawn() && ((Math.abs(prev.first - temp.first)) > 1 || (Math.abs(prev.second - temp.second)) > 1)) {
                    calculatePossibleWays(true);
                } else {
                    changePlayer();
                    pawns[selectedPawnCoords.first][selectedPawnCoords.second].unmarkSelected();
                    selectedPawnCoords = null;
                    board.unmarkAll();

                    int blacks = 0, whites = 0;
                    for(int i =0;i<8;i++)
                    {
                        for(int j=0;j<8;j++)
                        {
                            if(pawns[i][j] != null)
                            {
                                if(pawns[i][j].getPawnColor().equals(Pawn.PawnColor.Black))blacks++;
                                else if(pawns[i][j].getPawnColor().equals(Pawn.PawnColor.White)) whites++;
                            }
                        }
                    }


                    if (blacks == 0 || whites == 0) {
                        TextView turn = null;
                        if (activity != null)
                            turn = activity.findViewById(R.id.current_player);
                        if (turn != null && blacks == 0) {
                            turn.setText("White's Wins");
                            Toast.makeText(activity, "White Win", Toast.LENGTH_SHORT).show();
                        } else if (turn != null && whites == 0) {
                            turn.setText("Blacks's Wins");
                            Toast.makeText(activity, "Blacks Win", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

            }
        }
    }

    public void calculatePossibleWays(boolean hitOnly) {
        if (selectedPawnCoords != null) {
            Pair<Integer, Integer> temp;

            if (!hitOnly) {
                temp = new Pair<>(selectedPawnCoords.first - 1, selectedPawnCoords.second - 1);
                if (coordsCorrect(temp) && currentPlayerColor == Pawn.PawnColor.Black) {
                    if (pawns[temp.first][temp.second] == null) {
                        board.markField(temp.first, temp.second);
                    }
                }

                temp = new Pair<>(selectedPawnCoords.first - 1, selectedPawnCoords.second + 1);
                if (coordsCorrect(temp) && currentPlayerColor == Pawn.PawnColor.White) {
                    if (pawns[temp.first][temp.second] == null) {
                        board.markField(temp.first, temp.second);
                    }
                }

                temp = new Pair<>(selectedPawnCoords.first + 1, selectedPawnCoords.second - 1);
                if (coordsCorrect(temp) && currentPlayerColor == Pawn.PawnColor.Black) {
                    if (pawns[temp.first][temp.second] == null) {
                        board.markField(temp.first, temp.second);
                    }
                }

                temp = new Pair<>(selectedPawnCoords.first + 1, selectedPawnCoords.second + 1);
                if (coordsCorrect(temp) && currentPlayerColor == Pawn.PawnColor.White) {
                    if (pawns[temp.first][temp.second] == null) {
                        board.markField(temp.first, temp.second);
                    }
                }

                if (pawns[selectedPawnCoords.first][selectedPawnCoords.second].ivDamke) {
                    for (int i = selectedPawnCoords.first, j = selectedPawnCoords.second; i <= 7 && j <= 7; i++, j++) {
                        if (pawns[i][j] == null) {
                            board.markField(i, j);
                        }
                        else
                        {
                            if(pawns[i][j].getPawnColor().equals(currentPlayerColor) && i != selectedPawnCoords.first && j != selectedPawnCoords.second) break;
                        }
                    }

                    for (int i = selectedPawnCoords.first, j = selectedPawnCoords.second; i >= 0 && j <= 7; i--, j++) {
                        if (pawns[i][j] == null) {
                            board.markField(i, j);
                        }
                        else
                        {
                            if(pawns[i][j].getPawnColor().equals(currentPlayerColor) && i != selectedPawnCoords.first && j != selectedPawnCoords.second) break;
                        }
                    }

                    for (int i = selectedPawnCoords.first, j = selectedPawnCoords.second; i <= 7 && j >= 0; i++, j--) {
                        if (pawns[i][j] == null) {
                            board.markField(i, j);
                        }
                        else
                        {
                            if(pawns[i][j].getPawnColor().equals(currentPlayerColor) && i != selectedPawnCoords.first && j != selectedPawnCoords.second) break;
                        }
                    }

                    for (int i = selectedPawnCoords.first, j = selectedPawnCoords.second; i >= 0 && j >= 0; i--, j--) {
                        if (pawns[i][j] == null) {
                            board.markField(i, j);
                        }
                        else
                        {
                            if(pawns[i][j].getPawnColor().equals(currentPlayerColor) && i != selectedPawnCoords.first && j != selectedPawnCoords.second) break;
                        }
                    }
                }
            }

            Pair<Integer, Integer> temp2;
            temp2 = new Pair<>(selectedPawnCoords.first - 2, selectedPawnCoords.second - 2);
            temp = new Pair<>(selectedPawnCoords.first - 1, selectedPawnCoords.second - 1);
            if (coordsCorrect(temp) && coordsCorrect(temp2)) {
                if (pawns[temp.first][temp.second] != null) {
                    if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                            pawns[temp2.first][temp2.second] == null) {
                        board.markField(temp2.first, temp2.second);
                    }
                }
            }

            temp2 = new Pair<>(selectedPawnCoords.first - 2, selectedPawnCoords.second + 2);
            temp = new Pair<>(selectedPawnCoords.first - 1, selectedPawnCoords.second + 1);
            if (coordsCorrect(temp) && coordsCorrect(temp2)) {
                if (pawns[temp.first][temp.second] != null) {
                    if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                            pawns[temp2.first][temp2.second] == null) {
                        board.markField(temp2.first, temp2.second);
                    }
                }
            }

            temp2 = new Pair<>(selectedPawnCoords.first + 2, selectedPawnCoords.second - 2);
            temp = new Pair<>(selectedPawnCoords.first + 1, selectedPawnCoords.second - 1);
            if (coordsCorrect(temp) && coordsCorrect(temp2)) {
                if (pawns[temp.first][temp.second] != null) {
                    if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                            pawns[temp2.first][temp2.second] == null) {
                        board.markField(temp2.first, temp2.second);
                    }
                }
            }
            temp2 = new Pair<>(selectedPawnCoords.first + 2, selectedPawnCoords.second + 2);
            temp = new Pair<>(selectedPawnCoords.first + 1, selectedPawnCoords.second + 1);
            if (coordsCorrect(temp) && coordsCorrect(temp2)) {
                if (pawns[temp.first][temp.second] != null) {
                    if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                            pawns[temp2.first][temp2.second] == null) {
                        board.markField(temp2.first, temp2.second);
                    }
                }
            }
        }
    }

    public boolean canHitNextPawn() {
        Pair<Integer, Integer> temp;
        Pair<Integer, Integer> temp2;

        temp2 = new Pair<>(selectedPawnCoords.first - 2, selectedPawnCoords.second - 2);
        temp = new Pair<>(selectedPawnCoords.first - 1, selectedPawnCoords.second - 1);
        if (coordsCorrect(temp) && coordsCorrect(temp2)) {
            if (pawns[temp.first][temp.second] != null) {
                if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                        pawns[temp2.first][temp2.second] == null) {
                    return true;
                }
            }
        }

        temp2 = new Pair<>(selectedPawnCoords.first - 2, selectedPawnCoords.second + 2);
        temp = new Pair<>(selectedPawnCoords.first - 1, selectedPawnCoords.second + 1);
        if (coordsCorrect(temp) && coordsCorrect(temp2)) {
            if (pawns[temp.first][temp.second] != null) {
                if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                        pawns[temp2.first][temp2.second] == null) {
                    return true;
                }
            }
        }

        temp2 = new Pair<>(selectedPawnCoords.first + 2, selectedPawnCoords.second - 2);
        temp = new Pair<>(selectedPawnCoords.first + 1, selectedPawnCoords.second - 1);
        if (coordsCorrect(temp) && coordsCorrect(temp2)) {
            if (pawns[temp.first][temp.second] != null) {
                if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                        pawns[temp2.first][temp2.second] == null) {
                    return true;
                }
            }
        }
        temp2 = new Pair<>(selectedPawnCoords.first + 2, selectedPawnCoords.second + 2);
        temp = new Pair<>(selectedPawnCoords.first + 1, selectedPawnCoords.second + 1);
        if (coordsCorrect(temp) && coordsCorrect(temp2)) {
            if (pawns[temp.first][temp.second] != null) {
                if (pawns[temp.first][temp.second].getPawnColor().equals(opponentColor) &&
                        pawns[temp2.first][temp2.second] == null) {
                    return true;
                }
            }
        }

        return false;
    }

    public void killPawn(Pair<Integer, Integer> coords) {
        if (coordsCorrect(coords)) {
            if (playerWhiteTurn) {
                playerWhitePoints++;
            } else {
                playerBlackPoints++;
            }
            pawns[coords.first][coords.second] = null;
        }
    }

    private void drawPawns(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pawns[i][j] != null) {
                    int x = (offset + fieldSize / 2) + fieldSize * pawns[i][j].getPosition().first;
                    int y = (offset + fieldSize / 2) + fieldSize * pawns[i][j].getPosition().second;
                    canvas.drawCircle(x, y, Pawn.radius, pawns[i][j].getPaint());
                }
            }
        }
    }

    public void drawBoard(Canvas canvas) {
        board.draw(canvas, fieldSize, offset);
    }

    public void resetGame() {
        playerWhiteTurn = true;
        currentPlayerColor = Pawn.PawnColor.White;
        opponentColor = Pawn.PawnColor.Black;

        playerBlackPoints = 0;
        playerWhitePoints = 0;

        TextView turn = null;
        if (activity != null)
            turn = activity.findViewById(R.id.current_player);
        if (turn != null)
            turn.setText("White's Turn");

        pawns = new Pawn[8][8];

        for (int i = 0; i < 8; i += 2) {
            pawns[i][0] = new Pawn(new Pair<>(i, 0), Pawn.PawnColor.White);
        }
        for (int i = 0; i < 8; i += 2) {
            pawns[i + 1][1] = new Pawn(new Pair<>(i + 1, 1), Pawn.PawnColor.White);
        }
        for (int i = 0; i < 8; i += 2) {
            pawns[i][2] = new Pawn(new Pair<>(i, 2), Pawn.PawnColor.White);
        }

        for (int i = 0; i < 8; i += 2) {
            pawns[i + 1][5] = new Pawn(new Pair<>(i + 1, 5), Pawn.PawnColor.Black);
        }
        for (int i = 0; i < 8; i += 2) {
            pawns[i][6] = new Pawn(new Pair<>(i, 6), Pawn.PawnColor.Black);
        }
        for (int i = 0; i < 8; i += 2) {
            pawns[i + 1][7] = new Pawn(new Pair<>(i + 1, 7), Pawn.PawnColor.Black);
        }

        board.unmarkAll();
    }

    public void movePawn(Pair<Integer, Integer> currentPos, Pair<Integer, Integer> nextPos) {
        if (currentPos.equals(nextPos)) return;
        if (!coordsCorrect(currentPos) || !coordsCorrect(nextPos)) return;

        pawns[nextPos.first][nextPos.second] = pawns[currentPos.first][currentPos.second];
        pawns[currentPos.first][currentPos.second] = null;
        pawns[nextPos.first][nextPos.second].setPosition(nextPos);

        if (nextPos.second.equals(7) && currentPlayerColor.equals(Pawn.PawnColor.White))
            pawns[nextPos.first][nextPos.second].ivDamke = true;
        if (nextPos.second.equals(0) && currentPlayerColor.equals(Pawn.PawnColor.Black))
            pawns[nextPos.first][nextPos.second].ivDamke = true;
    }

    private boolean coordsCorrect(Pair<Integer, Integer> coords) {
        boolean gridCorrect = false;
        if (coords.first % 2 == 0 && coords.second % 2 == 0) gridCorrect = true;
        if (coords.first % 2 == 1 && coords.second % 2 == 1) gridCorrect = true;
        return coords.first >= 0 && coords.first < 8 && coords.second >= 0 && coords.second < 8 && gridCorrect;
    }

    public void changePlayer() {
        if (playerWhiteTurn) {
            currentPlayerColor = Pawn.PawnColor.Black;
            opponentColor = Pawn.PawnColor.White;
            playerWhiteTurn = false;


            TextView turn = activity.findViewById(R.id.current_player);
            if (turn != null)
                turn.setText("Black's Turn");
            // Toast.makeText(activity, "Player Black Turn", Toast.LENGTH_SHORT).show();
        } else {
            currentPlayerColor = Pawn.PawnColor.White;
            opponentColor = Pawn.PawnColor.Black;
            playerWhiteTurn = true;


            TextView turn = activity.findViewById(R.id.current_player);
            if (turn != null)
                turn.setText("White's Turn");
            //  Toast.makeText(activity, "Player White Turn", Toast.LENGTH_SHORT).show();
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
