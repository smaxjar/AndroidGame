package ca.georgebrown.game2011.spaceoff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Darren Yam on 2018-04-16.
 */
/*
public class Asteroid {
    Random random = new Random();
    private Bitmap bitmap;

    private float length;
    private float height;

    private float x;
    private float y;

    private float speed;
    public final int DOWN = 1;
    private int asteroidMoving = DOWN;
    boolean isVisible;

    RectF rect;


    private int spawnX;

    public Asteroid(Context context, int screenX, int screenY) {

        rect = new RectF();

        length = screenX/5;
        height = screenY/5;

        isVisible = true;
        x = screenX / 2;
        y = screenY / 2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        speed = 100;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLength(){
        return length;
    }

    public void update(long fps){
        y = y + height;

        // Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }
}
*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;


public class Asteroid {

    //bitmap for the enemy
    //we have already pasted the bitmap in the drawable folder
    private Bitmap bitmap;

    //x and y coordinates
    private int x;
    private int y;

    //enemy speed
    private int speed = 1;

    //min and max coordinates to keep the enemy inside the screen
    private int maxX;
    private int minX;

    private int maxY;
    private int minY;


    public Asteroid(Context context, int screenX, int screenY) {
        //getting bitmap from drawable resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteor);

        //initializing min and max coordinates
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        //generating a random coordinate to add enemy
        Random generator = new Random();
        speed = generator.nextInt(1) + 4;
        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

    }

    public void update(int playerSpeed) {
        //decreasing x coordinate so that enemy will move right to left
        x -= playerSpeed;
        x -= speed;
        //if the enemy reaches the left edge
        if (x < minX - bitmap.getWidth()) {
            //adding the enemy again to the right edge
            Random generator = new Random();
            speed = generator.nextInt(1) + 4;
            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }
    }

    //getters
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
