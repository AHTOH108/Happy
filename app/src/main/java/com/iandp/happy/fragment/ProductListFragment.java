package com.iandp.happy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.activity.CategoryListActivity;
import com.iandp.happy.activity.ProductDetailActivity;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Product;
import com.iandp.happy.utils.StringUtils;

import java.util.ArrayList;


public class ProductListFragment extends Fragment {

    public static final int CODE_SELECT_CATEGORY = 500;
    public static final int CODE_EDIT_PRODUCT = 600;

    private RelativeLayout mRelativeLayoutCategory;
    private TextView mTextViewCategory;
    private EditText mEditTextSearch;
    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter adapter;

    private DBHelper dbHelper;
    private ArrayList<Product> mListProduct;
    private CategoryProduct filterCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        dbHelper = new DBHelper(getContext());
        onCreateView(view);
        loadInstanceState(savedInstanceState);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CODE_SELECT_CATEGORY:
                    CategoryProduct categoryProduct = data.getParcelableExtra(CategoryListActivity.RESULT_CATEGORY);
                    if (categoryProduct != null && adapter != null) {
                        filterCategory = categoryProduct;
                        mTextViewCategory.setText(filterCategory.getName());
                        //TODO: обновление списка с ёчетом фильтра
                    }
                    break;
                case CODE_EDIT_PRODUCT:
                    updateProductList();
                    break;
            }
        }
    }

    private void onCreateView(View view) {
        mRelativeLayoutCategory = (RelativeLayout) view.findViewById(R.id.relativeLayoutCategory);
        mTextViewCategory = (TextView) view.findViewById(R.id.textViewCategory);
        mEditTextSearch = (EditText) view.findViewById(R.id.editTextSearch);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new RecyclerViewAdapter(getActivity(), mListProduct = new ArrayList<>());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);

        mRelativeLayoutCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSelectCategory();
            }
        });
    }

    private void loadInstanceState(Bundle savedInstanceState) {
        updateProductList();
    }

    private void updateProductList() {
        if (dbHelper != null) {
            mListProduct = dbHelper.getAllProduct();
            if (adapter != null) {
                adapter.updateList(mListProduct);
            }
        }
    }

    private void goSelectCategory() {
        Intent intent = new Intent(getActivity(), CategoryListActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, CODE_SELECT_CATEGORY);
    }

    private void goRemoveProduct(long idProduct) {
        //TODO: добавить анимацию удаления и оптимизировать процесс удаления
        dbHelper.removeProduct(idProduct);
        updateProductList();
    }

    private void goDetailProduct(long idProduct) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.DATA_ID_PRODUCT, idProduct);
        startActivityForResult(intent, CODE_EDIT_PRODUCT);
    }

    /**
     * *****************************************
     * *****************Adapter*****************
     * *****************************************
     */

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private static final int TYPE_ADD = 0;
        private static final int TYPE_ITEM = 1;
        private static final int TYPE_PROGRESS_BAR = 2;

        LayoutInflater lInflater;
        private ArrayList<Product> listItem = new ArrayList<>();


        public RecyclerViewAdapter(Context context, ArrayList<Product> listItem) {
            lInflater = LayoutInflater.from(context);
            this.listItem.addAll(listItem);
        }

        public void updateList(ArrayList<Product> listItem) {
            this.listItem.clear();
            this.listItem.addAll(listItem);
            notifyDataSetChanged();
        }

        public void removeItemList(int position) {
            if (listItem.size() > position) {
                this.listItem.remove(position);
                notifyItemRemoved(position);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            switch (viewType) {
                case TYPE_ADD:
                    return new ViewHolder(lInflater.inflate(R.layout.item_add_point, viewGroup, false));
                case TYPE_ITEM:
                    return new ViewHolderProduct(lInflater.inflate(R.layout.item_list_product, viewGroup, false));
                case TYPE_PROGRESS_BAR:
                default:
                    return new ViewHolder(lInflater.inflate(R.layout.item_spiner, viewGroup, false));
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_ADD:
                    TextView tv = (TextView) viewHolder.view.findViewById(R.id.textView);
                    if (tv != null) {
                        tv.setText(getString(R.string.add_product));
                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goDetailProduct(-1);
                            }
                        });
                    }
                    break;

                case TYPE_ITEM:
                    Product item = getItem(position);
                    viewHolder.view.setTag(item.getId());
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goDetailProduct((long) v.getTag());
                        }
                    });

                    ((ViewHolderProduct) viewHolder).imageButtonRemove.setTag(item.getId());
                    ((ViewHolderProduct) viewHolder).imageButtonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goRemoveProduct((long) v.getTag());
                        }
                    });

                    //TODO: установить первое изображение у товара если оно есть
                    //((ViewHolderProduct_) viewHolder).imageViewProduct;

                    ((ViewHolderProduct) viewHolder).textViewCategory.setText(item.getCategoryProduct().getName());
                    ((ViewHolderProduct) viewHolder).textViewNameProduct.setText(item.getBrand());
                    ((ViewHolderProduct) viewHolder).textViewDescription.setText(item.getDescription());
                    Cost cost = item.getFirstCost();
                    if (cost != null) {
                        if (cost.getShop() != null && cost.getShop().getImage() != null && cost.getShop().getImage().getPath().length() > 0) {
                            ((ViewHolderProduct) viewHolder).imageViewLogoShop.setVisibility(View.VISIBLE);
                            //TODO: сделать загрузку логотипа
                            ((ViewHolderProduct) viewHolder).textViewNameShop.setVisibility(View.GONE);
                        } else {
                            ((ViewHolderProduct) viewHolder).imageViewLogoShop.setVisibility(View.GONE);
                            ((ViewHolderProduct) viewHolder).textViewNameShop.setVisibility(View.VISIBLE);
                            ((ViewHolderProduct) viewHolder).textViewNameShop.setText(cost.getShop().getName());
                        }
                        ((ViewHolderProduct) viewHolder).textViewPrice.setText(StringUtils.getPriceRub(item.getFirstCost().getPriceFromUnit()));
                        ((ViewHolderProduct) viewHolder).textViewUnits.setText(StringUtils.getForUnits(1, cost.getUnits()));
                    } else {
                        ((ViewHolderProduct) viewHolder).imageViewLogoShop.setVisibility(View.GONE);
                        ((ViewHolderProduct) viewHolder).textViewNameShop.setVisibility(View.INVISIBLE);
                        ((ViewHolderProduct) viewHolder).textViewPrice.setText("");
                        ((ViewHolderProduct) viewHolder).textViewUnits.setText("");
                    }
                    break;

                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            if (this.listItem.size() < 1) return 1;
            else
                return this.listItem.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_ADD;
            if (listItem.size() < 1) return TYPE_PROGRESS_BAR;
            else if (listItem.size() > position - 1)
                return TYPE_ITEM;
            else
                return TYPE_PROGRESS_BAR;
        }

        public Product getItem(int position) {
            if (position == 0) return new Product();
            if (this.listItem.size() > position - 1)
                return this.listItem.get(position - 1);
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
            public ImageButton imageButtonRemove;
            public ImageView imageViewProduct;
            public ImageView imageViewLogoShop;
            public TextView textViewCategory;
            public TextView textViewNameProduct;
            public TextView textViewDescription;
            public TextView textViewPrice;
            public TextView textViewUnits;
            public TextView textViewNameShop;

            public ViewHolderProduct(View itemView) {
                super(itemView);
                imageButtonRemove = (ImageButton) view.findViewById(R.id.imageButtonRemove);
                imageViewProduct = (ImageView) view.findViewById(R.id.imageViewProduct);
                imageViewLogoShop = (ImageView) view.findViewById(R.id.imageViewLogoShop);
                textViewCategory = (TextView) view.findViewById(R.id.textViewCategory);
                textViewNameProduct = (TextView) view.findViewById(R.id.textViewNameProduct);
                textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
                textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
                textViewUnits = (TextView) view.findViewById(R.id.textViewUnits);
                textViewNameShop = (TextView) view.findViewById(R.id.textViewNameShop);
            }
        }
    }
}
