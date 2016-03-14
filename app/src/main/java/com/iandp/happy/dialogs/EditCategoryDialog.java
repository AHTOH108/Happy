package com.iandp.happy.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.iandp.happy.R;
import com.iandp.happy.model.object.CategoryProduct;

/**
 * Date creation: 14.03.2016.
 */
public class EditCategoryDialog extends AppCompatDialogFragment {

    private static final String CATEGORY_PRODUCT = "categoryProduct";
    private static final String TAG_PARENT_FRAGMENT = "BirthdayDialogFragment";

    private TextInputLayout textInputLayoutCategory;
    private EditText editTextCategory;
    private CategoryProduct mCategoryProduct;

    public interface OnConfirmEditCategoryListener {
        void onConfirmEditCategoryListener(CategoryProduct categoryProduct);
    }

    private OnConfirmEditCategoryListener mOnConfirmListener;

    public static EditCategoryDialog newInstance(CategoryProduct categoryProduct, String tagFragment) {
        EditCategoryDialog fragment = new EditCategoryDialog();

        Bundle args = new Bundle();
        args.putParcelable(CATEGORY_PRODUCT, categoryProduct);
        args.putString(TAG_PARENT_FRAGMENT, tagFragment);
        fragment.setArguments(args);

        return fragment;
    }

    public String getTagFragment() {
        return getArguments().getString(TAG_PARENT_FRAGMENT);
    }

    public CategoryProduct getCategory() {
        CategoryProduct object = getArguments().getParcelable(CATEGORY_PRODUCT);
        if (object == null) object = new CategoryProduct();
        return object;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getTagFragment().length() > 0) {
            mOnConfirmListener = (OnConfirmEditCategoryListener) getFragmentManager().findFragmentByTag(getTagFragment());
        } else {
            mOnConfirmListener = (OnConfirmEditCategoryListener) activity;
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
        mCategoryProduct = getCategory();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_category, null);
        setupView(view);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(view)
                .setPositiveButton(getString(R.string.apply), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveAndClose();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeDialog();
                    }
                });

        return adb.create();
    }

    private void setupView(View view) {
        textInputLayoutCategory = (TextInputLayout) view.findViewById(R.id.textInputLayoutCategory);
        editTextCategory = (EditText) view.findViewById(R.id.editTextCategory);

        showNameCategory(mCategoryProduct.getName());
    }

    private void showNameCategory(String name) {
        if (name == null) name = "";
        editTextCategory.setText(name);

    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textInputLayoutCategory.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void saveAndClose() {
        textInputLayoutCategory.setError(null);

        String name = editTextCategory.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            if (mCategoryProduct == null)
                mCategoryProduct = new CategoryProduct();

            mCategoryProduct.setName(name);
            closeKeyboard();
            mOnConfirmListener.onConfirmEditCategoryListener(mCategoryProduct);
            this.dismiss();
        } else {
            textInputLayoutCategory.setError(getString(R.string.error_edit_text));
            textInputLayoutCategory.requestFocus();
        }
    }

    private void closeDialog() {
        this.dismiss();
    }
}
