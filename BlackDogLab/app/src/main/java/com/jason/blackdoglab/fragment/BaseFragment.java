package com.jason.blackdoglab.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;


public abstract class BaseFragment extends Fragment {
    protected abstract void initView(View view);

    protected abstract ImageView getBgImgView();

    protected abstract int getBgDrawableID();

    protected void initListener() {
    }

    private int ctWidth, ctHeight;
    private float deviceDensity;
    private int deviceDensityDpi;

    private void initBackground(ImageView imgView, int drawableID) {
        imgView.post(() -> {
            Utils.setLog(this.getClass().getSimpleName());
            imgView.setImageBitmap(decodeBitmap(drawableID, imgView.getWidth(), imgView.getHeight()));
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //initial density
        deviceDensity = getResources().getDisplayMetrics().density;
        deviceDensityDpi = getResources().getDisplayMetrics().densityDpi;
//        Utils.setLog("device density = " + deviceDensity + ",dpi = " + deviceDensityDpi);
        initBackground(getBgImgView(), getBgDrawableID());
        initListener();
    }

    private Bitmap decodeBitmap(int drawableID, int ctWidth, int ctHeight) {
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
