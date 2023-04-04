package com.example.myattendanceapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myattendanceapp.Model.Month;
import com.example.myattendanceapp.R;

import java.util.List;

public class MonthAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<Month> monthList;

    public MonthAdapter(Context context, int layout, List<Month> monthList) {
        this.context = context;
        this.layout = layout;
        this.monthList = monthList;
    }

    @Override
    public int getCount() {
        return monthList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    class Holder{
        TextView date;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            holder = new Holder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.date = convertView.findViewById(R.id.month);
            convertView.setTag(holder);
        }else holder = (Holder) convertView.getTag();

        Month month = monthList.get(position);
        holder.date.setText(month.getDate());
        return convertView;
    }
}
