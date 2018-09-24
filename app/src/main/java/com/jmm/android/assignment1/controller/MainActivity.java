package com.jmm.android.assignment1.controller;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.Emotion;
import com.jmm.android.assignment1.model.EmotionEntry;
import com.jmm.android.assignment1.model.EmotionType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageView mJoyImageView;
    private ImageView mSadnessImageView;
    private ImageView mSurpriseImageView;
    private ImageView mLoveImageView;
    private ImageView mAngerImageView;
    private ImageView mFearImageView;

    private RecyclerView mEmotionEntryRecyclerView;
    private EmotionEntryAdapter mEmotionEntryAdapter;

    private List<EmotionEntry> mEmotionEntries;
    private Map<EmotionType, Integer> mEmotionCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmotionEntries = new ArrayList<>();

        // Get all activity_main view references
        mJoyImageView = findViewById(R.id.joy_image_view);
        mSadnessImageView = findViewById(R.id.sadness_image_view);
        mSurpriseImageView = findViewById(R.id.surprise_image_view);
        mLoveImageView = findViewById(R.id.love_image_view);
        mAngerImageView = findViewById(R.id.anger_image_view);
        mFearImageView = findViewById(R.id.fear_image_view);


        mEmotionEntryRecyclerView = findViewById(R.id.emotion_entry_recycler_view);
        // Set up RecyclerView's view layout and its adapter
        mEmotionEntryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEmotionEntryAdapter = new EmotionEntryAdapter();
        mEmotionEntryRecyclerView.setAdapter(mEmotionEntryAdapter);


        // Set up on click listener for all image views
        EmotionOnClickListener emotionOnClickListener = new EmotionOnClickListener();

        mJoyImageView.setOnClickListener(emotionOnClickListener);
        mSadnessImageView.setOnClickListener(emotionOnClickListener);
        mSurpriseImageView.setOnClickListener(emotionOnClickListener);
        mLoveImageView.setOnClickListener(emotionOnClickListener);
        mAngerImageView.setOnClickListener(emotionOnClickListener);
        mFearImageView.setOnClickListener(emotionOnClickListener);



    }

    private void updateAdapter(int position) {
        mEmotionEntryAdapter.notifyItemChanged(position);
    }

    class EmotionOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Emotion emotion = getEmotionFromResourceId(view.getId());
            EmotionEntry emotionEntry = new EmotionEntry(emotion);

            mEmotionEntries.add(emotionEntry);
            updateAdapter(mEmotionEntries.size() - 1);
            System.out.println(mEmotionEntries.size());
        }
    }

    /* Reference used for using RecyclerView, RecyclerView.Adapter and RecyclerView.ViewHolder
    https://developer.android.com/guide/topics/ui/layout/recyclerview#java
     */
    class EmotionEntryAdapter extends RecyclerView.Adapter<EmotionEntryAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.list_item_emotion, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EmotionEntry emotionEntry = mEmotionEntries.get(position);
            holder.bind(emotionEntry);
        }

        @Override
        public int getItemCount() {
            return mEmotionEntries.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView mEmotionImageView;
            private TextView mDateTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mEmotionImageView = itemView.findViewById(R.id.emotion_image_view);
                mDateTextView = itemView.findViewById(R.id.date_text_view);
            }

            public void bind(EmotionEntry emotionEntry) {
                int emotionImageId = getDrawableIdFromEmotion(emotionEntry.getEmotion());
                mEmotionImageView.setImageResource(emotionImageId);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                String dateString = simpleDateFormat.format(emotionEntry.getDate());
                mDateTextView.setText(dateString);
            }

        }


    }

    @Nullable
    private Emotion getEmotionFromResourceId(int resourceId) {
        switch (resourceId) {
            case R.id.joy_image_view:
                return new Emotion(EmotionType.JOY);
            case R.id.sadness_image_view:
                return new Emotion(EmotionType.SADNESS);
            case R.id.surprise_image_view:
                return new Emotion(EmotionType.SURPRISE);
            case R.id.love_image_view:
                return new Emotion(EmotionType.LOVE);
            case R.id.anger_image_view:
                return new Emotion(EmotionType.ANGER);
            case R.id.fear_image_view:
                return new Emotion(EmotionType.FEAR);
            default:
                // TODO: Raise an exception
                return null;
        }
    }

    private int getDrawableIdFromEmotion(Emotion emotion) {
        switch (emotion.getEmotionType()) {
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
                // TODO: Raise an exception
                return 0;
        }
    }

}
