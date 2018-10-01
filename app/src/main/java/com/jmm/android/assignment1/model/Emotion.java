package com.jmm.android.assignment1.model;

import android.support.annotation.Nullable;

import com.jmm.android.assignment1.R;

import java.io.Serializable;

public class Emotion implements Serializable {
    private EmotionType mEmotionType;

    public Emotion(EmotionType emotionType) {
        mEmotionType = emotionType;
    }

    public EmotionType getEmotionType() {
        return mEmotionType;
    }

    public int getDrawableId() {
        switch (this.getEmotionType()) {
            case JOY:
                return R.drawable.joy;
            case SADNESS:
                return R.drawable.sadness;
            case SURPRISE:
                return R.drawable.surprise;
            case LOVE:
                return R.drawable.love;
            case ANGER:
                return R.drawable.anger;
            case FEAR:
                return R.drawable.fear;
            default:
                return 0;
        }
    }

    @Nullable
    public static EmotionType getEmotionTypeFromResourceId(int resourceId) {
        switch (resourceId) {
            case R.id.joy_image_view:
                return EmotionType.JOY;
            case R.id.sadness_image_view:
                return EmotionType.SADNESS;
            case R.id.surprise_image_view:
                return EmotionType.SURPRISE;
            case R.id.love_image_view:
                return EmotionType.LOVE;
            case R.id.anger_image_view:
                return EmotionType.ANGER;
            case R.id.fear_image_view:
                return EmotionType.FEAR;
            default:
                return null;
        }
    }

}
