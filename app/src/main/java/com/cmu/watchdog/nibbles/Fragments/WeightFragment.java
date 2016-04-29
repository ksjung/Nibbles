package com.cmu.watchdog.nibbles.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.cmu.watchdog.nibbles.MainActivity;
import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.WeightResult;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;


/**
 * Shows activity from accelerometer.
 */
public class WeightFragment extends Fragment {
    UpdateBack backUpdate;
    private PointsGraphSeries<DataPoint> series;

    public WeightFragment() {

    }

    @Override
    public void onDestroyView() {
        if (backUpdate!=null) {
            backUpdate.cancel(true);
        }
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_weight, container, false);


        MainActivity activity = (MainActivity) getActivity();
        if (series != null) {
            GraphView graph = (GraphView) view.findViewById(R.id.graph);
            graph.addSeries(series);
        }

        List<WeightResult> result = null;
        try {
            result = activity.getFeederData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if ((result != null) && (!result.isEmpty())) {

            GraphView graph = (GraphView) view.findViewById(R.id.graph);
            DataPoint[] data = new DataPoint[result.size()];
            Calendar calendar = Calendar.getInstance();
            List<DataPoint> dataList = new ArrayList<DataPoint>();
            for (int i=0; i <data.length; i++) {
                WeightResult dataPoint = result.get(i);
                data[i] = new DataPoint(i, dataPoint.getWeight());
            }

            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>(data);
            graph.addSeries(series);

        }
        backUpdate = new UpdateBack();
        backUpdate.execute();

        return view;
    }

    private class UpdateBack extends AsyncTask<Void, Void, List<WeightResult>> {

        @Override
        protected List<WeightResult> doInBackground(Void... params) {
            MainActivity activity = (MainActivity) getActivity();
            List<WeightResult> result = null;
            try {
                result = activity.getFeederData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(List<WeightResult> result) {
            if (result == null || result.isEmpty()) {
                return;
            }
            DataPoint[] data = new DataPoint[result.size()];
            for (int i=0; i <data.length; i++) {
                WeightResult dataPoint = result.get(i);
                data[i] = new DataPoint(i, dataPoint.getWeight());
            }

            series = new PointsGraphSeries<DataPoint>(data);
        }
    }
}
