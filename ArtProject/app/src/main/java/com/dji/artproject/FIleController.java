package com.dji.artproject;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class FileController {

    private Context context;
    private static final String encoding = "UTF-8";
    private final String file_name;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;

    FileController(Context context,String file_name) {
        this.context = context;
        this.file_name = file_name;
    }

    public String[] read() {
        String[] data = null;
        try {
            FileInputStream inputStream = context.openFileInput(file_name);
            byte[] byteData = new byte[inputStream.available()];
            Log.d("test", inputStream.available() + "");
            inputStream.read(byteData);
            data = new String(byteData, encoding).split("[$]");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void write(String wrtData){
        try {
            FileOutputStream outputStream = context.openFileOutput(file_name, context.MODE_PRIVATE);
            outputStream.write(wrtData.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
