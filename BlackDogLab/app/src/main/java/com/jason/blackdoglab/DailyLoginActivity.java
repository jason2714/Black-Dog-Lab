package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DailyLoginActivity extends BaseActivity {

    private Button mBtnLogin;
    private final int[] moodsID = {R.id.img_mood1, R.id.img_mood2,
            R.id.img_mood3, R.id.img_mood4, R.id.img_mood5};
    private ImageView[] mImgMoods;
    private EditText mEtDailyMood;
    private TextView mTvWelcome;
    private int moodSelected = -1;
    private String playerName,oldData;

    @Override
    protected void initView() {
        mBtnLogin = findViewById(R.id.btn_login);
        mImgMoods = new ImageView[moodsID.length];
        mEtDailyMood = findViewById(R.id.et_daily_mood);
        mTvWelcome = findViewById(R.id.tv_welcome);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            oldData = fc_dailyMood.readFile();
            playerName = fc_basicInfo.readLineSplit()[0];
            mTvWelcome.setText("哈囉~" + playerName);
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
            ActivityUtils.getInstance().exitSystem();
        }
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
                    String intent_TAG = "mood_color";
                    switch (moodSelected){
                        case 0: case 1:
                            intent.putExtra(intent_TAG,R.style.Theme_BlackDogLab_Blue);
                            break;
                        case 2:
                            intent.putExtra(intent_TAG,R.style.Theme_BlackDogLab_Green);
                            break;
                        case 3: case 4:
                            intent.putExtra(intent_TAG,R.style.Theme_BlackDogLab_Brown);
                            break;
                        default:
                            intent.putExtra(intent_TAG,R.style.Theme_BlackDogLab_Default);
                            break;
                    }
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
        stringBuffer.append(oldData)
                .append(currentDate)
                .append(fc_basicInfo.getSplitChar())
                .append(moodSelected)
                .append(fc_basicInfo.getSplitChar())
                .append(mEtDailyMood.getText().toString())
                .append('\n');
        try {
            fc_dailyMood.write(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Utils.setLog(e.getMessage());
            return false;
        }
        return true;
    }
}