package com.example.firstpage;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamerView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder holder;
    GameThread gameThread;
    Gamer gamer;

    public GamerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GAMEVIEW", "created");
        gamer = new Gamer(
                getContext(),
                new Rect(0, 0, getWidth(), getHeight()),
                holder,
                getResources());
        gameThread = new GameThread(gamer);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("GAMEVIEW", "changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("GAMEVIEW", "destroyed");
        if(gameThread != null) {
            gameThread.shutdown();

            while(gameThread != null) {
                try {
                    gameThread.join();
                    gameThread = null;
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gamer.onTouchEvent(event);
        return true;
    }
}
