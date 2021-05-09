package com.jason.blackdoglab.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.jason.blackdoglab.BaseActivity;
import com.jason.blackdoglab.utils.FileController;
import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.ActivityUtils;
import com.jason.blackdoglab.utils.DialogUtils;
import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;

public class FirstLoginActivity extends BaseActivity {

    private Button mBtnRegister;
    private long exitTime = 0;
    private final int[] charactersID = {R.id.img_character1, R.id.img_character2, R.id.img_character3,
            R.id.img_character4, R.id.img_character5, R.id.img_character6, R.id.img_character7};
    private final int[] characterDrawables = {R.drawable.icon_select_character1, R.drawable.icon_select_character2,
            R.drawable.icon_select_character3, R.drawable.icon_select_character4,
            R.drawable.icon_select_character5, R.drawable.icon_select_character6,
            R.drawable.icon_select_character7};
    private ImageView[] mImgCharacters;
    private EditText mEtYear, mEtMonth, mEtDate;
    private EditText mEtName;
    private int characterSelected = -1;
    private DatePicker datePicker;
    private ImageView mBgFirstLogin;

    @Override
    protected void initView() {
        mBtnRegister = findViewById(R.id.btn_register);
        mEtYear = findViewById(R.id.et_year);
        mEtMonth = findViewById(R.id.et_month);
        mEtDate = findViewById(R.id.et_date);
        mEtName = findViewById(R.id.et_name);
        mBgFirstLogin = findViewById(R.id.bg_first_login);
        mImgCharacters = new ImageView[charactersID.length];
        //init drawable
        for (int idx = 0; idx < charactersID.length; idx++) {
            mImgCharacters[idx] = findViewById(charactersID[idx]);
            setImageDrawableFit(mImgCharacters[idx], characterDrawables[idx]);
        }
        setImageDrawableFit(mBgFirstLogin, R.drawable.bg_first_login);
    }

    @Override
    protected void initListener() {
        mBtnRegister.setOnClickListener(this);
        for (ImageView character : mImgCharacters)
            character.setOnClickListener(this);
        mEtYear.setOnClickListener(this);
        mEtMonth.setOnClickListener(this);
        mEtDate.setOnClickListener(this);
    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_first_login;
    }

    @Override
    protected int setThemeColor() {
        return R.style.Theme_BlackDogLab_Default;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int newCharacterSelected = -1;
        switch (v.getId()) {
            case R.id.btn_register:
//                TODO check register success
                if (checkInformation()) {
                    Intent intent = new Intent(FirstLoginActivity.this, DailyLoginActivity.class);
                    intent.putExtra("FIRST_LOGIN",true);
                    startActivity(intent);
                } else {
                    showToast("資料填寫不齊全");
                }
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
            case R.id.et_year:
            case R.id.et_month:
            case R.id.et_date:
                LayoutInflater inflater = LayoutInflater.from(FirstLoginActivity.this);
                View layoutPicker = inflater.inflate(R.layout.view_date_picker, null);
                datePicker = layoutPicker.findViewById(R.id.dp_birth_date);
                android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(FirstLoginActivity.this)
                        .setTitle("請選生日")
                        .setView(datePicker)
                        .setPositiveButton("確認", (dialog, which) -> {
                            String month = String.valueOf(datePicker.getMonth() + 1);
                            String date = String.valueOf(datePicker.getDayOfMonth());
                            mEtYear.setText(String.valueOf(datePicker.getYear()));
                            if (datePicker.getMonth() < 9)
                                month = "0" + month;
                            mEtMonth.setText(month);
                            if (datePicker.getDayOfMonth() < 10)
                                date = "0" + date;
                            mEtDate.setText(date);
                        })
                        .setNegativeButton("取消", null)
                        .create();
                DialogUtils.showDialogExceptActionBar(alertDialog);
                break;
            default:
                break;
        }
        if (newCharacterSelected != -1 && newCharacterSelected != characterSelected) {
            if (characterSelected != -1) {
                mImgCharacters[characterSelected].setBackgroundResource(0);
                mImgCharacters[characterSelected].setPadding(0, 0, 0, 0);
            }
            mImgCharacters[newCharacterSelected].setPadding(5, 5, 5, 5);
            mImgCharacters[newCharacterSelected].setBackground(getDrawable(R.drawable.bg_select_circle));
            characterSelected = newCharacterSelected;
        }
    }

    private boolean checkInformation() {
        Utils.setLog("start DailyActivity");
        if (mEtName.getText().toString().isEmpty() ||
                mEtYear.getText().toString().isEmpty() ||
                mEtMonth.getText().toString().isEmpty() ||
                mEtDate.getText().toString().isEmpty() ||
                characterSelected == -1)
            return false;
        StringBuffer stringBuffer = new StringBuffer();

        String BirthDate = mEtYear.getText().toString() + '-' +
                mEtMonth.getText().toString() + '-' +
                mEtDate.getText().toString();
        stringBuffer.append(mEtName.getText().toString())
                .append(FileController.getWordSplitChar())
                .append(BirthDate)
                .append(FileController.getWordSplitChar())
                .append(characterSelected)
                .append(FileController.getLineSplitChar());

        try {
            fcBasicInfo.write(stringBuffer.toString());
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
    }
}