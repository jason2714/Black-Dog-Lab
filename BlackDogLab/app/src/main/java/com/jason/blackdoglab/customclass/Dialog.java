package com.jason.blackdoglab.customclass;

import android.content.Context;
import android.graphics.Color;

import com.jason.blackdoglab.R;
import com.jason.blackdoglab.utils.Utils;

public class Dialog {

    private int character;
    private String text;
    private int charDrawableIndex;
    private int[] bgColor = {R.attr.dialog_player_bg_color, R.attr.dialog_npc_bg_color};
    private int[] textColor = {R.attr.dialog_player_text_color, R.attr.dialog_npc_text_color};
    private int[] nextDrawable = {R.attr.dialog_player_next, R.attr.dialog_npc_next};
    private int[] nameTextColor = {R.attr.dialog_player_name_color, R.attr.dialog_npc_name_color};
    private int[] nameBg = {R.drawable.bg_dialog_name_player, R.drawable.bg_dialog_name_npc};
    private String[] name = {"玩家", "路人甲", "菜菜子", "艾博士", "裴婆婆"};
    private int[][] charDrawable = {{}, {}, {
            R.drawable.char_tsai1, R.drawable.char_tsai2, R.drawable.char_tsai3, R.drawable.char_tsai4,
            R.drawable.char_tsai5, R.drawable.char_tsai6, R.drawable.char_tsai7}, {
            R.drawable.char_professor1, R.drawable.char_professor2, R.drawable.char_professor3,
            R.drawable.char_professor4, R.drawable.char_professor5, R.drawable.char_professor6,
            R.drawable.char_professor7, R.drawable.char_professor8, R.drawable.char_professor9}, {
            R.drawable.char_grandma1, R.drawable.char_grandma2, R.drawable.char_grandma3,
            R.drawable.char_grandma4, R.drawable.char_grandma5}, {
            R.drawable.char_friend1, R.drawable.char_friend2, R.drawable.char_friend3,
            R.drawable.char_friend4, R.drawable.char_friend5, R.drawable.char_friend6}
    };

    public Dialog(int character, String text) {
        this.character = character;
        this.text = text;
        this.charDrawableIndex = -1;
    }

    public Dialog(int character, String text, int charDrawableIndex) {
        this(character, text);
        this.charDrawableIndex = charDrawableIndex;
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
        return bgColor[Math.min(character, 1)];
    }

    public int getTextColor() {
        return textColor[Math.min(character, 1)];
    }

    public int getNextDrawable() {
        return nextDrawable[Math.min(character, 1)];
    }

    public String getName() {
        return name[character];
    }

    public int getNameTextColor(){
        return nameTextColor[Math.min(character, 1)];
    }

    public int getNameBg() {
        return nameBg[Math.min(character, 1)];
    }

    public int getCharDrawable() {
        Utils.setLog("character = " + character);
        Utils.setLog("charDrawableIndex = " + charDrawableIndex);
        if (charDrawableIndex != -1)
            return charDrawable[character][charDrawableIndex - 1];
        return charDrawableIndex;
    }
}
