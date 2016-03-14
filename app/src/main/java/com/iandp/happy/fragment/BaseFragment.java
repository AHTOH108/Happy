package com.iandp.happy.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Date creation: 14.03.2016.
 */
public abstract class BaseFragment extends Fragment {

    protected View view;

    protected abstract int getLayoutId();

    protected abstract void setupView(View view);

    protected abstract void loadInstanceState(Bundle savedInstanceState);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        setupView(view);
        loadInstanceState(savedInstanceState);
        return view;
    }

    protected void showSnackBarMessage(String textMessage) {
        if (getActivity() == null) return;
        /**
         * view можно заменить на getView();
         */
        if (view == null)return;
        Snackbar snackbar = Snackbar
                .make(view, textMessage, Snackbar.LENGTH_LONG)
                .setAction("OK", null); // чтобы показвалась кнопка ОК нужен обработчик!!!
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        snackbar.show();
    }
}
