package com.jason.blackdoglab;

import java.util.Objects;

public class DailyMoods {
    private String date;
    private int mood;
    private String diary;

    public DailyMoods(){
        this.date = "1999-06-20";
        this.mood = 0;
        this.diary = "tired";
    }

    public DailyMoods(String date, int mood,String diary){
        this.date = date;
        this.mood = mood;
        this.diary = diary;
    }

    public String getDate() {
        return date;
    }
    public String getDiary(){
        return diary;
    }

    public int getMood() {
        return mood;
    }

    @Override
    public String toString() {
        return "DailyMoods{" +
                "date='" + date + '\'' +
                ", mood=" + mood +
                ", diary='" + diary + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyMoods that = (DailyMoods) o;
//        return mood == that.mood &&
//                date.equals(that.date) &&
//                Objects.equals(diary, that.diary);
        return mood == that.mood;
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
