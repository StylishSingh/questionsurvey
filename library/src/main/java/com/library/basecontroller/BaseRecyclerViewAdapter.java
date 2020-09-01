package com.library.basecontroller;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<BD extends ViewDataBinding, T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseRecyclerViewHolder> {

    protected List<T> data;
    private int layoutId;

    public BaseRecyclerViewAdapter(List<T> data, @LayoutRes int layoutId) {
        this.data = data;
        this.layoutId = layoutId;
        if (this.data == null) {
            this.data = new ArrayList<>();
        }
    }

    public ArrayList<T> getData() {
        return new ArrayList<>(data);
    }

    public void addItem(T t) {
            data.add(t);
            notifyItemInserted(data.size() - 1);
    }

    public void addAllItems(List<T> t) {
        data.clear();
        data.addAll(t);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public T getItem(int position) {
        if (position < data.size())
            return data.get(position);
        return null;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void updateItem(int position, T t) {
        data.set(position, t);
        notifyItemChanged(position);
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {
        private BD binding;

        BaseRecyclerViewHolder(BD binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public BD getBinding() {
            return binding;
        }
    }
}
