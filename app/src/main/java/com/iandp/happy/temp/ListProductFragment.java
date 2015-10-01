package com.iandp.happy.temp;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.iandp.happy.R;

import java.util.ArrayList;


public class ListProductFragment extends Fragment {

    ListView lv;
    SimpleAdapter SAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        ArrayList<String> testList = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            testList.add("text_ " + i);

        SAdapter = new SimpleAdapter(getActivity().getApplicationContext(), testList);
        lv = (ListView) view.findViewById(R.id.listView);
        lv.setAdapter(SAdapter);

        return view;
    }


    static class SimpleAdapter extends BaseAdapter {

        LayoutInflater lInflater;
        ArrayList<String> listCity;

        public SimpleAdapter(Context context, ArrayList<String> listCity) {
            lInflater = LayoutInflater.from(context);
            this.listCity = listCity;
        }

        @Override
        public int getCount() {
            return listCity.size();
        }

        @Override
        public Object getItem(int i) {
            return listCity.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            view = lInflater.inflate(R.layout.item_list_product, parent, false);


            return view;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }


    }


}
