package com.jason.blackdoglab.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.jason.blackdoglab.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import kotlin.jvm.Volatile;

public class FrameSurfaceView extends BaseSurfaceView {
    public static final int INVALID_BITMAP_INDEX = Integer.MAX_VALUE;
    private List<Integer> bitmaps = new ArrayList<>();
    // Frame picture
    private Bitmap frameBitmap;
    // Frame Index
    private int bitmapIndex = INVALID_BITMAP_INDEX;
    private Paint paint = new Paint();
    private BitmapFactory.Options options = new BitmapFactory.Options();
    // Frame picture原始大小
    private Rect srcRect;
    // Frame picture目标大小
    private Rect dstRect = new Rect();
    private int defaultWidth, defaultHeight;
    private int scaleWidth, scaleHeight;
    private Context context;
    private boolean isOneShot;
    private boolean keepLastFrame;

    public void setTotalDuration(int duration) {
        new Thread(() -> {
            int i = 0;
            while (bitmaps.size() == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
                if (i >= 50)
                    break;
            }
            int frameDuration = duration / bitmaps.size();
            Utils.setLog(frameDuration + "frame duration");
            setFrameDuration(frameDuration);
        }).start();
    }

    public void setDuration(int frameDuration) {
        setFrameDuration(frameDuration);
    }

    public void setBitmaps(List<Integer> bitmaps) {
        if (bitmaps == null || bitmaps.size() == 0) {
            Utils.setLog("bitmaps is empty");
            return;
        }
        this.bitmaps = bitmaps;
        getBitmapDimension(bitmaps.get(0));
    }

    public void setSingleBitmap(int bitmap) {
        List<Integer> bitmaps = new ArrayList<>();
        bitmaps.add(bitmap);
        if (bitmaps.size() == 0) {
            Utils.setLog("bitmaps is empty");
            return;
        }
        this.bitmaps = bitmaps;
        getBitmapDimension(bitmaps.get(0));
    }

    private void getBitmapDimension(Integer integer) {
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(this.getResources(), integer, options);
        defaultWidth = options.outWidth;
        defaultHeight = options.outHeight;
        requestLayout();
    }

    public FrameSurfaceView(Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        isOneShot = true;
        keepLastFrame = true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        dstRect.set(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onFrameDrawFinish() {
        // Recycle a frame directly after it has been drawn
        recycleOneFrame();
    }

    // Recovery frame
    private void recycleOneFrame() {
        if (frameBitmap != null) {
            frameBitmap.recycle();
            frameBitmap = null;
        }
    }

    @Override
    protected void onFrameDraw(Canvas canvas) {
        // You need to clear the canvas before drawing a frame, otherwise all frames will be overlapped and displayed at the same time.
        if (!isFinish())
            clearCanvas(canvas);
        drawOneFrame(canvas);
        if (isFinish())
            onFrameAnimationEnd(canvas);
    }

    // Draw a frame. It's Bitmap.
    private void drawOneFrame(Canvas canvas) {
        frameBitmap = decodeBitmap(bitmaps.get(bitmapIndex), getWidth(), getHeight());
        srcRect = new Rect(0, 0, scaleWidth, scaleHeight);
        canvas.drawBitmap(frameBitmap, srcRect, dstRect, paint);
        bitmapIndex++;
    }

    private void onFrameAnimationEnd(Canvas canvas) {
        if (!keepLastFrame)
            clearCanvas(canvas);
        reset();
    }

    private void reset() {
        if (isOneShot) {
            animState = false;
            bitmapIndex = INVALID_BITMAP_INDEX;
        } else
            bitmapIndex = 0;
    }

    // Does Frame Animation End
    private boolean isFinish() {
        return bitmapIndex >= bitmaps.size() - 1;
    }

    // Does Frame Animation Start
//    private boolean isStart() {
//        return bitmapIndex != INVALID_BITMAP_INDEX;
//    }

    // Start playing frame animation
    public void start() {
        //must start after thread create
        this.post(() -> {
            bitmapIndex = 0;
            startAnim();
        });
    }

    public void startDelay(int delay) {
        //must start after thread create
        this.postDelayed(() -> {
            bitmapIndex = 0;
            startAnim();
        }, delay);
    }

    private void clearCanvas(Canvas canvas) {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    private Bitmap decodeBitmap(int drawableID, int ctWidth, int ctHeight) {
        // Calculate scale
        float scale = Utils.getDensity(context);
        if (defaultHeight > ctHeight)
            scale *= (defaultHeight / ctHeight);
        options.inSampleSize = (int) scale;
        options.inJustDecodeBounds = false;
//        calculate density yourself will have closer resolution
        // Set options.inJustDecodeBounds to false to read the image
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableID, options);
        scaleWidth = bitmap.getWidth();
        scaleHeight = bitmap.getHeight();
        return bitmap;
    }

    public void setOneShot(boolean isOneShot) {
        this.isOneShot = isOneShot;
    }

    public void setKeepLastFrame(boolean keepLastFrame) {
        this.keepLastFrame = keepLastFrame;
    }

    public boolean isRunning() {
        return animState;
    }

    public void setVisible(boolean isVisible) {
        if (bitmaps.size() == 0) {
            Utils.setLog("don't have image,already invisible");
            return;
        }
        if (isVisible) {
            paint.setAlpha(255);
        } else {
            paint.setAlpha(0);
        }
        redraw();
    }

    public void redraw() {
        if (!isRunning()) {
            this.post(() -> {
                bitmapIndex = bitmaps.size() - 1;
                startAnim();
            });
        }
    }

}