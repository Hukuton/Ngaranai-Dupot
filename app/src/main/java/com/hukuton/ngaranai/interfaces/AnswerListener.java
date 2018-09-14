package com.hukuton.ngaranai.interfaces;

import com.hukuton.ngaranai.widget.AnswerButton;

public interface AnswerListener {
    public void answerButtonIsFullListener(boolean isFull, String playerAnswer);
    public void answerButtonClicked(AnswerButton answerButton);
}
