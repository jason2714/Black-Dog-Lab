package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.Utils;

public class FirstLoginActivity extends BaseActivity {

    private Button mBtnRegister;
    private long exitTime = 0;
    private final int[] charactersID = {R.id.img_character1, R.id.img_character2, R.id.img_character3,
            R.id.img_character4, R.id.img_character5, R.id.img_character6, R.id.img_character7};
    private ImageView[] mImgCharacters;
    private int characterSelected = -1;

    @Override
    protected void initView() {
        mBtnRegister = findViewById(R.id.btn_register);
        mImgCharacters = new ImageView[charactersID.length];
        for (int idx = 0; idx < charactersID.length; idx++)
            mImgCharacters[idx] = findViewById(charactersID[idx]);
    }

    @Override
    protected void initListener() {
        mBtnRegister.setOnClickListener(this);
        for (ImageView character : mImgCharacters)
            character.setOnClickListener(this);
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
        ActivityUtils.getInstance().cleanActivity(this);
        ActivityUtils.getInstance().printActivity();
    }

    @Override
    public void onClick(View v) {
        int newCharacterSelected = -1;
        switch (v.getId()) {
            case R.id.btn_register:
//                TODO check register success
                Utils.setLog("start Activity");
                Intent intent = new Intent(FirstLoginActivity.this, DailyLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.img_character1:
                newCharacterSelected = 0;
                break;
            case R.id.img_character2:
                newCharacterSelected = 1;
                break;
            case R.id.img_character3:
                newCharacterSelected = 2;
                break;
            case R.id.img_character4:
                newCharacterSelected = 3;
                break;
            case R.id.img_character5:
                newCharacterSelected = 4;
                break;
            case R.id.img_character6:
                newCharacterSelected = 5;
                break;
            case R.id.img_character7:
                newCharacterSelected = 6;
                break;
            default:
                break;
        }
        if (newCharacterSelected != -1 && newCharacterSelected != characterSelected) {
            if(characterSelected != -1){
                mImgCharacters[characterSelected].setBackgroundResource(0);
                mImgCharacters[characterSelected].setPadding(0,0,0,0);
            }
            mImgCharacters[newCharacterSelected].setPadding(10,10,10,10);
            mImgCharacters[newCharacterSelected].setBackground(getDrawable(R.drawable.bg_select_circle));
            characterSelected = newCharacterSelected;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 監聽返回键，點兩次退出process
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showToast("真的要離開嗎QQ");
                exitTime = System.currentTimeMillis();
            } else {
                ActivityUtils.getInstance().exitSystem();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}