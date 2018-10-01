package com.jmm.android.assignment1.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFragment extends Fragment {

    private int REQUEST_EMOTION = 0;

    private static final String TAG = "ListFragment";
    private static final String FILENAME = "file.sav";

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
        updateAdapter(mEmotionEntryAdapter.getItemCount() - 1);

        System.out.println(mEmotionEntries.size());
    }

    public void updateAdapter(int position) {
        mEmotionEntryAdapter.notifyItemChanged(position);
        saveToFile();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EMOTION) {
            if (resultCode == Activity.RESULT_OK) {
                EmotionEntry emotionEntry = (EmotionEntry)
                        data.getSerializableExtra(EmotionActivity.EXTRA_EMOTION);

                mEmotionEntries.set(mEmotionEntryIndex, emotionEntry);
                Log.d(TAG, "Hello, comment is " + emotionEntry.getComment());
            }

            if (resultCode == EmotionActivity.RESULT_DELETE) {
                mEmotionEntries.remove(mEmotionEntryIndex);
                mEmotionEntryAdapter.notifyItemRemoved(mEmotionEntryIndex);
            }

            updateAdapter(mEmotionEntryIndex);
        }


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

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
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

    private void loadFromFile() {
        try {
            FileInputStream fileInputStream = getActivity().openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            Gson gson = new Gson();

            Type emotionEntryListType = new TypeToken<List<EmotionEntry>>(){}.getType();
            mEmotionEntries = gson.fromJson(bufferedReader, emotionEntryListType);

            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile() {
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput(FILENAME, 0);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            Gson gson = new Gson();
            gson.toJson(mEmotionEntries, bufferedWriter);
            bufferedWriter.flush();

            // also closes the outputStreamWriter and bufferedWriter since they depend on each other
            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
