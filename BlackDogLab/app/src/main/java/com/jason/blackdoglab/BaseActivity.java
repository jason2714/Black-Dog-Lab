package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.blackdoglab.customclass.TypeText;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.utils.Utils;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract int getLayoutViewID();

    private final int theme_default = R.style.Theme_BlackDogLab_Default;
    protected FileController fcBasicInfo, fcDailyMood, fcLoginDate;

    protected abstract int setThemeColor();

    //    protected abstract void
    private int ctWidth, ctHeight;
    private float deviceDensity;
    private int deviceDensityDpi;
    protected Handler mHandler;
    protected TypeText mTypeText;
    protected long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.hideNavigationBar(this);
        initBasicInfo();
        //setTheme一定要在setTheme前 不然會一堆奇怪得inflater error
        setTheme(this.setThemeColor());
        setContentView(getLayoutViewID());
        ActivityUtils.getInstance().addActivity(this);
        //initial density
        deviceDensity = getResources().getDisplayMetrics().density;
        deviceDensityDpi = getResources().getDisplayMetrics().densityDpi;
        Utils.setLog("device density = " + deviceDensity + ",dpi = " + deviceDensityDpi);
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
        ActivityUtils.getInstance().printActivity();
        // 監聽返回键，點兩次退出process
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("真的要離開嗎QQ");
                exitTime = System.currentTimeMillis();
            } else {
                ActivityUtils.getInstance().exitSystem();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void showToast(String massage) {
        Toast.makeText(this, massage, Toast.LENGTH_SHORT).show();
    }

    protected void initBasicInfo() {
        mTypeText = new TypeText();
        mHandler = new Handler(getMainLooper());
    }

    protected Bitmap decodeBitmap(int drawableID, int ctWidth, int ctHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Get Bitmap width and height without loading into memory
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableID, options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        Utils.setLog("Real Image Width：" + realWidth + ",Height:" + realHeight);
        Utils.setLog("view Width：" + ctWidth + ",Height:" + ctHeight);
        // Calculate scale
        float scale = deviceDensity;
        if (realHeight > ctHeight)
            scale *= (realHeight / ctHeight);
        Utils.setLog("scale : " + (int) scale);
        options.inSampleSize = (int) scale;
        options.inJustDecodeBounds = false;
//        calculate density yourself will have closer resolution
//        options.inDensity = densityDpi;
        // Set options.inJustDecodeBounds to false to read the image
        bitmap = BitmapFactory.decodeResource(getResources(), drawableID, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Utils.setLog("Image After Scaling Width:" + w + ",Height:" + h);
        return bitmap;
    }

    protected void setImageDrawableFit(ImageView imageView, int drawableID) {
        imageView.post(() -> {
            ctWidth = imageView.getWidth();
            ctHeight = imageView.getHeight();
            imageView.setImageBitmap(decodeBitmap(drawableID, ctWidth, ctHeight));
        });
    }
}