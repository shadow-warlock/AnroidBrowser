package com.anroidbrowser.browser.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.activities.MainActivity;
import com.anroidbrowser.browser.support.MyWebViewClient;

/**
 * Created by root on 09.10.17.
 */
public class Browser {

    private Activity context;
    private WebView webView;
    private RelativeLayout container;
    private View video;
    private ProgressBar progressBar;
    private DownloadManager downloadManager;
    private RelativeLayout mainContainer;

    public Browser(RelativeLayout root, MainActivity context){
        this.context = context;
        mainContainer = root;
        container = root.findViewById(R.id.container);
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        progressBar = root.findViewById(R.id.progressBar);
    }

    public void setTabMain(WebView web){
        if(webView != null){
            container.removeView(webView);
        }
        if(web.getParent() != null)
            ((ViewGroup)(web.getParent())).removeView(web);

        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        container.addView(web, 0, lParams);
        webView = web;
        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                mainContainer.addView(view);
                video = view;
                view.setBackgroundColor(Color.DKGRAY);

            }

            @Override
            public void onHideCustomView() {
                super.onHideCustomView();
                mainContainer.removeView(video);
            }
        });
        web.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,String contentDisposition, String mimetype,long contentLength)
            {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                if(checkPermission()) {
                    request.setDestinationInExternalPublicDir("/Download", Uri.parse(url).getLastPathSegment());
                    request.setTitle(Uri.parse(url).getLastPathSegment()); //заголовок будущей нотификации
                    request.setDescription(contentDisposition); //описание будущей нотификации
                    request.setMimeType(mimetype); //mine type загружаемого файла
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    downloadManager.enqueue(request);
                }
            }
        });
        web.setWebViewClient(new MyWebViewClient(context));
        MainApplication.getInstance().getTabs().setMain(web);
    }

    public void createTab(String url){
        WebView webView = new WebView(context);
        webView.setWebViewClient(new MyWebViewClient(context));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);
        MainApplication.getInstance().getTabs().addWeb(webView);
        webView.loadUrl(url);
        setTabMain(webView);
    }

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (context.shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("To download a file it is necessary to allow required permission");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            context.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    context.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


}
