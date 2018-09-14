package com.hukuton.ngaranai.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.interfaces.SelectionListener;

import java.util.ArrayList;

public class SelectionButtons extends LinearLayout {

    private String selectionButtonId[] = { "button1", "button2", "button3", "button4", "button5", "button6", "button7", "button8",
            "button9", "button10", "button11", "button12", "button13", "button14", "button15", "button16"};

    private ArrayList<SquareButton> selectionButton = new ArrayList<>(16);

    public SelectionListener selectionListener;

    public static final int SELECTION_BUTTON_TOTAL = 16;

    public SelectionButtons(Context context) {
        super(context);
        init(context);
    }

    public SelectionButtons(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SelectionButtons(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.selection_buttons, this);

        for (int i = 0; i < selectionButtonId.length; i++) {
            int temp = getResources().getIdentifier(selectionButtonId[i], "id", context.getPackageName());
            selectionButton.add((SquareButton) findViewById(temp));
            selectionButton.get(i).setLastPosition(i);
            selectionButton.get(i).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    SquareButton squareButton = (SquareButton) view;
                    selectionListener.selectionButtonClickListener(squareButton);
                }
            });
        }
    }

    public void setSelectionListener(SelectionListener selectionListener){
        this.selectionListener = selectionListener;
    }

    public void fillButtonWithChar(String str){
        for (int i = 0; i < selectionButton.size(); i++) {
            selectionButton.get(i).setText(""+str.charAt(i));
        }
    }

    public int getButtonPositionContain(char c){
        for (int i = 0; i < selectionButtonId.length; i++) {
            if(selectionButton.get(i).getText().charAt(0) == c)
                return i;
        }
        return 0;
    }

    public SquareButton getSquareButton(int index){
        return selectionButton.get(index);
    }
}
