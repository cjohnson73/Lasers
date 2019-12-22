package com.chancelot.lasers;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class Scene implements GameObject {
    public List<UIElement> uiElementList = new ArrayList<>();
    private float upX, upY;
    public String tag;

    Scene(String _tag){
        upX = -1;
        upY = -1;
        tag = _tag;
    }

    @Override
    public void update(){
        for(UIElement ui : uiElementList){
            ui.onAction(upX, upY);
        }
    }

    @Override
    public void draw(Canvas canvas){
        for(UIElement ui : uiElementList){
            ui.draw(canvas);
        }
    }

    public void update(float _upX, float _upY){
        upX = _upX;
        upY = _upY;
    }

    public void addUIElement(UIElement ui){
        uiElementList.add(ui);
    }

    public void deleteUIElement(UIElement ui){
        uiElementList.remove(ui);
    }
}
