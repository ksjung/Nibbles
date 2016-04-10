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
import java.util.Calendar;
import java.util.Date;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
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
                //series.resetData(new DataPoint[]{});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Calendar calendar = Calendar.getInstance();
        Date d1 = calendar.getTime();
        Log.d("asdf", d1.toString());
        calendar.add(Calendar.DATE, 1);
        Date d2 = calendar.getTime();
        Log.d("asdf", d2.toString());
        calendar.add(Calendar.DATE, 1);
        Date d3 = calendar.getTime();

        GraphView graph = (GraphView) view.findViewById(R.id.graph);

        // you can directly pass Date objects to DataPoint-Constructor
        // this will convert the Date to double via Date#getTime()
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(d1, 1),
                new DataPoint(d2, 5),
                new DataPoint(d3, 3)
        });
        series.setTitle("Doug");
        graph.addSeries(series);

        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        return view;
    }
}
