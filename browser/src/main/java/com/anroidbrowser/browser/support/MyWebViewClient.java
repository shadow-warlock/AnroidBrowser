package com.anroidbrowser.browser.support;

import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.dataBase.DBHelper;
import com.anroidbrowser.browser.myTypes.HistoryElement;
import com.anroidbrowser.browser.myTypes.Person;

import java.util.Calendar;

/**
 * Created by root on 09.10.17.
 */
public class MyWebViewClient extends WebViewClient {
    private Context context;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(MainApplication.getInstance().isAuthorization()){
            Person p = MainApplication.getInstance().getPerson();
            DBHelper helper = new DBHelper(context);
            int id = helper.addToHistory(Calendar.getInstance().getTime().toString(), view.getTitle(), view.getUrl(), (int)p.getId());
            p.addHistory(new HistoryElement(Calendar.getInstance().getTime().toString(), view.getTitle(), view.getUrl(), id));
        }
    }
}