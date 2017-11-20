package com.freelift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ADMIN on 23-Feb-17.
 */

public class Country_CustomAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<Holder> list;

    public Country_CustomAdapter(Context context, ArrayList<Holder> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public Object getItem(int paramInt) {
        return paramInt;
    }

    class ViewHolder {
        TextView code, name;
        RelativeLayout rl;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int paramInt) {
        return paramInt;
    }


    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        // Declare Variables


        ViewHolder holder;
        if (paramView == null) {
            paramView = inflater.inflate(R.layout.country_code, paramViewGroup, false);
            holder = new Country_CustomAdapter.ViewHolder();
            holder.code = (TextView) paramView.findViewById(R.id.txtcode);
            holder.name = (TextView) paramView.findViewById(R.id.txtname);
            holder.rl=(RelativeLayout)paramView.findViewById(R.id.rl);

            paramView.setTag(holder);
        } else {
            holder = (Country_CustomAdapter.ViewHolder) paramView.getTag();
        }
        Holder h = list.get(paramInt);
        holder.code.setText("+"+h.getCountry_code());
        holder.name.setText(h.getCountry_name());
        return paramView;
    }

}
