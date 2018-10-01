package com.jmm.android.assignment1.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.EmotionEntry;

import java.util.Date;

/**
 * This activity is started whenever an EmotionEntry in ListFragment's RecyclerView list is clicked.
 * Its views are populated with the entry's info and the activity allows the user to edit the
 * entry's comment and date (both day and time).
 *
 * It may send a result of RESULT_DELETE (2) back to MainActivity if the entry is asked by the user
 * to be deleted, or Activity.RESULT_OK (1) if the user presses the back button, which will
 * successfully save the EmotionEntry to the list.
 */
public class EmotionActivity extends AppCompatActivity implements DateDialogFragment.Callbacks, TimeDialogFragment.Callbacks {

    public final static String EXTRA_EMOTION = "com.jmm.android.assignment1.extra_emotion";
    public final static int RESULT_DELETE = 2;

    private final static String TAG = "EmotionActivity";

    private EmotionEntry mEmotionEntry;
    private ImageView mEmotionImageView;
    private TextView mDateTextView;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mCommentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);

        mEmotionEntry = (EmotionEntry) getIntent().getSerializableExtra(EXTRA_EMOTION);

        // Get references to the views
        mEmotionImageView = findViewById(R.id.emotion_image_view);
        mDateTextView = findViewById(R.id.date_text_view);
        mDateButton = findViewById(R.id.date_button);
        mTimeButton = findViewById(R.id.time_button);
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

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = TimeDialogFragment.newInstance(mEmotionEntry.getDate());
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
                // When the menu delete option is clicked, set the result for MainActivity
                // to be RESULT_DELETE so it knows to delete the current EmotionEntry
                setResult(RESULT_DELETE, null);
                finish();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        // Send a successful result back to MainActivity so it knows to update the current EmotionEntry
        Intent data = new Intent();
        data.putExtra(EXTRA_EMOTION, mEmotionEntry);
        setResult(Activity.RESULT_OK, data);

        super.onBackPressed();
    }

    /* onDateChanged and onTimeChanged are Callback methods that are called when Date and
    TimeDialogFragment's positive (OK) buttons are clicked;

    These methods are not called if the negative (CANCEL) buttons are clicked.
    */
    @Override
    public void onDateChanged(Date date) {
        mEmotionEntry.setDate(date);
        updateDate();
    }

    @Override
    public void onTimeChanged(Date date) {
        mEmotionEntry.setDate(date);
        updateDate();
    }

    private void updateDate() {
        mDateTextView.setText(mEmotionEntry.getDate().toString());
    }


}
