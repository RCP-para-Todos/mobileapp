package com.jk.rcp.main.sensors.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.jk.rcp.main.sensors.utils.SignQueue;

public class ShakeDetector implements SensorEventListener {
    private static final long MAX_WINDOW_SIZE = 250000000;

    private final SignQueue queue = new SignQueue(MAX_WINDOW_SIZE);
    private final Listener listener;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    public ShakeDetector(Listener listener) {
        this.listener = listener;
    }

    public boolean start(SensorManager sensorManager) {
        if (accelerometer != null) {
            return true;
        }

        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            this.sensorManager = sensorManager;
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
        return accelerometer != null;
    }

    public void stop() {
        if (accelerometer != null) {
            queue.clear();
            sensorManager.unregisterListener(this, accelerometer);
            sensorManager = null;
            accelerometer = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean accelerating = isAccelerating(event);
        long timestamp = event.timestamp;
        queue.add(timestamp, accelerating);
        if (queue.isMoving()) {
            queue.clear();
            listener.hearShake();
        }
    }

    private boolean isAccelerating(SensorEvent event) {
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
//        Log.d("MEDIDA", "X:" + ax + " Y:" + ay + " Z:" + az);

        double magnitudeSquared = ax * ax + ay * ay + az * az;
        return magnitudeSquared > 95.5;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface Listener {
        void hearShake();
    }
}
