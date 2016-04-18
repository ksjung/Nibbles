package com.cmu.watchdog.nibbles;

import netP5.NetAddress;
import oscP5.OscArgument;
import oscP5.OscEventListener;
import oscP5.OscMessage;
import oscP5.OscP5;
import oscP5.OscStatus;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.logging.Handler;

public class NetMan implements OscEventListener {

    public static final int PORT_IN = 12000;
    public static final int PORT_OUT = 32000;
    public static NetAddress SERVER;
    public static NetAddress EVERYTHING = new NetAddress("255.255.255.255",PORT_OUT);

    private static final String TAG = "NetMan";
    private OscP5 osc;

    public NetMan(){
        osc = new OscP5(this, PORT_IN);
    }
    public void oscEvent(OscMessage m) {
        String pattern = m.addrPattern();
        if(SERVER == null) {
            SERVER = new NetAddress(m.address().replace("/", ""), PORT_OUT);
            System.out.println(SERVER);
        }
        Log.d("NETMAN", m.get(0).stringValue());
    }
    public void connect(){
        Log.d("NetMan","connect");
        new ConnectTask().execute();
    }
    @Override
    public void oscStatus(OscStatus status) {
        System.out.println(status);
    }
}