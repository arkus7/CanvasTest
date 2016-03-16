package com.mooduplabs.canvas;

import android.graphics.Color;

import java.util.Random;

public class Entity {
    private int x;
    private int y;
    private String iconPath;
    private int speed;
    private double radius;
    private int color;

    public Entity(int x, int y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        Random rnd = new Random();
        this.color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    public void setColor(int color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isCollided(Entity other) {
        return Math.sqrt(Math.pow(other.y - y, 2) + Math.pow(other.x - x, 2)) <= radius + other.radius;
    }
}
