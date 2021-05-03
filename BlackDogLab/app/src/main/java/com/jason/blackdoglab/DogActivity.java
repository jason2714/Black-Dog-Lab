package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.util.HashSet;

public class DogActivity extends BaseActivity {

    private int themeColor = R.style.Theme_BlackDogLab_Default;
    private ImageView redDog;

    @Override
    protected void initView() {
        redDog = findViewById(R.id.red_dog);
        AnimationDrawable redDogWalk = new AnimationDrawable();
        AnimationDrawable redDogEat = new AnimationDrawable();
        redDog.post(() -> {
            Thread thread = new Thread(() -> {
                for (int i = 0; i < 289; i ++) {
                    String id;
                    if (i < 10)
                        id = "00" + i;
                    else if (i >= 10 && i < 100)
                        id = "0" + i;
                    else
                        id = String.valueOf(i);
                    String mDrawableName = "red_dog_eat" + id;
                    int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
                    Bitmap bitmap = decodeBitmap(resID, redDog.getWidth(), redDog.getHeight());
                    mHandler.post(() -> redDogEat.addFrame(new BitmapDrawable(bitmap), 33));
                }
                mHandler.post(() -> {
                    redDog.setBackgroundDrawable(redDogEat);
                    redDogEat.start();
                });
            });
            thread.setName("Jason Thread");
            thread.start();
        });
    }

    @Override
    protected void initListener() {

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

    }

    @Override
    protected int setThemeColor() {
        return themeColor;
    }

    @Override
    protected void initBasicInfo() {
        super.initBasicInfo();
        getThemeColor();
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
}