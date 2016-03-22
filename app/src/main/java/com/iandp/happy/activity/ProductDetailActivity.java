package com.iandp.happy.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.fragment.DetailProductFragment;
import com.iandp.happy.fragment.DetailShopFragment;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String DATA_ID_PRODUCT = "idProduct";


    private Toolbar mToolbar;
    private DetailProductFragment detailProductFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        //setToolbar();

        long idShop = getIntent().getLongExtra(DATA_ID_PRODUCT, -1);
        replaceContentFragment(idShop);
    }

    protected void replaceContentFragment(long id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.content_frame);

        detailProductFragment = DetailProductFragment.newInstance(id);

        if ((fragmentCurrent == null) || (!fragmentCurrent.getClass().equals(detailProductFragment.getClass()))) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, detailProductFragment, detailProductFragment.getClass().getCanonicalName()).commit();
        }
    }

    public void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                if (mToolbar != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mToolbar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                finishActivity(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void finishActivity(boolean isOk) {
        if (isOk) {
            setResult(RESULT_OK);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
