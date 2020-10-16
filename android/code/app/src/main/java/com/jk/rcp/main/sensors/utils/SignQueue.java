package com.jk.rcp.main.sensors.utils;

public class SignQueue {

    private final long minWindowSize;
    private final long maxWindowSize;
    private final SignPool pool = new SignPool();
    private Sign oldest;
    private Sign newest;
    private int sampleCount;
    private int movingCount;

    public SignQueue(long maxWindowSize) {
        this.maxWindowSize = maxWindowSize;
        this.minWindowSize = maxWindowSize >> 1;
    }

    public void add(long timestamp, boolean accelerating) {
        purge(timestamp - maxWindowSize);

        Sign added = pool.acquire();
        added.timestamp = timestamp;
        added.isMoving = accelerating;
        added.nextSign = null;
        if (newest != null) {
            newest.nextSign = added;
        }
        newest = added;
        if (oldest == null) {
            oldest = added;
        }

        sampleCount++;
        if (accelerating) {
            movingCount++;
        }
    }

    public void clear() {
        while (oldest != null) {
            Sign removed = oldest;
            oldest = removed.nextSign;
            pool.release(removed);
        }
        newest = null;
        sampleCount = 0;
        movingCount = 0;
    }

    void purge(long cutoff) {
        while (sampleCount >= 4
                && oldest != null && cutoff - oldest.timestamp > 0) {

            Sign removed = oldest;
            if (removed.isMoving) {
                movingCount--;
            }
            sampleCount--;

            oldest = removed.nextSign;
            if (oldest == null) {
                newest = null;
            }
            pool.release(removed);
        }
    }

    public boolean isMoving() {
        return newest != null
                && oldest != null
                && newest.timestamp - oldest.timestamp >= minWindowSize
                && movingCount >= (sampleCount >> 1) + (sampleCount >> 2);
    }
}
