package com.iandp.happy.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.iandp.happy.R;
import com.iandp.happy.fragment.CategoryListFragment;

public class CategoryListActivity extends BaseActivityWithFragment {

    public static final String RESULT_CATEGORY = "resultCategory";

    @Override
    protected Fragment getFragmentInContent() {
        return CategoryListFragment.newInstance();
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
