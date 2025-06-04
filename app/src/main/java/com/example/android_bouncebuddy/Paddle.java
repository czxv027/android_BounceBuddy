package com.example.android_bouncebuddy;

public class Paddle {
    public float x, y, width, height;

    public Paddle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean hit(float ballX, float ballY, float ballRadius) {
        return ballX + ballRadius > x && ballX - ballRadius < x + width &&
                ballY + ballRadius > y && ballY - ballRadius < y + height;
    }
}

