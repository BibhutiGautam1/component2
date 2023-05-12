package com.bibhuti.todoapp.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bibhuti.todoapp.model.Priority;
import com.bibhuti.todoapp.model.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE, MMM d");
        return simpleDateFormat.format(date);
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int priorityColor(Task task) {
        int color = 0;
        if (task.getPriority() == Priority.HIGH) {
            color = Color.argb(200, 201, 21, 23);
        } else if (task.getPriority() == Priority.MEDIUM) {
            color = Color.argb(200, 155, 179, 0);
        } else if (task.getPriority() == Priority.LOW) {
            color = Color.argb(200, 51, 182, 129);
        }
        return color;
    }

    public static Calendar getNewCalendarInstance() {
        Calendar calendar = Calendar.getInstance();
        return resetCalender(calendar);
    }

    public static Date resetDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return resetCalender(calendar).getTime();
    }
    public static Calendar resetCalender(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
    public static void setTimeInCalendar(Calendar calendar, Date date) {
        calendar.setTime(resetDate(date));
    }



}
