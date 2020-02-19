package com.anroidbrowser.browser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;

/**
 * Created by root on 11.09.17.
 */
public class MyAdapter extends BaseAdapter {

    private Context ctx;
    private LayoutInflater lInflater;
    private View view;

    public MyAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return MainApplication.getInstance().getTabs().webSize();
    }

    @Override
    public Object getItem(int position) {
        return MainApplication.getInstance().getTabs().getWebById(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }
        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.title)).setText(MainApplication.getInstance().getTabs().getTitles()[position]);
        ((TextView) view.findViewById(R.id.url)).setText(MainApplication.getInstance().getTabs().getURLS()[position]);
        ((ImageView) view.findViewById(R.id.image)).setImageBitmap(MainApplication.getInstance().getTabs().getIcons()[position]);
        Button b = ((Button) view.findViewById(R.id.delete_on_list));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = MainApplication.getInstance().getTabs().removeWeb(position);

                notifyDataSetChanged();
            }
        });
        b.setFocusable(false);


        return view;
    }
}
