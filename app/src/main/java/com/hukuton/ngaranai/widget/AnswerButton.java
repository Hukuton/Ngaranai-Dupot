package com.hukuton.ngaranai.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.util.CustomFontHelper;

import info.hoang8f.widget.FButton;

public class AnswerButton extends FButton {

    private int lastPosition;

    public AnswerButton(Context context) {
        super(context);
        setButtonColor(getResources().getColor(R.color.fbutton_color_belize_hole));
        init(context);
    }

    public AnswerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnswerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        CustomFontHelper.setCustomFont(this, "show_gothic.TTF", context);
    }

    public void setLastPosition(int position){
        lastPosition = position;
    }

    public int getLastPosition(){
        return lastPosition;
    }

}
