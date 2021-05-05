package com.jason.blackdoglab.customclass;

public class Food {
    private int drawableID;
    private String name;
    private String description;

    public Food(int drawableID, String name, String description) {
        this.drawableID = drawableID;
        this.name = name;
        this.description = description;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
