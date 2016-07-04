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
import android.widget.Spinner;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.activity.ShopDetailActivity;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.Shop;

import java.util.ArrayList;


public class ShopListFragment extends Fragment {

    private static final int SHOW_DETAIL = 1;

    private EditText mEditTextSearch;
    private Spinner mSpinnerFilter;
    private RecyclerView mRecyclerView;

    private ArrayList<Shop> listShop = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    private DBHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_list, container, false);
        dbHelper = new DBHelper(getContext());
        onCreateView(view);
        loadInstanceState(savedInstanceState);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SHOW_DETAIL) {
                updateShopList();
            }
        }
    }

    private void onCreateView(View view) {
        mEditTextSearch = (EditText) view.findViewById(R.id.editTextSearch);
        mSpinnerFilter = (Spinner) view.findViewById(R.id.spinnerFilter);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        adapter = new RecyclerViewAdapter(getActivity(), new ArrayList<Shop>());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    private void loadInstanceState(Bundle savedInstanceState) {
        updateShopList();
    }

    private void updateShopList() {
        listShop = dbHelper.getAllShop();
        adapter.updateListCar(listShop);
    }

    private void goRemoveShop(long id) {
        if (dbHelper.removeShop(id) > 0) {
            updateShopList();
        }
    }

    private void goDetailShop(long id) {
        Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
        intent.putExtra(ShopDetailActivity.DATA_ID_SHOP, id);
        startActivityForResult(intent, SHOW_DETAIL);
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
        private ArrayList<Shop> listShop = new ArrayList<>();


        public RecyclerViewAdapter(Context context, ArrayList<Shop> listShop) {
            lInflater = LayoutInflater.from(context);
            this.listShop.addAll(listShop);
        }

        public void updateListCar(ArrayList<Shop> listShop) {
            this.listShop.clear();
            this.listShop.addAll(listShop);
            notifyDataSetChanged();
        }

        public void removeItemList(int position) {
            if (listShop.size() > position) {
                this.listShop.remove(position);
                notifyItemRemoved(position);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            switch (viewType) {
                case TYPE_ADD:
                    return new ViewHolder(lInflater.inflate(R.layout.item_add_point, viewGroup, false));
                case TYPE_ITEM:
                    return new ViewHolderShop(lInflater.inflate(R.layout.item_list_shop, viewGroup, false));
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
                        tv.setText(getString(R.string.add_shop));
                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goDetailShop(-1);
                            }
                        });
                    }
                    break;

                case TYPE_ITEM:
                    Shop item = getItem(position);
                    ((ViewHolderShop) viewHolder).textViewName.setText(item.getName());
                    ((ViewHolderShop) viewHolder).textViewAddress.setText(item.getAddress());
                    ((ViewHolderShop) viewHolder).imageButtonRemove.setTag(item.getId());
                    ((ViewHolderShop) viewHolder).imageButtonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goRemoveShop((long) v.getTag());
                        }
                    });

                    ((ViewHolderShop) viewHolder).view.setTag(item.getId());
                    ((ViewHolderShop) viewHolder).view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goDetailShop((long) v.getTag());
                        }
                    });
                    break;

                default:
                    break;
            }
        }

        @Override
        public int getItemCount() {
            if (this.listShop.size() < 1) return 1;
            else
                return this.listShop.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_ADD;
            if (listShop.size() < 1) return TYPE_PROGRESS_BAR;
            else if (listShop.size() > position - 1)
                return TYPE_ITEM;
            else
                return TYPE_PROGRESS_BAR;
        }

        public Shop getItem(int position) {
            if (position == 0) return new Shop();
            if (this.listShop.size() > position - 1)
                return this.listShop.get(position - 1);
            else
                return new Shop();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
            }
        }

        class ViewHolderShop extends ViewHolder {
            public ImageView imageViewLogo;
            public ImageButton imageButtonRemove;
            public TextView textViewName;
            public TextView textViewAddress;

            public ViewHolderShop(View itemView) {
                super(itemView);
                imageViewLogo = (ImageView) view.findViewById(R.id.imageViewLogo);
                imageButtonRemove = (ImageButton) view.findViewById(R.id.imageButtonRemove);
                textViewName = (TextView) view.findViewById(R.id.textViewName);
                textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);
            }
        }
    }

}
