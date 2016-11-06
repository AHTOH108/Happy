package com.iandp.happy.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.iandp.happy.R;
import com.iandp.happy.adapters.SpinnerAdapter;
import com.iandp.happy.model.object.Shop;

import java.util.ArrayList;

/**
 * Date creation: 25.03.2016.
 */
public class SpinnerShops extends RelativeLayout {

    private Spinner mSpinnerShop;

    private SpinnerAdapter mAdapter;

    private ArrayList<Shop> mListShop;
    private String[] nullListSpinner = new String[]{"no items"};

    public SpinnerShops(Context context) {
        super(context);
        if (!isInEditMode())
            initControl(context);
    }

    public SpinnerShops(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode())
            initControl(context);
    }

    public SpinnerShops(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode())
            initControl(context);
    }

    private void initControl(Context context) {
        LayoutInflater li = ((Activity) getContext()).getLayoutInflater();
        li.inflate(R.layout.spinner_shop, this);

        mSpinnerShop = (Spinner) findViewById(R.id.spinnerShop);

        mAdapter = new SpinnerAdapter(context, R.layout.item_simple_spinner, nullListSpinner);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerShop.setAdapter(mAdapter);
        mListShop = new ArrayList<>();
        updateListSpinner(mListShop);
    }

    public void updateListSpinner(ArrayList<Shop> listShop) {
        if (mSpinnerShop == null || mAdapter == null) return;
        String mass[];
        if (listShop != null && listShop.size() > 0) {
            this.mListShop.clear();
            this.mListShop.addAll(listShop);
            mass = new String[listShop.size()];
            for (int i = 0; i < listShop.size(); i++)
                mass[i] = listShop.get(i).getName();
            if (mass.length > 0) {
                mAdapter.updateList(mass);
            }
        } else {
            this.mListShop.clear();
            mAdapter.updateList(nullListSpinner);
        }
    }

    public void setSelectedShop(long idShop) {
        if (mSpinnerShop == null || mAdapter == null || mListShop == null || idShop < 0)
            return;
        for (int i = 0; i < mListShop.size(); i++)
            if (mListShop.get(i).getId() == idShop) {
                if (mSpinnerShop.getCount() > i) {
                    mSpinnerShop.setSelection(i);
                }
            }

    }

    public Shop getSelectedShop() {
        int k = mSpinnerShop.getSelectedItemPosition();
        Shop shop = new Shop();
        if (k >= 0 && mListShop.size() > k)
            shop = mListShop.get(k);
        return shop;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

}
