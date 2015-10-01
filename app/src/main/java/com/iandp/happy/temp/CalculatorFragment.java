package com.iandp.happy.temp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.iandp.happy.R;


public class CalculatorFragment extends Fragment {


    EditText editTextVolumeProduct1;
    EditText editTextPrice1;
    TextView textViewResult1;
    EditText editTextVolumeProduct;
    EditText editTextPrice;
    TextView textViewResult;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        editTextVolumeProduct1 = (EditText) view.findViewById(R.id.editTextVolumeProduct1);
        editTextPrice1 = (EditText) view.findViewById(R.id.editTextPrice1);
        textViewResult1 = (TextView) view.findViewById(R.id.textViewResult1);
        editTextVolumeProduct = (EditText) view.findViewById(R.id.editTextVolumeProduct);
        editTextPrice = (EditText) view.findViewById(R.id.editTextPrice);
        textViewResult = (TextView) view.findViewById(R.id.textViewResult);

        return view;
    }


}
