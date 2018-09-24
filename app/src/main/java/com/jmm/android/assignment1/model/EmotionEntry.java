package com.jmm.android.assignment1.model;

import java.util.Date;

public class EmotionEntry {
    private Emotion mEmotion;
    private Date mDate;
    private String mComment;

    public static final int MAX_COMMENT_LENGTH = 100;

    public EmotionEntry(Emotion emotion) {
        mEmotion = emotion;
        mDate = new Date();
        mComment = "";
    }

    public EmotionEntry(Emotion emotion, String comment) {
        mEmotion = emotion;
        mDate = new Date();
        mComment = comment;
    }

    public Emotion getEmotion() {
        return mEmotion;
    }

    public Date getDate() {
        return mDate;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        if (comment.length() <= MAX_COMMENT_LENGTH) {
            mComment = comment;
        } else {
            // Handle comment length being too long
        }
    }
}
