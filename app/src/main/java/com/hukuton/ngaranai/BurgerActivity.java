package com.hukuton.ngaranai;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hukuton.ngaranai.dialog.CustomAlertDialogBuilder;
import com.hukuton.ngaranai.interfaces.OnDialogClickListener;
import com.hukuton.ngaranai.widget.MyFButton;

public class BurgerActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger);

        findViewById(R.id.imageButtonBack).setOnClickListener(this);
        findViewById(R.id.buttonShare).setOnClickListener(this);
        findViewById(R.id.buttonReset).setOnClickListener(this);
        findViewById(R.id.buttonAbout).setOnClickListener(this);
        findViewById(R.id.buttonContact).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageButtonBack:
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
            case R.id.buttonShare:
                shareDialog();
                break;
            case R.id.buttonReset:
                resetGamePlayDialog();
                break;
            case R.id.buttonAbout:
                aboutDialog();
                break;
            case R.id.buttonContact:
                contactDialog();
                break;
        }
    }

    private void shareDialog(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String sAux = "Timai-ka dikou iti game.\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    private void contactDialog() {
        CustomAlertDialogBuilder contactDialogBuilder = new CustomAlertDialogBuilder(this);
        contactDialogBuilder.setTitle("CONTACT");
        contactDialogBuilder.setMessage("Kalu ong varo duaton. \n\nhukuton.pl@gmail.com");
        contactDialogBuilder.setPositiveButtonText("facebook");
        contactDialogBuilder.seNegativeButtonText("e-mail");
        final AlertDialog alertAbout = contactDialogBuilder.show();
        contactDialogBuilder.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void positiveButtonClickListener(MyFButton fButton) {
                //open fb app
                Intent intent;
                String pageId = "199305283786450";
                try{
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + pageId));
                } catch (Exception e){
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook/" + pageId));
                }
                startActivity(intent);
                alertAbout.dismiss();
            }

            @Override
            public void negativeButtonClickListener(MyFButton fButton) {
                //open email app
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"hukuton.pl@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Avantang mai");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Binuli...");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));

                alertAbout.dismiss();
            }
        });
    }

    private void aboutDialog() {
        CustomAlertDialogBuilder aboutDialogBuilder = new CustomAlertDialogBuilder(this);
        aboutDialogBuilder.setTitle("ABOUT");
        aboutDialogBuilder.setMessage("Atag ma ong ozi kou mimomoi diti game. " +
                "\n\nHUKUTON may 2018");
        aboutDialogBuilder.setNegativeButtonVisibility(View.INVISIBLE);
        aboutDialogBuilder.setPositiveButtonText("OK");
        final AlertDialog alertAbout = aboutDialogBuilder.show();
        aboutDialogBuilder.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void positiveButtonClickListener(MyFButton fButton) {
                alertAbout.dismiss();
            }

            @Override
            public void negativeButtonClickListener(MyFButton fButton) {
                alertAbout.dismiss();
            }
        });

    }

    private void resetGamePlayDialog(){
        final CustomAlertDialogBuilder dialogBuilder = new CustomAlertDialogBuilder(this);
        dialogBuilder.setTitle("RESET");
        dialogBuilder.setMessage("YOU WILL LOSE ALL PROGRESS!!!");
        dialogBuilder.setPositiveButtonText("RESET");
        dialogBuilder.seNegativeButtonText("CANCEL");
        final AlertDialog alert = dialogBuilder.show();
        dialogBuilder.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void positiveButtonClickListener(MyFButton fButton) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(MainActivity.SHARED_PREF, 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                alert.dismiss();
            }

            @Override
            public void negativeButtonClickListener(MyFButton fButton) {
                alert.dismiss();
            }
        });
    }
}
