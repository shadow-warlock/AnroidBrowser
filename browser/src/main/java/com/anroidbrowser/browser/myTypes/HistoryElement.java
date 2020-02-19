package com.anroidbrowser.browser.myTypes;

/**
 * Created by root on 15.09.17.
 */
public class HistoryElement {
    private String title;
    private String url;
    private String data;
    private long id;

    public HistoryElement(String data, String title, String url, long id) {
        this.data = data;
        this.title = title;
        this.url = url;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
