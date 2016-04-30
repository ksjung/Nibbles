package com.cmu.watchdog.nibbles.models;

import java.util.Date;

/**
 * Created by ksjung on 4/28/16.
 */
public class WeightResult {
    String date;
    int weight;

    public WeightResult(String date, int weight) {
        this.date = date;
        this.weight = weight;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
