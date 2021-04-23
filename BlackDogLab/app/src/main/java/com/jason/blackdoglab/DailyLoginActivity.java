package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;

public class DailyLoginActivity extends BaseActivity {

    private Button mBtnLogin;
    private final int[] moodsID = {R.id.img_mood1, R.id.img_mood2,
            R.id.img_mood3, R.id.img_mood4, R.id.img_mood5};
    private ImageView[] mImgMoods;
    private int moodSelected = -1;

    @Override
    protected void initView() {
        mBtnLogin = findViewById(R.id.btn_login);
        mImgMoods = new ImageView[moodsID.length];
        for (int idx = 0; idx < moodsID.length; idx++)
            mImgMoods[idx] = findViewById(moodsID[idx]);
    }

    @Override
    protected void initListener() {
        mBtnLogin.setOnClickListener(this);
        for (ImageView character : mImgMoods)
            character.setOnClickListener(this);
    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_daily_login;
    }

    @Override
    protected int getThemeID() {
        return R.style.Theme_BlackDogLab_Default;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int newMoodSelected = -1;
        switch (v.getId()) {
            case R.id.btn_login:
//                TODO check register success
                Utils.setLog("start Activity");
                Intent intent = new Intent(DailyLoginActivity.this, MainPage.class);
                startActivity(intent);
                break;
            case R.id.img_mood1:
                newMoodSelected = 0;
                break;
            case R.id.img_mood2:
                newMoodSelected = 1;
                break;
            case R.id.img_mood3:
                newMoodSelected = 2;
                break;
            case R.id.img_mood4:
                newMoodSelected = 3;
                break;
            case R.id.img_mood5:
                newMoodSelected = 4;
                break;
            default:
                break;
        }
        if (newMoodSelected != -1 && newMoodSelected != moodSelected) {
            if(moodSelected != -1)
                mImgMoods[moodSelected].setBackgroundResource(0);
            mImgMoods[newMoodSelected].setBackground(getDrawable(R.drawable.bg_select_circle));
            moodSelected = newMoodSelected;
        }
    }
}