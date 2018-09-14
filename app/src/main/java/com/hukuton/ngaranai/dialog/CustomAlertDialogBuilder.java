package com.hukuton.ngaranai.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.interfaces.OnDialogClickListener;
import com.hukuton.ngaranai.widget.MyFButton;

public class CustomAlertDialogBuilder extends AlertDialog.Builder {

    private TextView textViewTitle;
    private TextView textViewMessage;
    private MyFButton buttonOK;
    private MyFButton buttonCancel;

    private OnDialogClickListener onDialogClickListener;

    public CustomAlertDialogBuilder(Context context) {
        super(context);
        View dialogView = View.inflate(context, R.layout.fragment_dialog, null);
        setView(dialogView);

        textViewTitle       = dialogView.findViewById(R.id.textViewTitle);
        textViewMessage     = dialogView.findViewById(R.id.textViewMessage);
        buttonOK            = dialogView.findViewById(R.id.buttonOk);
        buttonCancel        = dialogView.findViewById(R.id.buttonCancel);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFButton b = (MyFButton) view;
                onDialogClickListener.positiveButtonClickListener(b);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFButton b = (MyFButton) view;
                onDialogClickListener.negativeButtonClickListener(b);
            }
        });

    }

    public CustomAlertDialogBuilder setTitle(String title){
        textViewTitle.setText(title);
        return this;
    }

    public CustomAlertDialogBuilder setMessage(String message){
        textViewMessage.setText(message);
        return this;
    }

    public CustomAlertDialogBuilder setPositiveButtonText(String text){
        buttonOK.setText(text);
        return this;
    }

    public CustomAlertDialogBuilder seNegativeButtonText(String text){
        buttonCancel.setText(text);
        return this;
    }

    public CustomAlertDialogBuilder setOnDialogClickListener(OnDialogClickListener onDialogClickListener){
        this.onDialogClickListener = onDialogClickListener;
        return this;
    }

    public CustomAlertDialogBuilder setNegativeButtonVisibility(int visibility){
        buttonCancel.setVisibility(visibility);
        return this;
    }
}
