package com.chancelot.lasers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MThread thread;
    private Lasers leftLaser;
    private Lasers rightLaser;
    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private ArrayList<Enemies> ens = new ArrayList<>();
    private int radius, fps, eps;
    private float interx, intery, upX, upY;
    private Paint paint = new Paint();
    private Player player;
    private Scene state;
    private Scene play = new Scene("play");//has ui
    private Scene splash = new Scene("splash");//don't need ui
    private Scene title = new Scene("title");//has ui
    private Scene end = new Scene("end");//has ui
    private Scene settings = new Scene("settings");
    private Scene pause = new Scene("pause");
    private Text scoreText;

    public GamePanel(Context context){
        super(context);

        getHolder().addCallback(this);

        thread = new MThread(getHolder(), this);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder){
        thread = new MThread(getHolder(), this);
        state = title;
        scoreText = new Text("0", getWidth()/2, getHeight()/2-300, 100);
        Button playButton = new Button(getWidth()/2-300, getHeight()/2-100, 600, 200, "PLAY", 100){
            @Override
            public void onAction(float x, float y){
                if(isWithin(x, y))
                    state = play;
            }
        };

        Button settingsButton = new Button(getWidth()/2-300, getHeight()/2+300, 600, 200, "SETTINGS", 100){
            @Override
            public void onAction(float x, float y){
                if(isWithin(x, y))
                    state = settings;
            }
        };

        Button pauseButton = new Button(getWidth()-100, 50, 50, 50, "| |", 50){
            @Override
            public void onAction(float x, float y){
                if(isWithin(x, y))
                    state = pause;
            }
        };


        title.addUIElement(playButton);
        title.addUIElement(settingsButton);
        title.addUIElement(new Text("EARTH & LASERS", getWidth()/2, getHeight()/2-600, 100));

        end.addUIElement(playButton);
        end.addUIElement(settingsButton);
        end.addUIElement(new Text("EARTH & LASERS", getWidth()/2, getHeight()/2-600, 100));
        end.addUIElement(scoreText);

        settings.addUIElement(playButton);

        pause.addUIElement(playButton);
        pause.addUIElement(settingsButton);

        play.addUIElement(pauseButton);



        initialize();
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder){
        boolean retry = true;
        while(retry){
            try{
                thread.setRunning(false);
                thread.join();
            }
            catch(Exception e){
                //
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        super.onTouchEvent(e);
        if(state.tag==play.tag){
            switch(e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    if(e.getX(e.getActionIndex())<getWidth()/2)
                        leftLaser.setPid(e.getPointerId(e.getActionIndex()));
                    else
                        rightLaser.setPid(e.getPointerId(e.getActionIndex()));
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    if(rightLaser.getPid()==-1)
                        rightLaser.setPid(e.getPointerId(e.getActionIndex()));
                    else if(leftLaser.getPid()==-1)
                        leftLaser.setPid(e.getPointerId((e.getActionIndex())));
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    if(e.getPointerId(e.getActionIndex())==leftLaser.getPid())
                        leftLaser.clear();
                    else if(e.getPointerId(e.getActionIndex())==rightLaser.getPid())
                        rightLaser.clear();
                    break;
                case MotionEvent.ACTION_UP:
                    leftLaser.clear();
                    rightLaser.clear();
                    upX = e.getX();
                    upY = e.getY();
                    state.update(upX, upY);
                    upX = -1;
                    upY = -1;
                    break;
                default:
                    break;
            }

            for(int i = 0; i<e.getPointerCount(); i++){//good
                if(e.getPointerId(i)==leftLaser.getPid())
                    leftLaser.setXY(e.getX(i), e.getY(i));
                else if(e.getPointerId(i)==rightLaser.getPid())
                    rightLaser.setXY(e.getX(i), e.getY(i));
            }
        }
        else{
            switch(e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    upX = -1;
                    upY = -1;
                    break;
                case MotionEvent.ACTION_UP:
                    upX = e.getX();
                    upY = e.getY();
                    state.update(upX, upY);
                    upX = -1;
                    upY = -1;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public void initialize(){
        player = new Player();
        leftLaser = new Lasers(0, getHeight());
        rightLaser = new Lasers(getWidth(), getHeight());
        gameObjects.clear();
        ens.clear();
        gameObjects.add(leftLaser);
        gameObjects.add(rightLaser);
        gameObjects.add(player);
        radius = 10;
        upX = -1;
        upY = -1;
        eps = 5;
    }

    public void update(){
        if(state.tag==play.tag){
            interx = -1;
            intery = -1;
            if (leftLaser.getPid() != -1 && rightLaser.getPid() != -1) {
                float m = ((leftLaser.getY() - leftLaser.getSy()) / (leftLaser.getX() - leftLaser.getSx()));
                float k = ((rightLaser.getY() - rightLaser.getSy()) / (rightLaser.getX() - rightLaser.getSx()));
                float c = 0 - rightLaser.getX() * k + rightLaser.getY();
                float b = 0 - leftLaser.getX() * m + leftLaser.getY();

                interx = (c - b) / (m - k);
                intery = ((leftLaser.getY() - leftLaser.getSy()) / (leftLaser.getX() - leftLaser.getSx())) * (interx - leftLaser.getX()) + leftLaser.getY();
            }
            Random rand = new Random();
            if (rand.nextInt() % (fps / eps) == 0) {
                int rad = 33 + Math.abs(rand.nextInt() % 15);
                Enemies e = new Enemies(Math.abs(rand.nextInt() % (getWidth() - 400)) + 200, 0 - rad, 1, rad, 4 + rand.nextInt() % 5 + 2*(int)((Math.log(player.getScore() == 0 ? 1 : player.getScore())) / Math.log(2)), player, Enemies.Type.NORMAL);
                ens.add(e);
                gameObjects.add(e);
            }
            if ((player.getScore() - player.getLastSpecScore()) % 200 == 0 && player.getScore() > player.getLastSpecScore()) {
                boolean canSpec = true;
                for (Enemies en : ens) {
                    if (en.isSpecial())
                        canSpec = false;
                }
                int b = rand.nextInt() % 2;
                if (b == 1) {
                    player.setLastSpecScore(player.getScore());
                } else if (canSpec) {
                    int a = rand.nextInt() % 2;
                    Enemies e = new Enemies(a == 0 ? 0 : getWidth(), rand.nextInt(getHeight() / 2) + getHeight() / 4, 5, 35, a == 0 ? 10 : -10, player, Enemies.Type.HEALTH);
                    ens.add(e);
                    gameObjects.add(e);
                    player.setLastSpecScore(player.getScore());
                }
            }
            for (Enemies en : ens) {
                if (en.isAlive()) {
                    en.update(interx, intery, radius);
                }
            }
            for (GameObject g : gameObjects) {
                g.update();
            }
            if (!player.isAlive()) {
                scoreText.setText(Integer.toString(player.getScore()));
                state = end;
                initialize();
            }
        }
        state.update();
        state.update(upX, upY);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);

        if(state.tag==play.tag) {
            for(GameObject g : gameObjects){
                g.draw(canvas);
            }
            if(leftLaser.getPid()!=-1 && rightLaser.getPid()!=-1) {
                if(leftLaser.getX()!=-1 && leftLaser.getY()!=-1 && rightLaser.getX()!=-1 && rightLaser.getY()!=-1){
                    paint.setColor(Color.RED);
                    canvas.drawCircle(interx, intery, radius, paint);
                }
            }
        }
        state.draw(canvas);
        removals();
    }
    public void removals(){
        if(state.tag==play.tag){
            for(Enemies en : ens) {
                if(!en.isAlive()){
                    ens.remove(en);
                    gameObjects.remove(en);
                }
            }
        }
    }
    public void setFPS(int f){
        fps = f;
    }
}
