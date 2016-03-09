package com.iandp.happy.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.Shop;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailShopFragment extends Fragment {

    private static final String ARG_ID_SHOP = "idShop";

    private DBHelper dbHelper;


    public static DetailShopFragment newInstance(int idShop) {
        Bundle args = new Bundle();

        DetailShopFragment fragment = new DetailShopFragment();
        args.putInt(ARG_ID_SHOP, idShop);
        fragment.setArguments(args);

        return fragment;
    }

    public int getIdShop() {
        return getArguments().getInt(ARG_ID_SHOP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_shop, container, false);
        dbHelper = new DBHelper(getContext());
        onCreateView(view);
        loadInstanceState(savedInstanceState);
        return view;
    }

    private void onCreateView(View view) {
        /*mEditTextSearch = (EditText) view.findViewById(R.id.editTextSearch);
        mSpinnerFilter = (Spinner) view.findViewById(R.id.spinnerFilter);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mTextEmptyList = (TextView) view.findViewById(R.id.textEmptyList);

        adapter = new RecyclerViewAdapter(getActivity(), new ArrayList<Shop>());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);

        checkListIsEmpty();*/
    }

    private void loadInstanceState(Bundle savedInstanceState) {

    }

    public void goSaveInfoShop(){

    }

}
