package com.chancelot.lasers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Text implements UIElement {
    private String text;
    private Paint paint = new Paint();
    private float x;
    private float y;
    private int size;
    Text(String _text, float _x, float _y, int _size){
        text = _text;
        x = _x;
        y = _y;
        size = _size;
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(size);
    }
    @Override
    public void onAction(float x, float y){

    }
    @Override
    public void draw(Canvas canvas){
        paint.setColor(Color.WHITE);
        canvas.drawText(text, x, y, paint);
    }

    public void setText(String _text){
        text = _text;
    }
}
