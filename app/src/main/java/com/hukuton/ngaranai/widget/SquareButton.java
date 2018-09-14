package com.hukuton.ngaranai.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.util.CustomFontHelper;

import info.hoang8f.widget.FButton;

public class SquareButton extends FButton{
    private int lastPosition;

    public SquareButton(Context context) {
        super(context);
        init(context);
    }

    public SquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setButtonColor(getResources().getColor(R.color.fbutton_color_carrot));
        CustomFontHelper.setCustomFont(this, "show_gothic.TTF", context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        double dWidth = size*1.5;
        int newWidth = (int) dWidth;
        setMeasuredDimension(size, newWidth); // make it square

    }

    public void setLastPosition(int position){
        lastPosition = position;
    }

    public int getLastPosition(){
        return lastPosition;
    }
}