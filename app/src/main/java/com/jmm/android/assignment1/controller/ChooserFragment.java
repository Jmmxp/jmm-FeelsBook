package com.jmm.android.assignment1.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.Emotion;
import com.jmm.android.assignment1.model.EmotionType;

/**
 * This fragment represents the top of the main activity's layout (the emotion chooser)
 * Its ImageView references and its corresponding click listeners are all defined and used in this
 * fragment.
 */

public class ChooserFragment extends Fragment {

    private ImageView mJoyImageView;
    private ImageView mSadnessImageView;
    private ImageView mSurpriseImageView;
    private ImageView mLoveImageView;
    private ImageView mAngerImageView;
    private ImageView mFearImageView;

    private ImageView[] mEmotionImageViews;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onEmotionAdded(EmotionType emotionType);
        void onCountRequested(EmotionType emotionType);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbacks = (Callbacks) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooser, container, false);

        // Get all references to the views
        mJoyImageView = view.findViewById(R.id.joy_image_view);
        mSadnessImageView = view.findViewById(R.id.sadness_image_view);
        mSurpriseImageView = view.findViewById(R.id.surprise_image_view);
        mLoveImageView = view.findViewById(R.id.love_image_view);
        mAngerImageView = view.findViewById(R.id.anger_image_view);
        mFearImageView = view.findViewById(R.id.fear_image_view);

        mEmotionImageViews = new ImageView[] {
                mJoyImageView,
                mSadnessImageView,
                mSurpriseImageView,
                mLoveImageView,
                mAngerImageView,
                mFearImageView
        };

        // Set up on click listeners for all image views
        EmotionOnClickListener emotionOnClickListener = new EmotionOnClickListener();
        EmotionOnLongClickListener emotionOnLongClickListener = new EmotionOnLongClickListener();

        for (ImageView emotionImageView : mEmotionImageViews) {
            emotionImageView.setOnClickListener(emotionOnClickListener);
            emotionImageView.setOnLongClickListener(emotionOnLongClickListener);
        }

        return view;
    }

    /* OnClickListener that tells MainActivity when one of the emotions on the top bar is clicked
    This action will be sent to ListFragment and tells it to make a new EmotionEntry that has
    an Emotion of the type of the emotion that was clicked
    */
    class EmotionOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            EmotionType emotionType = Emotion.getEmotionTypeFromResourceId(view.getId());

            mCallbacks.onEmotionAdded(emotionType);
        }
    }

    /* OnLongClickListener that tells MainActivity when one of the emotions on the top bar is long clicked
    This action will be sent to ListFragment and tells it to display the count for that emotion
    */
    class EmotionOnLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View view) {
            EmotionType emotionType = Emotion.getEmotionTypeFromResourceId(view.getId());

            mCallbacks.onCountRequested(emotionType);
            return true;
        }
    }

}
