package com.hukuton.ngaranai.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.interfaces.GridAdapterClickListener;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView objectImage;
    public TextView objectName;
    private GridAdapterClickListener gridAdapterClickListener;


    public RecyclerViewHolders(View itemView, GridAdapterClickListener gridAdapterClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        objectImage = itemView.findViewById(R.id.objectImage);
        objectName = itemView.findViewById(R.id.objectName);
        this.gridAdapterClickListener = gridAdapterClickListener;
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
        gridAdapterClickListener.gridClickListener(getAdapterPosition());
    }
}

