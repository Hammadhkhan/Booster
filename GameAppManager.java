
package com.spse.gameresolutionchanger;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.util.Log;

public class GameAppManager {

    private static boolean isDNDActive = false;

    public static void toggleDNDMode(Context context) {
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (!isDNDActive) {
                Settings.Global.putInt(context.getContentResolver(), "zen_mode", 1);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Log.d("DND_MODE", "DND Activated: Notifications & Calls Blocked");
            } else {
                Settings.Global.putInt(context.getContentResolver(), "zen_mode", 0);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Log.d("DND_MODE", "DND Deactivated: Notifications & Calls Allowed");
            }
            isDNDActive = !isDNDActive;
        } catch (Exception e) {
            Log.e("DND_MODE", "Error toggling DND Mode", e);
        }
    }

    public static void rejectIncomingCall(Context context) {
        try {
            TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
            if (telecomManager != null) {
                telecomManager.endCall();
                Log.d("CALL_BLOCK", "Incoming Call Rejected");
            }
        } catch (Exception e) {
            Log.e("CALL_BLOCK", "Error rejecting call", e);
        }
    }
}
