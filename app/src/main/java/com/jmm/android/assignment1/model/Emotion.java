package com.jmm.android.assignment1.model;

public class Emotion {
    private EmotionType mEmotionType;

    public Emotion(EmotionType emotionType) {
        mEmotionType = emotionType;
    }

    public EmotionType getEmotionType() {
        return mEmotionType;
    }

}
