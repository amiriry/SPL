package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * Created by amirshkedy on 12/12/2018.
 */
public class TickBroadcast implements Broadcast {
    private int current_tick;

    public TickBroadcast(int current_tick) {
        this.current_tick = current_tick;
    }

    public int getCurrent_tick() {
        return current_tick;
    }
}
