package com.asalman.trellodoro.preferences;


import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.preferences.HelperSharedPreferences;

/**
 * Created by asalman on 1/9/16.
 */
public class Config {

    private static class PreferencesKeys{
        public static final String BOARD_ID = "board_id";
        public static final String TODO_LIST_ID = "todo_list_id";
        public static final String DOING_LIST_ID = "doing_list_id";
        public static final String DONE_LIST_ID = "done_list_id";
        public static final String ACTIVE_CARD_ID = "active_card_id";
        public static final String NOTIFICATION_ENABLED = "notification_enabled";
    }


    public Config(){}


    public static String getBoardID() {
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.BOARD_ID, "");
    }

    public static void setDoardID(String value) {
        HelperSharedPreferences.putString(MyApplication.getApp().getBaseContext(), PreferencesKeys.BOARD_ID, value);
    }

    public static String getTodoListID() {
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.TODO_LIST_ID, "");
    }

    public static void setTodoListID(String value) {
        HelperSharedPreferences.putString(MyApplication.getApp().getBaseContext(), PreferencesKeys.TODO_LIST_ID, value);
    }

    public static String getDoingListID() {
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.DOING_LIST_ID, "");
    }

    public static void setDoingListID(String value) {
        HelperSharedPreferences.putString(MyApplication.getApp().getBaseContext(), PreferencesKeys.DOING_LIST_ID, value);
    }

    public static String getDoneListID() {
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.DONE_LIST_ID, "");
    }

    public static void setDoneListID(String value) {
        HelperSharedPreferences.putString(MyApplication.getApp().getBaseContext(), PreferencesKeys.DONE_LIST_ID, value);
    }

    public static String getActiveCardID(){
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.ACTIVE_CARD_ID, "");
    }

    public static void setActiveCardID(String id){
        HelperSharedPreferences.putString(MyApplication.getApp().getBaseContext(), PreferencesKeys.ACTIVE_CARD_ID, id);
    }

    public static boolean isNotificationEnabled(){
        return HelperSharedPreferences.getBoolean(MyApplication.getApp().getBaseContext(), PreferencesKeys.NOTIFICATION_ENABLED, true);
    }

    public static void setNotificationEnabled(boolean enabled){
        HelperSharedPreferences.putBoolean(MyApplication.getApp().getBaseContext(), PreferencesKeys.NOTIFICATION_ENABLED, enabled);
    }
}
