package com.chancelot.lasers;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MThread extends Thread{
    private static final int MAX_FPS = 120;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;

    MThread(SurfaceHolder sh, GamePanel gp){
        super();
        surfaceHolder = sh;
        gamePanel = gp;
        gamePanel.setFPS(MAX_FPS);
    }

    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long targetTime = 1000/MAX_FPS;

        while(running){
            startTime = System.currentTimeMillis();
            Canvas canvas = null;
            try{
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    gamePanel.update();
                    gamePanel.draw(canvas);
                }
            }catch(Exception e){
                //
            }finally{
                if(canvas !=null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch(Exception e){
                        //
                    }
                }
            }

            timeMillis = System.currentTimeMillis()-startTime;
            waitTime = targetTime - timeMillis;
            try{
                if(waitTime>0)
                    sleep(waitTime);
            }catch(Exception e){
                //
            }
        }
    }

    void setRunning(Boolean r){
        running = r;
    }
}
