package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        FileController fileController = new FileController(this, "Login Date");
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = df.format(Calendar.getInstance().getTime());
            if (!fileController.fileExist()){
                Log.d("test",currentDate);
                fileController.write(currentDate);
            }else{
                //TODO
                String lastDate = fileController.readLine();
                if(currentDate.equals(lastDate)){
                    Log.d("test","same date");
                }else{
                    Log.d("test","new date");
                    fileController.write(currentDate);
                }
            }
        } catch (IOException e) {
            Log.d("test", e.getCause() + "");
            e.printStackTrace();
        }
        Intent intent = new Intent(MainActivity.this, MainPage.class);
        startActivity(intent);
    }

}