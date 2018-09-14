package com.hukuton.ngaranai;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hukuton.ngaranai.dialog.CustomAlertDialogBuilder;
import com.hukuton.ngaranai.interfaces.OnDialogClickListener;
import com.hukuton.ngaranai.widget.MyFButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private PageIcon pageIcon[] = new PageIcon[8];
    private int id[] = {R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4, R.id.icon5};
    public final int PAGE_ICON = 5;
    public static final String MINE_STR = "mine_str";
    public static final String SHARED_PREF = "NGARANAI_PREF_DUPOT";

    public static final int MAMALIA = 1;
    public static final int OMBOLOG = 2;
    public static final int SID_VAIG = 3;
    public static final int REPTILIA_AMFIBIA = 4;
    public static final int OPODOK = 5;

    //Shared preferences
    public static final String MAMALIA_SHARED = "mamalia";
    public static final String OMBOLOG_SHARED = "ombolog";
    public static final String SID_VAIG_SHARED = "sid_vaig";
    public static final String REPTILIA_AMFIBIA_SHARED = "reptilia_amfibia";
    public static final String OPODOK_SHARED = "opodok";

    public static final String OBJECT_SHARED[] = {MAMALIA_SHARED, OMBOLOG_SHARED, SID_VAIG_SHARED, REPTILIA_AMFIBIA_SHARED, OPODOK_SHARED};

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ImageButton imageButtonBurger;

    //Ads
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, getResources().getString(R.string.ad_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        imageButtonBurger = findViewById(R.id.imageViewBurger);
        imageButtonBurger.setOnClickListener(this);

        initializeSharedPref();

        setView();

    }

    private void initializeSharedPref() {
        pref = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREF, 0); // 0 - for private mode
        editor = pref.edit();
    }

    private void setView(){
        for(int i = 0; i < PAGE_ICON; i++){
            pageIcon[i] = findViewById(id[i]);
            pageIcon[i].setOnClickListener(this);
            pageIcon[i].setProgress(getProgressValuePercentage(OBJECT_SHARED[i]));
        }
    }

    private int getProgressValuePercentage(String shared_prefs) {
        return 100 * getProgressValue(shared_prefs) / getTotalObjectOf(shared_prefs);
    }

    public int getTotalObjectOf(String objectType){
        int total = 0;
        switch (objectType){
            case MAMALIA_SHARED:
                total = getResources().getStringArray(R.array.mamalia).length;
                break;
            case OMBOLOG_SHARED:
                total = getResources().getStringArray(R.array.ombolog).length;
                break;
            case SID_VAIG_SHARED:
                total = getResources().getStringArray(R.array.sid_vaig).length;
                break;
            case REPTILIA_AMFIBIA_SHARED:
                total = getResources().getStringArray(R.array.reptilia_amfibia).length;
                break;
            case OPODOK_SHARED:
                total = getResources().getStringArray(R.array.opodok).length;
                break;
        }
        return total;
    }

    public int getProgressValue(String shared_prefs) {
        return pref.getInt(shared_prefs, 0); // getting Integer
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, PlayActivity.class);
        switch (view.getId()){
            case R.id.icon1:
                if(pageIcon[0].getProgress() >= 100)
                    showResetDialog(MAMALIA_SHARED);
                else {
                    i.putExtra(MINE_STR, MAMALIA);
                    startActivity(i);
                }
                break;
            case R.id.icon2:
                if(pageIcon[1].getProgress() >= 100)
                    showResetDialog(OMBOLOG_SHARED);
                else {
                    i.putExtra(MINE_STR, MainActivity.OMBOLOG);
                    startActivity(i);
                }
                break;
            case R.id.icon3:
                if(pageIcon[2].getProgress() >= 100)
                    showResetDialog(SID_VAIG_SHARED);
                else {
                    i.putExtra(MINE_STR, MainActivity.SID_VAIG);
                    startActivity(i);
                }
                break;
            case R.id.icon4:
                if(pageIcon[3].getProgress() >= 100)
                    showResetDialog(REPTILIA_AMFIBIA_SHARED);
                else {
                    i.putExtra(MINE_STR, MainActivity.REPTILIA_AMFIBIA);
                    startActivity(i);
                }
                break;
            case R.id.icon5:
                if(pageIcon[4].getProgress() >= 100)
                    showResetDialog(OPODOK_SHARED);
                else {
                    i.putExtra(MINE_STR, MainActivity.OPODOK);
                    startActivity(i);
                }
                break;
            case R.id.imageViewBurger:
                Intent j = new Intent(this, BurgerActivity.class);
                startActivity(j);
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                break;
        }
    }

    private void showResetDialog(final String sharedStr){
        final CustomAlertDialogBuilder dialogBuilder = new CustomAlertDialogBuilder(this);
        final int tot = getTotalObjectOf(sharedStr);
        dialogBuilder.setTitle("MOMOZON VAGU?");
        dialogBuilder.setMessage("Okurangan " + tot + " point!!");
        dialogBuilder.setPositiveButtonText("OK");
        dialogBuilder.seNegativeButtonText("CANCEL");
        final AlertDialog alert = dialogBuilder.show();
        dialogBuilder.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void positiveButtonClickListener(MyFButton fButton) {
                saveProgressAndPointValue(sharedStr, tot);
                alert.dismiss();
                setView();
            }

            @Override
            public void negativeButtonClickListener(MyFButton fButton) {
                alert.dismiss();
            }
        });
    }

    private void saveProgressAndPointValue(String sharedStr, int decPoint){
        editor.putInt(sharedStr, 0);
        int point = pref.getInt(PlayActivity.POINT_SHARED, 10);
        point = point - decPoint;
        if(point < 0)
            point = 0;
        editor.putInt(PlayActivity.POINT_SHARED, point);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        CustomAlertDialogBuilder dialogBuilder = new CustomAlertDialogBuilder(this);
        dialogBuilder.setTitle("EXIT");
        dialogBuilder.setMessage("You sure want to exit?");
        dialogBuilder.setPositiveButtonText("YES");
        dialogBuilder.seNegativeButtonText("CANCEL");
        final AlertDialog alertExit = dialogBuilder.show();
        dialogBuilder.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void positiveButtonClickListener(MyFButton fButton) {
                finish();
                alertExit.dismiss();
            }

            @Override
            public void negativeButtonClickListener(MyFButton fButton) {
                alertExit.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
