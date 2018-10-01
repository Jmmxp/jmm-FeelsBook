package com.jmm.android.assignment1.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.jmm.android.assignment1.R;

import java.util.Calendar;
import java.util.Date;

public class DateDialogFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    private DatePicker mDatePicker;
    private Callbacks mCallbacks;

    interface Callbacks {
        void onDateChanged(Date date);
    }

    public static DateDialogFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DateDialogFragment fragment = new DateDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCallbacks = (Callbacks) getActivity();

        /* Reference used for getting reference to views for a dialog fragment:
        Android Big Nerd Ranch book Chapter 12, Listing 12.4 "Adding DatePicker to AlertDialog"
        */
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);
        mDatePicker = view.findViewById(R.id.date_picker);

        /* Reference used for getting year, month, day from a date object:
        https://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal
        User: Florent Guillaume @ https://stackoverflow.com/users/857987/florent-guillaume
        */
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        mDatePicker.updateDate(year, month, day);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.date_picker_message)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();

                        calendar.set(year, month, day);

                        Date date = calendar.getTime();

                        mCallbacks.onDateChanged(date);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);

        return builder.create();

    }

}
