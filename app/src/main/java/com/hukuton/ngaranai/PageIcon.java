package com.hukuton.ngaranai;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class PageIcon extends LinearLayout {

    private ImageView imageView;
    private ProgressBar progressBar;

    private int imageId;
    private int progress;

    public PageIcon(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public PageIcon(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PageIcon(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        inflate(context, R.layout.page_icon, this);
        imageView = findViewById(R.id.imageButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.carrot), android.graphics.PorterDuff.Mode.SRC_IN);
        initAttributes(context, attrs);

    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context, attributeSet, R.styleable.PageIcon);
            try {
                imageId = attr.getResourceId(R.styleable.PageIcon_srcImage, 0);
                progress = attr.getInteger(R.styleable.PageIcon_barProgress, 90);

                setImageId(imageId);
                setProgress(progress);
            } finally {
                attr.recycle();
            }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    public void setImageId(int imageID){
        imageView.setImageResource(imageID);


    }

    public int getImageId(){
        return imageId;
    }

    public void setProgress(int nProgress){
        progressBar.setProgress(nProgress);
    }

    public int getProgress(){
        return progressBar.getProgress();
    }

    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public void setProgressBarColor(int color) {
        progressBar.getProgressDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
    }


}
