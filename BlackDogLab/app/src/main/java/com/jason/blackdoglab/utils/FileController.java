package com.jason.blackdoglab.utils;

import android.content.Context;
import android.util.Log;

import com.jason.blackdoglab.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileController {

    private final Context context;
    private static final String encoding = "UTF-8";
    private final String file_name;
    private final String file_dir;
    private final String abs_file_name;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;
    private final static String WORD_SPLIT_REGEX = "\\$";
    private final static String LINE_SPLIT_REGEX = "\\$\n";
    private final static Character WORD_SPLIT_CHAR = '$';
    private final static String LINE_SPLIT_CHAR = "$\n";
    private int lineIdx;

    public FileController(Context context, String file_name) {
        this.context = context;
        this.file_name = file_name;
        this.file_dir = context.getFilesDir().getPath();
        this.abs_file_name = file_dir + "/" + file_name;
        this.lineIdx = 0;
    }

    public String readFile() throws IOException {
        String data = null;
        inputStream = context.openFileInput(file_name);
        byte[] byteData = new byte[inputStream.available()];
//        Utils.setLog("file size : " + inputStream.available());
        inputStream.read(byteData);
        data = new String(byteData, encoding);

        inputStream.close();
        return data;
    }

    public String[] readFileSplit() throws IOException {
        return readFile().split(LINE_SPLIT_REGEX);
    }

    public String readLine() throws IOException {
        String[] fileData = readFile().split(LINE_SPLIT_REGEX);
        String lineData = null;
        if (this.lineIdx < fileData.length)
            lineData =  fileData[lineIdx];
        else{
            this.lineIdx = -1;
            Utils.setLog("End Of File");
        }
        this.lineIdx++;
        return lineData;
    }

    public String readLine(int lineIdx) throws IOException {
        String[] fileData = readFile().split(LINE_SPLIT_REGEX);
        String lineData = null;
        if (lineIdx < fileData.length)
            lineData =  fileData[lineIdx];
        else
            Utils.setLog("Line Index Error");
        return lineData;
    }

    public String[] readLineSplit() throws IOException {
        return readLine().split(WORD_SPLIT_REGEX);
    }

    public void write(String wrtData) throws IOException {
        outputStream = context.openFileOutput(file_name, Context.MODE_PRIVATE);
        outputStream.write(wrtData.getBytes());
        outputStream.close();
    }

    public void append(String wrtData) throws IOException {
        String fileData = readFile();
        wrtData = fileData + wrtData;
        outputStream = context.openFileOutput(file_name, Context.MODE_PRIVATE);
        outputStream.write(wrtData.getBytes());
        outputStream.close();
    }

    public boolean fileExist() {
        //must be absolute path
        return new File(abs_file_name).exists();
    }

    public static Character getWordSplitChar() {
        return WORD_SPLIT_CHAR;
    }

    public static String getLineSplitChar() {
        return LINE_SPLIT_CHAR;
    }

    public static String getWordSplitRegex() {
        return WORD_SPLIT_REGEX;
    }

    public static String getLineSplitRegex() {
        return LINE_SPLIT_REGEX;
    }

}
