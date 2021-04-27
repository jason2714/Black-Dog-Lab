package com.jason.blackdoglab;

import java.util.Calendar;
import java.util.Date;

public class Player {

    private String playerName;
    private String birthDate;
    private int characterDrawable;
    private final int[] characterDrawables = {R.drawable.icon_select_character1, R.drawable.icon_select_character2,
            R.drawable.icon_select_character3, R.drawable.icon_select_character4,
            R.drawable.icon_select_character5, R.drawable.icon_select_character6,
            R.drawable.icon_select_character7};

    public Player() {
        this.playerName = "無名氏";
        this.birthDate = "1999-06-20";
        this.characterDrawable = characterDrawables[0];
    }

    public Player(String playerName, String birthDate, int characterIdx) {
        this.playerName = playerName;
        this.birthDate = birthDate;
        this.characterDrawable = characterDrawables[characterIdx];
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public int getCharacterDrawable() {
        return this.characterDrawable;
    }
}
