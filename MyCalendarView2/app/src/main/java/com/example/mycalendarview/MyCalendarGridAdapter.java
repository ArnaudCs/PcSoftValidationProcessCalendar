package com.example.mycalendarview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyCalendarGridAdapter extends ArrayAdapter {
    List<Date> Dates;
    Calendar currentDate;
    LayoutInflater inflater;
    private List<Date> selectedDates = new ArrayList<>();

    private boolean isSelectionMode = false;

    public void setSelectionMode(boolean isSelectionMode){
        this.isSelectionMode = isSelectionMode;
    }

    public MyCalendarGridAdapter(@NonNull Context context, List<Date> Dates, Calendar currentDate) {
        super(context, R.layout.calendar_cell);
        this.Dates = Dates;
        this.currentDate = currentDate;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View alterView, @NonNull ViewGroup parent) {
        Date monthDate = Dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);
        int day_digit = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        //Récupération date réelles
        Calendar actualDates = Calendar.getInstance(); // Obtenir la date et l'heure actuelles
        int actualMonth = actualDates.get(Calendar.MONTH) + 1;
        int actualYear = actualDates.get(Calendar.YEAR);
        int actualDay = actualDates.get(Calendar.DAY_OF_MONTH);

        View view = alterView;
        if(view == null){
            view = inflater.inflate(R.layout.calendar_cell, parent, false);
        }

        //Coloration des jours du mois
        if (displayMonth == currentMonth && displayYear == currentYear) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.limeBlue));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lightGrey));
        }

        //Affichage du jour actuel
        if(actualDay == day_digit && actualMonth == displayMonth && actualYear == displayYear){
            view.setBackgroundColor(getContext().getResources().getColor(R.color.lightOrange));
        }

        TextView cell_day_digit = view.findViewById(R.id.dayNumberDisplay);
        //Affichage des jours dans les cellules
        cell_day_digit.setText(String.valueOf(day_digit));

        //Gestion du click seulement en mode selection
        if (!isSelectionMode) {
            view.setClickable(false);
        } else {
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedDates.contains(monthDate)) {
                        selectedDates.remove(monthDate);
                        if (displayMonth == currentMonth && displayYear == currentYear) {
                            if (day_digit == actualDay && displayMonth == actualMonth && displayYear == actualYear) {
                                v.setBackgroundColor(getContext().getResources().getColor(R.color.lightOrange));
                            } else {
                                v.setBackgroundColor(getContext().getResources().getColor(R.color.limeBlue));
                            }
                        } else {
                            v.setBackgroundColor(getContext().getResources().getColor(R.color.lightGrey));
                        }
                    } else {
                        selectedDates.add(monthDate);
                        v.setBackgroundColor(getContext().getResources().getColor(R.color.selectedDays));
                    }
                }
            });
        }


        return view;
    }

    @Override
    public int getCount() {
        return Dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return Dates.indexOf(item);
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return Dates.get(position);
    }
}
