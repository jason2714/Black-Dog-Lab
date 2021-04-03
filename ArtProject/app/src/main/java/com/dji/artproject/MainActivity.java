package com.dji.artproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FileController fileController = new FileController(this, getString(R.string.file_name));

        StringBuffer stringBuffer = new StringBuffer("file$")
                .append("123$")
                .append("123$")
                .append("123$");
        fileController.write(stringBuffer.toString());
        String[] data = fileController.read();
        for (String str : data)
            Log.d("test", str);
        Intent intent = new Intent(MainActivity.this, MainPage.class);
        startActivity(intent);
    }

}