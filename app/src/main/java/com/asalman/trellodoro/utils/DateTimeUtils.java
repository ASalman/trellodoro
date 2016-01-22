package com.asalman.trellodoro.utils;

import android.content.Context;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.pomodoro.Pomodoro;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by asalman on 1/16/16.
 */
public class DateTimeUtils {

    public static final int MINUTE_Millis = 60000;
    public static final int SECOND_Millis = 1000;

    public static String convertTimeLeftToPrettyMinutesLeft(Context context, DateTime diffTime) {
        int minutes = diffTime.getMinuteOfHour();
        if (minutes == 0) {
            return context.getString(R.string.timeleft_less_than_minute);
        } else {
            return context.getResources().getQuantityString(R.plurals.timeleft_minutes, minutes, minutes);
        }
    }

    public static String getRemainingTime(Pomodoro pomodoro) {
        long remaining = 0;
        if (pomodoro.getNextPomodoro() != null)
            remaining = pomodoro.getNextPomodoro().getMillis() - DateTime.now().getMillis();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("mm:ss");
        return dateTimeFormatter.print(remaining);
    }

    public static String getTimeFormatted(DateTime dateTime) {
        long remaining = dateTime.getMillis();
        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("mm:ss");
        return dateTimeFormatter.print(remaining);
    }

}
