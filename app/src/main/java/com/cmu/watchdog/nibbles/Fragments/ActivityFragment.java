package com.cmu.watchdog.nibbles.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;

import com.cmu.watchdog.nibbles.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;




/**
 * Shows activity from accelerometer.
 */
public class ActivityFragment extends Fragment {
    private LineGraphSeries<DataPoint> series;

    public ActivityFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_activity, container, false);

        /* x-series selection spinner*/
        Spinner spin = (Spinner) view.findViewById(R.id.activity_xseries);
        List<String> xrange = new ArrayList<String>();
        xrange.add("PAST 4 HOURS");
        xrange.add("PAST 24 HOURS");
        ArrayAdapter < String > adapter = new ArrayAdapter < String > (getActivity(), android.R.layout.simple_spinner_item, xrange);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    Log.d("here", "here");
                }
                series.resetData(new DataPoint[]{});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        /* Graph */
        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

        return view;
    }
}
