package com.jmm.android.assignment1.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.Emotion;
import com.jmm.android.assignment1.model.EmotionEntry;
import com.jmm.android.assignment1.model.EmotionType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFragment extends Fragment {

    private int REQUEST_EMOTION = 0;

    private static final String TAG = "ListFragment";
    private static final String ENTRIES_FILENAME = "entries.sav";
    private static final String COUNTS_FILENAME = "counts.sav";

    private RecyclerView mEmotionEntryRecyclerView;
    private EmotionEntryAdapter mEmotionEntryAdapter;

    private List<EmotionEntry> mEmotionEntries;
    private Map<EmotionType, Integer> mEmotionCounts;

    private int mEmotionEntryIndex;
    private boolean mRequireLoadFromFile = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize entries and counts data structures
        mEmotionEntries = new ArrayList<>();
        mEmotionCounts = new HashMap<>();

        // Initialize all emotion counts to zero
        setDefaultEmotionCounts();

        if (mRequireLoadFromFile) {
            loadFromFile();
            mRequireLoadFromFile = false;
        }

        mEmotionEntryRecyclerView = view.findViewById(R.id.emotion_entry_recycler_view);
        // Set up RecyclerView's view layout and its adapter
        mEmotionEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mEmotionEntryAdapter = new EmotionEntryAdapter();
        mEmotionEntryRecyclerView.setAdapter(mEmotionEntryAdapter);

        return view;
    }


    public void showEmotionCount(EmotionType emotionType) {
        int count = mEmotionCounts.get(emotionType);
        String toastString = String.valueOf(count) + " " + emotionType.toString() + " emotions";

        Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
    }

    public void addEmotionEntry(EmotionType emotionType) {
        EmotionEntry emotionEntry = new EmotionEntry(new Emotion(emotionType));
        mEmotionEntries.add(emotionEntry);

        startEmotionActivity(emotionEntry);

        // Increment count for the emotion type clicked;
        mEmotionCounts.put(emotionType, mEmotionCounts.get(emotionType) + 1);

        // Notify RecyclerView adapter of the emotion entry we just added
        updateAdapter();

    }

    public void updateAdapter() {
        sortEmotionEntries();
        mEmotionEntryAdapter.notifyDataSetChanged();
        saveToFile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EMOTION) {
            if (resultCode == Activity.RESULT_OK) {
                EmotionEntry emotionEntry = (EmotionEntry)
                        data.getSerializableExtra(EmotionActivity.EXTRA_EMOTION);

                mEmotionEntries.set(mEmotionEntryIndex, emotionEntry);
            }

            if (resultCode == EmotionActivity.RESULT_DELETE) {
                EmotionType emotionType = mEmotionEntries.get(mEmotionEntryIndex)
                        .getEmotion().getEmotionType();

                int count = mEmotionCounts.get(emotionType);
                mEmotionCounts.put(emotionType, count - 1);

                mEmotionEntries.remove(mEmotionEntryIndex);
                mEmotionEntryAdapter.notifyItemRemoved(mEmotionEntryIndex);
            }

            updateAdapter();
        }


    }

    private void sortEmotionEntries() {
        Collections.sort(mEmotionEntries);
    }

    private void startEmotionActivity(EmotionEntry emotionEntry) {
        Intent intent = new Intent(getActivity(), EmotionActivity.class);
        intent.putExtra(EmotionActivity.EXTRA_EMOTION, emotionEntry);

        startActivityForResult(intent, REQUEST_EMOTION);
        mEmotionEntryIndex = mEmotionEntries.indexOf(emotionEntry);
    }

    private void setDefaultEmotionCounts() {
        for (EmotionType emotionType : EmotionType.values()) {
            mEmotionCounts.put(emotionType, 0);
        }
    }

    /* Reference used for using RecyclerView, RecyclerView.Adapter and RecyclerView.ViewHolder
    https://developer.android.com/guide/topics/ui/layout/recyclerview#java
     */
    class EmotionEntryAdapter extends RecyclerView.Adapter<EmotionEntryAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity())
                    .inflate(R.layout.list_item_emotion, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            EmotionEntry emotionEntry = mEmotionEntries.get(position);
            holder.bind(emotionEntry);
        }

        /* References I looked at for setting up an onClickListener for ViewHolders:
        https://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener
        User: Lee @ https://stackoverflow.com/users/700206/lee

        Android Big Nerd Ranch book, Listing 8.24 "Detecting presses in CrimeHolder"
        */
        @Override
        public int getItemCount() {
            return mEmotionEntries.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private EmotionEntry mEmotionEntry;
            private ImageView mEmotionImageView;
            private TextView mDateTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mEmotionImageView = itemView.findViewById(R.id.emotion_image_view);
                mDateTextView = itemView.findViewById(R.id.date_button);

                itemView.setOnClickListener(this);
            }

            public void bind(EmotionEntry emotionEntry) {
                mEmotionEntry = emotionEntry;

                int emotionImageId = emotionEntry.getEmotion().getDrawableId();
                mEmotionImageView.setImageResource(emotionImageId);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateString = simpleDateFormat.format(emotionEntry.getDate());
                mDateTextView.setText(dateString);
            }

            @Override
            public void onClick(View view) {
                startEmotionActivity(mEmotionEntry);
                mEmotionEntryIndex = getAdapterPosition();
            }

        }

    }

    /**
     * loadFromFile() and saveFromFile() code was used from the code that was shown to us by TAs
     * from the CMPUT 301 Lab 3: Java I/O and Persistence with GSON.
     * The base lonelyTwitter code was written by Joshua Carles Campbell @ https://github.com/joshua2ua/lonelyTwitter
     *
     * This loadFromFile and saveFromFile GSON code was introduced to us by TA Shaiful Chowdhury
     * in the Thursday 5-8 ETLC lab
     */
    private void loadFromFile() {
        try {
            // Load EmotionEntries from the entries file
            FileInputStream fileInputStream = getActivity().openFileInput(ENTRIES_FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Gson gson = new Gson();

            Type emotionEntryListType = new TypeToken<List<EmotionEntry>>(){}.getType();
            mEmotionEntries = gson.fromJson(bufferedReader, emotionEntryListType);

            fileInputStream.close();

            // Load EmotionEntry counts from the counts file
            fileInputStream = getActivity().openFileInput(COUNTS_FILENAME);
            inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            Type emotionCountsType = new TypeToken<Map<EmotionType, Integer>>(){}.getType();
            mEmotionCounts = gson.fromJson(bufferedReader, emotionCountsType);

            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try {
            // Save EmotionEntries into the entries file
            FileOutputStream fileOutputStream = getActivity().openFileOutput(ENTRIES_FILENAME, 0);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            Gson gson = new Gson();
            gson.toJson(mEmotionEntries, bufferedWriter);
            bufferedWriter.flush();

            fileOutputStream.close();

            // Save EmotionEntries into the counts file
            fileOutputStream = getActivity().openFileOutput(COUNTS_FILENAME, 0);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            bufferedWriter = new BufferedWriter(outputStreamWriter);

            gson.toJson(mEmotionCounts, bufferedWriter);
            bufferedWriter.flush();

            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
