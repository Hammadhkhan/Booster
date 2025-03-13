package com.spse.gameresolutionchanger;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ExecuteADBCommands extends MainActivity
{
    public static boolean canRunRootCommands()
    {
        boolean retval = false;
        Process suProcess;

        try
        {
            suProcess = Runtime.getRuntime().exec("su");

            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            DataInputStream osRes = new DataInputStream(suProcess.getInputStream());

            if (null != os && null != osRes)
            {
                // Getting the id of the current user to check if this is root
                os.writeBytes("id\n");
                os.flush();

                String currUid = osRes.readLine();

                boolean exitSu = false;
                if (null == currUid)
                {
                    retval = false;
                    exitSu = false;
                    Log.d("ROOT", "Can't get root access or denied by user");
                }
                else if (currUid.contains("uid=0"))
                {
                    retval = true;
                    exitSu = true;
                    Log.d("ROOT", "Root access granted");
                }
                else
                {
                    retval = false;
                    exitSu = true;
                    Log.d("ROOT", "Root access rejected: " + currUid);
                }

                if (exitSu)
                {
                    os.writeBytes("exit\n");
                    os.flush();
                }
            }
        }
        catch (Exception e)
        {
            // Can't get root !
            // Probably broken pipe exception on trying to write to output stream (os) after su failed, meaning that the device is not rooted

            retval = false;
            Log.d("ROOT", "Root access rejected [" + e.getClass().getName() + "] : " + e.getMessage());
        }

        return retval;
    }

    public static void execute(String command, boolean isSuperUser){
        execute(new ArrayList<>(Collections.singletonList(command)), isSuperUser);
    }

    public static boolean execute(ArrayList<String> commands,boolean isSuperUser)
    {
        boolean retval = false;

        try
        {
            if (null != commands && commands.size() > 0)
            {
                Process process = Runtime.getRuntime().exec(isSuperUser ? "su" : "");

                DataOutputStream os = new DataOutputStream(process.getOutputStream());

                // Execute commands that may require root access
                for(int i=0; i<commands.size(); i++)
                {
                    String currCommand = commands.get(i);
                    os.writeBytes(currCommand + "\n");
                    os.flush();
                }

                os.writeBytes("exit\n");
                os.flush();

                try
                {
                    int processRetval = process.waitFor();
                    // Root access granted/denied
                    retval = 255 != processRetval;
                }
                catch (Exception ex)
                {
                    Log.e("ROOT", "Error executing root action", ex);
                }
            }
        }
        catch (IOException ex)
        {
            Log.w("ROOT", "Can't get root access", ex);
        }
        catch (SecurityException ex)
        {
            Log.w("ROOT", "Can't get root access", ex);
        }
        catch (Exception ex)
        {
            Log.w("ROOT", "Error executing internal operation", ex);
        }

        return retval;
    }

}
    public static void setHigherRefreshRate() {
        try {
            // Attempt to set higher refresh rate without root (for supported devices)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                Settings.System.putInt(
                    context.getContentResolver(),
                    "peak_refresh_rate",
                    120  // Target 120Hz if supported
                );
                Settings.System.putInt(
                    context.getContentResolver(),
                    "min_refresh_rate",
                    90  // Set minimum to 90Hz
                );
                Log.d("FPS_BOOST", "Refresh rate set to 120Hz (non-root method)");
            } else {
                Log.d("FPS_BOOST", "Non-root refresh rate adjustment not supported on this Android version.");
            }
        } catch (Exception e) {
            Log.e("FPS_BOOST", "Failed to set refresh rate: " + e.getMessage());
        }

        // Root-based fallback using ADB commands
        if (canRunRootCommands()) {
            executeRootCommand("settings put system peak_refresh_rate 120.0");
            executeRootCommand("settings put system min_refresh_rate 90.0");
            Log.d("FPS_BOOST", "Refresh rate set to 120Hz (root method)");
        } else {
            Log.d("FPS_BOOST", "Root access not available, FPS boost limited.");
        }
    }
