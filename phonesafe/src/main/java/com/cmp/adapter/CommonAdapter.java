package com.cmp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 作者：cmp on 2016/9/17 15:49
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 实体类集合
     */
    private List<T> mData;
    private LayoutInflater mInflater;
    /**
     * 控件id
     */
    private int mItemLayoutId;

    protected CommonAdapter(Context context, List<T> mData, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mData = mData;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }
}
