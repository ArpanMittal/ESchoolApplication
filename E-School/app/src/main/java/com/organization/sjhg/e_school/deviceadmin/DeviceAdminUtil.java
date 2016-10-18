package com.organization.sjhg.e_school.deviceadmin;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.organization.sjhg.e_school.ClassSession.LockActivity;

public class DeviceAdminUtil {
    public static final String MY_REDIRECT_INTENT = "MyRedirectIntent";
    private static boolean isLocked;

    private DeviceAdminUtil() {
    }

    public static void checkAndPrompt(Activity activity) {
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager(activity);
        if (!devicePolicyManager.isAdminActive(getAdminComponent(activity))) {
            Intent intent = activity.getIntent();
            activity.startActivity(new Intent(activity, AdminPromptActivity.class).putExtra
                    (MY_REDIRECT_INTENT, intent));
        }
    }

    @NonNull
    static ComponentName getAdminComponent(Context context) {
        return new ComponentName(context.getApplicationContext(), AdminReceiver.class);
    }

    public static void lockIfNeeded(Context context) {
        if (isLocked) {
            DevicePolicyManager devicePolicyManager = getDevicePolicyManager(context);
            if (devicePolicyManager.isAdminActive(getAdminComponent(context))) {
                devicePolicyManager.lockNow();
            }
        }
    }

    public static void lockNow(Context context) {
        if (isLocked) {
            LockActivity current = LockActivity.sCurrent;
            if (current != null && !current.isFinishing() && Build.VERSION.SDK_INT >= Build
                    .VERSION_CODES.JELLY_BEAN_MR1 && !current.isDestroyed()) {
                return;
            }
        }
        Log.i("Lock", "attempting to lock");
        Intent intent = new Intent(context, LockActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager(context);
        if (devicePolicyManager.isAdminActive(getAdminComponent(context))) {
            devicePolicyManager.resetPassword("foobar", 0);
            devicePolicyManager.lockNow();
            Log.i("LOCK", "device admin locked device");
            isLocked = true;
        } else {
            Log.i("LOCK", "device admin not active. can't lock");
        }
    }

    private static DevicePolicyManager getDevicePolicyManager(Context context) {
        return (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }

    public static void unlockNow(Context context) {
        if (!isLocked)
            return;

        isLocked = false;
        LockActivity.dismiss();
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager(context);
        if (devicePolicyManager.isAdminActive(getAdminComponent(context))) {
            devicePolicyManager.resetPassword("", 0);
        }
    }
}
