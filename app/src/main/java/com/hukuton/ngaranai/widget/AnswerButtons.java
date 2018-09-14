package com.hukuton.ngaranai.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hukuton.ngaranai.R;
import com.hukuton.ngaranai.interfaces.AnswerListener;

import java.util.ArrayList;

public class AnswerButtons extends LinearLayout{

    private String answerButtonId[] = { "ab1", "ab2", "ab3", "ab4", "ab5", "ab6", "ab7", "ab8", "ab9", "ab10", "ab11", "ab12",
            "ab13", "ab14", "ab15", "ab16", "ab17", "ab18", "ab19", "ab20", "ab21", "ab22", "ab23", "ab24"};

    private ArrayList<AnswerButton> answerButton = new ArrayList<>(24);

    private String firstLineAnswer = "";
    private String secondLineAnswer = "";

    private boolean answerIsOneLine = false ;

    private static final int SECOND_LINE = 13;

    AnswerListener answerListener;

    public AnswerButtons(Context context) {
        super(context);
        init(context);
    }

    public AnswerButtons(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnswerButtons(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        inflate(context, R.layout.answer_buttons, this);

        for(int i = 0; i < answerButtonId.length; i++){
            int temp = getResources().getIdentifier(answerButtonId[i], "id", context.getPackageName());
            answerButton.add((AnswerButton) findViewById(temp));
            answerButton.get(i).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    AnswerButton b = (AnswerButton) view;
                    if(!isButtonEmpty(b)){
                        answerListener.answerButtonClicked(b);
                        b.setText("");
                    }
                }
            });
        }
    }

    public AnswerButton getAnswerButton(int index){
        return answerButton.get(index);
    }

    public void setAnswerButtonVisibility(String ans){
        int firstLine;
        answerIsOneLine = isOneLineAnswer(ans);

        if(answerIsOneLine) {
            firstLineAnswer = ans;
            firstLine = ans.length();
            setAnswerButtonVisibility(0, firstLine);
        }
        else {
            firstLineAnswer = ans.substring(0, ans.indexOf(" "));
            secondLineAnswer = ans.substring(ans.indexOf(" ") + 1, ans.length());
            setAnswerButtonVisibility(0, firstLineAnswer.length());
            setAnswerButtonVisibility(SECOND_LINE, secondLineAnswer.length());
        }
    }


    private void setAnswerButtonVisibility(int startButtonPosition, int length){
        for(int i = startButtonPosition; i < length + startButtonPosition; i++){
            answerButton.get(i).setVisibility(View.VISIBLE);
        }
    }

    private String getPlayerAnswer(){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < SECOND_LINE; i++) {
            if (!isButtonEmpty(answerButton.get(i)))
                stringBuilder.append(answerButton.get(i).getText());
        }

        if(!answerIsOneLine){
            stringBuilder.append(" ");
            for(int i = SECOND_LINE; i < answerButtonId.length; i++) {
                if (!isButtonEmpty(answerButton.get(i)))
                    stringBuilder.append(answerButton.get(i).getText());
            }
        }
        return stringBuilder.toString();
    }

    public void setSingleChar(Character c, int lastPosition){
        answerButton.get(getMostLeftEmptyButton()).setText(c);
        answerButton.get(getMostLeftEmptyButton()).setLastPosition(lastPosition);
    }

    public void resetView(){
        for(int i = 0; i < answerButtonId.length; i++){
            answerButton.get(i).setText("");
            answerButton.get(i).setVisibility(View.GONE);
        }
    }

    public boolean isButtonEmpty(AnswerButton answerButton){
        String str = answerButton.getText().toString();
        if(str.length() > 0)
            return false;
        else
            return true;
    }

    private boolean isOneLineAnswer(String ans){
        if(ans.contains(" "))
            return false;
        else
            return true;
    }

    //To get the most left empty button on answer button
    //Fill in first from left to right
    public int getMostLeftEmptyButton(){

        if(answerIsOneLine || !isLineOneFull()) {
            return getIndexMostLeftEmptyFirstLineButton();
        }
        else {
            return getIndexMostLeftEmptySecondLineButton();
        }
    }

    private int getIndexMostLeftEmptyFirstLineButton(){
        int pos = 0;
        for (int c = 0; c < SECOND_LINE; c++) {
            if (answerButton.get(c).getVisibility() == View.VISIBLE) {
                if(!isButtonEmpty(answerButton.get(c)))
                    pos++;
                else
                    return pos;
            }
        }
        return pos;
    }

    private int getIndexMostLeftEmptySecondLineButton(){
        int pos = SECOND_LINE;
        for (int c = SECOND_LINE; c < answerButton.size(); c++) {
            if (answerButton.get(c).getVisibility() == View.VISIBLE) {
                if(!isButtonEmpty(answerButton.get(c)))
                    pos++;
                else
                    return pos;
            }
        }
        return pos;
    }

    private boolean isLineOneFull(){
        int pos = 0;
        for (int c = 0; c < SECOND_LINE; c++) {
            if (answerButton.get(c).getVisibility() == View.VISIBLE) {
                if(!isButtonEmpty(answerButton.get(c)))
                    pos++;
            }
        }

        if(pos == firstLineAnswer.length())
            return true;
        else
            return false;
    }

    private boolean isLineTwoFull(){
        int pos = SECOND_LINE;
        for (int c = SECOND_LINE; c < answerButton.size(); c++) {
            if (answerButton.get(c).getVisibility() == View.VISIBLE) {
                if(!isButtonEmpty(answerButton.get(c)))
                    pos++;
            }
        }

        if(pos == (SECOND_LINE + secondLineAnswer.length()))
            return true;
        else
            return false;
    }

    public void setAnswerListener(AnswerListener answerListener){
        this.answerListener = answerListener;
    }

    public boolean isFull(){

        boolean allAnswerButtonFull = false;

        if(answerIsOneLine && isLineOneFull()){
            answerListener.answerButtonIsFullListener(true, getPlayerAnswer());
            allAnswerButtonFull =  true;
        } else if(!answerIsOneLine && isLineOneFull() && isLineTwoFull()){
            answerListener.answerButtonIsFullListener(true, getPlayerAnswer());
            allAnswerButtonFull =  true;
        } else
            allAnswerButtonFull =  false;

        return allAnswerButtonFull;
    }

    public boolean isAllEmpty(){
        StringBuilder builder = new StringBuilder();
        for(int j = 0; j < answerButton.size(); j++){
            builder.append(answerButton.get(j).getText());
        }

        if(builder.length() > 0)
            return false;
        else
            return true;
    }
}
