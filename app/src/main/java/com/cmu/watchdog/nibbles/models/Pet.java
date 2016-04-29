package com.cmu.watchdog.nibbles.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alicesypark on 4/9/16.
 */
public class Pet {
    private String name;
    private String gender;
    private String type;
    private String breed;
    private int age;
    private int id;
    private Map<Integer, Device> devices;

    public Pet(String name, String gender, String type, String breed, int age, int id) {
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.id = id;
        this.devices = new HashMap<Integer, Device>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Device> getDevices() {
        return devices;
    }

    public void setDevices(Map<Integer, Device> devices) {
        this.devices = devices;
    }

    public void getDeviceById(int i) {
        devices.get(i);
    }

    public void addDevice(Device d) {
        int device_id = d.getDevice_id();
        this.devices.put(device_id, d);
    }

    @Override
    public String toString() {
        return "Pet Name : " + name;
    }
}
