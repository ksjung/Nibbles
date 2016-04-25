package com.cmu.watchdog.nibbles.models;

/**
 * Created by alicesypark on 4/25/16.
 */
public class Command {
    private int command_id;
    private int device_id;
    private String command_desc;
    private int value;

    public Command(int c_id, int d_id, String c_desc, int val) {
        command_id = c_id;
        device_id = d_id;
        command_desc = c_desc;
        value = val;
    }

    public int getCommand_id() {
        return command_id;
    }

    public void setCommand_id(int command_id) {
        this.command_id = command_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public String getCommand_desc() {
        return command_desc;
    }

    public void setCommand_desc(String command_desc) {
        this.command_desc = command_desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
