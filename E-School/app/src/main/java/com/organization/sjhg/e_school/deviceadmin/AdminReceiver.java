package com.organization.sjhg.e_school.deviceadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Process;

public class AdminReceiver extends android.app.admin.DeviceAdminReceiver {
    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        // Kill the whole app, so that user ends up going to the login screen.
        Process.killProcess(Process.myPid());
        return super.onDisableRequested(context, intent);
    }
}
