package com.iandp.happy.activity;

import android.content.Context;
import android.content.Intent;
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
import com.iandp.happy.fragment.DetailShopFragment;

public class ShopDetailActivity extends AppCompatActivity {

    public static final String DATA_ID_SHOP = "idShop";

    private Toolbar mToolbar;
    private DetailShopFragment detailShopFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        setToolbar();

        long idShop = getIntent().getLongExtra(DATA_ID_SHOP, -1);
        replaceContentFragment(idShop);

    }

    protected void replaceContentFragment(long id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.content_frame);

        detailShopFragment = DetailShopFragment.newInstance(id);

        if ((fragmentCurrent == null) || (!fragmentCurrent.getClass().equals(detailShopFragment.getClass()))) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, detailShopFragment, detailShopFragment.getClass().getCanonicalName()).commit();
        }
    }

    private void setToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ((TextView)mToolbar.findViewById(R.id.textViewTitle)).setText(getString(R.string.title_shop));
        mToolbar.findViewById(R.id.textViewSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailShopFragment != null){
                    detailShopFragment.goSaveInfoShop();
                }
            }
        });

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

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mToolbar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finishActivity(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void finishActivity(boolean isOk){
        if (isOk){
            setResult(RESULT_OK);
            finish();
        }else{
            super.onBackPressed();
        }
    }
}
