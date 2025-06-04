package com.example.android_bouncebuddy;

public class Ball {
    public float x, y, radius;
    public float dx, dy;
    public boolean isSad = false;
    public boolean hasHitPaddle = false;

    public Ball(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.dx = 5;
        this.dy = 10;
    }

    public void move() {
        x += dx;
        y += dy;

        if (x < radius || x > GameView.SCREEN_WIDTH - radius) dx = -dx;
        if (y < radius) dy = -dy;
    }

    public void bounce() {
        dy = -Math.abs(dy);
    }

    // 根据分数调整速度，分数越高，速度越快
    public void updateSpeed(int score) {
        float baseDx = 5;
        float baseDy = 10;

        // 每分数点加速10%
        float speedFactor = 1 + score * 0.1f;

        dx = (dx > 0 ? 1 : -1) * baseDx * speedFactor;
        dy = (dy > 0 ? 1 : -1) * baseDy * speedFactor;
    }
}
