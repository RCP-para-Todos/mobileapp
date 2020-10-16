package com.jk.rcp.main.sensors.gyroscope;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.jk.rcp.main.sensors.utils.SignQueue;

public class RotationDetector implements SensorEventListener {
    private static final long MAX_WINDOW_SIZE = 250000000;

    private final SignQueue queue = new SignQueue(MAX_WINDOW_SIZE);
    private final Listener listener;

    private SensorManager sensorManager;
    private Sensor gyroscope;

    public RotationDetector(Listener listener) {
        this.listener = listener;
    }

    public boolean start(SensorManager sensorManager) {
        if (gyroscope != null) {
            return true;
        }

        gyroscope = sensorManager.getDefaultSensor(
                Sensor.TYPE_GYROSCOPE);

        if (gyroscope != null) {
            this.sensorManager = sensorManager;
            sensorManager.registerListener(this, gyroscope,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }
        return gyroscope != null;
    }

    public void stop() {
        if (gyroscope != null) {
            queue.clear();
            sensorManager.unregisterListener(this, gyroscope);
            sensorManager = null;
            gyroscope = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        boolean rotating = isRotating(event);
        long timestamp = event.timestamp;
        queue.add(timestamp, rotating);
        if (queue.isMoving()) {
            queue.clear();
            listener.noRotation();
        }
    }

    private boolean isRotating(SensorEvent event) {
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
//        Log.d("MEDIDA GISROCOPIO", "X:" + ax + " Y:" + ay + " Z:" + az);

        double magnitude = ax * ax + ay * ay + az * az;
        return !(magnitude < 0.0005);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface Listener {
        void noRotation();
    }
}
