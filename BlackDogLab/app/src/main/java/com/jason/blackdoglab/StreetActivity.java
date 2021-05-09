package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;

public class StreetActivity extends BaseActivity {

    private ImageView mBgStreet;
    private ImageButton mBtnRight, mBtnLeft;

    @Override
    protected void initView() {
        mBgStreet = findViewById(R.id.bg_street);
        mBtnLeft = findViewById(R.id.btn_street_left);
        mBtnRight = findViewById(R.id.btn_street_right);
        setImageDrawableFit(mBgStreet, Utils.getAttrID(this, R.attr.bg_main_street, Utils.RESOURCE_ID));
    }

    @Override
    protected void initListener() {
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);
    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_street;
    }

    @Override
    protected int setThemeColor() {
        return getThemeColor();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().cleanActivity(this);
        ActivityUtils.getInstance().printActivity();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_street_left:
                intent = new Intent(StreetActivity.this, ParkActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_street_right:
                intent = new Intent(StreetActivity.this, MainPage.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initBasicInfo() {
        super.initBasicInfo();
        getThemeColor();
    }

    private int getThemeColor() {
        int themeColor = R.style.Theme_BlackDogLab_Default;
        fcLoginDate = new FileController(this, getResources().getString(R.string.login_date));
        fcDailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        try {
            //set basic theme color
            String[] splitFileData = fcDailyMood.readFileSplit();
            for (String lineData : splitFileData) {
                String[] lineDataArray = lineData.split(FileController.getWordSplitRegex());
                if (lineDataArray[0].equals(fcLoginDate.readFile())) {
                    Utils.setLog("Mood Type = " + lineDataArray[1]);
                    switch (Integer.parseInt(lineDataArray[1])) {
                        case 0:
                        case 1:
                            themeColor = R.style.Theme_BlackDogLab_Blue;
                            break;
                        case 2:
                            themeColor = R.style.Theme_BlackDogLab_Green;
                            break;
                        case 3:
                        case 4:
                            themeColor = R.style.Theme_BlackDogLab_Brown;
                            break;
                        default:
                            themeColor = R.style.Theme_BlackDogLab_Default;
                            Utils.setLog("Not Set Today's Mood Yet");
                            break;
                    }
                }
            }
            Utils.setLog("Set Theme Success");
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
        }
        return themeColor;
    }
}