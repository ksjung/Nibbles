package com.cmu.watchdog.nibbles;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import oscP5.OscEventListener;
import netP5.*;


import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class Rpi_communicate implements OscEventListener{

    private static final int PORT_IN = 12000;
    private static final int PORT_OUT = 32000;
    private NetAddress thisLocation;
    private OscP5 osc;

    static String data = "";

    public void oscEvent(OscMessage m) {
        data = m.get(0).stringValue();
        Log.d("SUCCESS", data);
    }

    public void run() {
        Log.d("here","123");
        OscP5 osc = new OscP5(this,PORT_IN);
        try{
            connect();
        }catch(Exception e){
            e.printStackTrace();
        }

        while (true) {

        }
    }



    public void connect(){
        OscMessage m = new OscMessage("/server/connect",new Object[0]);
        OscP5.flush(m, thisLocation);
    }
    public void disconnect(){
        OscMessage m = new OscMessage("/server/disconnect",new Object[0]);
        OscP5.flush(m,thisLocation);
    }


    @Override
    public void oscStatus(OscStatus status) {
        Log.d("received:",status.toString());
    }

}
