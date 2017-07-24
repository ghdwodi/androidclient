package com.hb.ex01.a0622_dbserver2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HB04-03 on 2017-06-22.
 */

public class ListAdapter extends BaseAdapter{
    Context context;
    int layout;
    ArrayList<ListVO> voList;
    LayoutInflater inflater;

    public ListAdapter() {}

    public ListAdapter(Context context, int layout, ArrayList<ListVO> voList) {
        this.context = context;
        this.layout = layout;
        this.voList = voList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(layout,parent,false);
        }
        TextView tv_idx = (TextView)convertView.findViewById(R.id.tv_idx);
        TextView tv_id = (TextView)convertView.findViewById(R.id.tv_id);
        TextView tv_pwd = (TextView)convertView.findViewById(R.id.tv_pwd);
        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_name);
        TextView tv_age = (TextView)convertView.findViewById(R.id.tv_age);
        TextView tv_addr = (TextView)convertView.findViewById(R.id.tv_addr);

        ListVO vo = voList.get(position);
        tv_idx.setText(vo.getIdx().toString().trim());
        tv_id.setText(vo.getId().toString().trim());
        tv_pwd.setText(vo.getPwd().toString().trim());
        tv_name.setText(vo.getName().toString().trim());
        tv_age.setText(vo.getAge().toString().trim());
        tv_addr.setText(vo.getAddr().toString().trim());

        return convertView;
    }

    @Override
    public int getCount() {
        return voList.size();
    }

    @Override
    public Object getItem(int position) {
        return voList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}