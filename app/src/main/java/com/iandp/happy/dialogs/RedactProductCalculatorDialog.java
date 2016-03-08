package com.iandp.happy.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.iandp.happy.R;
import com.iandp.happy.model.object.Product;

/**
 * Created on 21.11.2015.
 */


// DELETE !!!
    
public class RedactProductCalculatorDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private static final String PRODUCT = "product";
    private static final String TAG_PARENT_FRAGMENT = "BirthdayDialogFragment";

    public interface OnConfirmRedactProductListener {
        void onConfirmRedactProductListener(Product product);
    }

    private OnConfirmRedactProductListener mOnConfirmListener;

    public static RedactProductCalculatorDialog newInstance(Product product, String tagFragment) {
        RedactProductCalculatorDialog fragment = new RedactProductCalculatorDialog();

        Bundle args = new Bundle();
        args.putParcelable(PRODUCT, product);
        args.putString(TAG_PARENT_FRAGMENT, tagFragment);
        fragment.setArguments(args);

        return fragment;
    }

    public String getTagFragment() {
        return getArguments().getString(TAG_PARENT_FRAGMENT);
    }

    public Product getProduct() {
        return (Product) getArguments().getParcelable(PRODUCT);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getTagFragment().length() > 0) {
            mOnConfirmListener = (OnConfirmRedactProductListener) getFragmentManager().findFragmentByTag(getTagFragment());
        } else {
            mOnConfirmListener = (OnConfirmRedactProductListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnConfirmListener = null;
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_redact_product_calculator, null);
        view.findViewById(R.id.buttonApply).setOnClickListener(this);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle("setTitle");
        adb.setView(view);

        return adb.create();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonApply:
                this.dismiss();

                break;
        }
    }
}
