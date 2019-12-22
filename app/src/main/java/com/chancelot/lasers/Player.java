package com.chancelot.lasers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player implements GameObject {
    private int score;
    private int health;
    private int lastSpecScore;
    private Paint paint = new Paint();
    private boolean alive;
    public Player(){
        score = 0;
        health = 10;
        lastSpecScore = 0;
        alive = true;
        paint.setTextSize(60);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public boolean isAlive(){
        return alive;
    }

    public int getLastSpecScore(){
        return lastSpecScore;
    }

    public void setLastSpecScore(int l){
        lastSpecScore = l;
    }

    public int getScore(){
        return score;
    }

    public int getHealth(){
        return health;
    }

    public void setScore(int s){
        score = s;
    }

    public void setHealth(int h){
        health = h;
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawText(score+"", canvas.getWidth()/2, canvas.getHeight()/4, paint);
        paint.setColor(Color.rgb(255*(10-health)/10, 255*health/10, 0));
        canvas.drawRect(0, canvas.getHeight()-25, canvas.getWidth()*health/10, canvas.getHeight(), paint);
    }

    @Override
    public void update(){
        if(health<=0)
            alive = false;
    }
}
