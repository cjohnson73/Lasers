package com.chancelot.lasers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Button implements UIElement {

    private float x;
    private float y;
    private float width;
    private float height;
    private int textSize;
    private String text;
    private Paint paint = new Paint();

    Button(float _x, float _y, float _w, float _h, String _text, int _textSize){
        x = _x;
        y = _y;
        width = _w;
        height = _h;
        text = _text;
        textSize = _textSize;
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
    }

    @Override
    public void onAction(float _x, float _y){
    }

    @Override
    public void draw(Canvas canvas){
        paint.setColor(Color.BLACK);
        canvas.drawRect(x, y, x+width, y+height, paint);
        paint.setColor(Color.WHITE);
        canvas.drawText(text, x+width/2, y+5*height/8, paint);
    }

    public boolean isWithin(float _x, float _y){
        return (_x>=x-20 && _y>=y-20 && _x<=x+width+20 && _y<=y+height+20);
    }
}
