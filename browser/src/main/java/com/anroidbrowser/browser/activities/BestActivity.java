package com.anroidbrowser.browser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.adapters.BestAdapter;

/**
 * Created by root on 15.09.17.
 */
public class BestActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        view = findViewById(R.id.lvMain);
        // создаем адаптер
        BestAdapter adapter = new BestAdapter(this);

        // присваиваем адаптер списку
        view.setAdapter(adapter);
        view.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(BestActivity.this, MainActivity.class);
        intent.putExtra("url", MainApplication.getInstance().getPerson().getHistoryElementList().get(position).getUrl());
        startActivity(intent);
    }


}
