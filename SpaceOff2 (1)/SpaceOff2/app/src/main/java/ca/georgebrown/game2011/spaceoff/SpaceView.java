package ca.georgebrown.game2011.spaceoff;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import java.lang.*;
import java.lang.InterruptedException;

/**
 * Created by Sean on 2018-04-16.
 */

public class SpaceView extends SurfaceView implements Runnable{
    Context context;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;

    private volatile boolean isPlaying;
    private boolean isPaused = true;

    private Canvas canvas;
    private Paint paint;

    private long fps;
    private long timeThisFrame;

    private int screenX;
    private int screenY;

    private PlayerShip playerShip;
    private Bullet bullet;
    private Asteroid asteroid;

    //Number of asteroids to spawn
    int numAsteroids = 3;

    //Declare an array of asteroids
    Asteroid[] asteroids = new Asteroid[numAsteroids];

    private int shootID = -1;

    int score = 0;

    private int lives = 3;

    public class PlayerShip {
        RectF rect;

        private Bitmap bitmap;
        private float length;
        private float height;

        private float x;
        private float y;
        private float shipSpeed;

        private int RectX;
        private int RectY;

        public final int STOPPED = 0;
        public final int LEFT = 1;
        public final int RIGHT = 2;

        private int shipMoving = STOPPED;

        private Rect detectCollision;

        public PlayerShip(Context context, int screenX, int screenY){
            rect = new RectF();
            length = screenX/10;
            height = screenY/10;

            // ship spawn
            x = screenX / 2;
            y = screenY - 20;

            RectX = screenX /2;
            RectY = screenY - 20;

            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership);

            bitmap = Bitmap.createScaledBitmap(bitmap,
                    (int) (length),
                    (int) (height),
                    false);

            // speed per second
            shipSpeed = 350;

            detectCollision =  new Rect(RectX, RectY, bitmap.getWidth(), bitmap.getHeight());
        }

        public RectF getRect(){
            return rect;
        }

        public Bitmap getBitmap(){
            return bitmap;
        }

        public float getX(){
            return x;
        }

        public float getLength(){
            return length;
        }

        public void setMovementState(int state){
            shipMoving = state;
        }

        public void update(long fps){
            if(shipMoving == LEFT){
                x = x - shipSpeed / fps;
            }

            if(shipMoving == RIGHT){
                x = x + shipSpeed / fps;
            }

            detectCollision.left = RectX;
            detectCollision.top = RectY;
            detectCollision.right = RectX + bitmap.getWidth();
            detectCollision.bottom = RectY + bitmap.getHeight();
        }

        public Rect getDetectCollision() {
            return detectCollision;
        }
    }

    public class Asteroid {
        private Bitmap bitmap;

        //x and y coordinates
        private int x;
        private int y;

        private int speed = 1;

        private int maxX;
        private int minX;
        private int maxY;
        private int minY;

        private Rect detectCollision;

        public Asteroid(Context context, int screenX, int screenY) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor);

            minX = 0;
            minY = 0;
            maxX = screenX;
            maxY = screenY;

            Random generator = new Random();
            speed = generator.nextInt(5) + 4;

            x = generator.nextInt(maxX) - bitmap.getWidth();
            y = 0;

            detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
        }

        public void update(long playerSpeed) {
            //y += playerSpeed;
            y += speed;

            if (y > maxY - bitmap.getHeight()) {
                Random generator = new Random();
                speed = generator.nextInt(5) + 4;
                x = generator.nextInt(maxX) - bitmap.getWidth();
                y = 0;
            }

            detectCollision.left = x;
            detectCollision.top = y;
            detectCollision.right = x + bitmap.getWidth();
            detectCollision.bottom = y + bitmap.getHeight();
        }

        public void setX(int x){
            this.x = x;
        }

        public Rect getDetectCollision() {
            return detectCollision;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getSpeed() {
            return speed;
        }
    }

    public class Bullet {
        private float x;
        private float y;

        private RectF rect;
        private Rect detectCollision;

        private int RectX;
        private int RectY;

        public final int UP = 0;

        // Going nowhere
        int heading = -1;
        float speed =  350;

        private int width = 3;
        private int height = 5;

        private boolean isActive;


        public Bullet(int screenY) {

            height = screenY / 20;
            isActive = false;

            rect = new RectF();

            RectX = (int)x;
            RectY = (int)y;

            detectCollision =  new Rect(RectX, RectY, width, height);
        }

        public RectF getRect(){
            return rect;
        }

        public Rect getDetectCollision() {
            return detectCollision;
        }

        public boolean getStatus(){
            return isActive;
        }

        public void setInactive(){
            isActive = false;
        }

        public void setX(int x){
            this.x = x;
        }

        public float getImpactPointY(){
            return  y;
        }

        public boolean shoot(float startX, float startY, int direction) {
            if (!isActive) {
                x = startX;
                y = startY;
                heading = direction;
                isActive = true;
                return true;
            }

            // Bullet already active
            return false;
        }

        public void update(long fps){

            // Just move up or down
            if(heading == UP){
                y = y - speed / fps;
            }else{
                y = y + speed / fps;
            }

            // Update rect
            rect.left = x;
            rect.right = x + width;
            rect.top = y;
            rect.bottom = y + height;

            detectCollision.left = RectX;
            detectCollision.top = RectY;
            detectCollision.right = RectX + width;
            detectCollision.bottom = RectY + height;
        }
    }

    public SpaceView(Context context, int x, int y) {
        super(context);

        this.context = context;

        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        asteroids = new Asteroid[numAsteroids];
        for(int i=0; i<numAsteroids; i++){
            asteroids[i] = new Asteroid(context, screenX, screenY);
        }

        prepareLevel();
    }

    private void prepareLevel(){
        playerShip = new PlayerShip(context, screenX, screenY);

        bullet = new Bullet(screenY);

        asteroid = new Asteroid(context, screenX, screenY);
    }

    @Override
    public void run() {
        while (isPlaying) {
            long startFrameTime = System.currentTimeMillis();

            if (!isPaused) {
                update();
            }

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update () {
        boolean lost = false;
        playerShip.update(fps);

        if (lost) {
            prepareLevel();
        }

        if(bullet.getStatus()){
            bullet.update(fps);
        }

        // top of screen destruction
        if(bullet.getImpactPointY() < 0){
            bullet.setInactive();
        }

        if (lives == 0) {
            lives = 3;
            score = 0;
        }
        //updating the enemy coordinate with respect to player speed
        for(int i = 0; i<numAsteroids; i++){
            asteroids[i].update(1);

            if (Rect.intersects(playerShip.getDetectCollision(), asteroids[i].getDetectCollision())) {
                asteroids[i].setX(-9999);
                lives--;
            }

            if (Rect.intersects(bullet.getDetectCollision(), asteroids[i].getDetectCollision())) {
                asteroids[i].setX(-9999);
                bullet.setX(-9999);
                score++;
            }
        }
    }

    private void draw () {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            // BG colour
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Bullet colour
            paint.setColor(Color.argb(255, 255, 255, 0));

            // Draw spaceship
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - 50, paint);

            // Draw an Asteroid
            canvas.drawBitmap(asteroid.getBitmap(), asteroid.getX(), asteroid.getY(), paint);
            asteroid.update(fps);

            //drawing the enemies
            for (int i = 0; i < numAsteroids; i++) {
                canvas.drawBitmap(
                        asteroids[i].getBitmap(),
                        asteroids[i].getX(),
                        asteroids[i].getY(),
                        paint
                );
                asteroids[i].update(fps);
            }

            // draw bullet
            if(bullet.getStatus()){
                canvas.drawRect(bullet.getRect(), paint);
            }

            // Text Colour
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(40);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isPaused = false;
                if(motionEvent.getY() > screenY - screenY / 8) {
                    if (motionEvent.getX() > screenX / 2) {
                        playerShip.setMovementState(playerShip.RIGHT);
                    } else {
                        playerShip.setMovementState(playerShip.LEFT);
                    }
                }

                if(motionEvent.getY() < screenY - screenY / 8) {

                    if (bullet.shoot(playerShip.getX()+ playerShip.getLength()/2,screenY,bullet.UP)) {
                        //
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                if(motionEvent.getY() > screenY - screenY / 10) {
                    playerShip.setMovementState(playerShip.STOPPED);
                }
                break;
        }
        return true;
    }
}