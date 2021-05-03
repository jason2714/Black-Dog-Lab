package com.jason.blackdoglab.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jason.blackdoglab.MainPage;

public class Utils {

    private static final String LOG_TAG = "test";
    public static final int RESOURCE_ID = 0;
    public static final int DATA = 1;

    public static void hideNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隱藏手機虛擬按鍵HOME/BACK/LIST按鍵
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);//讓navigation bar 過一段時間自動隱藏
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {//called when UI change
                if (visibility == 0) {//當navigation bar顯示時會=0
                    decorView.setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隱藏手機虛擬按鍵HOME/BACK/LIST按鍵
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);//讓navigation bar 過一段時間自動隱藏
                    //只設hide的話會蓋掉immersive
                }
            });
        }
    }

    public static void setLog(String massage) {
        Log.d(LOG_TAG, massage);
    }

    public static int getAttrID(Context context, int attrID, int type) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attrID, typedValue, true);
        if (type == Utils.RESOURCE_ID)
            return typedValue.resourceId;
        else if (type == Utils.DATA)
            return typedValue.data;
        else {
            setLog("get attr resource error");
            return -1;
        }
    }

    public static float convertDpToPixel(Context context, float dp) {
        float px = dp * getDensity(context);
        return px;
    }

    public static float convertPixelToDp(Context context, float px) {
        float dp = px / getDensity(context);
        return dp;
    }

    public static float getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static Size getScreenSizePixel(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new Size(metrics.widthPixels, metrics.heightPixels);
    }

    public static void setWindowBackground(Context context,float alpha) {
        if (context == null) return;
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = alpha;
            window.setAttributes(layoutParams);
        }
    }

    public static void showBackgroundAnimator(Context context,Float fromAlpha, float toAlpha) {
        if (toAlpha > 1f) return;
        ValueAnimator animator = ValueAnimator.ofFloat(fromAlpha, toAlpha);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            setWindowBackground(context,alpha);
        });
        animator.setDuration(500);
        animator.start();
    }
}
