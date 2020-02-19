package com.anroidbrowser.browser.support;


import android.content.Context;
import android.content.SharedPreferences;

import com.anroidbrowser.browser.MainApplication;

public class NotificationAppearanceSettings {
    private static final String FIND_ENGINE = "FIND_ENGINE";
    private static final String LANG = "LANG";


    private static NotificationAppearanceSettings self;

    public static NotificationAppearanceSettings GetCurrent() {
        if (self == null)
            self = new NotificationAppearanceSettings();
        return self;
    }


    public int findEngine;
    public int language;

    private SharedPreferences mySharedPreferences;

    private NotificationAppearanceSettings() {
        self = this;
        mySharedPreferences = MainApplication.getInstance().getSharedPreferences("myBrowser", Context.MODE_PRIVATE);
        language = mySharedPreferences.getInt(LANG, 0);
        findEngine = mySharedPreferences.getInt(FIND_ENGINE, 0);

    }

    private void SetSettings(String name, int value) {
        SharedPreferences.Editor edit = mySharedPreferences.edit();
        edit.putInt(name, value);
        edit.apply();
    }


    public void setFindEngine(int progress) {
        findEngine = progress;
        SetSettings(FIND_ENGINE, progress);
    }

    public void setLanguage(int progress) {
        language = progress;
        SetSettings(LANG, progress);
    }
}