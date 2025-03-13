
package com.spse.gameresolutionchanger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class FloatingSidebarService extends Service {

    private WindowManager windowManager;
    private View floatingButtonView, floatingSidebarView;
    private WindowManager.LayoutParams buttonParams, sidebarParams;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Inflate Floating Button
        floatingButtonView = LayoutInflater.from(this).inflate(R.layout.floating_button, null);
        ImageView floatingButton = floatingButtonView.findViewById(R.id.floating_button);

        buttonParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : 
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        buttonParams.gravity = Gravity.TOP | Gravity.END;
        buttonParams.x = 20;
        buttonParams.y = 100;

        windowManager.addView(floatingButtonView, buttonParams);

        // Inflate Floating Sidebar
        floatingSidebarView = LayoutInflater.from(this).inflate(R.layout.floating_sidebar, null);
        floatingSidebarView.setVisibility(View.GONE);

        sidebarParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : 
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        sidebarParams.gravity = Gravity.START;

        windowManager.addView(floatingSidebarView, sidebarParams);

        // Drag to Move Button
        floatingButton.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = buttonParams.x;
                        initialY = buttonParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        buttonParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        buttonParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingButtonView, buttonParams);
                        return true;

                    case MotionEvent.ACTION_UP:
                        return true;
                }
                return false;
            }
        });

        // Click to Show Sidebar
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatingSidebarView.getVisibility() == View.GONE) {
                    floatingSidebarView.setVisibility(View.VISIBLE);
                } else {
                    floatingSidebarView.setVisibility(View.GONE);
                }
            }
        });

        // Sidebar Buttons Logic
        Button btnFpsBoost = floatingSidebarView.findViewById(R.id.btn_fps_boost);
        Button btnDndMode = floatingSidebarView.findViewById(R.id.btn_dnd_mode);
        Button btnScreenRecorder = floatingSidebarView.findViewById(R.id.btn_screen_recorder);
        Button btnAutoTap = floatingSidebarView.findViewById(R.id.btn_auto_tap);

        btnFpsBoost.setOnClickListener(v -> ExecuteADBCommands.setHigherRefreshRate());
        btnDndMode.setOnClickListener(v -> toggleDNDMode());
        btnScreenRecorder.setOnClickListener(v -> startScreenRecording());
        btnAutoTap.setOnClickListener(v -> startAutoTap());
    }

    private void toggleDNDMode() {
        // Implement DND Mode logic here
    }

    private void startScreenRecording() {
        // Implement screen recording logic here
    }

    private void startAutoTap() {
        // Implement auto tap functionality here
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingButtonView != null) windowManager.removeView(floatingButtonView);
        if (floatingSidebarView != null) windowManager.removeView(floatingSidebarView);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
