package com.iandp.happy.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.iandp.happy.R;

public class CategoryListActivity extends BaseActivityWithFragment {

    @Override
    protected Fragment getFragmentInContent() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_category_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
