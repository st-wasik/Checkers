package com.checkers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Pair;

public class Pawn {
    public PawnColor getPawnColor() {
        return pawnColor;
    }

    public enum PawnColor {
        White, Black
    }

    private PawnColor pawnColor;
    private int x;
    private int y;

    private final Integer radius = 50;

    private Paint paint;

    public Pawn(Pair<Integer, Integer> position, PawnColor color) {
        this.x = position.first;
        this.y = position.second;

        this.pawnColor = color;

        this.paint = new Paint();
        paint.setColor(pawnColor == PawnColor.Black ? Color.BLACK : Color.WHITE);
    }

    public void setPosition(Pair<Integer, Integer> position)
    {
        this.x = position.first;
        this.y = position.second;
    }

    public Pair<Integer,Integer> getPosition()
    {
        return new Pair<>(x,y);
    }

    void draw(Canvas canvas)
    {
        canvas.drawCircle(x,y,radius, paint);
    }

}
