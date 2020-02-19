package com.anroidbrowser.browser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.anroidbrowser.browser.MainApplication;
import com.anroidbrowser.browser.R;
import com.anroidbrowser.browser.myTypes.Person;

/**
 * Created by root on 15.09.17.
 */
public class BestAdapter extends BaseAdapter{

    private Context ctx;
    private LayoutInflater lInflater;
    private View view;
    private Person p;

    public BestAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        p = MainApplication.getInstance().getPerson();
    }


    @Override
    public int getCount() {
        return p.getBestElementList().size();
    }

    @Override
    public Object getItem(int position) {
        return p.getBestElementList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.history_item, parent, false);
        }
        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.title)).setText(p.getBestElementList().get(position).getTitle());
        ((TextView) view.findViewById(R.id.url)).setText(p.getBestElementList().get(position).getUrl());

        return view;
    }
}
