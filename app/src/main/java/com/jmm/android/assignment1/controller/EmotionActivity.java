package com.jmm.android.assignment1.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private EmotionEntry mEmotionEntry;
    private ImageView mEmotionImageView;
    private Button mDateButton;
    private EditText mCommentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        mEmotionEntry = (EmotionEntry) getIntent().getSerializableExtra(EXTRA_EMOTION);

        mEmotionImageView = findViewById(R.id.emotion_image_view);
        mDateButton = findViewById(R.id.date_button);
        mCommentEditText = findViewById(R.id.comment_edit_text);

        mEmotionImageView.setImageResource(mEmotionEntry.getEmotion().getDrawableId());
        mDateButton.setText(mEmotionEntry.getDate().toString());
        mCommentEditText.setText(mEmotionEntry.getComment());

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEmotionEntry.setComment(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCommentEditText.setSelection(mCommentEditText.length());

    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(EXTRA_EMOTION, mEmotionEntry);
        Log.d(TAG, mEmotionEntry.getComment());
        setResult(Activity.RESULT_OK, data);

        super.finish();
    }
}
