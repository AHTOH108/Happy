package com.iandp.happy.temp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iandp.happy.R;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.Product;

import java.util.ArrayList;


public class ProductListFragment extends Fragment {

    private RelativeLayout mRelativeLayoutCategory;
    private TextView mTextViewCategory;
    private EditText mEditTextSearch;
    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter adapter;

    private DBHelper dbHelper;
    private ArrayList<Product> mListProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        dbHelper = new DBHelper(getContext());
        onCreateView(view);
        loadInstanceState(savedInstanceState);

        return view;
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
                Toast.makeText(getActivity(), "Тут выбрать категорию", Toast.LENGTH_SHORT).show();
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
                adapter.updateListCar(mListProduct);
            }
        }
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

        public void updateListCar(ArrayList<Product> listItem) {
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
                                //goDetailShop(-1);
                            }
                        });
                    }
                    break;

                case TYPE_ITEM:
                    Product item = getItem(position);

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
            //public ImageView imageViewLog;

            public ViewHolderProduct(View itemView) {
                super(itemView);
                //imageViewLogo = (ImageView) view.findViewById(R.id.imageViewLogo);
            }
        }
    }


}
