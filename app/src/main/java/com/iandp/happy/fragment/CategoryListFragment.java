package com.iandp.happy.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryListFragment extends BaseFragment {


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_list;
    }

    @Override
    protected void setupView(View view) {

    }

    @Override
    protected void loadInstanceState(Bundle savedInstanceState) {

    }

    private void goAddNewCategory(){

    }

    private void goEditCategory(long id){

    }

    private void goRemoveCategory(long id){

    }

    private void goSelectCategory(long id){

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

        private LayoutInflater lInflater;
        private ArrayList<CategoryProduct> listItem = new ArrayList<>();


        public RecyclerViewAdapter(Context context, ArrayList<CategoryProduct> listItem) {
            lInflater = LayoutInflater.from(context);
            this.listItem.addAll(listItem);
        }

        public void updateListCar(ArrayList<CategoryProduct> listItem) {
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
                    return new ViewHolderCategory(lInflater.inflate(R.layout.item_list_product, viewGroup, false));
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
                                goAddNewCategory();
                            }
                        });
                    }
                    break;

                case TYPE_ITEM:
                    CategoryProduct item = getItem(position);
                    viewHolder.view.setTag(item.getId());
                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goSelectCategory((long) v.getTag());
                        }
                    });

                    ((ViewHolderCategory) viewHolder).imageButtonRemove.setTag(item.getId());
                    ((ViewHolderCategory) viewHolder).imageButtonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goRemoveCategory((long) v.getTag());
                        }
                    });

                    ((ViewHolderCategory) viewHolder).imageButtonEdit.setTag(item.getId());
                    ((ViewHolderCategory) viewHolder).imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goEditCategory((long) v.getTag());
                        }
                    });

                    ((ViewHolderCategory) viewHolder).textViewName.setText(item.getName());

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

        public CategoryProduct getItem(int position) {
            if (position == 0) return new CategoryProduct();
            if (this.listItem.size() > position - 1)
                return this.listItem.get(position - 1);
            else
                return new CategoryProduct();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
            }
        }

        class ViewHolderCategory extends ViewHolder {
            public TextView textViewName;
            public ImageButton imageButtonEdit;
            public ImageButton imageButtonRemove;

            public ViewHolderCategory(View itemView) {
                super(itemView);
                textViewName = (TextView) view.findViewById(R.id.textViewName);
                imageButtonEdit = (ImageButton) view.findViewById(R.id.imageButtonEdit);
                imageButtonRemove = (ImageButton) view.findViewById(R.id.imageButtonRemove);
            }
        }
    }

}
