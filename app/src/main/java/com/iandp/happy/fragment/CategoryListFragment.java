package com.iandp.happy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iandp.happy.R;
import com.iandp.happy.activity.CategoryListActivity;
import com.iandp.happy.activity.MainActivity;
import com.iandp.happy.dialogs.EditCategoryDialog;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Product;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryListFragment extends BaseFragment implements EditCategoryDialog.OnConfirmEditCategoryListener {

    private static final String ADD_CATEGORY = "addCategory";
    private static final String EDIT_CATEGORY = "editCategory";

    private RecyclerView mRecyclerView;

    private RecyclerViewAdapter adapter;
    private Toolbar mToolbar;

    private ArrayList<CategoryProduct> mListCategory;
    private DBHelper dbHelper;


    public static CategoryListFragment newInstance() {
        Bundle args = new Bundle();

        CategoryListFragment fragment = new CategoryListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category_list;
    }

    @Override
    protected void setupView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(getActivity(), mListCategory = new ArrayList<>());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void loadInstanceState(Bundle savedInstanceState) {
        dbHelper = new DBHelper(getContext());
        updateCategoryList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (CategoryListActivity.class.isInstance(getActivity())) {
            ((CategoryListActivity) getActivity()).setToolbar(mToolbar);
        }
    }

    private void updateCategoryList() {
        if (dbHelper != null) {
            mListCategory = dbHelper.getAllCategoryProduct();
            if (adapter != null) {
                adapter.updateList(mListCategory);
            }
        }
    }

    private void goAddNewCategory() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag(ADD_CATEGORY);
        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        EditCategoryDialog dialogFragment = EditCategoryDialog.newInstance(null, getTag());
        dialogFragment.show(ft, ADD_CATEGORY);
    }

    private void goEditCategory(CategoryProduct category) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag(EDIT_CATEGORY);
        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        EditCategoryDialog dialogFragment = EditCategoryDialog.newInstance(category, getTag());
        dialogFragment.show(ft, EDIT_CATEGORY);
    }

    private void goRemoveCategory(long id) {
        //TODO: добавить диалог с подтверждением и анимацию
        if (dbHelper.removeCategoryProduct(id) > 0)
            updateCategoryList();
    }

    private void goSelectCategory(long id) {
        if (CategoryListActivity.class.isInstance(getActivity())) {
            Intent intent = new Intent();
            intent.putExtra(CategoryListActivity.RESULT_CATEGORY, dbHelper.getCategoryProduct(id));
            ((CategoryListActivity) getActivity()).finishActivity(true, intent);
        }
    }

    @Override
    public void onConfirmEditCategoryListener(CategoryProduct categoryProduct) {
        dbHelper.addCategoryProduct(categoryProduct);
        updateCategoryList();
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

        public void updateList(ArrayList<CategoryProduct> listItem) {
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
                    return new ViewHolderCategory(lInflater.inflate(R.layout.item_list_category, viewGroup, false));
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
                        tv.setText(getString(R.string.add_category));
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

                    ((ViewHolderCategory) viewHolder).imageButtonEdit.setTag(item);
                    ((ViewHolderCategory) viewHolder).imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goEditCategory((CategoryProduct) v.getTag());
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
