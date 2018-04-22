package ca.georgebrown.game2011.spaceoff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Created by Darren Yam on 2018-04-16.
 */

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
