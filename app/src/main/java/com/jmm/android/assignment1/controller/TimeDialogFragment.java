package com.jmm.android.assignment1.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.jmm.android.assignment1.R;

import java.util.Calendar;
import java.util.Date;

public class TimeDialogFragment extends DialogFragment {

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;
    private TimeDialogFragment.Callbacks mCallbacks;

    interface Callbacks {
        void onTimeChanged(Date date);
    }

    public static TimeDialogFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, date);

        TimeDialogFragment fragment = new TimeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallbacks = (Callbacks) getActivity();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_time_picker, null);
        mTimePicker = view.findViewById(R.id.time_picker);

        Date date = (Date) getArguments().getSerializable(ARG_TIME);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.time_picker_message)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour;
                        int minute;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            hour = mTimePicker.getHour();
                            minute = mTimePicker.getMinute();
                        } else {
                            hour = mTimePicker.getCurrentHour();
                            minute = mTimePicker.getCurrentMinute();
                        }

                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, second);

                        Date date = calendar.getTime();

                        mCallbacks.onTimeChanged(date);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();

    }

}
