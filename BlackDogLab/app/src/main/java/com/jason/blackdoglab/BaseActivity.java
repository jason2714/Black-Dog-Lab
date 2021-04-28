package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract int getLayoutViewID();

    private final int theme_default = R.style.Theme_BlackDogLab_Default;
    protected FileController fc_basicInfo, fc_dailyMood, fc_loginDate;

    protected int setThemeColor() {
        return theme_default;
    }
//    protected abstract void

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideNavigationBar(this);
        initBasicInfo();
        //setTheme一定要在setTheme前 不然會一堆奇怪得inflater error
        setTheme(this.setThemeColor());
        setContentView(getLayoutViewID());
        ActivityUtils.getInstance().addActivity(this);
        initView();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除Activity
        ActivityUtils.getInstance().removeActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 點返回鍵
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 移除Activity
            ActivityUtils.getInstance().removeActivity(this);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void showToast(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }

    protected void initBasicInfo() {}

    protected Bitmap decodeBitmap(int ctWidth, int ctHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_main_lab, options);
        if (bitmap == null) {
            Utils.setLog("bitmap为空");
        }
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;

        float density = getResources().getDisplayMetrics().density;
        int densityDpi = getResources().getDisplayMetrics().densityDpi;
        Utils.setLog("device density = " + density + ",dpi = " + densityDpi);
        Utils.setLog("view高度：" + ctHeight + "宽度:" + ctWidth);
        Utils.setLog("真实图片高度：" + realHeight + "宽度:" + realWidth);
        // 计算缩放比
        float scale = density;
        if (realHeight > ctHeight)
            scale *= (realHeight / ctHeight);
//        int scale = (int) ((realHeight > realWidth ? realHeight : realWidth) / 100);
//        if (scale <= 0)
//        {
//            scale = 1;
//        }
        Utils.setLog((int)scale + "");
        options.inSampleSize = (int)scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_main_lab, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Utils.setLog("缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }
}