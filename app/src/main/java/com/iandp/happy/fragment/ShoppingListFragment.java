package com.iandp.happy.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.model.api.ProductApi;
import com.iandp.happy.model.object.ProductSimple;
import com.iandp.happy.utils.AmountParser;
import com.iandp.happy.utils.GuiUtil;
import com.iandp.happy.views.ButtonTypeAmount;

import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListFragment extends BaseFragment {

    private ButtonTypeAmount mButtonTypeAmount;
    private RecyclerView mRecyclerView;
    private EditText mEditTextPrice;
    private EditText mEditTextAmount;
    private EditText mEditTextName;
    private Button mButtonAdd;
    private Button mButtonCancel;
    private TextView mTextViewSum;

    private RecyclerViewAdapter mAdapter;

    EditText.OnEditorActionListener onEditorActionListenerGoPrice = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((actionId == EditorInfo.IME_ACTION_NEXT)) {
                mEditTextAmount.requestFocus();
            }
            return false;
        }
    };

    EditText.OnEditorActionListener onEditorActionListenerGoAmount = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((actionId == EditorInfo.IME_ACTION_NEXT)) {
                mEditTextName.requestFocus();
            }
            return false;
        }
    };

    EditText.OnEditorActionListener onEditorActionListenerGoName = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((actionId == EditorInfo.IME_ACTION_NEXT)) {
                onAddProduct();
            }
            return false;
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_shopping_list;
    }

    @Override
    protected void setupView(View view) {
        mButtonTypeAmount = (ButtonTypeAmount) view.findViewById(R.id.buttonTypeAmount);
        mEditTextPrice = (EditText) view.findViewById(R.id.editTextPrice);
        mEditTextAmount = (EditText) view.findViewById(R.id.editTextAmount);
        mEditTextName = (EditText) view.findViewById(R.id.editTextName);
        mButtonAdd = (Button) view.findViewById(R.id.buttonAdd);
        mButtonCancel = (Button) view.findViewById(R.id.buttonCancel);
        mTextViewSum = (TextView) view.findViewById(R.id.textViewSum);

        mButtonTypeAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonTypeAmount.nextTypeAmount();
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mAdapter = new RecyclerViewAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearViewField();
            }
        });

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddProduct();
            }
        });
        mEditTextPrice.setOnEditorActionListener(onEditorActionListenerGoPrice);
        mEditTextAmount.setOnEditorActionListener(onEditorActionListenerGoAmount);
        mEditTextName.setOnEditorActionListener(onEditorActionListenerGoName);
        clearViewField();
    }

    @Override
    public void onStart() {
        super.onStart();
       // GuiUtil.openKeyboard(getActivity());
    }

    @Override
    protected void loadInstanceState(Bundle savedInstanceState) {
        onListProduct();
    }

    private void onListProduct() {
        mAdapter.updateList(new ProductApi(getActivity()).getListProductSimple(-1, -1));
        updateSum();
    }

    private void onAddProduct() {
        ProductSimple product = collectProduct();
        if (product == null) {
            Snackbar.make(view, getString(R.string.error_data_field), Snackbar.LENGTH_LONG).show();
        } else {
            new ProductApi(getActivity()).addProductSimple(product);
            clearViewField();
        }
        onListProduct();
    }

    private void onRemoveProduct(long id) {
        new ProductApi(getActivity()).removeProductSimple(id);
        onListProduct();
    }

    private void updateSum(){
        mTextViewSum.setText(String.format(Locale.ENGLISH, "%.2f", mAdapter.getSummList()));
    }

    private ProductSimple collectProduct() {

        double price = -1;
        double amount = -1;
        try {
            price = Double.valueOf(mEditTextPrice.getText().toString());
            amount = Double.valueOf(mEditTextAmount.getText().toString());

        } catch (NumberFormatException e) {
            System.err.println("Format string error!");
        }
        if (price <= 0 || amount <= 0)
            return null;

        ProductSimple product = new ProductSimple();
        product.setName(mEditTextName.getText().toString());
        product.setTypeAmount(mButtonTypeAmount.getTypeAmount());
        product.setPrice(price);
        product.setAmount(amount);

        return product;
    }

    private void clearViewField() {
        mEditTextName.setText("");
        mEditTextPrice.setText("");
        mEditTextAmount.setText("");
        mEditTextPrice.requestFocus();
    }



    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * *****************************************
     * *****************Adapter*****************
     * *****************************************
     */

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private static final int TYPE_NULL = 0;
        private static final int TYPE_ITEM = 1;

        private LayoutInflater lInflater;

        private ArrayList<ProductSimple> listItems;

        RecyclerViewAdapter(Context context) {
            lInflater = LayoutInflater.from(context);
            listItems = new ArrayList<>();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(lInflater.inflate(R.layout.item_list_shopping, parent, false));

        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            int type = getItemViewType(position);
            final ProductSimple product = getItem(position);
            switch (type) {
                case TYPE_NULL:

                    break;
                case TYPE_ITEM:
                    viewHolder.imageButtonRemove.setTag(product.getId());
                    viewHolder.imageButtonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long id = (long) v.getTag();
                            onRemoveProduct(id);
                        }
                    });

                    viewHolder.textViewName.setText(product.getName());
                    viewHolder.textViewPrice.setText(String.format(Locale.ENGLISH, "%.2f", product.getPrice() * product.getAmount()));
                    viewHolder.textViewAmount.setText(getString(R.string.amount_in_shopping,
                            product.getAmount(),
                            AmountParser.getShortNameType(product.getTypeAmount())));

                    viewHolder.textViewPriceForOne.setText(getString(R.string.price_for_one_product,
                            product.getPrice(),
                            AmountParser.getShortNameType(product.getTypeAmount())));
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (listItems.size() > position)
                return TYPE_ITEM;
            else
                return TYPE_NULL;
        }

        ProductSimple getItem(int position) {
            if (listItems.size() > position)
                return listItems.get(position);
            else
                return new ProductSimple();
        }

        void updateList(ArrayList<ProductSimple> listItems) {
            this.listItems.clear();
            this.listItems.addAll(listItems);
            notifyDataSetChanged();
        }

        double getSummList() {
            double sum = 0;
            for (ProductSimple product : listItems) {
                sum += product.getPrice() * product.getAmount();
            }
            return sum;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View view;
            TextView textViewName;
            ImageButton imageButtonRemove;
            TextView textViewPrice;
            TextView textViewAmount;
            TextView textViewPriceForOne;

            ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                textViewName = (TextView) view.findViewById(R.id.textViewName);
                imageButtonRemove = (ImageButton) view.findViewById(R.id.imageButtonRemove);
                textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
                textViewAmount = (TextView) view.findViewById(R.id.textViewAmount);
                textViewPriceForOne = (TextView) view.findViewById(R.id.textViewPriceForOne);
            }
        }
    }

}
