package com.example.android_bouncebuddy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    private Ball ball;
    private Paddle paddle;
    private int score = 0;
    private boolean isRunning = true;
    private Bitmap smile, sad;
    private GameThread thread;

    // 重新开始按钮属性
    private float restartBtnX, restartBtnY, restartBtnWidth = 400, restartBtnHeight = 120;
    private boolean showRestartButton = false;

    // 返回主页面按钮属性（只游戏结束时显示）
    private float backBtnX, backBtnY, backBtnWidth = 400, backBtnHeight = 120;

    // 固定图片大小（px）
    private final int ballSize = 80;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        smile = BitmapFactory.decodeResource(getResources(), R.drawable.smile_ball);
        smile = Bitmap.createScaledBitmap(smile, ballSize, ballSize, true);

        sad = BitmapFactory.decodeResource(getResources(), R.drawable.sad_ball);
        sad = Bitmap.createScaledBitmap(sad, ballSize, ballSize, true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        SCREEN_WIDTH = getWidth();
        SCREEN_HEIGHT = getHeight();
        startNewGame();
        thread = new GameThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // 不用处理
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        if (thread != null) {
            thread.setRunning(false);
            boolean retry = true;
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void update() {
        if (!isRunning) return;

        ball.updateSpeed(score);  // 随分数调整速度
        ball.move();

        if (paddle.hit(ball.x, ball.y, ball.radius)) {
            if (!ball.hasHitPaddle) {
                ball.bounce();
                score++;
                ball.hasHitPaddle = true;
            }
        } else {
            ball.hasHitPaddle = false;
        }

        if (ball.y > SCREEN_HEIGHT - ball.radius) {
            isRunning = false;
            ball.isSad = true;
            ScoreManager.saveScore(getContext(), score);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(ball.isSad ? sad : smile, ball.x - ballSize / 2f, ball.y - ballSize / 2f, null);

            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            canvas.drawRect(paddle.x, paddle.y, paddle.x + paddle.width, paddle.y + paddle.height, paint);

            paint.setColor(Color.BLACK);
            paint.setTextSize(60);
            canvas.drawText("Score: " + score, 50, 100, paint);

            if (!isRunning) {
                paint.setTextSize(80);
                paint.setColor(Color.RED);
                canvas.drawText("Game Over", SCREEN_WIDTH / 2 - 200, SCREEN_HEIGHT / 2 - 150, paint);

                restartBtnX = SCREEN_WIDTH / 2 - restartBtnWidth / 2;
                restartBtnY = SCREEN_HEIGHT / 2 - 50;
                paint.setColor(Color.GRAY);
                canvas.drawRect(restartBtnX, restartBtnY, restartBtnX + restartBtnWidth, restartBtnY + restartBtnHeight, paint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(60);
                canvas.drawText("重新开始", restartBtnX + 100, restartBtnY + 80, paint);

                backBtnX = restartBtnX;
                backBtnY = restartBtnY + restartBtnHeight + 40;
                paint.setColor(Color.DKGRAY);
                canvas.drawRect(backBtnX, backBtnY, backBtnX + backBtnWidth, backBtnY + backBtnHeight, paint);

                paint.setColor(Color.WHITE);
                paint.setTextSize(60);
                canvas.drawText("返回主页面", backBtnX + 80, backBtnY + 80, paint);

                showRestartButton = true;
            } else {
                showRestartButton = false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isRunning && showRestartButton) {
                if (touchX >= restartBtnX && touchX <= restartBtnX + restartBtnWidth &&
                        touchY >= restartBtnY && touchY <= restartBtnY + restartBtnHeight) {
                    restartGame();
                    return true;
                }
                if (touchX >= backBtnX && touchX <= backBtnX + backBtnWidth &&
                        touchY >= backBtnY && touchY <= backBtnY + backBtnHeight) {
                    if (getContext() instanceof android.app.Activity) {
                        ((android.app.Activity) getContext()).finish();
                    }
                    return true;
                }
            }
        }

        if (isRunning) {
            paddle.x = touchX - paddle.width / 2;
            if (paddle.x < 0) paddle.x = 0;
            if (paddle.x + paddle.width > SCREEN_WIDTH) paddle.x = SCREEN_WIDTH - paddle.width;
        }
        return true;
    }

    public void stopGame() {
        if (thread != null) {
            thread.setRunning(false);
        }
    }

    private void startNewGame() {
        score = 0;
        isRunning = true;
        showRestartButton = false;
        ball = new Ball(SCREEN_WIDTH / 2, 300, ballSize / 2);
        paddle = new Paddle(SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT - 150, 300, 40);
        ball.isSad = false;
        ball.hasHitPaddle = false;
    }

    private void restartGame() {
        startNewGame();
    }
}
