package com.anroidbrowser.browser;

import android.app.Application;
import android.graphics.Bitmap;
import android.webkit.WebView;
import com.anroidbrowser.browser.myTypes.Person;
import com.anroidbrowser.browser.support.TabsEngine;

import java.util.ArrayList;
import java.util.List;


public class MainApplication extends Application {

    static MainApplication self;
    private TabsEngine tabs;
    private Person person;

    public static MainApplication getInstance() {
        return self;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        tabs = new TabsEngine();
    }

    public Person getPerson() {
        return person;
    }

    public void authorization(Person p){
        person = p;
    }
    public void exit(){
        person = null;
    }

    public boolean isAuthorization() {
        return person != null;
    }

    public TabsEngine getTabs() {
        return tabs;
    }
}