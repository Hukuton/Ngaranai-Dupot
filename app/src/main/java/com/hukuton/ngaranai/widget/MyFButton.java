package com.hukuton.ngaranai.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.util.CustomFontHelper;

import info.hoang8f.widget.FButton;

public class MyFButton extends FButton{
    public MyFButton(Context context) {
        super(context);
        init(context);
    }

    public MyFButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyFButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        CustomFontHelper.setCustomFont(this, "show_gothic.TTF", context);
        setButtonColor(getResources().getColor(R.color.fbutton_color_carrot));
    }
}
