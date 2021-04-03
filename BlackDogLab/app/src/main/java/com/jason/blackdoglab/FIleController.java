package com.jason.blackdoglab;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

class FileController {

    private final Context context;
    private static final String encoding = "UTF-8";
    private final String file_name;
    private final String file_dir;
    private final String abs_file_name;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;

    FileController(Context context, String file_name) {
        this.context = context;
        this.file_name = file_name;
        this.file_dir = context.getFilesDir().getPath();
        this.abs_file_name = file_dir + "/" + file_name;
    }

    public String[] readFile() throws IOException {
        String[] data = null;
        FileInputStream inputStream = context.openFileInput(file_name);
        byte[] byteData = new byte[inputStream.available()];
        Log.d("test", inputStream.available() + "");
        inputStream.read(byteData);
        data = new String(byteData, encoding).split("[$]");

        inputStream.close();
        return data;
    }

    public String readLine() throws IOException {
        String data = null;
        FileInputStream inputStream = context.openFileInput(file_name);
        byte[] byteData = new byte[inputStream.available()];
        Log.d("test", "file size : " + inputStream.available());
        inputStream.read(byteData);
        data = new String(byteData, encoding);

        inputStream.close();
        return data;
    }

    public void write(String wrtData) throws IOException {
        FileOutputStream outputStream = context.openFileOutput(file_name, context.MODE_PRIVATE);
        outputStream.write(wrtData.getBytes());
        outputStream.close();
    }

    public boolean fileExist() {
        //must be absolute path
        return new File(abs_file_name).exists();
    }

}
