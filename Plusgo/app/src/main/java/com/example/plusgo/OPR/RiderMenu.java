/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/9/19 11:50 AM
 *
 */

package com.example.plusgo.OPR;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.plusgo.FC.UserProfile;
import com.example.plusgo.R;

import java.util.Calendar;

public class RiderMenu extends AppCompatActivity implements View.OnClickListener {

    EditText start_date, start_time;
    private int mYear, mMonth, mDay, mHour, mMinute, mAmPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_menu);

        start_date = findViewById(R.id.start_date);
        start_time = findViewById(R.id.start_time);

        start_date.setOnClickListener(this);
        start_date.setShowSoftInputOnFocus(false);

        start_time.setOnClickListener(this);
        start_time.setShowSoftInputOnFocus(false);
    }

    @Override
    public void onClick(View v) {

        if (v == start_date) {

            start_date.setShowSoftInputOnFocus(false);
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            start_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == start_time) {

            start_time.setShowSoftInputOnFocus(false);
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR);
            mMinute = c.get(Calendar.MINUTE);
            mAmPm = c.get(Calendar.AM_PM);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            start_time.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

}
