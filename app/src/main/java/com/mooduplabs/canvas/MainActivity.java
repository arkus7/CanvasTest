package com.mooduplabs.canvas;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SurfaceView.OnTouchListener {

    @Bind(R.id.canvas)
    SurfaceView surfaceView;

    private Entity player;
    private List<Entity> circles;

    private int SCREEN_WIDTH;
    private int SCREEN_HEIGHT;
    private final double CIRCLE_RADIUS = 100.0;
    private int score = 0;

    Timer moveTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getScreenSize();
        initFields();
    }

    private void initFields() {
        moveTimer = new Timer();
        circles = new ArrayList<>();
        player = new Entity(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 500, CIRCLE_RADIUS);
        surfaceView.setOnTouchListener(this);
        Bitmap playerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        player.setIcon(playerBitmap);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            player.setSpeed(500 * (x < SCREEN_WIDTH / 2.0 ? - 1 : 1));
        } else {
            player.setSpeed(0);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTimer.cancel();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        moveTimer.cancel();
        super.onPause();
    }

    @Override
    protected void onStop() {
        moveTimer.cancel();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        moveTimer = new Timer();
        moveTimer.scheduleAtFixedRate(new MoveTask(), 1000, 5);
        moveTimer.scheduleAtFixedRate(new RandomCircleTask(), 1000, 750);
    }

    public void getScreenSize() {
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        SCREEN_WIDTH = screenSize.x;
        SCREEN_HEIGHT = screenSize.y;
    }

    class RandomCircleTask extends TimerTask {

        @Override
        public void run() {
            Random random = new Random();
            int x = random.nextInt((int) (SCREEN_WIDTH - CIRCLE_RADIUS - 300)) + 250;
            int y = - 300;
            Entity circle = new Entity(x, y, CIRCLE_RADIUS);
            circle.setSpeed(500);
            circles.add(circle);
        }
    }

    class MoveTask extends TimerTask {
        @Override
        public void run() {
            List<Entity> toRemove = new ArrayList<>();
            double xPerFrame = player.getSpeed() / 60.0;
            int newX = (int) (player.getX() + xPerFrame);
            if(newX < 0)
                newX = 0;
            else if(newX > SCREEN_WIDTH - player.getIcon().getWidth())
                newX = SCREEN_WIDTH - player.getIcon().getWidth();
            player.setX(newX);
            SurfaceHolder holder = surfaceView.getHolder();
            Canvas canvas = holder.lockCanvas();
            canvas.drawColor(getResources().getColor(R.color.colorPrimaryDark));
            for(Entity circle : circles) {
                double yPerFrame = circle.getSpeed() / 60.0;
                int newY = (int) (circle.getY() + yPerFrame);
                circle.setY(newY);
                Paint paint = new Paint();
                paint.setColor(circle.getColor());
                if(! circle.isCollided(player)) {
                    canvas.drawCircle(circle.getX(), newY, (float) circle.getRadius(), paint);
                } else if(circle.isCollided(player)) {
                    toRemove.add(circle);
                    score += 10;
                } else if(circle.getY() > SCREEN_HEIGHT) {
                    toRemove.add(circle);
                }
            }
            Paint textPaint = new Paint();
            textPaint.setColor(getResources().getColor(android.R.color.white));
            textPaint.setTextSize(100);
            canvas.drawText("Score: " + String.valueOf(score), 100, 100, textPaint);
            canvas.drawBitmap(player.getIcon(), player.getX(), player.getY(), null);
            holder.unlockCanvasAndPost(canvas);
            circles.removeAll(toRemove);
        }
    }
}
