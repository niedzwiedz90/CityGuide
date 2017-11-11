package com.niedzwiecki.przemyslguide.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter
        .ViewHolder> implements BaseAdapterItem<T> {

    private List<T> items;
    private int selectedItemPosition;

    public BaseRecyclerAdapter() {
        this.items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(createViewItem(parent.getContext(), viewType).getView());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.itemView instanceof BaseItemView) {
            ((BaseItemView) holder.itemView).setData(position, getItem(position));
        }
    }

    public T getItem(int position) {
        if (position == items.size()) {
            return null;
        }

        return items.get(position);
    }

    public void setItems(List<T> items) {
        if (items == null) {
            return;
        }

        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(T t, int position) {
        items.add(position, t);
        notifyItemInserted(position);
    }

    public void addItems(List<T> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
