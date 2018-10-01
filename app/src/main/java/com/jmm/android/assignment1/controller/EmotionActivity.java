package com.jmm.android.assignment1.controller;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.EmotionEntry;

import java.util.Date;

public class EmotionActivity extends AppCompatActivity implements DateDialogFragment.Callbacks {

    public final static String EXTRA_EMOTION = "com.jmm.android.assignment1.extra_emotion";
    public final static int RESULT_DELETE = 2;

    public final static int REQUEST_DATE = 0;

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

        mCommentEditText.setText(mEmotionEntry.getComment());
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = DateDialogFragment.newInstance(mEmotionEntry.getDate());
                dialogFragment.show(getSupportFragmentManager(), TAG);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_emotion, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_emotion:
                setResult(RESULT_DELETE, null);
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(EXTRA_EMOTION, mEmotionEntry);
        Log.d(TAG, mEmotionEntry.getComment());
        setResult(Activity.RESULT_OK, data);

        super.onBackPressed();
    }

    @Override
    public void onDateChanged(Date date) {
        mEmotionEntry.setDate(date);
        updateDate();
    }

    private void updateDate() {
        mDateButton.setText(mEmotionEntry.getDate().toString());
    }

}
