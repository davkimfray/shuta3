package com.example.davkimfray.listview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TimeTrackerAdapter extends BaseAdapter {

    private ArrayList<TimeRecord> times = new ArrayList<TimeRecord>();

    public TimeTrackerAdapter(){
        times.add(new TimeRecord("38:23", "Feeling good!"));
        times.add(new TimeRecord("49:01", "Tired. Needed more caffeine"));
        times.add(new TimeRecord("26:21", "I’m rocking it!"));
        times.add(new TimeRecord("29:42", "Lost some time on the hills, but pretty good."));
    }

    @Override
    public int getCount() {
        return times.size();
    }

    @Override
    public Object getItem(int index) {
        return getItem(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.time_list_item, parent, false);
        }
        TimeRecord time = times.get(index);
        TextView timeTextView = view.findViewById(R.id.time_view);
        timeTextView.setText(time.getTime());

        TextView notesTextView = view.findViewById(R.id.notes_view);
        notesTextView.setText(time.getNotes());
        return view;
    }
}
