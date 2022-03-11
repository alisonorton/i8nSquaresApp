package edu.byuh.cis.cs203.numberedsquares.util;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;


/**
 * The Timer class is where we have a scheduled loop to redraw all the squares
 * so they move across the screen.
 */

    public class Timer extends Handler {

//        private int delay;
        private List<TimerListener> listeners;
        private static Timer singleton = null;

        public Timer() {
            listeners = new ArrayList<>();
//            delay = d;
            sendEmptyMessageDelayed(0, 50);
        }

        public static Timer getInstance(){
            if(singleton == null){
                singleton = new Timer();
            }
            return singleton;
        }

        public void register(TimerListener t){
            listeners.add(t);
        }

        public void unRegister(TimerListener t){
            listeners.remove(t);
        }

        public void notifyListeners(){
            for(TimerListener t : listeners){
                t.update();
            }
        }

        @Override
        public void handleMessage(Message m){
            notifyListeners();
            sendEmptyMessageDelayed(0, 50);
        }
    }

