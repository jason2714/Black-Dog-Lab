package com.jason.blackdoglab.loginpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.blackdoglab.BaseActivity;
import com.jason.blackdoglab.DialogActivity;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.MainPage;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyLoginActivity extends BaseActivity {

    private Button mBtnLogin;
    private final int[] moodsID = {R.id.img_mood1, R.id.img_mood2,
            R.id.img_mood3, R.id.img_mood4, R.id.img_mood5};
    private final int[] moodDrawables = {R.drawable.icon_select_mood1, R.drawable.icon_select_mood2,
            R.drawable.icon_select_mood3, R.drawable.icon_select_mood4, R.drawable.icon_select_mood5};
    private ImageView[] mImgMoods;
    private EditText mEtDailyMood;
    private TextView mTvWelcome;
    private int moodSelected = -1;
    private String playerName;
    private ImageView mBgDailyLogin;
    private boolean isFirstLogin;

    @Override
    protected void initView() {
        mBtnLogin = findViewById(R.id.btn_login);
        mImgMoods = new ImageView[moodsID.length];
        mEtDailyMood = findViewById(R.id.et_daily_mood);
        mTvWelcome = findViewById(R.id.tv_welcome);
        mBgDailyLogin = findViewById(R.id.bg_daily_login);
        mTvWelcome.setText("哈囉~" + playerName);
        for (int idx = 0; idx < moodsID.length; idx++) {
            mImgMoods[idx] = findViewById(moodsID[idx]);
            setImageDrawableFit(mImgMoods[idx], moodDrawables[idx]);
        }
        setImageDrawableFit(mBgDailyLogin, R.drawable.bg_first_login);
        //for test
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
    protected int setThemeColor() {
        return R.style.Theme_BlackDogLab_Default;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        isFirstLogin = intent.getBooleanExtra("FIRST_LOGIN", false);
//        try {
//            fcDailyMood.append("2021-04-05$0$happy$\n");
//            fcDailyMood.append("2021-04-06$1$happy$\n");
//            fcDailyMood.append("2021-04-07$2$happy$\n");
//            fcDailyMood.append("2021-04-08$3$happy$\n");
//            fcDailyMood.append("2021-04-09$4$happy$\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 點返回鍵
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 移除Activity
            ActivityUtils.getInstance().removeActivity(this);
            this.finish();
        }
        return false;
    }


    @Override
    public void onClick(View v) {

        int newMoodSelected = -1;
        switch (v.getId()) {
            case R.id.btn_login:
//                TODO check register success
                Utils.setLog("start MainPageActivity");
                if (checkInformation()) {
                    if (isFirstLogin)
                        ActivityUtils.getInstance().backToDialog();
                    else {
                        Intent intent = new Intent(DailyLoginActivity.this, MainPage.class);
                        startActivity(intent);
                    }
                } else {
                    showToast("記得記錄心情文字歐~");
                }
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
            if (moodSelected != -1)
                mImgMoods[moodSelected].setBackgroundResource(0);
            mImgMoods[newMoodSelected].setBackground(getDrawable(R.drawable.bg_select_circle));
            moodSelected = newMoodSelected;
        }
    }

    private boolean checkInformation() {
        Utils.setLog("start DailyActivity");
        if (moodSelected == -1 || mEtDailyMood.getText().toString().isEmpty())
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(Calendar.getInstance().getTime());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(currentDate)
                .append(FileController.getWordSplitChar())
                .append(moodSelected)
                .append(FileController.getWordSplitChar())
                .append(mEtDailyMood.getText().toString())
                .append(FileController.getLineSplitChar());
        try {
//            for(int i = 0; i< 5; i++){
//                String data = fcDailyMood.readLine();
//                if(data != null)
//                    Utils.setLog(data);
//            }
            if (fcDailyMood.fileExist())
                fcDailyMood.append(stringBuffer.toString());
            else
                fcDailyMood.write(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
            return false;
        }
        return true;
    }

    protected void initBasicInfo() {
        super.initBasicInfo();
        fcBasicInfo = new FileController(this, getResources().getString(R.string.basic_information));
        fcDailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        try {
            playerName = fcBasicInfo.readLineSplit()[0];
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
        }
    }
}