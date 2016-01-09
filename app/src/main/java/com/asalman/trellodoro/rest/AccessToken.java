package com.asalman.trellodoro.rest;


import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.preferences.HelperSharedPreferences;

/**
 * Created by asalman on 1/9/16.
 */
public class AccessToken {

    private static class PreferencesKeys{
        public static final String TOKEN_VALUE = "token_value";
        public static final String USER_ID = "user_id";
        public static final String TOKEN_TYPE = "token_type";
        public static final String EXPIRES_ID = "token_expires_in";
        public static final String AUTHORIZED_SCOPES = "token_authorized_scopes";
    }


    public AccessToken(){}

    public AccessToken(String value, String userId) {
        setValue(value);
        setUserId(userId);
    }

    public AccessToken(String value, String userId, String authorizedScopes) {
        this(value, userId);
        this.setAuthorizedScopes(authorizedScopes);
    }

    public String getValue() {
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.TOKEN_VALUE, "");
    }

    public void setValue(String value) {
        HelperSharedPreferences.putString(MyApplication.getApp().getBaseContext(), PreferencesKeys.TOKEN_VALUE, value);
    }

    public String getUserId() {
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.USER_ID, "");
    }

    public void setUserId(String userID) {
        HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.USER_ID, userID);
    }

    public String getType() {
        String type = HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.USER_ID, "");
        if (type.isEmpty()) {
            type = "Bearer";
        }
        return type;
    }

    public void setType(String type) {
        HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.USER_ID, type);
    }

    public long getExpiresIn() {
        return HelperSharedPreferences.getLong(MyApplication.getApp().getBaseContext(), PreferencesKeys.USER_ID, 0);
    }

    public void setExpiresIn(long expiresIn) {
        HelperSharedPreferences.putLong(MyApplication.getApp().getBaseContext(), PreferencesKeys.USER_ID, expiresIn);
    }

    public String getAuthorizedScopes() {
        return HelperSharedPreferences.getString(MyApplication.getApp().getBaseContext(), PreferencesKeys.AUTHORIZED_SCOPES, "");
    }

    public void setAuthorizedScopes(String authorizedScopes) {
        HelperSharedPreferences.putString(MyApplication.getApp().getBaseContext(), PreferencesKeys.AUTHORIZED_SCOPES, authorizedScopes);
    }
}
