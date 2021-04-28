package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.jason.blackdoglab.utils.Utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends BaseActivity {

    private VideoView mVvBeginAnimation;

    @Override
    protected void initView() {
        mVvBeginAnimation = findViewById(R.id.vv_begin_animation);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        String testStr = "a$b$c$\nd$e$f$\nhi\njason$\nlin\n";
//        for(String str : testStr.split("\\$\n")){
//            Utils.setLog(str);
//        }


//        FileController fileController = new FileController(this, getString(R.string.file_name));
//
//        StringBuffer stringBuffer = new StringBuffer("file$")
//                .append("123$")
//                .append("123$")
//                .append("123$");
//        fileController.write(stringBuffer.toString());
//        String[] data = fileController.read();
//        for (String str : data)
//            Log.d("test", str);
        Intent intent;
        Uri animationUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_begin_animation);
        mVvBeginAnimation.setVideoURI(animationUri);
        mVvBeginAnimation.setOnPreparedListener(mp -> {
//                mVvBeginAnimation.start();
        });
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = df.format(Calendar.getInstance().getTime());
            if (!fc_loginDate.fileExist()) {
                Log.d("test", currentDate);
                fc_loginDate.write(currentDate);
                intent = new Intent(MainActivity.this, FirstLoginActivity.class);
            } else {
                //TODO
                String lastDate = fc_loginDate.readFile();
                if (currentDate.equals(lastDate)) {
                    Log.d("test", "same date");
                    intent = new Intent(MainActivity.this, MainPage.class);
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
//        intent = new Intent(MainActivity.this, FirstLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }

    @Override
    protected void initBasicInfo(){
        fc_loginDate = new FileController(this, getResources().getString(R.string.login_date));
    }
}