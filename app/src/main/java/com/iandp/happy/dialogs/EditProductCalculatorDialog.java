package com.iandp.happy.dialogs;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Product;
import com.iandp.happy.model.object.Shop;
import com.iandp.happy.model.object.Units;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created on 21.11.2015.
 */


public class EditProductCalculatorDialog extends AppCompatDialogFragment implements View.OnClickListener {

    private static final String PRODUCT = "product";
    private static final String TAG_PARENT_FRAGMENT = "tagParentFragment";

    private TextView textViewCategoryProduct;
    private EditText editTextBrand;
    private EditText editTextPrice;
    private EditText editTextVolume;
    private Spinner spinnerUnits;
    private Spinner spinnerShop;
    private ImageView labelErrorPrice;
    private ImageView labelErrorVolume;

    private TextInputLayout textInputLayoutBrand;
    private TextInputLayout textInputLayoutPrice;
    private TextInputLayout textInputLayoutVolume;

    private ArrayList<Units> listUnits = new Units().getListUnits();
    private Product mProduct;

    public interface OnConfirmEditProductListener {
        void onConfirmEditProductListener(Product product);
    }

    private OnConfirmEditProductListener mOnConfirmListener;

    public static EditProductCalculatorDialog newInstance(Product product, String tagFragment) {
        EditProductCalculatorDialog fragment = new EditProductCalculatorDialog();

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
        Product product = getArguments().getParcelable(PRODUCT);
        if (product == null) product = new Product();
        return product;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getTagFragment().length() > 0) {
            mOnConfirmListener = (OnConfirmEditProductListener) getFragmentManager().findFragmentByTag(getTagFragment());
        } else {
            mOnConfirmListener = (OnConfirmEditProductListener) activity;
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
        final View view = inflater.inflate(R.layout.dialog_edit_product_calculator, null);
        setupView(view);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(view);

        return adb.create();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageButtonClose:
                closeKeyboard();
                this.dismiss();
                break;
            case R.id.buttonApply:
                saveProductAndClose();
                break;
        }
    }

    private void setupView(View view) {
        view.findViewById(R.id.imageButtonClose).setOnClickListener(this);
        view.findViewById(R.id.buttonApply).setOnClickListener(this);

        textViewCategoryProduct = (TextView) view.findViewById(R.id.textViewCategoryProduct);
        editTextBrand = (EditText) view.findViewById(R.id.editTextBrand);
        editTextPrice = (EditText) view.findViewById(R.id.editTextPrice);
        editTextVolume = (EditText) view.findViewById(R.id.editTextVolume);
        spinnerUnits = (Spinner) view.findViewById(R.id.spinnerUnits);
        //textViewShop = (TextView) view.findViewById(R.id.textViewShop);
        textInputLayoutBrand = (TextInputLayout) view.findViewById(R.id.textInputLayoutBrand);
        textInputLayoutPrice = (TextInputLayout) view.findViewById(R.id.textInputLayoutPrice);
        textInputLayoutVolume = (TextInputLayout) view.findViewById(R.id.textInputLayoutVolume);
        spinnerShop= (Spinner) view.findViewById(R.id.spinnerShop);
        labelErrorPrice= (ImageView) view.findViewById(R.id.labelErrorPrice);
        labelErrorVolume= (ImageView) view.findViewById(R.id.labelErrorVolume);

        editTextPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editTextPrice.setText("");
            }
        });

        editTextVolume.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    editTextVolume.setText("");
            }
        });

        setupSpinnerUnitsView(spinnerUnits);

        showInfoProduct(getProduct());
    }

    private void setupSpinnerUnitsView(Spinner spinner) {

        if (listUnits == null || listUnits.size() <= 0) return;

        int countType = listUnits.size();
        String[] data = new String[countType];
        for (int i = 0; i < countType; i++) {
            data[i] = listUnits.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    private void saveProductAndClose() {
        if (isCorrectData()) {

            double price;
            double volume;
            String brand = editTextBrand.getText().toString();
            Units units = new Units(-1);
            int position = spinnerUnits.getSelectedItemPosition();
            if (listUnits.size() > position)
                units = listUnits.get(position);
            try {
                price = Double.parseDouble(editTextPrice.getText().toString());
                volume = Double.parseDouble(editTextVolume.getText().toString());
            } catch (NumberFormatException e) {
                price = -1;
                volume = -1;
            }
            Shop shopSelect = new Shop();

            Cost cost = new Cost(Calendar.getInstance().getTimeInMillis(), price, volume, units, shopSelect);

            mProduct.setBrand(brand);
            mProduct.setFirstCost(cost);

            closeKeyboard();
            mOnConfirmListener.onConfirmEditProductListener(mProduct);
            this.dismiss();
        }
    }

    private boolean isCorrectData() {
        textInputLayoutBrand.setError(null);
        //textInputLayoutVolume.setError(null);
        //textInputLayoutPrice.setError(null);
        labelErrorPrice.setVisibility(View.INVISIBLE);
        labelErrorVolume.setVisibility(View.INVISIBLE);

        String brand = editTextBrand.getText().toString();
        double price;
        double volume;

        try {
            price = Double.parseDouble(editTextPrice.getText().toString());
        } catch (NumberFormatException e) {
            price = -1;
        }
        try {
            volume = Double.parseDouble(editTextVolume.getText().toString());
        } catch (NumberFormatException e) {
            volume = -1;
        }

        View focusView = null;
        boolean error = false;

        if (volume <= 0) {
            //textInputLayoutVolume.setError(getString(R.string.error_edit_text));
            labelErrorVolume.setVisibility(View.VISIBLE);

            focusView = textInputLayoutVolume;
            error = true;
        }

        if (price <= 0) {
            //textInputLayoutPrice.setError(getString(R.string.error_edit_text));
            labelErrorPrice.setVisibility(View.VISIBLE);
            focusView = textInputLayoutPrice;
            error = true;
        }

        if (TextUtils.isEmpty(brand)) {
            textInputLayoutBrand.setError(getString(R.string.error_edit_text));
            focusView = textInputLayoutBrand;
            error = true;
        }

        if (error && focusView != null) {
            focusView.requestFocus();
        }

        return !error;
    }

    private void showInfoProduct(Product product) {
        if (product == null) return;

        mProduct = product;
        textViewCategoryProduct.setText(mProduct.getCategoryProduct().getName());
        editTextBrand.setText(mProduct.getBrand());
        Cost cost = mProduct.getFirstCost();
        editTextPrice.setText(String.valueOf(cost.getPrice()));
        editTextVolume.setText(String.valueOf(cost.getVolume()));
        String nameShop = cost.getShop().getName();
        if (TextUtils.isEmpty(nameShop))
            nameShop = getString(R.string.select_shop);
        //textViewShop.setText(nameShop);

        for (int i = 0; i < listUnits.size(); i++)
            if (cost.getUnits().getId() == listUnits.get(i).getId())
                spinnerUnits.setSelection(i);
        editTextBrand.requestFocus();
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewCategoryProduct.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
