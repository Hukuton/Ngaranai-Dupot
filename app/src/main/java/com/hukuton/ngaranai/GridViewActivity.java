package com.hukuton.ngaranai;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.hukuton.ngaranai.adapter.RecyclerViewAdapter;
import com.hukuton.ngaranai.dialog.ReviewDialogBuilder;
import com.hukuton.ngaranai.interfaces.GridAdapterClickListener;
import com.hukuton.ngaranai.interfaces.OnDialogClickListener;
import com.hukuton.ngaranai.item.MyObject;
import com.hukuton.ngaranai.widget.MyFButton;

import java.util.ArrayList;
import java.util.List;

public class GridViewActivity extends AppCompatActivity implements GridAdapterClickListener{

    private GridLayoutManager lLayout;
    private String objectName[];
    private TypedArray objectImageSmall;
    private TypedArray objectImageLarge;
    private static final int COLUMN_IN_A_ROW = 4;

    private SharedPreferences pref;
    private int progressValue;
    private RecyclerView rView;
    private String intentGrid; //intent string to identify animal type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        initializeSharedPref();

        intentGrid = getIntent().getStringExtra("TO_GRID_VIEW");
        selectionAnimalType(intentGrid);

        List<MyObject> objectArrayList = new ArrayList<>();
        MyObject myObject;

        for(int i = 0; i < objectName.length; i++){
            if(i<progressValue)
                myObject = new MyObject((i+1)+ ". " + objectName[i], objectImageSmall.getResourceId(i, 0));
            else if(i == progressValue){
                objectName[i] = objectName[i].replaceAll(".", "?"); //replace any character with ?
                myObject = new MyObject((i+1) + ". " + objectName[i], objectImageSmall.getResourceId(i, 0));
            }
            else{
                objectName[i] = objectName[i].replaceAll(".", "?"); //replace any character with ?
                myObject = new MyObject((i+1) + ". " + objectName[i], R.mipmap.ic_question_mark);
            }
            objectArrayList.add(myObject);
        }

        lLayout = new GridLayoutManager(this, COLUMN_IN_A_ROW);

        rView = findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RecyclerViewAdapter rcAdapter = new RecyclerViewAdapter(this, objectArrayList, this);
        rView.setAdapter(rcAdapter);
    }

    private void selectionAnimalType(String intentGrid) {

        switch (intentGrid){
            case MainActivity.MAMALIA_SHARED:
                objectName = getResources().getStringArray(R.array.mamalia);
                objectImageSmall = getResources().obtainTypedArray(R.array.icMamaliaID);
                objectImageLarge = getResources().obtainTypedArray(R.array.mamaliaID);
                break;
            case MainActivity.OMBOLOG_SHARED:
                objectName = getResources().getStringArray(R.array.ombolog);
                objectImageSmall = getResources().obtainTypedArray(R.array.icOmbologID);
                objectImageLarge = getResources().obtainTypedArray(R.array.ombologID);
                break;
            case MainActivity.SID_VAIG_SHARED:
                objectName = getResources().getStringArray(R.array.sid_vaig);
                objectImageSmall = getResources().obtainTypedArray(R.array.icSid_vaigID);
                objectImageLarge = getResources().obtainTypedArray(R.array.sid_vaigID);
                break;
            case MainActivity.REPTILIA_AMFIBIA_SHARED:
                objectName = getResources().getStringArray(R.array.reptilia_amfibia);
                objectImageSmall = getResources().obtainTypedArray(R.array.icReptilia_amfibiaID);
                objectImageLarge = getResources().obtainTypedArray(R.array.reptilia_amfibiaID);
                break;
            case MainActivity.OPODOK_SHARED:
                objectName = getResources().getStringArray(R.array.opodok);
                objectImageSmall = getResources().obtainTypedArray(R.array.icOpodokID);
                objectImageLarge = getResources().obtainTypedArray(R.array.opodokID);
                break;
        }
        progressValue = getProgressValue(intentGrid);
    }

    private void initializeSharedPref() {
        pref = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREF, 0); // 0 - for private mode
    }

    public int getProgressValue(String shared_prefs) {
        return pref.getInt(shared_prefs, 0); // getting Integer
    }

    @Override
    public void gridClickListener(int position) {
        if(position > progressValue){
            YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(rView.findViewHolderForAdapterPosition(progressValue).itemView);
        } else if(position == progressValue){
            Intent vr = new Intent(this, PlayActivity.class);
            vr.putExtra(MainActivity.MINE_STR, intentGrid);
            startActivity(vr);
            gotoPlay(intentGrid);
        } else {
            //Start review dialog
            ReviewDialogBuilder dialogBuilder = new ReviewDialogBuilder(this);
            dialogBuilder.setObjectName(objectName[position]);
            dialogBuilder.setObjectImage(objectImageLarge.getResourceId(position, 0));
            dialogBuilder.setPositiveButtonText("OK");
            final AlertDialog alertExit = dialogBuilder.show();
            dialogBuilder.setOnDialogClickListener(new OnDialogClickListener() {
                @Override
                public void positiveButtonClickListener(MyFButton fButton) {
                    alertExit.dismiss();
                }

                @Override
                public void negativeButtonClickListener(MyFButton fButton) {
                    //not implemented on ReviewDialogBuilder
                }
            });
        }
    }

    private void gotoPlay(String intentGrid) {
        Intent i = new Intent(this, PlayActivity.class);
        switch (intentGrid){
            case MainActivity.MAMALIA_SHARED:
                i.putExtra(MainActivity.MINE_STR, MainActivity.MAMALIA);
                startActivity(i);
                break;
            case MainActivity.OMBOLOG_SHARED:
                i.putExtra(MainActivity.MINE_STR, MainActivity.OMBOLOG);
                startActivity(i);
                break;
            case MainActivity.SID_VAIG_SHARED:
                i.putExtra(MainActivity.MINE_STR, MainActivity.SID_VAIG);
                startActivity(i);
                break;
            case MainActivity.REPTILIA_AMFIBIA_SHARED:
                i.putExtra(MainActivity.MINE_STR, MainActivity.REPTILIA_AMFIBIA);
                startActivity(i);
                break;
            case MainActivity.OPODOK_SHARED:
                i.putExtra(MainActivity.MINE_STR, MainActivity.OPODOK);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
