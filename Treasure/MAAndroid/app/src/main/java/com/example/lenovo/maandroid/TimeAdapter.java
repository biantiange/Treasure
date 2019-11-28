package com.example.lenovo.maandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeAdapter extends BaseAdapter {
    private ArrayList<Dates> datas;
    Context context;

    public TimeAdapter(ArrayList<Dates> dates, Context context) {
        super();
        this.datas = dates;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item=null;
        if (convertView == null) {
            item = new Item();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_time, null);
            item.time =  convertView.findViewById(R.id.show_time);
            item.title =  convertView.findViewById(R.id.show_title);
            item.lineNorma = convertView.findViewById(R.id.line_normal);
            item.lineHiLight =  convertView.findViewById(R.id.line_highlight);
            item.image =  convertView.findViewById(R.id.image);
            convertView.setTag(item);

        } else {
            item = (Item) convertView.getTag();
        }


        //根据数据状态对视图做不同的操作
        if (datas.get(position).getStatu() == 1) {
           // item.lineHiLight.setVisibility(View.VISIBLE);
            item.image.setImageResource(R.drawable.check);
            item.time.setVisibility(View.VISIBLE);
        }

        item.time.setText(datas.get(position).getTime());
        item.title.setText(datas.get(position).getTitle());


        //这里在起始位置，就不显示“轴”了
        if (position == 0) {
            item.lineNorma.setVisibility(View.INVISIBLE);
            item.lineHiLight.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
    private class Item {
        TextView time, title;
        View lineNorma, lineHiLight;
        ImageView image;
    }

}

