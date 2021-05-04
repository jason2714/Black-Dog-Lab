package com.jason.blackdoglab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;
import com.jason.blackdoglab.view.FrameSurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class DogActivity extends BaseActivity {

    private int themeColor = R.style.Theme_BlackDogLab_Default;
    private FrameSurfaceView redDog;
    private HashMap<String, Integer> dogMotion;
    private GifImageView mGifDogBg;
    private ImageView mBgDog;
    private ImageButton mBtnLeft, mBtnRight;
    private ImageView mImgThreeBowls;

    @Override
    protected void initView() {
        redDog = findViewById(R.id.red_dog);

        mGifDogBg = findViewById(R.id.gif_dog_background);
        mGifDogBg.setImageResource(R.drawable.gif_dog_bg);

        mBgDog = findViewById(R.id.bg_dog);
        setImageDrawableFit(mBgDog, R.drawable.bg_dog);

        redDog.setDuration(1000);
        redDog.setBitmaps(getMotionList("red", "eat"));
        redDog.setOneShot(true);
        redDog.setKeepLastFrame(true);
        redDog.start();

        mBtnLeft = findViewById(R.id.btn_dog_left);
        mBtnRight = findViewById(R.id.btn_dog_right);
        mImgThreeBowls = findViewById(R.id.img_three_bowls);
        setImageDrawableFit(mImgThreeBowls, R.drawable.bg_three_dog_bowls);
    }

    @Override
    protected void initListener() {
        mBtnLeft.setOnClickListener(this);
        mBtnRight.setOnClickListener(this);
        redDog.setOnClickListener(this);
    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_dog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.getInstance().cleanActivity(this);
        ActivityUtils.getInstance().printActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.red_dog:
                if (!redDog.isRunning()) {
                    int motion = (int) (Math.random() * 5);
                    String[] move = {"eat", "sit", "sleep", "stand", "walk"};
                    redDog.setBitmaps(getMotionList("red", move[motion]));
                    redDog.start();
                }
                break;
            case R.id.btn_dog_left:
                Intent intent = new Intent(DogActivity.this, MainPage.class);
                startActivity(intent);
                break;
            case R.id.btn_dog_right:
                break;
            default:
                break;
        }
    }

    @Override
    protected int setThemeColor() {
        return themeColor;
    }

    @Override
    protected void initBasicInfo() {
        super.initBasicInfo();
        getThemeColor();
        initDogMotion();
    }

    private void getThemeColor() {
        fc_loginDate = new FileController(this, getResources().getString(R.string.login_date));
        fc_dailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        try {
            //set basic theme color
            String[] splitFileData = fc_dailyMood.readFileSplit();
            for (String lineData : splitFileData) {
                String[] lineDataArray = lineData.split(FileController.getWordSplitRegex());
                if (lineDataArray[0].equals(fc_loginDate.readFile())) {
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
    }

    private List<Integer> getMotionList(String dogColor, String motion) {
        if (!dogMotion.containsKey(motion)) {
            Utils.setLog("don't have motion : " + motion);
            return null;
        }
        List<Integer> bitmaps = new ArrayList<>();
        for (int i = 0; i < dogMotion.get(motion); i++) {
            String id;
            if (i < 10)
                id = "00" + i;
            else if (i >= 10 && i < 100)
                id = "0" + i;
            else
                id = String.valueOf(i);
            String mDrawableName = dogColor + "_dog_" + motion + id;
            int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
            bitmaps.add(resID);
        }
        return bitmaps;
    }

    private void initDogMotion() {
        dogMotion = new HashMap<>();
        dogMotion.put("eat", 289);
        dogMotion.put("sit", 116);
        dogMotion.put("sleep", 116);
        dogMotion.put("stand", 116);
        dogMotion.put("walk", 115);
    }


}