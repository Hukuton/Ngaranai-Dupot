package com.hukuton.ngaranai.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.interfaces.OnDialogClickListener;
import com.hukuton.ngaranai.widget.MyFButton;

public class ReviewDialogBuilder extends AlertDialog.Builder {

    private TextView textViewName;
    private ImageView imageViewObject;
    private MyFButton buttonOK;

    OnDialogClickListener onDialogClickListener;

    public ReviewDialogBuilder(Context context) {
        super(context);
        View dialogView = View.inflate(context, R.layout.fragment_dialog_review, null);
        setView(dialogView);

        textViewName       = dialogView.findViewById(R.id.objectName);
        imageViewObject     = dialogView.findViewById(R.id.objectImage);
        buttonOK            = dialogView.findViewById(R.id.buttonOk);
        buttonOK        = dialogView.findViewById(R.id.buttonOk);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFButton b = (MyFButton) view;
                onDialogClickListener.positiveButtonClickListener(b);
            }
        });
    }

    public ReviewDialogBuilder setObjectName(String title){
        textViewName.setText(title);
        return this;
    }

    public ReviewDialogBuilder setObjectImage(int imageResourceId){
        imageViewObject.setImageResource(imageResourceId);
        return this;
    }

    public ReviewDialogBuilder setPositiveButtonText(String text){
        buttonOK.setText(text);
        return this;
    }

    public ReviewDialogBuilder setOnDialogClickListener(OnDialogClickListener onDialogClickListener){
        this.onDialogClickListener = onDialogClickListener;
        return this;
    }

}
