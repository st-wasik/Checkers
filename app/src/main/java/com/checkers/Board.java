package com.checkers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

public class Board {
    private Boolean fields[][] = new Boolean[8][8]; //is marked
    private Paint whitePaint;
    private Paint blackPaint;
    private Paint blackStroke;

    public Board()
    {
        whitePaint = new Paint();
        blackPaint = new Paint();
        blackStroke = new Paint();

        whitePaint.setColor(Color.WHITE);
        blackPaint.setColor(Color.BLACK);
        blackStroke.setColor(Color.RED);

        whitePaint.setStyle(Paint.Style.FILL);
        blackPaint.setStyle(Paint.Style.FILL);
        blackStroke.setStyle(Paint.Style.FILL);


        unmarkAll();
    }

    public void markField(int x, int y)
    {
        fields[x][y] = true;
    }

    public void unmarkAll()
    {
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                fields[i][j]=Boolean.FALSE;
            }
        }
    }

    public boolean isMarked(Pair<Integer,Integer> pos)
    {
        return fields[pos.first][pos.second] == Boolean.TRUE;
    }

    void draw(Canvas canvas, int fieldSize, int offset)
    {
        canvas.drawColor(Color.YELLOW);
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {

                Paint paint;
                if(fields[i][j]==true)
                {
                    paint = blackStroke;
                }
                else if(i % 2 == 0)
                {
                    if(j % 2 == 0)
                        paint = blackPaint;
                    else
                        paint = whitePaint;
                }
                else
                {
                    if(j % 2 == 0)
                        paint = whitePaint;
                    else
                        paint = blackPaint;
                }

                canvas.drawRect(offset + fieldSize*i, offset + fieldSize*j,offset + fieldSize*(i+1), offset +fieldSize*(j+1), paint);
            }
        }
    }
}
