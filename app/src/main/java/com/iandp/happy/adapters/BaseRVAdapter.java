package com.iandp.happy.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iandp.happy.R;

import java.util.ArrayList;

/**
 * Date creation: 29.03.2016.
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseRVAdapter.ViewHolder> {

    private static final int TYPE_NULL = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_PROGRESS_BAR = 2;
    private static final int TYPE_ADD_NEW = 3;

    Context context;
    LayoutInflater lInflater;
    private ArrayList<T> listItem = new ArrayList<>();
    protected OnClickItemListener<T> mListener;

    protected abstract ViewHolder getViewHolderNull(ViewGroup viewGroup);

    protected abstract ViewHolder getViewHolderItem(ViewGroup viewGroup);

    protected ViewHolder getViewHolderProgressBar(ViewGroup viewGroup) {
        return new ViewHolder(lInflater.inflate(R.layout.item_simple_spinner, viewGroup, false));
    }

    protected abstract ViewHolder getViewHolderAddNew(ViewGroup viewGroup);

    public interface OnClickItemListener<T> {
        void onClickItem(T item);
    }

    public BaseRVAdapter(Context context, ArrayList<T> listItem) {
        this.context = context;
        lInflater = LayoutInflater.from(context);
        this.listItem = new ArrayList<>();
        this.listItem.addAll(listItem);
    }

    public void updateList(ArrayList<T> listItem) {
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
            case TYPE_NULL:
                return getViewHolderNull(viewGroup);
            case TYPE_ITEM:
                return getViewHolderItem(viewGroup);
            case TYPE_PROGRESS_BAR:
                return getViewHolderProgressBar(viewGroup);
            case TYPE_ADD_NEW:
                return getViewHolderAddNew(viewGroup);
            default:
                return getViewHolderNull(viewGroup);
        }
    }

    //@Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        int type = getItemViewType(position);

        switch (type) {
            case TYPE_NULL:
                onBindNull(viewHolder, position);
                break;
            case TYPE_ITEM:
                onBindItem(viewHolder, position);
                break;
            case TYPE_PROGRESS_BAR:
                onBindProgressBar(viewHolder, position);
                break;
            case TYPE_ADD_NEW:
                onBindAddNew(viewHolder, position);
                break;
            default:
                break;
        }
    }

    protected void onBindNull(ViewHolder viewHolder, final int position) {

    }

    protected void onBindItem(ViewHolder viewHolder, final int position) {

    }

    protected void onBindProgressBar(ViewHolder viewHolder, final int position) {

    }

    protected void onBindAddNew(ViewHolder viewHolder, final int position) {

    }

    @Override
    public int getItemCount() {
        return this.listItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listItem.size() > position && position > -1)
            return TYPE_ITEM;
        else
            return TYPE_NULL;
    }

    public T getItem(int position) {
        if (listItem.size() > position && position > -1) return (T) new Object();
        return this.listItem.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setData() {
            int[] attrs = new int[]{R.attr.selectableItemBackground};
            TypedArray typedArray = context.obtainStyledAttributes(attrs);
            int backgroundResource = typedArray.getResourceId(0, 0);
            view.setBackgroundResource(backgroundResource);
            typedArray.recycle();
        }
    }


}
