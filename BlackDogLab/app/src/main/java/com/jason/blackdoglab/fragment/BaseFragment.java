package com.jason.blackdoglab.fragment;

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

    private void initBackground(ImageView imgView, int drawableID) {
        imgView.post(() -> imgView.setImageBitmap(decodeBitmap(drawableID, imgView.getWidth(), imgView.getHeight())));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initBackground(getBgImgView(),getBgDrawableID());
        initListener();
    }

    public Bitmap decodeBitmap(int drawableID, int ctWidth, int ctHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 通过这个bitmap获取图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableID, options);
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
        Utils.setLog("scale : " + (int) scale);
        options.inSampleSize = (int) scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        bitmap = BitmapFactory.decodeResource(getResources(), drawableID, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Utils.setLog("缩略图高度：" + h + "宽度:" + w);
        return bitmap;
    }
}
