package com.jmm.android.assignment1.model;

import java.io.Serializable;
import java.util.Date;

/**
 * An EmotionEntry represents one instance of an Emotion being created by a user and it
 * is created when the user clicks on one of the emotion icons in MainActivity.
 *
 * This is the element that is displayed by ListFragment's recycler view
 */
public class EmotionEntry implements Serializable {
    private Emotion mEmotion;
    private Date mDate;
    private String mComment;

    public EmotionEntry(Emotion emotion) {
        mEmotion = emotion;
        mDate = new Date();
        mComment = "";
    }

    public Emotion getEmotion() {
        return mEmotion;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        // Originally used a constant MAXCOMMENTLENGTH but I simply set the EditText's max length instead
        mComment = comment;
    }
}
