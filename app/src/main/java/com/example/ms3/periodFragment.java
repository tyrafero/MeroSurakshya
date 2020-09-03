package com.example.ms3;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static com.example.ms3.otp.TAG;


public class periodFragment extends Fragment {




    public periodFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_period,container,false);

        final List<String> mutableBookings = new ArrayList<>();



        //toolbar.setTitle(dateFormatForMonth.format(cView.getFirstDayOfCurrentMonth()));



        // events has size 2 with the 2 events inserted previously
        // define a listener to receive callbacks when certain events happen.

        
        
        return inflater.inflate(R.layout.fragment_period, container, false);
    }



}