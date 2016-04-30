package com.cmu.watchdog.nibbles.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.DatabaseHandler;
import com.cmu.watchdog.nibbles.models.Pet;
import com.cmu.watchdog.nibbles.models.WeightResult;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


public class WeightFragment extends Fragment{
    private DatabaseHandler db;
    UpdateBack backUpdate;
    Handler handler = new Handler();

    LineChartView chart;
    TextView weightText;

    public WeightFragment() {

    }

    @Override
    public void onDestroyView() {
        backUpdate.cancel(true);
        handler.removeCallbacks(dataRunnable);
        db.closeDB();
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_weight, container, false);

        weightText = (TextView) view.findViewById(R.id.weight_text);
        //Activity Text Animation
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        weightText.startAnimation(anim);

        chart = (LineChartView) view.findViewById(R.id.chart);

        chart.setInteractive(true);
        chart.setOnValueTouchListener(new ValueTouchListener());

        db = new DatabaseHandler();
        db.connectDB();


        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();

        List<WeightResult> weightResultList = null;
        try {
            weightResultList = db.getFeederData(4);

            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < weightResultList.size(); i++) {
                WeightResult wr = weightResultList.get(i);
                int weight = wr.getWeight();
                values.add(new PointValue(i, weight));
                axisValues.add(new AxisValue(i).setLabel(wr.getDate()));
                if (weight < min) {
                    min = weight;
                }
                if (weight > max) {
                    max = weight;
                }
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);
            LineChartData lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(5));

            chart.setLineChartData(lineData);
            // For build-up animation you have to disable viewport recalculation.
            chart.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, max + 1, 6, min -1);
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        handler.post(dataRunnable);

        return view;
    }

    private Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            if (backUpdate != null) {
                backUpdate.cancel(true);
            }
            backUpdate = new UpdateBack();
            backUpdate.execute();
            handler.postDelayed(dataRunnable, 500);
        }
    };

    private class UpdateBack extends AsyncTask<Void, Void, Integer> {

        @Override

        protected Integer doInBackground(Void... params) {
            Integer result = null;
            try {
                result = (Integer) db.getCurrentWeight(4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Integer result) {
            if (result == null) {
                return;
            }

            if (result < 0) {
                weightText.setText("Weight unavailable");
            }
            else {
                weightText.setText(result.toString());
            }
        }
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Weight: " + value.getY() + "kgs", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
