package moe.akagi.chibaproject.dialog;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;

import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.activity.AddEvent;
import moe.akagi.chibaproject.datatype.Time;

/**
 * Created by yunze on 12/8/15.
 */
public class TimePickerUtil implements TimePicker.OnTimeChangedListener {

    private AddEvent activity;
    private int hour;
    private int minute;
    private TimePicker timePicker;
    AlertDialog alertDialog;

    public TimePickerUtil(AddEvent activity, int hour, int minute) {
        this.activity = activity;
        this.hour = hour;
        this.minute = minute;
    }

    public void initTime(TimePicker timePicker) {
        Calendar calendar = Calendar.getInstance();
        if (hour != -1) {
            calendar = getCalendarByInitDate();
        } else {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
    }

    public AlertDialog timePickDialog(final Time time) {
        LinearLayout timePickerLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.add_event_timepicker_dialog, null);
        timePicker = (TimePicker) timePickerLayout.findViewById(R.id.add_event_timepicker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        initTime(timePicker);
        alertDialog = new AlertDialog.Builder(activity)
                //.setTitle("设置时间")
                .setView(timePickerLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        time.setHour(hour);
                        time.setMinute(minute);
                        activity.refreshInfo();
                    }
                })
                .setNegativeButton("待定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        time.setHour(-1);
                        activity.refreshInfo();
                    }
                }).show();
        return alertDialog;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDayV, int minuteV) {
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
    }

    private Calendar getCalendarByInitDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1970, 0, 1, hour, minute);
        return calendar;
    }
}