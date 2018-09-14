package com.hukuton.ngaranai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hukuton.ngaranai.holder.RecyclerViewHolders;
import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.interfaces.GridAdapterClickListener;
import com.hukuton.ngaranai.item.MyObject;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<MyObject> itemList;
    private Context context;
    private GridAdapterClickListener gridAdapterClickListener;

    public RecyclerViewAdapter(Context context, List<MyObject> itemList, GridAdapterClickListener gridAdapterClickListener) {
        this.itemList = itemList;
        this.context = context;
        this.gridAdapterClickListener = gridAdapterClickListener;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView, gridAdapterClickListener);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.objectImage.setImageResource(itemList.get(position).getImageId());
        holder.objectName.setText(itemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    private void setAnimation(View viewToAnimate, int position)
    {
    }
}