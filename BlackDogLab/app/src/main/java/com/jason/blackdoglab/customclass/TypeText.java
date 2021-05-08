package com.jason.blackdoglab.customclass;

import android.os.Handler;
import android.os.HandlerThread;
import android.widget.TextView;

public class TypeText {

    private TextView tv;
    private String s;
    private int length;
    private long time;
    private int n = 0;
    private HandlerThread handlerThread;
    private Handler handler;


    public TypeText() {
        handlerThread = new HandlerThread("TypeText Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        handlerThread.quit();
        handler = null;
    }

    public void showText(TextView tv, String s, long time) {
        this.tv = tv;//textview
        this.s = s;//字符串
        this.time = time;//间隔时间
        this.length = s.length();
        startType();
    }


    private void startType() {
        n = 0;
        handler.post(new typeRunnable());
    }

    private class typeRunnable implements Runnable {

        @Override
        public void run() {
            try {
                if (isRunning()) {
                    n++;
                    final String stv = s.substring(0, n);//截取要填充的字符串
                    tv.post(() -> tv.setText(stv));
                    handlerThread.sleep(time);//休息片刻
                    if (isRunning()) {//如果还有汉字，那么继续开启线程，相当于递归的感觉
                        handler.post(new typeRunnable());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return n + 1 <= length;
    }

    public void endType() {
        if (isRunning()) {
            n = length;
            tv.post(() -> tv.setText(s));
        }
    }
}
