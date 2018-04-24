package ca.georgebrown.game2011.spaceoff;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Sean on 2018-04-16.
 */

public class Bullet {
    private float x;
    private float y;

    private RectF rect;

    private int RectX;
    private int RectY;

    public final int UP = 0;


    // Going nowhere
    int heading = -1;
    float speed =  350;

    private int width = 1;
    private int height;

    private boolean isActive;

    private Rect detectCollision;

    public Bullet(int screenY) {

        height = screenY / 20;
        isActive = false;

        rect = new RectF();

        detectCollision =  new Rect(RectX, RectY, width, height);
    }

    public RectF getRect(){
        return  rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
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

    public Rect getDetectCollision() {
        return detectCollision;
    }
}
