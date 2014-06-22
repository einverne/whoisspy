package com.einverne.whoisspy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.einverne.whoisspy.R;

import java.util.ArrayList;

/**
 * Created by EinVerne on 2014/6/20.
 */
public class PersonAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<String> List;

    public PersonAdapter(Context context,ArrayList<String> list) {
        mContext = context;
        List = list;
    }

    @Override
    public int getCount() {
        return List.size();
    }

    @Override
    public Object getItem(int i) {
        return List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
        View v;
        v = inflater.inflate(R.layout.person,viewGroup,false);
        TextView tvpersonname = (TextView)v.findViewById(R.id.person_name);
        tvpersonname.setText(List.get(i));
        return v;
    }
}
