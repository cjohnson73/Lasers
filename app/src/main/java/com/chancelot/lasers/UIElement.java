package com.chancelot.lasers;

import android.graphics.Canvas;

public interface UIElement {
    void onAction(float x, float y);
    void draw(Canvas canvas);
}
