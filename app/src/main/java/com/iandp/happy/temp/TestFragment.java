package com.iandp.happy.temp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iandp.happy.R;
import com.iandp.happy.views.SpinnerShops;
import com.iandp.happy.model.dataBase.DBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends Fragment {

    private SpinnerShops spinnerShops;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        DBHelper dbHelper = new DBHelper(getActivity());
        spinnerShops = (SpinnerShops) view.findViewById(R.id.mySpinnerShop);
        spinnerShops.updateListSpinner(dbHelper.getAllShop());

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), spinnerShops.getSelectedShop().getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }


}
