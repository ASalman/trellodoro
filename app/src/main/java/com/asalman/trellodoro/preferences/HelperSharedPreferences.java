package com.asalman.trellodoro.preferences;

/**
 * Created by asalman on 1/9/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import org.joda.time.DateTime;

public class HelperSharedPreferences {

    private static SharedPreferences preferences;

    public static SharedPreferences getSharedPreferences(Context context){
        if (preferences == null){
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return preferences;
    }

    public static void putInt(Context context, String key, int value){
        Editor edit = getSharedPreferences(context).edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void putBoolean(Context context, String key, boolean val){
        Editor edit=getSharedPreferences(context).edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    public static void putString(Context context, String key, String val){
        Editor edit=getSharedPreferences(context).edit();
        edit.putString(key, val);
        edit.commit();
    }

    public static void putFloat(Context context, String key, float val){
        Editor edit=getSharedPreferences(context).edit();
        edit.putFloat(key, val);
        edit.commit();
    }

    public static void putLong(Context context, String key, long val){
        Editor edit=getSharedPreferences(context).edit();
        edit.putLong(key, val);
        edit.commit();
    }

    public static long getLong(Context context, String key, long _default){
        return getSharedPreferences(context).getLong(key, _default);
    }

    public static float getFloat(Context context, String key, float _default){
        return getSharedPreferences(context).getFloat(key, _default);
    }

    public static String getString(Context context, String key, String _default){
        return getSharedPreferences(context).getString(key, _default);
    }

    public static int getInt(Context context, String key, int _default){
        return getSharedPreferences(context).getInt(key, _default);
    }

    public static boolean getBoolean(Context context, String key, boolean _default){
        return getSharedPreferences(context).getBoolean(key, _default);
    }

    public static void putDateTime(Context context, String key, DateTime val){
        long value = val != null ? val.getMillis() : -1;
        putLong(context, key, value);
    }

    public static DateTime getDateTime(Context context, String key, DateTime _default){
        long value = _default != null ? _default.getMillis() : -1;
        long Millis = getLong(context, key, value);
        return Millis == -1 ? null : new DateTime(Millis);
    }
}