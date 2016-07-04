package com.iandp.happy.fragment;


import android.content.Context;
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
import com.iandp.happy.dialogs.EditProductCalculatorDialog;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Product;

import java.util.ArrayList;


public class CalculatorFragment extends BaseFragment implements EditProductCalculatorDialog.OnConfirmEditProductListener {

    protected static final String REDACT_PRODUCT_CALCULATOR = "redactProductCalculator";
    protected static final String LIST_POSITION_REDACT = "listPositionRedact";
    protected static final String LIST_PRODUCT = "listProduct";

    private EditText editTextSearchCategory;
    private Button buttonAddCategory;
    private RecyclerView recyclerViewProducts;

    private RecyclerViewAdapter rVAdapter;

    private ArrayList<Product> listProduct = new ArrayList<>();
    private ArrayList<CategoryProduct> listCategory = new ArrayList<>();
    private CategoryProduct currentCategory;

    private DBHelper dbHelper;

    private int listPositionRedact = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_calculator;
    }

    @Override
    protected void setupView(View view) {
        editTextSearchCategory = (EditText) view.findViewById(R.id.editTextSearchCategory);
        buttonAddCategory = (Button) view.findViewById(R.id.buttonAddCategory);
        recyclerViewProducts = (RecyclerView) view.findViewById(R.id.recyclerViewProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rVAdapter = new RecyclerViewAdapter(getActivity());
        recyclerViewProducts.setAdapter(rVAdapter);
        recyclerViewProducts.setLayoutManager(layoutManager);
        rVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LIST_POSITION_REDACT, listPositionRedact);
        outState.putParcelableArrayList(LIST_PRODUCT, listProduct);
    }

    @Override
    protected void loadInstanceState(Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());

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

    private void goAddProduct() {
        Product product = new Product();
        product.setCategoryProduct(currentCategory);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag(REDACT_PRODUCT_CALCULATOR);
        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        EditProductCalculatorDialog dialogFragment = EditProductCalculatorDialog.newInstance(product, getTag());
        dialogFragment.show(ft, REDACT_PRODUCT_CALCULATOR);

    }

    private void goRedactProduct(Product product, int position) {
        listPositionRedact = position;

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag(REDACT_PRODUCT_CALCULATOR);
        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        EditProductCalculatorDialog dialogFragment = EditProductCalculatorDialog.newInstance(product, getTag());
        dialogFragment.show(ft, REDACT_PRODUCT_CALCULATOR);

    }

    private void goSelectCategory(CategoryProduct category) {

    }

    /**
     * *****************************************
     * *****************Adapter*****************
     * *****************************************
     */

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private static final int TYPE_BUTTON_ADD = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_SPINNER = 2;

        private LayoutInflater lInflater;

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

    /**
     * *****************************************
     * *****************Adapter*****************
     * *****************************************
     */

    public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.ViewHolder> {

        private static final int TYPE_ITEM = 1;
        private static final int TYPE_NULL = 2;

        private LayoutInflater lInflater;

        private ArrayList<CategoryProduct> listItem = new ArrayList<>();

        public RecyclerViewCategoryAdapter(Context context) {
            lInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_ITEM:
                    return new ViewHolder(lInflater.inflate(R.layout.item_list_string, parent, false));
                default:
                    return new ViewHolder(lInflater.inflate(R.layout.item_list_string, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_ITEM:
                    CategoryProduct item = getItem(position);
                    viewHolder.view.setTag(item);
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goSelectCategory((CategoryProduct) v.getTag());
                        }
                    });
                    viewHolder.textView.setText(item.getName());
                    break;
                case TYPE_NULL:
                    viewHolder.view.setOnClickListener(null);
                    viewHolder.textView.setText("Список категорий пуст");
                    break;
                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return listItem.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (listProduct.size() > position && position >= 0)
                return TYPE_ITEM;
            else
                return TYPE_NULL;
        }

        public CategoryProduct getItem(int position) {
            if (listItem.size() > position)
                return listItem.get(position);
            else
                return new CategoryProduct();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public View view;
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }
    }
}
