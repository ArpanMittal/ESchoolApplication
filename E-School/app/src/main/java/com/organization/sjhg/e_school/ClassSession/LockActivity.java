package com.organization.sjhg.e_school.ClassSession;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

public class LockActivity extends ActionBarActivity implements View.OnClickListener {

    @Nullable
    public static LockActivity sCurrent;

    public static void dismiss() {
        if (sCurrent != null) sCurrent.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sCurrent = this;
        setContentView(R.layout.lock_activity);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lock) {
            DeviceAdminUtil.unlockNow(this);
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (sCurrent == this) sCurrent = null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            DeviceAdminUtil.lockIfNeeded(this);
        }
    }
}
