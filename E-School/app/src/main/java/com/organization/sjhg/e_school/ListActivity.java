package com.organization.sjhg.e_school;

import android.os.Bundle;
import android.view.ViewStub;

/**
 * Created by arpan on 8/24/2016.
 */
public class ListActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub viewStub=(ViewStub)findViewById(R.id.viewstub);
        viewStub.setLayoutResource(R.layout.content_main);
        viewStub.inflate();
    }
}
