package com.jason.blackdoglab.customclass;

import android.content.Context;
import android.graphics.Color;

import com.jason.blackdoglab.R;

public class Dialog {

    private int character;
    private String text;
    private int[] bgColor = {R.color.white, R.color.grey1};
    private int[] textColor = {R.color.grey1, R.color.white};
    private int[] nextDrawable = {R.drawable.icon_dialog_next_player, R.drawable.icon_dialog_next_character};
    private String[] name = {"玩家", "路人甲"};

    public Dialog(int character, String text) {
        this.character = character;
        this.text = text;
    }

    public int getCharacter() {
        return character;
    }

    public void setCharacter(int character) {
        this.character = character;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBgColor() {
        return bgColor[character];
    }

    public int getTextColor(){
        return textColor[character];
    }

    public int getNextDrawable(){
        return nextDrawable[character];
    }

    public String getName(){
        return name[character];
    }
}
