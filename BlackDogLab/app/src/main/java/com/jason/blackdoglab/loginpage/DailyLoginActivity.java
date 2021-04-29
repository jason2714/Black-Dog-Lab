package com.jason.blackdoglab.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.blackdoglab.BaseActivity;
import com.jason.blackdoglab.FileController;
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
//        try {
//            fc_dailyMood.append("2021-05-09$0$hey$\n");
//            fc_dailyMood.append("2021-06-15$0$far$\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            fc_dailyMood.append("2021-04-05$0$happy$\n");
//            fc_dailyMood.append("2021-04-06$1$happy$\n");
//            fc_dailyMood.append("2021-04-07$2$happy$\n");
//            fc_dailyMood.append("2021-04-08$3$happy$\n");
//            fc_dailyMood.append("2021-04-09$4$happy$\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void onClick(View v) {

        int newMoodSelected = -1;
        switch (v.getId()) {
            case R.id.btn_login:
//                TODO check register success
                Utils.setLog("start MainPageActivity");
                if (checkInformation()) {
                    Intent intent = new Intent(DailyLoginActivity.this, MainPage.class);
                    startActivity(intent);
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
//                String data = fc_dailyMood.readLine();
//                if(data != null)
//                    Utils.setLog(data);
//            }
            if (fc_dailyMood.fileExist())
                fc_dailyMood.append(stringBuffer.toString());
            else
                fc_dailyMood.write(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
            return false;
        }
        return true;
    }

    protected void initBasicInfo() {
        fc_basicInfo = new FileController(this, getResources().getString(R.string.basic_information));
        fc_dailyMood = new FileController(this, getResources().getString(R.string.daily_mood));
        try {
            playerName = fc_basicInfo.readLineSplit()[0];
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
        }
    }
}