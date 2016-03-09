package com.iandp.happy.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.iandp.happy.R;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.Shop;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailShopFragment extends Fragment {

    private static final String ARG_ID_SHOP = "idShop";

    private EditText mEditTextName;
    private ImageView mImageViewLogo;
    private ImageButton mImageButton;
    private EditText mEditTextAddress;

    private DBHelper dbHelper;

    private Shop mShop = null;


    public static DetailShopFragment newInstance(int idShop) {
        Bundle args = new Bundle();

        DetailShopFragment fragment = new DetailShopFragment();
        args.putInt(ARG_ID_SHOP, idShop);
        fragment.setArguments(args);

        return fragment;
    }

    public int getIdShop() {
        return getArguments().getInt(ARG_ID_SHOP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_shop, container, false);
        dbHelper = new DBHelper(getContext());
        onCreateView(view);
        loadInstanceState(savedInstanceState);
        return view;
    }

    private void onCreateView(View view) {
        mEditTextName = (EditText) view.findViewById(R.id.editTextName);
        mImageViewLogo = (ImageView) view.findViewById(R.id.imageViewLogo);
        mImageButton = (ImageButton) view.findViewById(R.id.imageButton);
        mEditTextAddress = (EditText) view.findViewById(R.id.editTextAddress);
    }

    private void loadInstanceState(Bundle savedInstanceState) {
        int idShop = getIdShop();
        if (idShop > 0)
            mShop = dbHelper.getShop(getIdShop());
        else {
            mShop = new Shop();
        }
        showInfoAboutShop(mShop);
    }

    private void showInfoAboutShop(Shop shop) {
        if (shop != null) {
            mEditTextName.setText(shop.getName());
            mEditTextAddress.setText(shop.getAddress());
        }
    }

    public void finishFragment(boolean isOk) {
        if (isOk)
            getActivity().finish();
    }

    public void goSaveInfoShop() {
        if (mShop == null) return;
        String name = mEditTextName.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            mShop.setAddress(mEditTextAddress.getText().toString());
            mShop.setName(name);
            dbHelper.addShop(mShop);
            Toast.makeText(getActivity(), "Save successful!", Toast.LENGTH_SHORT).show();
            finishFragment(true);
        } else {
            // TODO: доработать обработку ошибок и поведение приложения при этом
            mEditTextName.requestFocus();
        }

    }


}
