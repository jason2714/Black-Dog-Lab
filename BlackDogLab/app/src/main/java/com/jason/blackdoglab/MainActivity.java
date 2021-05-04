package com.jason.blackdoglab;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.jason.blackdoglab.loginpage.DailyLoginActivity;
import com.jason.blackdoglab.loginpage.FirstLoginActivity;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends BaseActivity {

    private VideoView mVvBeginAnimation;
    private Button mBtnSkip, mBtnStartGame;
    private GifImageView mGifLoginAnimation;

    @Override
    protected void initView() {
        mVvBeginAnimation = findViewById(R.id.vv_begin_animation);
        mBtnSkip = findViewById(R.id.btn_skip);
        mGifLoginAnimation = findViewById(R.id.gif_login_animation);
        mBtnStartGame = findViewById(R.id.btn_start_game);
    }

    @Override
    protected void initListener() {
        mBtnSkip.setOnClickListener(this);
        mBtnStartGame.setOnClickListener(this);
        mVvBeginAnimation.setOnPreparedListener(mp -> mVvBeginAnimation.start());
        mVvBeginAnimation.setOnCompletionListener(mp -> toStartGamePage());
    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri animationUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_begin_animation);
        mVvBeginAnimation.setVideoURI(animationUri);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
                toStartGamePage();
                break;
            case R.id.btn_start_game:
                gameStart();
                break;
            default:
                break;
        }
    }

    @Override
    protected void initBasicInfo() {
        fc_loginDate = new FileController(this, getResources().getString(R.string.login_date));
        fc_basicInfo = new FileController(this, getResources().getString(R.string.basic_information));
        fc_dailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.setLog("123123");
    }

    private void toStartGamePage() {
        Utils.showBackgroundAnimator(this, 1.0f, 0.0f);
        mHandler.postDelayed(()->{
            mVvBeginAnimation.stopPlayback();
            mVvBeginAnimation.setVisibility(View.GONE);
            mBtnSkip.setVisibility(View.GONE);
            mBtnStartGame.setVisibility(View.VISIBLE);
            mGifLoginAnimation.setImageResource(R.drawable.gif_login_animation);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Utils.showBackgroundAnimator(this, 0.0f, 1.0f);
        },100);
    }

    private void gameStart() {
        Intent intent;
        try {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            if (!fc_loginDate.fileExist() || !fc_basicInfo.fileExist()) {
                Log.d("test", currentDate);
                fc_loginDate.write(currentDate);
                intent = new Intent(MainActivity.this, FirstLoginActivity.class);
            } else {
                if (fc_dailyMood.fileExist()) {
                    boolean flag = false;
                    String[] splitFileData = fc_dailyMood.readFileSplit();
                    for (String lineData : splitFileData) {
                        String[] lineDataArray = lineData.split(FileController.getWordSplitRegex());
                        if(currentDate.equals(lineDataArray[0])){
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        intent = new Intent(MainActivity.this, MainPage.class);
                        Utils.setLog("have current date");
                    }
                    else{
                        Utils.setLog("don't have current date");
                        intent = new Intent(MainActivity.this, DailyLoginActivity.class);
                    }
                } else {
                    Log.d("test", "new date");
                    fc_loginDate.write(currentDate);
                    intent = new Intent(MainActivity.this, DailyLoginActivity.class);
                }
            }
        } catch (IOException e) {
            intent = new Intent(MainActivity.this, MainPage.class);
            Log.d("test", e.getCause() + "");
            e.printStackTrace();
        }
//        intent = new Intent(MainActivity.this, DogActivity.class);
        startActivity(intent);
    }
}