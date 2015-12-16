package moe.akagi.chibaproject.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.util.Calendar;

import moe.akagi.chibaproject.R;
import moe.akagi.chibaproject.activity.AddEvent;
import moe.akagi.chibaproject.datatype.Time;

/**
 * Created by yunze on 12/8/15.
 */
public class DatePickerUtil implements DatePicker.OnDateChangedListener {

    private Activity activity;
    private DateDialogAdapter dateDialogAdapter;
    private int year;
    private int month;
    private int day;
    private DatePicker datePicker;
    AlertDialog alertDialog;

    public DatePickerUtil(Activity activity,DateDialogAdapter dateDialogAdapter, int year, int month, int day) {
        this.activity = activity;
        this.dateDialogAdapter = dateDialogAdapter;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void initDate(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        if (year != -1) {
            calendar = getCalendarByInitDate();
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        datePicker.init(year, month, day, this);
    }

    public AlertDialog datePickDialog(final Time time) {
        LinearLayout datePickerLayout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.add_event_datepicker_dialog, null);
        datePicker = (DatePicker) datePickerLayout.findViewById(R.id.add_event_datepicker);
        initDate(datePicker);
        alertDialog = new AlertDialog.Builder(activity)
            //.setTitle("设置日期")
            .setView(datePickerLayout)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    time.setYear(year);
                    time.setMonth(month);
                    time.setDay(day);
                    dateDialogAdapter.refreshDateInfo();
                }
            })
            .setNegativeButton("待定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    time.setYear(-1);
                    dateDialogAdapter.refreshDateInfo();
                }
            }).show();
        return alertDialog;
    }

    @Override
    public void onDateChanged(DatePicker view, int yearV, int monthOfYearV, int dayOfMonthV) {
        year = datePicker.getYear();
        month = datePicker.getMonth();
        day = datePicker.getDayOfMonth();
    }

    private Calendar getCalendarByInitDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar;
    }
}
