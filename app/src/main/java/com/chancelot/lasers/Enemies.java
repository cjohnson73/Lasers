package com.chancelot.lasers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Enemies implements GameObject {

    private float x, y, vel, fx, fy;
    private int health, radius, height, width, fr;
    private boolean alive;
    private Paint paint = new Paint();
    private Player p;
    private Type type;
    public enum Type {NORMAL, HEALTH, CLEARING, POINTS};

    public Enemies(float _x, float _y, int _h, int _r, int _v, Player player, Type _type) {
        x = _x;
        y = _y;
        health = _h;
        radius = _r;
        vel = _v;
        alive = true;
        height = -1;
        width = -1;
        p = player;
        type = _type;
    }

    @Override
    public void update() {
        if(isSpecial()){
            x+=vel;
            if(fx!=-1 && fy!=-1 && (x-fx)*(x-fx)+(y-fy)*(y-fy)<(radius+fr)*(radius+fr)){
                health--;
            }
            if(health<=0){
                alive = false;
                //make cases for other types
                if(p.getHealth()<10){
                    p.setHealth(10);
                }
                else{
                    p.setScore(p.getScore()+25);
                    p.setLastSpecScore(p.getScore());
                }

            }
            if((x>width+radius|| x<0-radius) && width!=-1){
                alive = false;
            }
        }else{
            y+=vel;
            if(fx!=-1 && fy!=-1 && (x-fx)*(x-fx)+(y-fy)*(y-fy)<(radius+fr)*(radius+fr)){
                health--;
            }
            if(health<=0){
                alive = false;
                p.setScore(p.getScore()+1);
            }
            if(y>=height && height!=-1){
                alive = false;
                p.setHealth(p.getHealth()-1);
            }
        }
    }

    public void update(float _fx, float _fy, int _fr) {
        fx = _fx;
        fy = _fy;
        fr = _fr;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(isSpecial()?Color.BLUE:Color.GREEN);
        if(height==-1){
            height = canvas.getHeight();
        }
        if(width==-1){
            width = canvas.getWidth();
        }
        if(alive)
            canvas.drawCircle(x, y, radius, paint);
    }

    public boolean isAlive(){
        return alive;
    }

    public boolean isSpecial(){
        return type!=Type.NORMAL;
    }
}
