package com.example.mycalendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyCalendarView extends LinearLayout {

    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    TextView actualDateDisplay;
    ImageView actualCalendarBtn;
    ImageView selectionModeOn;
    ImageView selectionModeOff;

    private boolean selectionModeBool = false;
    GridView calendarGrid;
    LinearLayout calendarContainer;
    LinearLayout calendarDaysDisplay;
    MyCalendarGridAdapter myCalendarGridAdapter;

    SimpleDateFormat date_format = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);

    List<Date> Dates = new ArrayList<>();

    public MyCalendarView(Context context) {
        super(context);
    }

    @SuppressLint({"ClickableViewAccessibilitay", "ClickableViewAccessibility"})
    public MyCalendarView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        InitLayout();
        setClickable(true);
        CalendarCreation();

        //Gestion du balayage
        calendarGrid.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
                calendar.add(Calendar.YEAR, 1);
                CalendarCreation();
            }

            public void onSwipeRight() {
                calendar.add(Calendar.MONTH, -1);
                CalendarCreation();
            }

            public void onSwipeLeft() {
                calendar.add(Calendar.MONTH, 1);
                CalendarCreation();
            }

            public void onSwipeBottom() {
                calendar.add(Calendar.YEAR, -1);
                CalendarCreation();
            }
        });

        //Bouton pour revenir à la vue actuel (mois actuel)
        actualCalendarBtn.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            CalendarCreation();
        });

        //Appui pour passage au mode selection de dates (passage au mode selection)
        selectionModeOff.setOnClickListener(view -> {
            selectionModeBool = !selectionModeBool;
            myCalendarGridAdapter.setSelectionMode(selectionModeBool);
            selectionModeOff.setVisibility(GONE);
            selectionModeOn.setVisibility(VISIBLE);
            myCalendarGridAdapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(getContext(), "Selection mode", Toast.LENGTH_SHORT);
            toast.show();
        });

        //Appui pour annulation du mode selection de dates (passage au mode balayage)
        selectionModeOn.setOnClickListener(view -> {
            selectionModeBool = !selectionModeBool;
            myCalendarGridAdapter.setSelectionMode(selectionModeBool);
            selectionModeOff.setVisibility(VISIBLE);
            selectionModeOn.setVisibility(GONE);
            myCalendarGridAdapter.notifyDataSetChanged();
            Toast toast = Toast.makeText(getContext(), "View draging mode", Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    public MyCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //Chargement du layout
    private void InitLayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        actualDateDisplay = view.findViewById(R.id.actualDateDisplay);
        calendarGrid = view.findViewById(R.id.calendarGridView);
        actualCalendarBtn = view.findViewById(R.id.actualCalendarBtn);
        calendarContainer = view.findViewById(R.id.calendarContainer);
        calendarDaysDisplay = view.findViewById(R.id.calendarDaysDisplay);
        selectionModeOff = view.findViewById(R.id.selectionModeOff);
        selectionModeOn = view.findViewById(R.id.selectionModeOn);
    }

    private void CalendarCreation() {
        String currentDate = date_format.format(calendar.getTime());
        actualDateDisplay.setText(currentDate);
        Dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        //Récupération du premier jour de la semaine du mois précédent
        while (monthCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            monthCalendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        //Ajout de tous les jours du mois au tableau Dates
        while (Dates.size() < MAX_CALENDAR_DAYS){
            Dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //Appel de l'adapter
        myCalendarGridAdapter = new MyCalendarGridAdapter(context, Dates, calendar);
        calendarGrid.setAdapter(myCalendarGridAdapter);
    }
}
