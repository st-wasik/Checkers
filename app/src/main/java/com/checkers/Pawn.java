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

    static private final int white = Color.parseColor("#e3dac9");;
    static private final int black = Color.parseColor("#663d00");;

    public static final Integer radius = 50;

    private Paint paint;

    public Pawn(Pair<Integer, Integer> position, PawnColor color) {
        this.x = position.first;
        this.y = position.second;

        this.pawnColor = color;

        this.paint = new Paint();
        paint.setColor(pawnColor == PawnColor.Black ? black : white);
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

    public Paint getPaint()
    {
        return paint;
    }

    public void markSelected()
    {
        paint.setColor(Color.RED);
    }

    public void unmarkSelected()
    {
        paint.setColor(pawnColor == PawnColor.Black ? black : white);
    }
}
