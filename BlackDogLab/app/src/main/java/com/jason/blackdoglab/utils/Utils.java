package com.jason.blackdoglab.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jason.blackdoglab.MainPage;

public class Utils {

    private static final String LOG_TAG = "test";

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

    public static void showToast(Context context, String massage) {
        Toast.makeText(context, massage, Toast.LENGTH_SHORT).show();
    }

    public static void setLog(String massage) {
        Log.d(LOG_TAG, massage);
    }
}
