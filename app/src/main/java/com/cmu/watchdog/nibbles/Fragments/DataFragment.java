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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmu.watchdog.nibbles.R;
import com.cmu.watchdog.nibbles.models.DatabaseHandler;
import com.cmu.watchdog.nibbles.models.Pet;
import com.cmu.watchdog.nibbles.models.WeightResult;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


public class DataFragment extends Fragment{
    private DatabaseHandler db;
    UpdateBack backUpdate;
    Handler handler = new Handler();

    TextView name;
    ImageView activityIcon;
    TextView activityText;
    Pet selectedPet;
    TextView temperature;
    TextView humidity;
    LineChartView predictionChart;

    public DataFragment() {

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
        View view = inflater.inflate(R.layout.pet_data_display, container, false);

        name = (TextView) view.findViewById(R.id.pet_name);
        activityIcon = (ImageView) view.findViewById(R.id.activity_icon);
        activityText = (TextView) view.findViewById(R.id.activity_text);
        temperature = (TextView) view.findViewById(R.id.temperature_value);
        humidity = (TextView) view.findViewById(R.id.humidity_value);
        predictionChart = (LineChartView) view.findViewById(R.id.prediction_chart);

        //Activity Text Animation
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        activityText.startAnimation(anim);

        db = new DatabaseHandler();
        db.connectDB();


        predictionChart.setInteractive(true);

        List<PointValue> values = new ArrayList<PointValue>();

        List<Float> predictions = null;
        try {
            predictions = db.getPrediction(3);

            float min = (float) Integer.MAX_VALUE;
            float max = (float) Integer.MIN_VALUE;
            for (int i = 0; i < predictions.size(); i++) {
                float accel = (float) predictions.get(i);
                values.add(new PointValue(i, accel));

                if (accel < min) {
                    min = accel;
                }
                if (accel > max) {
                    max = accel;
                }
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);
            LineChartData lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis());
            lineData.setAxisYLeft(new Axis().setMaxLabelChars(5));

            predictionChart.setLineChartData(lineData);
            // For build-up animation you have to disable viewport recalculation.
            predictionChart.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, max + 1, 24, min -1);
            predictionChart.setMaximumViewport(v);
            predictionChart.setCurrentViewport(v);

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

    //TODO: change to static
    private class UpdateBack extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected Map<String, String> doInBackground(Void... params) {
            Map<String, String> result = null;
            try {
                result = db.getBackpackData(3);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(Map<String, String> result) {
            if (result == null || result.isEmpty()) {
                return;
            }

            //activity
            if (result.containsKey("activity")) {
                String activity = result.get("activity");

                if (activity == null) {
                    activityIcon.setImageResource(R.drawable.paw);
                    activityText.setText("No data available. Please refresh");
                }
                else if (activity.equals("walking")) {
                    activityIcon.setImageResource(R.drawable.walking);
                    activityText.setText("Walking");
                }
                else if (activity.equals("running")) {
                    activityIcon.setImageResource(R.drawable.running);
                    activityText.setText("Running");
                }
                else if (activity.equals("resting")) {
                    activityIcon.setImageResource(R.drawable.resting);
                    activityText.setText("Resting");
                }
                else {
                    activityIcon.setImageResource(R.drawable.paw);
                    activityText.setText("Please refresh");
                }
            }

            //humidity
            if (result.containsKey("humidity")) {
                humidity.setText(result.get("humidity" + " %"));
            }

            //temperature
            if (result.containsKey("temperature")) {
                temperature.setText(result.get("temperature") + " C");
            }
        }
    }
}
