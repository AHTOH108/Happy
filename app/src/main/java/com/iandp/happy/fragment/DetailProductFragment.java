package com.iandp.happy.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iandp.happy.R;
import com.iandp.happy.activity.ProductDetailActivity;
import com.iandp.happy.adapters.SpinnerAdapter;
import com.iandp.happy.dialogs.EditCategoryDialog;
import com.iandp.happy.dialogs.EditCostDialog;
import com.iandp.happy.dialogs.EditTextDialog;
import com.iandp.happy.model.dataBase.DBHelper;
import com.iandp.happy.model.object.CategoryProduct;
import com.iandp.happy.model.object.Cost;
import com.iandp.happy.model.object.Image;
import com.iandp.happy.model.object.Product;
import com.iandp.happy.utils.DateUtil;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailProductFragment extends BaseFragment implements View.OnClickListener,
        EditCategoryDialog.OnConfirmEditCategoryListener,
        EditTextDialog.OnConfirmEditListener,
        EditCostDialog.OnConfirmEditCostListener {

    private static final String ARG_ID_PRODUCT = "idProduct";

    private static final String ADD_CATEGORY = "addCategory";
    private static final String ADD_DESCRIPTION = "addDescription";
    private static final String ADD_COST = "addCost";

    private static final String DATE_FORMAT = "dd MMMM yyyy г.   HH:mm";

    //private Toolbar mToolbar;
    private TextView mTextViewSave;
    private Spinner mSpinnerCategory;
    private EditText mEditTextNameProduct;
    private ImageView mPhotoProduct;
    private TextView mTextViewDescription;

    private RecyclerViewAdapter mAdapter;

    SpinnerAdapter adapterCategory;
    private DBHelper dbHelper;
    //private ArrayList<Cost> mListCost;
    private ArrayList<CategoryProduct> mListCategory;
    private Product mProduct;

    String[] nullListSpinner = new String[]{"Категорий нет"};

    public static DetailProductFragment newInstance(long id) {
        Bundle args = new Bundle();

        DetailProductFragment fragment = new DetailProductFragment();
        args.putLong(ARG_ID_PRODUCT, id);
        fragment.setArguments(args);

        return fragment;
    }

    public long getIdProduct() {
        return getArguments().getLong(ARG_ID_PRODUCT);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_product;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddCategory:
                goAddNewCategory();
                break;
            case R.id.imageButtonAddPhoto:
                goAddNewPhoto();
                break;
            case R.id.buttonEditDescription:
                goAddNewDescription();
                break;
        }
    }

    @Override
    protected void setupView(View view) {
        setToolbar(view);
        mTextViewSave = (TextView) view.findViewById(R.id.textViewSave);
        mSpinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        mEditTextNameProduct = (EditText) view.findViewById(R.id.editTextNameProduct);
        mPhotoProduct = (ImageView) view.findViewById(R.id.photoProduct);
        mTextViewDescription = (TextView) view.findViewById(R.id.textViewDescription);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        Button buttonAddCategory = (Button) view.findViewById(R.id.buttonAddCategory);
        ImageButton imageButtonAddPhoto = (ImageButton) view.findViewById(R.id.imageButtonAddPhoto);
        ImageButton buttonEditDescription = (ImageButton) view.findViewById(R.id.buttonEditDescription);

        mAdapter = new RecyclerViewAdapter(getActivity(), new ArrayList<Cost>());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(layoutManager);

        buttonAddCategory.setOnClickListener(this);
        imageButtonAddPhoto.setOnClickListener(this);
        buttonEditDescription.setOnClickListener(this);

        setupCategorySpinner(mListCategory = new ArrayList<>());
        mTextViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveProduct();
            }
        });
    }

    private void selectCategory(long idCategory) {
        if (mSpinnerCategory == null || adapterCategory == null || mListCategory == null || idCategory < 0)
            return;
        for (int i = 0; i < mListCategory.size(); i++)
            if (mListCategory.get(i).getId() == idCategory) {
                if (mSpinnerCategory.getCount() > i) {
                    mSpinnerCategory.setSelection(i);
                }
            }
    }

    @Override
    protected void loadInstanceState(Bundle savedInstanceState) {
        dbHelper = new DBHelper(getActivity());
        loadProduct();
        updateCategoryList();
        showInfoProduct(mProduct);
    }

    private void setupCategorySpinner(ArrayList<CategoryProduct> listCategory) {

        adapterCategory = new SpinnerAdapter(getActivity(), R.layout.item_simple_spinner, nullListSpinner);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCategory.setAdapter(adapterCategory);
        //mSpinnerCategory.setOnItemSelectedListener(onItemSelectedListener);

        updateCategorySpinner(listCategory);
    }

    private void updateCategorySpinner(ArrayList<CategoryProduct> listCategory) {
        if (mSpinnerCategory == null || adapterCategory == null) return;
        String mass[];
        if (listCategory != null && listCategory.size() > 0) {
            mass = new String[listCategory.size()];
            for (int i = 0; i < listCategory.size(); i++)
                mass[i] = listCategory.get(i).getName();
            if (mass.length > 0) {
                adapterCategory.updateList(mass);
            }
        } else {
            adapterCategory.updateList(nullListSpinner);
        }
    }

    private void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.textViewTitle);
        if (ProductDetailActivity.class.isInstance(getActivity())) {
            ((ProductDetailActivity) getActivity()).setToolbar(toolbar);
        }
        title.setText(getString(R.string.product));
    }

    private void loadProduct() {
        if (dbHelper != null) {
            mProduct = dbHelper.getProduct(getIdProduct());
        }
    }

    private void updateCategoryList() {
        if (dbHelper != null) {
            mListCategory = dbHelper.getAllCategoryProduct();
            updateCategorySpinner(mListCategory);
        }
    }

    private void showListCost(ArrayList<Cost> list) {
        mAdapter.updateListCar(list);
    }

    private void showInfoProduct(Product product) {
        selectCategory(product.getCategoryProduct().getId());
        mEditTextNameProduct.setText(product.getBrand());
        showDescription(product.getDescription());
        if (product.getImageList().size() > 0) {
            showPhoto(product.getImageList().get(0));
        }

        showListCost(mProduct.getCostList());
    }

    private void showDescription(String text) {
        if (mTextViewDescription != null) {
            if (TextUtils.isEmpty(text))
                text = "описание отсутствует";
            mTextViewDescription.setText(text);
        }
    }

    private void showPhoto(Image image) {
        // TODO: добавить отображение фото продукта
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

    private void goAddNewPhoto() {
        Toast.makeText(getActivity(), "goAddNewPhoto", Toast.LENGTH_SHORT).show();
    }

    private void goAddNewDescription() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prevDialog = getFragmentManager().findFragmentByTag(ADD_DESCRIPTION);
        if (prevDialog != null) {
            ft.remove(prevDialog);
        }

        EditTextDialog dialogFragment = EditTextDialog.newInstance(mProduct.getDescription(), getTag());
        dialogFragment.show(ft, ADD_DESCRIPTION);

    }

    private void goUpdateCost(Cost cost) {
        if (mProduct.getId() > 0 || addProductInDB()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prevDialog = getFragmentManager().findFragmentByTag(ADD_COST);
            if (prevDialog != null) {
                ft.remove(prevDialog);
            }

            EditCostDialog dialogFragment = EditCostDialog.newInstance(cost, getTag());
            dialogFragment.show(ft, ADD_COST);
        } else {
            showSnackBarMessage("Введите имя товара!");
        }
    }

    private void onSaveProduct() {
        if (addProductInDB()) {
            if (ProductDetailActivity.class.isInstance(getActivity())) {
                ((ProductDetailActivity) getActivity()).finishActivity(true);
            }
        } else {
            showSnackBarMessage("Введите имя товара!");
        }
    }

    private boolean addProductInDB() {
        String brand = mEditTextNameProduct.getText().toString();
        if (!TextUtils.isEmpty(brand)) {
            mProduct.setBrand(brand);
            int k = mSpinnerCategory.getSelectedItemPosition();
            if (k < mListCategory.size()) {
                mProduct.setCategoryProduct(mListCategory.get(k));
            } else {
                return false;
            }
            long id = dbHelper.addNewProduct(mProduct);
            if (id > 0) {
                mProduct.setId(id);
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onConfirmEditCategoryListener(CategoryProduct categoryProduct) {
        long id = dbHelper.addCategoryProduct(categoryProduct);
        updateCategoryList();
        if (id > 0)
            selectCategory(id);
    }

    @Override
    public void onConfirmEditListener(String string) {
        if (mTextViewDescription != null) {
            mTextViewDescription.setText(string);
            mProduct.setDescription(string);
        }
    }

    @Override
    public void onConfirmEditCostListener(Cost cost) {
        if (mProduct.getId() <= 0) {
            showSnackBarMessage("Ошибка сохранения: Товар не добавлен в базу!");
        } else {
            dbHelper.addCost(cost, mProduct.getId());
        }
        loadProduct();
        showListCost(mProduct.getCostList());
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
        private ArrayList<Cost> listItem = new ArrayList<>();


        public RecyclerViewAdapter(Context context, ArrayList<Cost> listItem) {
            lInflater = LayoutInflater.from(context);
            this.listItem.addAll(listItem);
        }

        public void updateListCar(ArrayList<Cost> listItem) {
            this.listItem.clear();
            this.listItem.addAll(listItem);
            notifyDataSetChanged();
        }

        /*public void removeItemList(int position) {
            if (listItem.size() > position) {
                this.listItem.remove(position);
                notifyItemRemoved(position);
            }
        }*/

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            switch (viewType) {
                case TYPE_ADD:
                    return new ViewHolder(lInflater.inflate(R.layout.item_add_point, viewGroup, false));
                case TYPE_ITEM:
                    return new ViewHolderProduct(lInflater.inflate(R.layout.item_list_cost, viewGroup, false));
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
                        tv.setText(getString(R.string.add_cost));
                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goUpdateCost(null);
                            }
                        });
                    }
                    break;

                case TYPE_ITEM:
                    Cost item = getItem(position);

                    ((ViewHolderProduct) viewHolder).textViewNameShop.setText(item.getShop().getName());

                    if (TextUtils.isEmpty(item.getShop().getImage().getPath())) {
                        ((ViewHolderProduct) viewHolder).imageViewLogoShop.setVisibility(View.GONE);
                    } else {
                        ((ViewHolderProduct) viewHolder).imageViewLogoShop.setVisibility(View.VISIBLE);
                        //TODO: тут добавить загрузку картинки
                    }

                    String str = String.format("%1.2f", item.getPriceFromUnit()).replace(",", ".");

                    ((ViewHolderProduct) viewHolder).textViewPrice.setText(item.getPrice() + " за " + item.getVolume() + " " + item.getUnits().getShortName());
                    ((ViewHolderProduct) viewHolder).textViewPriceFromUnit.setText(str + " за 1 " + item.getUnits().getShortName());

                    ((ViewHolderProduct) viewHolder).textViewDate.setText(DateUtil.getStringDateForMask(item.getDate(), DATE_FORMAT));

                    ((ViewHolderProduct) viewHolder).buttonUpdate.setTag(item);
                    ((ViewHolderProduct) viewHolder).buttonUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goUpdateCost((Cost) v.getTag());
                        }
                    });

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

        public Cost getItem(int position) {
            if (position == 0) return new Cost();
            if (this.listItem.size() > position - 1)
                return this.listItem.get(position - 1);
            else
                return new Cost();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View view;

            public ViewHolder(View itemView) {
                super(itemView);
                view = itemView;
            }
        }

        class ViewHolderProduct extends ViewHolder {
            public TextView textViewNameShop;
            public ImageView imageViewLogoShop;
            public TextView textViewPrice;
            public TextView textViewPriceFromUnit;
            public TextView textViewDate;
            public Button buttonUpdate;

            public ViewHolderProduct(View itemView) {
                super(itemView);
                textViewNameShop = (TextView) view.findViewById(R.id.textViewNameShop);
                imageViewLogoShop = (ImageView) view.findViewById(R.id.imageViewLogoShop);
                textViewPrice = (TextView) view.findViewById(R.id.textViewPrice);
                textViewPriceFromUnit = (TextView) view.findViewById(R.id.textViewPriceFromUnit);
                textViewDate = (TextView) view.findViewById(R.id.textViewDate);
                buttonUpdate = (Button) view.findViewById(R.id.buttonUpdate);
            }
        }
    }
}
