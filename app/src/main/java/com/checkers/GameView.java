package com.checkers;

import android.content.Context;
import android.view.SurfaceView;

public final class GameView extends SurfaceView implements Runnable {

    private boolean running = true;
    //the game thread
    private Thread gameThread = null;

    //Class constructor
    public GameView(Context context) {
        super(context);
    }

    @Override
    public void run() {
        while (running) {

            control();

            update();

            draw();
        }
    }


    private void update() {

    }

    private void draw() {

    }

    private void control() {
        try {
            gameThread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        //when the game is paused
        //setting the variable to false
        running = false;
        try {
            //stopping the thread
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        //when the game is resumed
        //starting the thread again
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
