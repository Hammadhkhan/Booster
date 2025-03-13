
package com.spse.gameresolutionchanger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class FPSOverlayService extends Service {

    private WindowManager windowManager;
    private View fpsOverlayView;
    private TextView fpsTextView;
    private Handler fpsHandler = new Handler();
    private int frameCount = 0;
    private long startTime = System.currentTimeMillis();

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        addFPSOverlay();
        startFPSTracking();
    }

    private void addFPSOverlay() {
        fpsOverlayView = LayoutInflater.from(this).inflate(R.layout.fps_overlay, null);
        fpsTextView = fpsOverlayView.findViewById(R.id.fps_text);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 50;
        params.y = 50;

        windowManager.addView(fpsOverlayView, params);
    }

    private void startFPSTracking() {
        fpsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                float fps = frameCount / ((currentTime - startTime) / 1000f);
                fpsTextView.setText("FPS: " + Math.round(fps));

                frameCount = 0;
                startTime = currentTime;
                fpsHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public static void updateFrame() {
        // Call this method from the game loop or rendering thread to track frames
        frameCount++;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fpsOverlayView != null) {
            windowManager.removeView(fpsOverlayView);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public android.os.IBinder onBind(Intent intent) {
        return null;
    }
}
