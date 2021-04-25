package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Context;
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
        // 点击手机上的返回键，返回上一层
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

}