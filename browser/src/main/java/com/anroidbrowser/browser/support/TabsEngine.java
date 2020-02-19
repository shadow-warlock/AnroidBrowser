package com.anroidbrowser.browser.support;

import android.graphics.Bitmap;
import android.webkit.WebView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 05.10.17.
 */
public class TabsEngine {
    private List<WebView> tabs = new ArrayList<WebView>();
    private int tabNum;


    public WebView getCurrentWeb() {
        if (!isWebEmpty())
            return tabs.get(tabNum);
        return null;
    }

    public void addWeb(WebView view) {
        tabs.add(view);
    }

    public void setMain(WebView view) {
        tabNum = tabs.indexOf(view);
    }

    public WebView getWebById(int id) {
        return tabs.get(id);
    }

    public boolean isWebEmpty() {
        return tabs.size() == 0;
    }

    public int webSize() {
        return tabs.size();
    }

    public String[] getTitles() {
        String[] returned = new String[tabs.size()];
        for (int i = 0; i < tabs.size(); i++) {
            returned[i] = tabs.get(i).getTitle();
        }
        return returned;
    }

    public String[] getURLS() {
        String[] returned = new String[tabs.size()];
        for (int i = 0; i < tabs.size(); i++) {
            returned[i] = tabs.get(i).getUrl();
        }
        return returned;
    }

    public Bitmap[] getIcons() {
        Bitmap[] returned = new Bitmap[tabs.size()];
        for (int i = 0; i < tabs.size(); i++) {
            returned[i] = tabs.get(i).getFavicon();
        }
        return returned;
    }

    public int removeMainWeb() {
        tabs.remove(tabNum);
        if (tabNum != 0) {
            tabNum--;
        }
        return tabNum;
    }

    public int removeWeb(int index) {
        if (webSize() > 1) {
            tabs.remove(index);
            if (tabNum == index && tabNum != 0) {
                tabNum--;
            }
            return tabNum;
        }
        return -1;
    }
}
