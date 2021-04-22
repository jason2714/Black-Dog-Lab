package com.jason.blackdoglab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        GifImageView mGifBgDialogCry = findViewById(R.id.gif_bg_dialog_cry);
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.drawable.bg_dialog_cry);
            mGifBgDialogCry.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}