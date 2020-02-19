package com.anroidbrowser.browser.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.anroidbrowser.browser.adapters.MyAdapter;
import com.anroidbrowser.browser.R;

/**
 * Created by root on 10.09.17.
 */
public class PageFindActivity extends Activity implements AdapterView.OnItemClickListener {

    private ListView view;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageFindActivity.this.onBackPressed();
            }
        });
        // находим список
        view = findViewById(R.id.lvMain);

        // создаем адаптер
        MyAdapter adapter = new MyAdapter(this);

        // присваиваем адаптер списку
        view.setAdapter(adapter);
        view.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("tab num", position);
        setResult(RESULT_OK, intent);
        finish();
    }
}
