package com.jason.blackdoglab.customclass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DailyMoods {
    private String date;
    private int mood;
    private String diary;

    public DailyMoods() {
        this.date = "1999-06-20";
        this.mood = 0;
        this.diary = "tired";
    }

    public DailyMoods(String date) {
        this.date = date;
        this.mood = 0;
        this.diary = "empty";
    }

    public DailyMoods(String date, int mood, String diary) {
        this.date = date;
        this.mood = mood;
        this.diary = diary;
    }

    public String getDate() {
        return date;
    }

    public String getDiary() {
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
        if (o == null) return false;
        else if(getClass() == o.getClass()){
            DailyMoods that = (DailyMoods) o;
            return date.equals(that.date);
        }else{
            if(o.getClass() == Date.class){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if(date.equals(sdf.format((Date)o)))
                    return true;
            }
            return false;
        }

//        return mood == that.mood &&
//                date.equals(that.date) &&
//                Objects.equals(diary, that.diary);

    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
