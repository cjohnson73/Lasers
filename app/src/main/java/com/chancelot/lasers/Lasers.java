package com.chancelot.lasers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Lasers implements GameObject {
    private float x = -1, y = -1;
    private float sx, sy;
    private int pid;
    private Paint paint = new Paint();

    public Lasers(float _sx, float _sy){
        sx = _sx;
        sy = _sy;
    }
    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        if(x!=-1 && y!=-1)
            canvas.drawLine(sx, sy, canvas.getWidth()-sx, f(canvas.getWidth()), paint);
    }

    public void setPid(int id){
        pid = id;
    }

    public int getPid(){
        return pid;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getSx(){
        return sx;
    }

    public float getSy(){
        return sy;
    }

    public void setXY(float _x, float _y){
        x = _x;
        y = _y;
    }

    public void clear(){
        x = -1;
        y = -1;
        pid = -1;
    }

    private float f(int w){
        return ((y-sy)/(x-sx))*(w-sx-x)+y;
    }
}
