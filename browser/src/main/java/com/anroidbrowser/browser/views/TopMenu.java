package com.anroidbrowser.browser.views;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.activities.InputActivity;
import com.anroidbrowser.browser.activities.MainActivity;
import com.anroidbrowser.browser.activities.PersonActivity;

/**
 * Created by root on 09.10.17.
 */
public class TopMenu {
    private Button searchButton, profileButton;
    private EditText textView;

    public TopMenu(View root, final MainActivity context){
        searchButton = root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = textView.getText().toString();
                if(android.util.Patterns.WEB_URL.matcher(str).matches() ){
                    if(!str.contains("http://") && !str.contains("https://") && !str.contains("ftp://"))
                        str = "http://"+str;
                    System.out.println(str);
                    MainApplication.getInstance().getTabs().getCurrentWeb().loadUrl(str);
                }
                else
                    MainApplication.getInstance().getTabs().getCurrentWeb().loadUrl(context.getFoundEngineAnswerString() + str);
            }
        });
        profileButton = root.findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainApplication.getInstance().isAuthorization()){
                    Intent intent = new Intent(context, InputActivity.class);
                    context.startActivity(intent);
                }
                else{
                    Intent intent = new Intent(context, PersonActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        textView = root.findViewById(R.id.searchTextView);
        textView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                  if(event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                      searchButton.performClick();
                      return true;
                  }
                  return false;
            }
        });
    }

    public void setLocale(){
        textView.setHint(R.string.hint_search_bar);
    }
}
