package com.jason.blackdoglab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.jason.blackdoglab.utils.Utils;

public abstract class BaseSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public static final int DEFAULT_FRAME_DURATION_MILLISECOND = 50;
    // Threads for computing frame data
    private HandlerThread handlerThread;
    private Handler handler;
    // Frame refresh frequency
    private int frameDuration = DEFAULT_FRAME_DURATION_MILLISECOND;
    // A canvas for drawing frames
    private Canvas canvas;
    private boolean isAlive;
    protected boolean animState;

    public BaseSurfaceView(Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected void init() {
        getHolder().addCallback(this);
        // Set a transparent background, otherwise the Surface View background is black
        setBackgroundTransparent();
    }

    private void setBackgroundTransparent() {
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Utils.setLog("surface create");
        isAlive = true;
        animState = false;
        startDrawThread();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Utils.setLog("surface change");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Utils.setLog("surface destroy");
        stopDrawThread();
        isAlive = false;
        animState = true;
    }

    // Stop frame drawing thread
    private void stopDrawThread() {
        handlerThread.quit();
        handler = null;
    }

    // Start frame drawing thread
    private void startDrawThread() {
        Utils.setLog("thread create");
        handlerThread = new HandlerThread("Red Dog Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(new DrawRunnable());
    }

    private class DrawRunnable implements Runnable {

        @Override
        public void run() {
            if (!isAlive) {
                return;
            }
            try {
                // 1. Getting Canvas
                canvas = getHolder().lockCanvas();
                // 2. Draw a frame
                onFrameDraw(canvas);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 3. Submit frame data
                getHolder().unlockCanvasAndPost(canvas);
                // 4. End of Drawing a Frame
                onFrameDrawFinish();
            }
            // Continuously pushing itself to the message queue of the drawing thread to achieve frame refresh
            if(animState)
                handler.postDelayed(this, frameDuration);
        }
    }

    protected void setFrameDuration(int frameDuration) {
        this.frameDuration = frameDuration;
    }

    protected abstract void onFrameDrawFinish();

    protected abstract void onFrameDraw(Canvas canvas);

    protected void startAnim(){
        if(handler == null){
            Utils.setLog("handler is null");
            return;
        }
        animState = true;
        handler.post(new DrawRunnable());
    }
}