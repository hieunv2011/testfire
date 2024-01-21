package com.example.testing;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class LightSensorService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        handler = new Handler();
        startService(new Intent(this, LightSensorService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (lightSensor != null) {
            sensorManager.unregisterListener(this);
        }
        stopService(new Intent(this, LightSensorService.class));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            float lightValue = event.values[0];
            adjustBrightness((int) lightValue);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Không cần thực hiện gì nếu có sự thay đổi về độ chính xác của cảm biến
    }

    private void adjustBrightness(final int brightness) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
                } catch (Exception e) {
                    Log.e("Brightness", "Error changing brightness");
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

