package com.iandp.happy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iandp.happy.R;
import com.iandp.happy.activity.RedactProductCalculatorActivity;
import com.iandp.happy.dialogs.EditProductCalculatorDialog;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Product;

import java.util.ArrayList;


public class CalculatorFragment extends Fragment implements EditProductCalculatorDialog.OnConfirmEditProductListener {

    protected static final String REDACT_PRODUCT_CALCULATOR = "redactProductCalculator";
    protected static final String LIST_POSITION_REDACT = "listPositionRedact";
    protected static final String LIST_PRODUCT = "listProduct";
    protected static final int OK_REDACT_PRODUCT_CALCULATOR = 11;
    protected static final int OK_ADD_PRODUCT_CALCULATOR = 12;

    private EditText editTextSearchCategory;
    private Button buttonSelectExist;
    private RecyclerView recyclerViewProducts;

    private RecyclerViewAdapter rVAdapter;

    private ArrayList<Product> listProduct = new ArrayList<>();

    private DBHelper dbHelper;

    private int listPositionRedact = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rVAdapter = new RecyclerViewAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        dbHelper = new DBHelper(getContext());
        setupView(view);
        loadInstanceState(savedInstanceState);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIST_POSITION_REDACT, listPositionRedact);
        outState.putParcelableArrayList(LIST_PRODUCT, listProduct);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: Потом удалить это т.к. не используем ( + Удалить активити которая отвечает за это)
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == OK_REDACT_PRODUCT_CALCULATOR) {

                if (data != null) {
                    Product product = data.getParcelableExtra(RedactProductCalculatorActivity.REDACT_PRODUCT);
                    if (listPositionRedact >= 0 && listProduct.size() > listPositionRedact) {
                        listProduct.set(listPositionRedact, product);
                        rVAdapter.notifyDataSetChanged();
                        listPositionRedact = -1;
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (requestCode == OK_ADD_PRODUCT_CALCULATOR) {

                if (data != null) {
                    Product product = data.getParcelableExtra(RedactProductCalculatorActivity.REDACT_PRODUCT);
                    product.getCategoryProduct().setName(product.getBrand());

                    //long id = new ProductApi(getActivity()).addProduct(product);
                    //long id = dbHelper.addNewProduct(product);
                    listProduct.add(product);
                    rVAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void setupView(View view) {
        editTextSearchCategory = (EditText) view.findViewById(R.id.editTextSearchCategory);
        buttonSelectExist = (Button) view.findViewById(R.id.buttonSelectExist);
        recyclerViewProducts = (RecyclerView) view.findViewById(R.id.recyclerViewProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerViewProducts.setAdapter(rVAdapter);
        recyclerViewProducts.setLayoutManager(layoutManager);
        rVAdapter.notifyDataSetChanged();
    }

    private void loadInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            listPositionRedact = savedInstanceState.getInt(LIST_POSITION_REDACT);
            listProduct = savedInstanceState.getParcelableArrayList(LIST_PRODUCT);
        } else {
            listPositionRedact = -1;
            listProduct = new ArrayList<>();

            //listProduct = dbHelper.getAllProduct();
        }
        if (rVAdapter != null)
            rVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfirmEditProductListener(Product product) {
        if (product == null) return;
        if (product.getId() >= 0) {
            if (listPositionRedact >= 0 && listProduct.size() > listPositionRedact) {
                listProduct.set(listPositionRedact, product);
                rVAdapter.notifyDataSetChanged();
                listPositionRedact = -1;
            } else {
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        } else {
            product.getCategoryProduct().setName(product.getBrand());
            //long id = dbHelper.addNewProduct(product);
            product.setId(listProduct.size() + 1);
            listProduct.add(product);
            rVAdapter.notifyDataSetChanged();
        }
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private static final int TYPE_BUTTON_ADD = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_SPINNER = 2;

        LayoutInflater lInflater;

        public RecyclerViewAdapter(Context context) {
            lInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_BUTTON_ADD:
                    return new ViewHolder(lInflater.inflate(R.layout.item_add_product_calculator, parent, false));
                case TYPE_ITEM:
                    return new ViewHolderProduct(lInflater.inflate(R.layout.item_list_calculator, parent, false));
                case TYPE_SPINNER:
                    return new ViewHolderSpinner(lInflater.inflate(R.layout.item_spiner, parent, false));
                default:
                    return new ViewHolderSpinner(lInflater.inflate(R.layout.item_spiner, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            int type = getItemViewType(position);
            final Product product = getItem(position);
            switch (type) {
                case TYPE_BUTTON_ADD:
                    viewHolder.view.setTag(position);
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goAddProduct();
                        }
                    });/*
                    ImageLoader.getInstance().displayImage(HttpRequestManager.getServerUrl(getActivity()) + news.getImage().getSrc(), ((ViewHolderNews) viewHolder).imageViewNewsPicture, ImageLoaderOptions.getDefaultDisplayImageOptions());
                    ((ViewHolderNews) viewHolder).textViewNewsTitle.setText(news.getTitle());
                    ((ViewHolderNews) viewHolder).textViewNewsAnnouncement.setText((news.getPreview().length() > 200) ? news.getPreview().substring(0, 200) : news.getPreview());
                    */
                    break;
                case TYPE_ITEM:
                    viewHolder.view.setTag(position);
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goRedactProduct(product, (int) v.getTag());
                        }
                    });
                    ((ViewHolderProduct) viewHolder).textViewBrand.setText(product.getBrand());
                    Cost cost = product.getFirstCost();
                    ((ViewHolderProduct) viewHolder).textViewPrice.setText(String.valueOf(cost.getPrice()));
                    ((ViewHolderProduct) viewHolder).textViewShop.setText(cost.getShop().getName());
                    ((ViewHolderProduct) viewHolder).textViewUnits.setText(cost.getUnits().getName());
                    ((ViewHolderProduct) viewHolder).textViewVolume.setText(String.valueOf(cost.getVolume()));
                    ((ViewHolderProduct) viewHolder).textViewPriceFromUnit.setText(String.valueOf(cost.getPriceFromUnit()));
                    break;
                case TYPE_SPINNER:
                    //((ViewHolderNull)viewHolder).progressBarEndList.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return listProduct.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (listProduct.size() > position && position >= 0)
                if (listProduct.get(position) != null)
                    return TYPE_ITEM;
                else
                    return TYPE_SPINNER;
            else if (listProduct.size() == position)
                return TYPE_BUTTON_ADD;
            else
                return TYPE_SPINNER;
        }

        public Product getItem(int position) {
            if (listProduct.size() > position)
                return listProduct.get(position);
            else
                return new Product();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
            }
        }

        class ViewHolderProduct extends ViewHolder {
            public TextView textViewBrand;
            public TextView textViewShop;
            public TextView textViewPrice;
            public TextView textViewVolume;
            public TextView textViewUnits;
            public TextView textViewPriceFromUnit;


            public ViewHolderProduct(View itemView) {
                super(itemView);
                textViewBrand = (TextView) itemView.findViewById(R.id.textViewBrand);
                textViewShop = (TextView) itemView.findViewById(R.id.textViewShop);
                textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
                textViewVolume = (TextView) itemView.findViewById(R.id.textViewVolume);
                textViewUnits = (TextView) itemView.findViewById(R.id.textViewUnits);
                textViewPriceFromUnit = (TextView) itemView.findViewById(R.id.textViewPriceFromUnit);
            }
        }

        class ViewHolderSpinner extends ViewHolder {
            public ProgressBar progressBarEndList;

            public ViewHolderSpinner(View itemView) {
                super(itemView);
                progressBarEndList = (ProgressBar) itemView.findViewById(R.id.progressBarEndList);
            }
        }
    }

    private void goAddProduct() {
        Product product = new Product();
        product.setCategoryProduct(new CategoryProduct(editTextSearchCategory.getText().toString()));
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag(REDACT_PRODUCT_CALCULATOR);
        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        EditProductCalculatorDialog dialogFragment = EditProductCalculatorDialog.newInstance(product, getTag());
        dialogFragment.show(ft, REDACT_PRODUCT_CALCULATOR);


        /*Intent intent = new Intent(getActivity(), RedactProductCalculatorActivity.class);
        Product product = new Product();
        product.setCategoryProduct(new CategoryProduct(editTextSearchCategory.getText().toString()));
        intent.putExtra(RedactProductCalculatorActivity.REDACT_PRODUCT, product);
        startActivityForResult(intent, OK_ADD_PRODUCT_CALCULATOR);*/


        /*ContentValues cv = new ContentValues();

        SQLiteDatabase db = dbHelper_2.getWritableDatabase();
        // получаем данные из полей ввода
        String name = "Test 1";
        String email = "Test_2";

        cv.put("name", name);
        cv.put("email", email);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("mytable", null, cv);
        dbHelper_2.close();*/
    }

    private void goRedactProduct(Product product, int position) {
        listPositionRedact = position;
        /*Intent intent = new Intent(getActivity(), RedactProductCalculatorActivity.class);
        intent.putExtra(RedactProductCalculatorActivity.REDACT_PRODUCT, product);
        startActivityForResult(intent, OK_REDACT_PRODUCT_CALCULATOR);*/

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag(REDACT_PRODUCT_CALCULATOR);
        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        EditProductCalculatorDialog dialogFragment = EditProductCalculatorDialog.newInstance(product, getTag());
        dialogFragment.show(ft, REDACT_PRODUCT_CALCULATOR);

    }

}
