package com.iandp.happy.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.iandp.happy.R;

/**
 * Date creation: 14.03.2016.
 */
public abstract class BaseActivityWithFragment extends AppCompatActivity {

    private Toolbar mToolbar;
    private Fragment fragmentContent;

    protected abstract Fragment getFragmentInContent();

    protected abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        replaceContentFragment();
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

    protected void replaceContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentCurrent = fragmentManager.findFragmentById(R.id.content_frame);

        Fragment fragmentContent = getFragmentInContent();

        if ((fragmentCurrent == null) || (!fragmentCurrent.getClass().equals(fragmentContent.getClass()))) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentContent, fragmentContent.getClass().getCanonicalName()).commit();
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

    public void finishActivity(boolean isOk) {
        if (isOk) {
            setResult(RESULT_OK);
            finish();
        } else {
            super.onBackPressed();
        }
    }

}
