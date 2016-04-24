package com.cmu.watchdog.nibbles.models;

/**
 * Created by alicesypark on 4/19/16.
 */
public class Device {
    private int device_id;
    private String name;
    private int pet_id;

    public Device(int device_id, String name, int pet_id) {
        this.device_id = device_id;
        this.name = name;
        this.pet_id = pet_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPet_id() {
        return pet_id;
    }

    public void setPet_id(int pet_id) {
        this.pet_id = pet_id;
    }

    @Override
    public String toString() {
        return "Device Name : " + name + " == Device id :" + device_id;
    }
}
