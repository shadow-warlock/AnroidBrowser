package com.anroidbrowser.browser.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.adapters.DownloadAdapter;

/**
 * Created by root on 15.09.17.
 */
public class DownloadActivity extends Activity implements AdapterView.OnItemClickListener{

    private ListView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // находим список
        view = findViewById(R.id.lvMain);

        // создаем адаптер
        DownloadAdapter adapter = new DownloadAdapter(this);

        // присваиваем адаптер списку
        view.setAdapter(adapter);
        view.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(MainApplication.getInstance().getPerson().getDownloadsList().get(position).getUrl());
        intent.setDataAndType(uri, MainApplication.getInstance().getPerson().getDownloadsList().get(position).getTitle());

        startActivity(intent);
    }


}
