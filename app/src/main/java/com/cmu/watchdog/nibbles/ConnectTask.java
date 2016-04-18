package com.cmu.watchdog.nibbles;

import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import android.os.AsyncTask;

public class ConnectTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        System.out.println("sending connect message: " + NetMan.PORT_OUT);
        OscMessage m = new OscMessage("/server/connect",new Object[0]);
        try{
            OscP5.flush(m,NetMan.SERVER != null ? NetMan.SERVER : NetMan.EVERYTHING);
        }catch(NullPointerException e) {
            System.err.println(e.getMessage());
        }
        if(NetMan.SERVER != null) System.out.println("NetMan.SERVER: " + NetMan.SERVER);
        return null;
    }

}