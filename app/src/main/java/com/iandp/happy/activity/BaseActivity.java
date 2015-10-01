package com.iandp.happy.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.iandp.happy.R;


import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public abstract class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION_LOCATION = 1;

    protected Toolbar mToolbar;
    protected ImageView mLogoTitle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResource());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mLogoTitle = (ImageView) findViewById((R.id.imgview_logo_title));

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION:

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    protected abstract int getLayoutResource();



    public void setVisibilityLogoTitle(boolean isVisible) {

        if (mLogoTitle != null) {

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(!isVisible);
            }

            if (isVisible) {
                mLogoTitle.setVisibility(View.VISIBLE);
            } else {
                mLogoTitle.setVisibility(View.GONE);
            }
        }
    }


}

