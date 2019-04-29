package com.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

public final class GameView extends View {

    public static final int fieldSize = 130;
    public static final int offset = 20;

    private Paint whitePaint;
    private Paint blackPaint;

    private Pawn[][] pawns = new Pawn[8][8];

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        whitePaint = new Paint();
        blackPaint = new Paint();

        whitePaint.setColor(Color.WHITE);
        blackPaint.setColor(Color.BLACK);

        whitePaint.setStyle(Paint.Style.FILL);
        blackPaint.setStyle(Paint.Style.FILL);

        resetGame();
        movePawn(new Pair<Integer, Integer>(0,0), new Pair<Integer, Integer>(2,0));
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

            invalidate();

//            if (win != ' ') {
//                activity.gameEnded(win);
//            } else {
//                // computer plays ...
//                win = gameEngine.computer();
//                invalidate();
//
//                if (win != ' ') {
//                    activity.gameEnded(win);
//                }
//            }
        }

        return super.onTouchEvent(event);
    }

    private void drawPawns(Canvas canvas)
    {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(pawns[i][j] != null)
                {
                    int x = (offset + fieldSize/2)+ fieldSize*pawns[i][j].getPosition().first;
                    int y = (offset + fieldSize/2)+ fieldSize*pawns[i][j].getPosition().second;
                    canvas.drawCircle(x,y,50, pawns[i][j].getPaint() );
                }
            }
        }
    }

    public void drawBoard(Canvas canvas)
    {
        canvas.drawColor(Color.YELLOW);
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(i % 2 == 0)
                {
                    if(j % 2 == 0)
                        canvas.drawRect(offset + fieldSize*i, offset + fieldSize*j,offset + fieldSize*(i+1), offset +fieldSize*(j+1), blackPaint);
                    else
                        canvas.drawRect(offset + fieldSize*i, offset + fieldSize*j,offset + fieldSize*(i+1), offset +fieldSize*(j+1), whitePaint);
                }
                else
                {
                    if(j % 2 == 0)
                        canvas.drawRect(offset + fieldSize*i, offset + fieldSize*j,offset + fieldSize*(i+1), offset +fieldSize*(j+1), whitePaint);
                    else
                        canvas.drawRect(offset + fieldSize*i, offset + fieldSize*j,offset + fieldSize*(i+1), offset +fieldSize*(j+1), blackPaint);
                }
            }
        }
    }

    public void resetGame()
    {
        for(int i=0;i<8;i+=2)
        {
            pawns[i][0] = new Pawn(new Pair<>(i,0), Pawn.PawnColor.White);
        }
        for(int i=0;i<8;i+=2)
        {
            pawns[i+1][1] = new Pawn(new Pair<>(i+1,1), Pawn.PawnColor.White);
        }

        for(int i=0;i<8;i+=2)
        {
            pawns[i][6] = new Pawn(new Pair<>(i,6), Pawn.PawnColor.Black);
        }
        for(int i=0;i<8;i+=2)
        {
            pawns[i+1][7] = new Pawn(new Pair<>(i+1,7), Pawn.PawnColor.Black);
        }
    }

    public void movePawn(Pair<Integer, Integer> currentPos, Pair<Integer,Integer> nextPos)
    {
        pawns[nextPos.first][nextPos.second] = pawns[currentPos.first][currentPos.second];
        pawns[currentPos.first][currentPos.second] = null;
        pawns[nextPos.first][nextPos.second].setPosition(nextPos);
    }

}
