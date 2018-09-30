package com.jmm.android.assignment1.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.EmotionEntry;

public class EmotionActivity extends AppCompatActivity {

    public final static String EXTRA_EMOTION = "com.jmm.android.assignment1.extra_emotion";

    private final static String TAG = "EmotionActivity";

    private ImageView mEmotionImageView;
    private Button mDateButton;
    private EditText mCommentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        EmotionEntry emotionEntry = (EmotionEntry) getIntent().getSerializableExtra(EXTRA_EMOTION);
        Log.d(TAG, String.valueOf(emotionEntry == null));

        mEmotionImageView = findViewById(R.id.emotion_image_view);
        mDateButton = findViewById(R.id.date_button);
        mCommentEditText = findViewById(R.id.comment_edit_text);

        mEmotionImageView.setImageResource(emotionEntry.getEmotion().getDrawableId());
        mDateButton.setText(emotionEntry.getDate().toString());
        mCommentEditText.setText(emotionEntry.getComment());

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
