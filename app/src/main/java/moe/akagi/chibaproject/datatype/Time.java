package moe.akagi.chibaproject.datatype;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by yunze on 12/1/15.
 */
public class Time {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public Time(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        year = ca.get(Calendar.YEAR);
        month = ca.get(Calendar.MONTH);
        day = ca.get(Calendar.DAY_OF_MONTH);
        hour = ca.get(Calendar.HOUR_OF_DAY); // 24-hour
        minute = ca.get(Calendar.MINUTE);
        second = ca.get(Calendar.SECOND);
    }

    public String formatDateAndTime() {
        int monthTmp = month + 1;
        String timeTmp = formatTime();
        return "" + monthTmp + "月" + day + "日 " + timeTmp;
    }

    public String formatDate() {
        int monthTmp = month + 1;
        return "" + monthTmp + "月" + day + "日";
    }

    public String formatTime() {
        String hourTmp;
        String minuteTmp;
        if (hour < 10) {
            hourTmp = "0" + hour;
        } else {
            hourTmp = "" + hour;
        }
        if (minute < 10) {
            minuteTmp = "0" + minute;
        } else {
            minuteTmp = "" + minute;
        }
        return hourTmp + ":" + minuteTmp;
    }

    public long formatLong() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d H:m:s.SSS");
        sdf.setTimeZone(TimeZone.getDefault());
        int monthTmp = month + 1;
        String inputString = "" + year + "-" + monthTmp + "-" + day + " " + hour + ":" + minute + ":0.000";
        Date date = null;
        try {
            date = sdf.parse(inputString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
