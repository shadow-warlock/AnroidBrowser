package com.anroidbrowser.browser.activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.*;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.*;
import android.widget.*;
import com.anroidbrowser.browser.*;
import com.anroidbrowser.browser.dataBase.DBHelper;
import com.anroidbrowser.browser.myTypes.HistoryElement;
import com.anroidbrowser.browser.myTypes.Person;
import com.anroidbrowser.browser.support.DownloadBroadcast;
import com.anroidbrowser.browser.support.NotificationAppearanceSettings;
import com.anroidbrowser.browser.views.BottomMenu;
import com.anroidbrowser.browser.views.Browser;
import com.anroidbrowser.browser.views.TopMenu;

import java.util.Locale;

public class MainActivity extends Activity {

    private TopMenu topMenu;
    private RelativeLayout mainContainer;
    private int findEngineNum;
    private int numOfLang;
    private boolean isBackApp;
    private BottomMenu bottomMenu;
    private Browser browser;
    private DownloadBroadcast broadcast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findEngineNum = NotificationAppearanceSettings.GetCurrent().findEngine;
        numOfLang = NotificationAppearanceSettings.GetCurrent().language;
        setContentView(R.layout.activity_main);
        mainContainer = findViewById(R.id.mainContainer);
        browser = new Browser(mainContainer, this);
        bottomMenu = new BottomMenu(mainContainer, this);
        topMenu = new TopMenu(mainContainer, this);

        Intent intent = getIntent();
        String str = intent.getStringExtra("url");
        if(intent.getData() != null){
            isBackApp = true;
            browser.createTab(intent.getData().toString());
        }
        else if(str != null){
            browser.createTab(str);
        }
        else{

            if(MainApplication.getInstance().getTabs().isWebEmpty()){
                browser.createTab(getFoundEngineString());
            }
            else {
                browser.setTabMain(MainApplication.getInstance().getTabs().getCurrentWeb());
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocale();
        broadcast = new DownloadBroadcast(this);
        registerReceiver(broadcast, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcast);
    }

    @Override
    public void onBackPressed() {
        if(isBackApp)
            super.onBackPressed();
        if(MainApplication.getInstance().getTabs().getCurrentWeb().canGoBack()) {
            MainApplication.getInstance().getTabs().getCurrentWeb().goBack();

        }else if(MainApplication.getInstance().getTabs().webSize() >1){
            int id = MainApplication.getInstance().getTabs().removeMainWeb();
            browser.setTabMain(MainApplication.getInstance().getTabs().getWebById(id));
        } else {
            super.onBackPressed();
        }
    }


    public String getFoundEngineString(){
        switch (findEngineNum){
            case 0:
                return "http://google.ru/";
            case 1:
                return "http://yandex.ru/";
            case 2:
                return "http://mail.ru/";
        }
        return "";
    }

    public String getFoundEngineAnswerString(){
        switch (findEngineNum){
            case 0:
                return "http://google.ru/search?q=";
            case 1:
                return "http://yandex.ru/search?text=";
            case 2:
                return "http://go.mail.ru/search?q=";
        }
        return "";
    }

    private void setLocale(){
        String locale = numOfLang == 0 ? "en" : "ru";
        Resources res = this.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(locale.toLowerCase());
        res.updateConfiguration(conf, dm);
        topMenu.setLocale();
        invalidateOptionsMenu();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        if(requestCode == 1){
            int buf = data.getIntExtra("tab num", -1);
            if(buf != -1)
                browser.setTabMain(MainApplication.getInstance().getTabs().getWebById(buf));
            bottomMenu.updateWebCount();
        }

    }

    public Browser getBrowser() {
        return browser;
    }
}
