package com.asalman.trellodoro.rest;


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
}
