
package com.spse.gameresolutionchanger;

import android.content.Context;
import android.util.Log;

public class SettingsManager {

    public static void applyGameMode(Context context, int mode) {
        switch (mode) {
            case 1:
                enablePerformanceMode();
                break;
            case 2:
                enableBalancedMode();
                break;
            case 3:
                enableBatterySaverMode();
                break;
            default:
                Log.e("GAME_MODE", "Invalid mode selected");
        }
    }

    private static void enablePerformanceMode() {
        ExecuteADBCommands.executeRootCommand("settings put global cpu_boost 1");
        ExecuteADBCommands.executeRootCommand("settings put global gpu_boost 1");
        ExecuteADBCommands.executeRootCommand("settings put system min_refresh_rate 120.0");
        ExecuteADBCommands.executeRootCommand("settings put system peak_refresh_rate 120.0");
        ExecuteADBCommands.executeRootCommand("settings put system background_process_limit 0");
        Log.d("GAME_MODE", "Performance Mode Enabled: Max CPU/GPU, 120Hz, No Background Processes");
    }

    private static void enableBalancedMode() {
        ExecuteADBCommands.executeRootCommand("settings put global cpu_boost 1");
        ExecuteADBCommands.executeRootCommand("settings put global gpu_boost 1");
        ExecuteADBCommands.executeRootCommand("settings put system min_refresh_rate 90.0");
        ExecuteADBCommands.executeRootCommand("settings put system peak_refresh_rate 90.0");
        ExecuteADBCommands.executeRootCommand("settings put system background_process_limit 5");
        Log.d("GAME_MODE", "Balanced Mode Enabled: Optimized CPU/GPU, 90Hz, Limited Background Processes");
    }

    private static void enableBatterySaverMode() {
        ExecuteADBCommands.executeRootCommand("settings put global cpu_boost 0");
        ExecuteADBCommands.executeRootCommand("settings put global gpu_boost 0");
        ExecuteADBCommands.executeRootCommand("settings put system min_refresh_rate 60.0");
        ExecuteADBCommands.executeRootCommand("settings put system peak_refresh_rate 60.0");
        ExecuteADBCommands.executeRootCommand("settings put system background_process_limit 2");
        Log.d("GAME_MODE", "Battery Saver Mode Enabled: Lower CPU/GPU, 60Hz, Reduced Background Processes");
    }
}
