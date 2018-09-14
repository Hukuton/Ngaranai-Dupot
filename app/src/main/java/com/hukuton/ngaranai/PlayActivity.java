package com.hukuton.ngaranai;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.hukuton.ngaranai.dialog.CustomAlertDialogBuilder;
import com.hukuton.ngaranai.interfaces.AnswerListener;
import com.hukuton.ngaranai.interfaces.OnDialogClickListener;
import com.hukuton.ngaranai.interfaces.SelectionListener;
import com.hukuton.ngaranai.widget.AnswerButton;
import com.hukuton.ngaranai.widget.AnswerButtons;
import com.hukuton.ngaranai.widget.MyFButton;
import com.hukuton.ngaranai.widget.SelectionButtons;
import com.hukuton.ngaranai.widget.SquareButton;
import com.onurkaganaldemir.ktoastlib.KToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity implements AnswerListener, SelectionListener, View.OnClickListener, RewardedVideoAdListener {

    private String objectName[];
    private TypedArray objectImage;
    private String currentObjectName;

    private Button backButton;
    private TextView progressTextView, pointTextView;
    private int point;
    private int progressValue;  //counter to next object

    private AnswerButtons answerButtonsLayout;
    private SelectionButtons selectionButtonsLayout;
    private String sharedStr;  //String of Shared Pref
    public static final String POINT_SHARED = "points";

    private ImageView imageViewObject;

    private LinearLayout linearLayoutFinalAns;
    private TextView textViewFinalAns;
    private TextView textViewPointAdd;
    private ImageButton imageButtonContinue;

    private Button buttonRevealOneLetter;
    private ImageButton buttonRemoveAllLetter;


    private SharedPreferences pref;
    SharedPreferences.Editor editor;

    private int falseAnswerCounter = 0;
    private final int SHOW_AD_WHEN_FALSE_ANSWER_REACH = 5;

    //Ads
    private AdView mAdView;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        MobileAds.initialize(this, getResources().getString(R.string.ad_id));

        //Banner ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Rewarded video ad
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.loadAd(getResources().getString(R.string.ad_id_video), new AdRequest.Builder().build());
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        int intentPlay = getIntent().getIntExtra(MainActivity.MINE_STR, 1);

        answerButtonsLayout = findViewById(R.id.answerButtons);
        selectionButtonsLayout = findViewById(R.id.selectionButtons);
        imageViewObject = findViewById(R.id.imageView2);
        backButton = findViewById(R.id.backButton);
        progressTextView = findViewById(R.id.progressTextview);
        pointTextView = findViewById(R.id.textviewPoint);

        linearLayoutFinalAns = findViewById(R.id.layoutFinalAns);
        textViewFinalAns = findViewById(R.id.textViewFinalAns);
        textViewPointAdd = findViewById(R.id.textViewPointAdd);
        imageButtonContinue = findViewById(R.id.imageButtonContinue);

        buttonRevealOneLetter = findViewById(R.id.buttonRevealOneLetter);
        buttonRemoveAllLetter = findViewById(R.id.buttonRemoveAll);

        backButton.setOnClickListener(this);
        answerButtonsLayout.setAnswerListener(this);
        selectionButtonsLayout.setSelectionListener(this);
        imageButtonContinue.setOnClickListener(this);
        buttonRevealOneLetter.setOnClickListener(this);
        buttonRemoveAllLetter.setOnClickListener(this);
        progressTextView.setOnClickListener(this);      // Open gridview to view answered questions

        pref = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREF, 0); // 0 - for private mode
        editor = pref.edit();

        selectPlay(intentPlay);

    }

    private void selectPlay(int intentPlay) {

        switch (intentPlay){
            case MainActivity.MAMALIA:
                objectName = getResources().getStringArray(R.array.mamalia);
                objectImage = getResources().obtainTypedArray(R.array.mamaliaID);
                sharedStr = MainActivity.MAMALIA_SHARED;

                break;
            case MainActivity.OMBOLOG:
                objectName = getResources().getStringArray(R.array.ombolog);
                objectImage = getResources().obtainTypedArray(R.array.ombologID);
                sharedStr = MainActivity.OMBOLOG_SHARED;
                break;
            case MainActivity.SID_VAIG:
                objectName = getResources().getStringArray(R.array.sid_vaig);
                objectImage = getResources().obtainTypedArray(R.array.sid_vaigID);
                sharedStr = MainActivity.SID_VAIG_SHARED;
                break;
            case MainActivity.REPTILIA_AMFIBIA:
                objectName = getResources().getStringArray(R.array.reptilia_amfibia);
                objectImage = getResources().obtainTypedArray(R.array.reptilia_amfibiaID);
                sharedStr = MainActivity.REPTILIA_AMFIBIA_SHARED;
                break;
            case MainActivity.OPODOK:
                objectName = getResources().getStringArray(R.array.opodok);
                objectImage = getResources().obtainTypedArray(R.array.opodokID);
                sharedStr = MainActivity.OPODOK_SHARED;
                break;
        }
        progressValue = getProgressValue(sharedStr);
        point = pref.getInt(POINT_SHARED, 10);
        initLoopGame();
    }

    private int getProgressValue(String shared_prefs) {
        return pref.getInt(shared_prefs, 0); // getting Integer
    }

    private void initLoopGame() {

        if(progressValue < objectName.length) {
            answerButtonsLayout.resetView();
            saveProgressAndPointValue(sharedStr, progressValue, point);
            playGames(progressValue);
        } else if(progressValue == objectName.length) {
            answerButtonsLayout.setAnswerButtonVisibility(objectName[progressValue-1]);
            imageViewObject.setImageResource(objectImage.getResourceId(progressValue-1, 0));
            progressTextView.setText("" + progressValue  + "/" + objectName.length);
            pointTextView.setText("" + point);
            createCongratulationDialog();
            saveProgressAndPointValue(sharedStr, progressValue, point);
        }else{
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    private void saveProgressAndPointValue(String sharedStr, int curProgress, int curPoint){
        editor.putInt(sharedStr, curProgress);
        editor.putInt(POINT_SHARED, curPoint);
        editor.commit();
    }

    private void playGames(int pl) {
        currentObjectName = objectName[pl];

        progressTextView.setText("" + (pl + 1) + "/" + objectName.length);
        pointTextView.setText("" + point);

        int remainingChar = SelectionButtons.SELECTION_BUTTON_TOTAL - currentObjectName.length();
        Log.i("MINE", currentObjectName);
        String objectNameWithRandChar = currentObjectName + randomCharAdd(remainingChar);
        objectNameWithRandChar = shuffleChar(objectNameWithRandChar);
        String objectNameWithRandCharNoSpace = replaceWhiteSpaceWithRandomChar(objectNameWithRandChar);
        selectionButtonsLayout.fillButtonWithChar(objectNameWithRandCharNoSpace);
        imageViewObject.setImageResource(objectImage.getResourceId(pl, 0));

        answerButtonsLayout.setAnswerButtonVisibility(currentObjectName);
    }

    @Override
    public void answerButtonIsFullListener(boolean isFull, String playerAnswer) {
        if(isFull && currentObjectName.equals(playerAnswer)){                                                   //
            YoYo.with(Techniques.SlideOutLeft).duration(500).repeat(0).playOn(selectionButtonsLayout);          //
            YoYo.with(Techniques.SlideOutLeft).duration(500).repeat(0).playOn(answerButtonsLayout);             //
            linearLayoutFinalAns.setVisibility(View.VISIBLE);                                                   //
            YoYo.with(Techniques.SlideInRight).duration(500).repeat(0).playOn(linearLayoutFinalAns);            //  things to do if answer is correct
            textViewFinalAns.setText(currentObjectName);                                                        //
            YoYo.with(Techniques.ZoomIn).duration(700).repeat(0).playOn(textViewFinalAns);                      //
            YoYo.with(Techniques.ZoomIn).duration(700).repeat(0).playOn(textViewPointAdd);                      //
            YoYo.with(Techniques.ZoomIn).duration(700).repeat(0).playOn(imageButtonContinue);                   //
            YoYo.with(Techniques.FadeOut).duration(700).repeat(0).playOn(buttonRevealOneLetter);                //
            YoYo.with(Techniques.FadeOut).duration(700).repeat(0).playOn(buttonRemoveAllLetter);                //
        } else {
            YoYo.with(Techniques.Shake)                         //
                    .duration(1000)                             //  Animation if answer is wrong
                    .repeat(0)                                  //
                    .playOn(answerButtonsLayout);               //

            Log.i("MINE", ""+falseAnswerCounter);
            falseAnswerCounter = falseAnswerCounter + 1;
            if(falseAnswerCounter >= SHOW_AD_WHEN_FALSE_ANSWER_REACH && point < 4) {
                showVideoAd();
            }
            if(point >= 4){
                falseAnswerCounter = 0;
            }
        }
    }

    @Override
    public void answerButtonClicked(AnswerButton answerButton) {
        if(!answerButtonsLayout.isButtonEmpty(answerButton)){
            int lastPosit = answerButton.getLastPosition();
            selectionButtonsLayout.getSquareButton(lastPosit).setText(answerButton.getText());
            YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(selectionButtonsLayout.getSquareButton(lastPosit));
            answerButton.setText("");
        }
    }

    @Override
    public void selectionButtonClickListener(SquareButton squareButton) {
        if(!answerButtonsLayout.isFull()){
            int posit = answerButtonsLayout.getMostLeftEmptyButton();
            answerButtonsLayout.getAnswerButton(posit).setText(squareButton.getText());
            YoYo.with(Techniques.Tada).duration(700).repeat(0).playOn(answerButtonsLayout.getAnswerButton(posit));
            answerButtonsLayout.getAnswerButton(posit).setLastPosition(squareButton.getLastPosition());
            squareButton.setText("");
        }
        answerButtonsLayout.isFull();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.imageButtonContinue:
                progressValue++;
                point = point + 1;  //Add 1 point every correct answer
                YoYo.with(Techniques.SlideOutLeft).duration(500).repeat(0).playOn(linearLayoutFinalAns);
                initLoopGame();
                YoYo.with(Techniques.FadeIn).duration(500).repeat(0).playOn(imageViewObject);
                YoYo.with(Techniques.SlideInRight).duration(500).repeat(0).playOn(selectionButtonsLayout);
                YoYo.with(Techniques.SlideInRight).duration(500).repeat(0).playOn(answerButtonsLayout);
                YoYo.with(Techniques.SlideInRight).duration(700).repeat(0).playOn(buttonRevealOneLetter);
                YoYo.with(Techniques.SlideInRight).duration(700).repeat(0).playOn(buttonRemoveAllLetter);
                break;
            case R.id.buttonRevealOneLetter:
                int revealAt = answerButtonsLayout.getMostLeftEmptyButton();
                if(point >= 4 && revealAt < currentObjectName.length()) {
                    int selectionButtonPos = selectionButtonsLayout.getButtonPositionContain(currentObjectName.charAt(revealAt));
                    selectionButtonsLayout.getSquareButton(selectionButtonPos).performClick();
                    point = point - 4;
                    pointTextView.setText(""+point);
                    YoYo.with(Techniques.Wobble).duration(500).repeat(0).playOn(pointTextView);
                    answerButtonsLayout.isFull();
                    saveProgressAndPointValue(sharedStr, progressValue, point);
                } else if (point < 4) {
                    YoYo.with(Techniques.Shake).duration(500).repeat(0).playOn(pointTextView);
                }
                else {
                    YoYo.with(Techniques.Shake).duration(500).repeat(0).playOn(answerButtonsLayout);
                }
                break;
            case R.id.buttonRemoveAll:
                returnBackAllAnswer();
                break;
            case R.id.progressTextview:
                Intent gridIntent = new Intent(this, GridViewActivity.class);
                gridIntent.putExtra("TO_GRID_VIEW", sharedStr);
                startActivity(gridIntent);
                break;
        }
    }

    private void createCongratulationDialog(){
        CustomAlertDialogBuilder dialogBuilder = new CustomAlertDialogBuilder(this);
        dialogBuilder.setTitle("Navi-no ma");
        dialogBuilder.setMessage("");
        dialogBuilder.setPositiveButtonText("OK");
        dialogBuilder.setNegativeButtonVisibility(View.INVISIBLE);
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

    private void showVideoAd(){
        Log.i("MINE", "Show Add");
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
            Log.i("MINE", "Show Add is loaded");
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.i("MINE", "Show Add is opev");
    }

    @Override
    public void onRewardedVideoStarted() {
        falseAnswerCounter = 0;
        KToast.infoToast(this, "Watch video till the end to get 10 Points!!!", Gravity.BOTTOM, KToast.LENGTH_LONG);
        Log.i("MINE", "Show Add is started");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.i("MINE", "Show Add is closed");
        mRewardedVideoAd.loadAd(getResources().getString(R.string.ad_id_video), new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.i("MINE", "Show Add is rewarded");
        point += 10;
        pointTextView.setText("" + point);
        saveProgressAndPointValue(sharedStr, progressValue, point);
        Log.i("MINE", ""+point);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.i("MINE", "Show Add left");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Log.i("MINE", "Show Add is fail");
    }

    @Override
    public void onRewardedVideoCompleted() {
        Log.i("MINE", "Show Add is complete");
    }

    ///////////////android//////////////////////

    @Override
    protected void onPause() {
        if(mRewardedVideoAd != null)
            mRewardedVideoAd.pause(this);
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mRewardedVideoAd != null)
            mRewardedVideoAd.resume(this);
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if(mRewardedVideoAd != null)
            mRewardedVideoAd.destroy(this);
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

        //----------------------------------------------------------------------------------------------//
    // Methods needed for the game
    //----------------------------------------------------------------------------------------------//

    private void returnBackAllAnswer() {
        if(!answerButtonsLayout.isAllEmpty()){
            for(int j = 0; j < currentObjectName.length(); j++){
                AnswerButton b = answerButtonsLayout.getAnswerButton(j);
                if(b.getText().length() > 0) {
                    int lastPosit = b.getLastPosition();
                    selectionButtonsLayout.getSquareButton(lastPosit).setText(b.getText());
                    b.setText("");
                }
            }
        }

    }

    public String shuffleChar(String input){
        List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    public String replaceWhiteSpaceWithRandomChar(String txt){
        StringBuilder builder = new StringBuilder(txt);
        int index = txt.indexOf(" ");
        if(txt.contains(" ")){
            builder.replace(index, index + 1, randomCharAdd(1));
        }
        return  builder.toString();
    }

    public String randomCharAdd(int txtLen){
        StringBuilder str = new StringBuilder();
        Random r;
        char c;

        for(int counter = 0; counter < txtLen; counter++){
            r = new Random();
            c = (char)(r.nextInt(26) + 'A');
            str.append(c);
        }
        return str.toString();
    }


}