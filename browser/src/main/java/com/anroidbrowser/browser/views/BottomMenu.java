package com.anroidbrowser.browser.views;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.activities.MainActivity;
import com.anroidbrowser.browser.activities.PageFindActivity;
import com.anroidbrowser.browser.dataBase.DBHelper;
import com.anroidbrowser.browser.myTypes.HistoryElement;
import com.anroidbrowser.browser.myTypes.Person;

import java.util.Calendar;

/**
 * Created by root on 09.10.17.
 */
public class BottomMenu {

    private Button backButton, pagesButton, bestButton, abbButton;

    public BottomMenu(final RelativeLayout root, final MainActivity context) {
        backButton = root.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.onBackPressed();
            }
        });

        abbButton = root.findViewById(R.id.add);
        abbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.getBrowser().createTab(context.getFoundEngineString());
            }
        });


        pagesButton = root.findViewById(R.id.all);
        pagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PageFindActivity.class);

                context.startActivityForResult(intent, 1);
            }
        });
        bestButton = root.findViewById(R.id.best);
        final WebView webView = MainApplication.getInstance().getTabs().getCurrentWeb();
        bestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainApplication.getInstance().isAuthorization()) {
                    Person p = MainApplication.getInstance().getPerson();
                    DBHelper helper = new DBHelper(context);
                    long id = helper.addToBests(Calendar.getInstance().getTime().toString(), webView.getTitle(), webView.getUrl(), p.getId());
                    p.addBest(new HistoryElement(Calendar.getInstance().getTime().toString(), webView.getTitle(), webView.getUrl(), id));
                } else {
                    Toast.makeText(context, "only for register users", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void updateWebCount() {
        pagesButton.setText(String.valueOf(MainApplication.getInstance().getTabs().webSize()));
    }
}
