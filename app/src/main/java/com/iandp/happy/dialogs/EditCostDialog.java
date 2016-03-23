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
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.iandp.happy.R;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Shop;
import com.iandp.happy.model.object.Units;
import com.iandp.happy.utils.DateUtil;

import java.util.ArrayList;

/**
 * Date creation: 22.03.2016.
 */
public class EditCostDialog extends AppCompatDialogFragment {

    private static final String COST = "cost";
    private static final String TAG_PARENT_FRAGMENT = "tagParentFragment";

    private TextInputLayout mTextInputLayoutPrice;
    //private TextInputLayout textInputLayoutPriceMax;
    private TextInputLayout mTextInputLayoutVolume;
    private EditText mEditTextPrice;
    private EditText mEditTextPriceMax;
    private EditText mEditTextVolume;
    private Spinner mSpinnerUnits;
    private Spinner mSpinnerShop;
    private Button mButtonCancel;
    private Button mButtonOk;

    private Cost mCost;
    private ArrayList<Units> listUnits = new Units().getListUnits();
    private ArrayList<Shop> listShop;

    public interface OnConfirmEditCostListener {
        void onConfirmEditCostListener(Cost cost);
    }

    private OnConfirmEditCostListener mOnConfirmListener;

    public static EditCostDialog newInstance(Cost cost, String tagFragment) {
        EditCostDialog fragment = new EditCostDialog();

        Bundle args = new Bundle();
        args.putParcelable(COST, cost);
        args.putString(TAG_PARENT_FRAGMENT, tagFragment);
        fragment.setArguments(args);

        return fragment;
    }

    public String getTagFragment() {
        return getArguments().getString(TAG_PARENT_FRAGMENT);
    }

    public Cost getCost() {
        Cost cost = getArguments().getParcelable(COST);
        if (cost == null) cost = new Cost();
        return cost;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getTagFragment().length() > 0) {
            mOnConfirmListener = (OnConfirmEditCostListener) getFragmentManager().findFragmentByTag(getTagFragment());
        } else {
            mOnConfirmListener = (OnConfirmEditCostListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnConfirmListener = null;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        closeKeyboard();
        closeKeyboard();
        super.onDismiss(dialog);
        closeKeyboard();
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {

        mCost = getCost();
        listShop = new DBHelper(getContext()).getAllShop();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_cost, null);
        setupView(view);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(view);

        return adb.create();
    }

    private void setupView(View view) {
        mTextInputLayoutPrice = (TextInputLayout) view.findViewById(R.id.textInputLayoutPrice);
        //textInputLayoutPriceMax = (TextInputLayout) view.findViewById(R.id.textInputLayoutPriceMax);
        mTextInputLayoutVolume = (TextInputLayout) view.findViewById(R.id.textInputLayoutVolume);
        mEditTextPrice = (EditText) view.findViewById(R.id.editTextPrice);
        mEditTextPriceMax = (EditText) view.findViewById(R.id.editTextPriceMax);
        mEditTextVolume = (EditText) view.findViewById(R.id.editTextVolume);
        mSpinnerUnits = (Spinner) view.findViewById(R.id.spinnerUnits);
        mSpinnerShop = (Spinner) view.findViewById(R.id.spinnerShop);
        mButtonCancel = (Button) view.findViewById(R.id.buttonCancel);
        mButtonOk = (Button) view.findViewById(R.id.buttonOk);

        setupSpinnerUnitsView(mSpinnerUnits);
        setupSpinnerShopView(mSpinnerShop, listShop);

        showInfoProduct(mCost);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCostAndClose();
            }
        });
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

    private void setupSpinnerShopView(Spinner spinner, ArrayList<Shop> listShop) {

        if (listShop == null || listShop.size() <= 0) return;

        int countType = listShop.size();
        String[] data = new String[countType];
        for (int i = 0; i < countType; i++) {
            data[i] = listShop.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    private void selectItemSpinnerUnits(int idUnits) {
        if (mSpinnerUnits == null || listUnits == null) return;
        for (int i = 0; i < listUnits.size() && i < mSpinnerUnits.getCount(); i++) {
            if (listUnits.get(i).getId() == idUnits)
                mSpinnerUnits.setSelection(i);
        }
    }

    private void selectItemSpinnerShop(long idShop) {
        if (mSpinnerShop == null || listShop == null) return;
        for (int i = 0; i < listShop.size() && i < mSpinnerShop.getCount(); i++) {
            if (listShop.get(i).getId() == idShop)
                mSpinnerShop.setSelection(i);
        }
    }

    private void showInfoProduct(Cost cost) {
        if (cost == null) return;
        mEditTextPrice.setText(cost.getPrice() > 0 ? String.valueOf(cost.getPrice()) : "");
        mEditTextPriceMax.setText(cost.getPriceMax() > 0 ? String.valueOf(cost.getPriceMax()) : "");
        mEditTextVolume.setText(cost.getVolume() > 0 ? String.valueOf(cost.getVolume()) : "");
        selectItemSpinnerUnits(cost.getUnits().getId());
        selectItemSpinnerShop(cost.getShop().getId());
        openKeyboard();
        mEditTextPrice.setSelection(mEditTextPrice.getText().length());
        mEditTextPrice.requestFocus();
    }

    private void saveCostAndClose() {
        if (isCorrectData()) {
            try {
                mCost.setPrice(Double.parseDouble(mEditTextPrice.getText().toString()));
            } catch (NumberFormatException e) {
                mCost.setPrice(-1);
            }

            try {
                mCost.setPriceMax(Double.parseDouble(mEditTextPrice.getText().toString()));
            } catch (NumberFormatException e) {
                mCost.setPriceMax(-1);
            }

            try {
                mCost.setVolume(Double.parseDouble(mEditTextVolume.getText().toString()));
            } catch (NumberFormatException e) {
                mCost.setVolume(-1);
            }
            int k = mSpinnerUnits.getSelectedItemPosition();
            if (listUnits.size() > k)
                mCost.setUnits(listUnits.get(k));
            k = mSpinnerShop.getSelectedItemPosition();
            if (listShop.size() > k)
                mCost.setShop(listShop.get(k));
            mCost.setDate(DateUtil.getNowTime());

            mOnConfirmListener.onConfirmEditCostListener(mCost);
            this.dismiss();
        }
    }

    private boolean isCorrectData() {
        mTextInputLayoutPrice.setError(null);
        mTextInputLayoutVolume.setError(null);

        double price;
        double volume;

        try {
            price = Double.parseDouble(mEditTextPrice.getText().toString());
        } catch (NumberFormatException e) {
            price = -1;
        }

        try {
            volume = Double.parseDouble(mEditTextVolume.getText().toString());
        } catch (NumberFormatException e) {
            volume = -1;
        }

        View focusView = null;
        boolean error = false;

        if (volume <= 0) {
            mTextInputLayoutVolume.setError(getString(R.string.error_edit_text));
            focusView = mTextInputLayoutVolume;
            error = true;
        }

        if (price <= 0) {
            mTextInputLayoutPrice.setError(getString(R.string.error_edit_text));
            focusView = mTextInputLayoutPrice;
            error = true;
        }

        if (error && focusView != null) {
            focusView.requestFocus();
        }

        return !error;
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mButtonCancel.getWindowToken(), 0);
    }

    private void openKeyboard() {
        /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTextInputLayoutPrice.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);*/
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, 0);
    }

    private void closeDialog() {
        this.dismiss();
    }

}
