package com.iandp.happy.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Product;
import com.iandp.happy.model.object.Shop;
import com.iandp.happy.model.object.Units;

import java.util.ArrayList;
import java.util.Calendar;

public class RedactProductCalculatorActivity extends AppCompatActivity {

    public static final String REDACT_PRODUCT = "redactProduct";


    private TextView textViewCategoryProduct;
    private EditText editTextBrand;
    private EditText editTextPrice;
    private EditText editTextVolume;
    private Spinner spinnerUnits;
    private TextView textViewShop;

    private TextInputLayout textInputLayoutBrand;
    private TextInputLayout textInputLayoutPrice;
    private TextInputLayout textInputLayoutVolume;

    private ArrayList<Units> listUnits = new Units().getListUnits();
    private Product product;
    private Shop shopSelect = new Shop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redact_product_calculator);
        setupView();

        Intent intent = getIntent();
        if (intent != null)
            product = intent.getParcelableExtra(REDACT_PRODUCT);
        else
            product = new Product();

        showInfoProduct(product);
    }

    private void setupView() {

        textViewCategoryProduct = (TextView) findViewById(R.id.textViewCategoryProduct);
        ImageButton imageButtonClose = (ImageButton) findViewById(R.id.imageButtonClose);
        editTextBrand = (EditText) findViewById(R.id.editTextBrand);
        editTextPrice = (EditText) findViewById(R.id.editTextPrice);
        editTextVolume = (EditText) findViewById(R.id.editTextVolume);
        spinnerUnits = (Spinner) findViewById(R.id.spinnerUnits);
        textViewShop = (TextView) findViewById(R.id.textViewShop);
        textInputLayoutBrand = (TextInputLayout) findViewById(R.id.textInputLayoutBrand);
        textInputLayoutPrice = (TextInputLayout) findViewById(R.id.textInputLayoutPrice);
        textInputLayoutVolume = (TextInputLayout) findViewById(R.id.textInputLayoutVolume);
        Button buttonApply = (Button) findViewById(R.id.buttonApply);

        setupSpinnerView();

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finishActivity();
            }
        });
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                saveProductAndClose();
            }
        });
    }

    private void setupSpinnerView() {

        if (listUnits == null || listUnits.size() <= 0) return;
        String[] data;
        int countType = listUnits.size();
        data = new String[countType];
        for (int i = 0; i < countType; i++) {
            data[i] = listUnits.get(i).getName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinnerUnits);
        spinner.setAdapter(adapter);
        //spinner.setPrompt(getString(R.string.types_inspection));
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (listUnits.size() > position) {
                    if (product.getCostList().size() > 0)
                        product.getCostList().get(product.getCostList().size() - 1).setUnits(listUnits.get(position));
                    else
                        product.getCostList().add(new Cost(listUnits.get(position)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(textViewCategoryProduct.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void finishActivity() {
        finish();
    }

    private void saveProductAndClose() {
        if (isCorrectData()) {

            double price;
            double volume;
            String brand = editTextBrand.getText().toString();
            Units units = new Units();
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
            Cost cost = new Cost(Calendar.getInstance().getTimeInMillis(), price, volume, units, shopSelect);

            product.setBrand(brand);
            product.setFirstCost(cost);
            Intent intent = new Intent();
            intent.putExtra(REDACT_PRODUCT, product);
            setResult(RESULT_OK, intent);
            finishActivity();
        }
    }

    private boolean isCorrectData() {
        textInputLayoutBrand.setError(null);
        textInputLayoutVolume.setError(null);
        textInputLayoutPrice.setError(null);

        String brand = editTextBrand.getText().toString();
        double price;
        double volume;

        try {
            price = Double.parseDouble(editTextPrice.getText().toString());
            volume = Double.parseDouble(editTextVolume.getText().toString());
        } catch (NumberFormatException e) {
            price = -1;
            volume = -1;
        }

        View focusView = null;
        boolean error = false;

        if (volume <= 0) {
            textInputLayoutVolume.setError(getString(R.string.error_edit_text));
            focusView = textInputLayoutVolume;
            error = true;
        }

        if (price <= 0) {
            textInputLayoutPrice.setError(getString(R.string.error_edit_text));
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
        textViewCategoryProduct.setText(product.getCategoryProduct().getName());
        editTextBrand.setText(product.getBrand());
        Cost cost = product.getFirstCost();
        editTextPrice.setText(String.valueOf(cost.getPrice()));
        editTextVolume.setText(String.valueOf(cost.getPrice()));
        String nameShop = cost.getShop().getName();
        if (TextUtils.isEmpty(nameShop))
            nameShop = getString(R.string.select_shop);
        textViewShop.setText(nameShop);
        for (int i = 0; i < listUnits.size(); i++)
            if (cost.getUnits().getId() == listUnits.get(i).getId())
                spinnerUnits.setSelection(i);
    }
}
