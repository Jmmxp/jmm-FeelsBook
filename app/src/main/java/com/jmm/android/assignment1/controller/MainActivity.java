package com.jmm.android.assignment1.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.EmotionType;

/**
 * This activity is started when the application is started. Its view consists of two FrameLayouts
 * that host two fragments respectively; ChooserFragment and ListFragment.
 *
 * It is responsible for listening to callback methods from ChooserFragment and calling the appropriate
 * methods on ListFragment based on the callback (e.g., one of ChooserFragment's emotion images is clicked
 * and MainActivity must tell ListFragment to add a new EmotionEntry to its list with that emotion
 */
public class MainActivity extends AppCompatActivity implements ChooserFragment.Callbacks {

    private ChooserFragment mChooserFragment;
    private ListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up both fragments that we want to host
        FragmentManager fragmentManager = getSupportFragmentManager();
        mChooserFragment = (ChooserFragment) fragmentManager.findFragmentById(R.id.fragment_chooser_container);

        if (mChooserFragment == null) {
            mChooserFragment = new ChooserFragment();
            addFragment(R.id.fragment_chooser_container, mChooserFragment);
        }

        mListFragment = (ListFragment) fragmentManager.findFragmentById(R.id.fragment_list_container);

        if (mListFragment == null) {
            mListFragment = new ListFragment();
            addFragment(R.id.fragment_list_container, mListFragment);
        }

    }

    // Helper method to add a fragment to the given container layout ID
    private void addFragment(int containerLayoutId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(containerLayoutId, fragment)
                .commit();
    }


    // Callback methods that ChooserFragment calls when a user interacts with the top bar's emotions
    @Override
    public void onEmotionAdded(EmotionType emotionType) {
        mListFragment.addEmotionEntry(emotionType);
    }

    @Override
    public void onCountRequested(EmotionType emotionType) {
        mListFragment.showEmotionCount(emotionType);
    }
}
