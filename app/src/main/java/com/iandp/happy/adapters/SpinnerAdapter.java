package com.iandp.happy.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Date creation: 15.02.2016.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private int textViewResourceId;
    private String[] objects;
    //public static boolean flag = false;

    public SpinnerAdapter(Context context, int textViewResourceId,
                          String[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, textViewResourceId, null);

            TextView tv = (TextView) convertView;
            tv.setText(getItem(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return objects.length;
    }

    @Override
    public String getItem(int position) {
        if (objects.length > position )
        return objects[position];
        else
            return "";
    }

    public void updateList(String[] objects){
        this.objects = objects;
        notifyDataSetChanged();
    }
}
