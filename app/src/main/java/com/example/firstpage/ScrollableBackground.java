package com.example.firstpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class ScrollableBackground {

    public Sprites[] sprites;
    public int speed;

    public ScrollableBackground(Bitmap image, Context context, Rect hitbox, Rect screen, int speed) {
        sprites = new Sprites[2];
        this.speed = speed;

        sprites[0] = new Sprites(image, context, hitbox, screen);
        sprites[1] = new Sprites(image, context, hitbox, screen);
        sprites[0].setX(0);
        sprites[1].setX(sprites[0].getRight());
    }

    public void update(long elapsed) {
        for(Sprites s : sprites) {
            //Log.d("SCROLLABLE_BACKGROUND", "Updating with speed " + speed);
            s.setX(s.getX() - speed);

            // Move sprite to right of other one if it's past the left side of screen
            if(s.getRight() < 0) s.setX(
                    (s == sprites[0]) ? sprites[1].getRight() - speed: sprites[0].getRight() - speed);
        }
    }

    public void draw(Canvas canvas) {
        for(Sprites s : sprites) {
            //Log.d("SCROLLABLE_BACKGROUND", "Drawing at x = " + s.getX() + "!");
            s.draw(canvas, 0);
        }
    }
}
