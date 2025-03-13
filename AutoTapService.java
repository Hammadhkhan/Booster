
package com.spse.gameresolutionchanger;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AutoTapService extends AccessibilityService {

    private WindowManager windowManager;
    private View floatingTapButton;
    private Handler autoTapHandler = new Handler();
    private boolean isAutoTapping = false;
    private int tapInterval = 300; // Default tap interval in milliseconds

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        addFloatingTapButton();
    }

    private void addFloatingTapButton() {
        floatingTapButton = LayoutInflater.from(this).inflate(R.layout.floating_tap_button, null);
        Button tapButton = floatingTapButton.findViewById(R.id.tap_button);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.END;
        params.x = 50;
        params.y = 200;

        windowManager.addView(floatingTapButton, params);

        tapButton.setOnClickListener(v -> toggleAutoTap());
    }

    private void toggleAutoTap() {
        if (!isAutoTapping) {
            isAutoTapping = true;
            autoTapHandler.post(autoTapRunnable);
        } else {
            isAutoTapping = false;
            autoTapHandler.removeCallbacks(autoTapRunnable);
        }
    }

    private final Runnable autoTapRunnable = new Runnable() {
        @Override
        public void run() {
            performGlobalAction(GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN); // Simulates a tap
            if (isAutoTapping) {
                autoTapHandler.postDelayed(this, tapInterval);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingTapButton != null) {
            windowManager.removeView(floatingTapButton);
        }
    }

    @Override
    public void onAccessibilityEvent(android.view.accessibility.AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }
}
