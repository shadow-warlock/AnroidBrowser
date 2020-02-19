package com.anroidbrowser.browser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.anroidbrowser.browser.adapters.HistoryAdapter;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;

/**
 * Created by root on 15.09.17.
 */
public class HistoryActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // находим список
        view = findViewById(R.id.lvMain);

        // создаем адаптер
        HistoryAdapter adapter = new HistoryAdapter(this);

        // присваиваем адаптер списку
        view.setAdapter(adapter);
        view.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        intent.putExtra("url", MainApplication.getInstance().getPerson().getHistoryElementList().get(position).getUrl());
        startActivity(intent);
    }


}
