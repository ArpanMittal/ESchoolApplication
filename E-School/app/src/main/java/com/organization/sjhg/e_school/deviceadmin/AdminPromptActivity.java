package com.organization.sjhg.e_school.deviceadmin;

import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.organization.sjhg.e_school.R;

public class AdminPromptActivity extends ActionBarActivity {

    private static final int SETUP_ADMIN_REQUEST_CODE = 47;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_admin);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.setupAdmin) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, DeviceAdminUtil
                    .getAdminComponent(this));
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "The app needs to be the " +
                    "device admin to run.");
            startActivityForResult(intent, SETUP_ADMIN_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETUP_ADMIN_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Can't proceed without being an admin.", Toast.LENGTH_LONG)
                        .show();
            } else {
                Intent intent = getIntent().getParcelableExtra(DeviceAdminUtil.MY_REDIRECT_INTENT);
                finish();
                if (intent != null) {
                    startActivity(intent);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Can't proceed without being an admin.", Toast.LENGTH_LONG)
                .show();
    }
}
