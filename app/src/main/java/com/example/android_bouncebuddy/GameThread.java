package com.example.android_bouncebuddy;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private SurfaceHolder holder;
    private GameView gameView;
    private boolean isRunning;

    public GameThread(SurfaceHolder holder, GameView gameView) {
        this.holder = holder;
        this.gameView = gameView;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        while (isRunning) {
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                synchronized (holder) {
                    gameView.update();
                    gameView.draw(canvas);
                }
            } finally {
                if (canvas != null) holder.unlockCanvasAndPost(canvas);
            }

            try {
                sleep(16); // ~60 FPS
            } catch (InterruptedException ignored) {}
        }
    }
}

