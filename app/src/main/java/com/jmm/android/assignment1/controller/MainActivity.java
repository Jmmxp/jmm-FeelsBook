package com.jmm.android.assignment1.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.jmm.android.assignment1.R;
import com.jmm.android.assignment1.model.EmotionType;

public class MainActivity extends AppCompatActivity implements ChooserFragment.Callbacks {

    private ChooserFragment mChooserFragment;
    private ListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    private void addFragment(int containerLayoutId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager
                .beginTransaction()
                .add(containerLayoutId, fragment)
                .commit();
    }

    @Override
    public void onEmotionAdded(EmotionType emotionType) {
        mListFragment.addEmotionEntry(emotionType);
    }

    @Override
    public void onCountRequested(EmotionType emotionType) {
        mListFragment.showEmotionCount(emotionType);
    }
}
