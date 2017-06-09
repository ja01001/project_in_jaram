package com.example.ja010.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ja010 on 17-06-01.
 */

public class DataAdapter  extends BaseAdapter {
    Context context;
    ArrayList<dataclass> data = new ArrayList<>();

    public DataAdapter(Context context, ArrayList<dataclass> music){
        this.context = context;
        this.data = music;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view ==null)
            view = LayoutInflater.from(context).inflate(R.layout.item,null);

        final TextView tv = (TextView)view.findViewById(R.id.tv1);
        dataclass one = data.get(i);
        tv.setText(one.getMUSIC_NAME());
        return view;
    }
    Comparator<dataclass> NAMEASC = new Comparator<dataclass>() {
        @Override
        public int compare(dataclass o1, dataclass o2) {
            return o1.getMUSIC_NAME().compareTo(o2.getMUSIC_NAME());
        }
    };
    public void setNAMEASC(){
        Collections.sort(data,NAMEASC);
        this.notifyDataSetChanged();
    }
}
