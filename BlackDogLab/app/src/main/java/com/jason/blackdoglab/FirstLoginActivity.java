package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstLoginActivity extends BaseActivity {

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_first_login;
    }

    @Override
    protected int getThemeID() {
        return R.style.Theme_BlackDogLab_Default;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button mBtnSubmit = findViewById(R.id.btn_submit);
    }

    @Override
    public void onClick(View v) {

    }
}