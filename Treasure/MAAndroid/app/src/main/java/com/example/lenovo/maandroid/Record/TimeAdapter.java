package com.example.lenovo.maandroid.Record;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.maandroid.Entity.Dates;
import com.example.lenovo.maandroid.R;

import java.util.ArrayList;

public class TimeAdapter extends BaseAdapter {
    private ArrayList<Dates> datas;
    Context context;
    int curposition;

    public TimeAdapter(ArrayList<Dates> dates, Context context,int position) {
        super();
        this.datas = dates;
        this.context = context;
        this.curposition=position;
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
    public View getView( final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_time, null);
      final   TextView time =  convertView.findViewById(R.id.show_time);
         View lineNorma = convertView.findViewById(R.id.line_normal);
        View lineHiLight =  convertView.findViewById(R.id.line_highlight);
      final  ImageView image =  convertView.findViewById(R.id.image);
        time.setText(datas.get(position).getTime());

        //这里在起始位置，就不显示“轴”了
        if (position == 0) {
            lineNorma.setVisibility(View.INVISIBLE);
            lineHiLight.setVisibility(View.INVISIBLE);
        }
        if(position==curposition){
            image.setImageResource(R.drawable.check);
        }else{
            image.setImageResource(R.drawable.circle);
        }

//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datas.get(position).setStatu(1);
//                image.setImageResource(R.drawable.check);
//                Log.e("点击时间",time.getText().toString()) ;
//              //\  notifyDataSetChanged();
//            }
//        });
        if (datas.get(position).getStatu() == 1) {
            Log.e("Statu","Statu");
            image.setImageResource(R.drawable.check);
            time.setVisibility(View.VISIBLE);
        }
        return convertView;


    }

}

