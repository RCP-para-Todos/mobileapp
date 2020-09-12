package com.jk.rcp.main.sensors.utils;

public class SignPool {
    private Sign head;

    Sign acquire() {
        Sign acquired = head;
        if (acquired == null) {
            acquired = new Sign();
        } else {
            head = acquired.nextSign;
        }
        return acquired;
    }

    void release(Sign sample) {
        sample.nextSign = head;
        head = sample;
    }
}