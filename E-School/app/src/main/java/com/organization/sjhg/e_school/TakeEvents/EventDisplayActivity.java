package com.organization.sjhg.e_school.TakeEvents;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.TakeNotes.NotesDetailTable;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

/**
 * Created by Arpan on 3/4/2016.
 */
public  class EventDisplayActivity extends Activity {
    private TextView mTitleText;
    private TextView mBodyText;
    private TextView mDateText;
    private Long mRowId;
    private Cursor event;
    private EventDetailTable table_obj=new EventDetailTable(this);
    //public EventDisplayActivity(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        setContentView(R.layout.activity_diary_display);
        mTitleText = (TextView) findViewById(R.id.event_title);
        mBodyText = (TextView) findViewById(R.id.event_body);
        mDateText = (TextView) findViewById(R.id.diarylist_date);
       // mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState.getSerializable(EventDetailTable.KEY_ROWID);
       // if (mRowId == null)
        //{

            mRowId = extras != null ? extras.getLong(EventDetailTable.KEY_ROWID) : null;

        //}
        populateDiaryWindow();

    }

    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
    }

    private void populateDiaryWindow()
    {
        if (mRowId != null)
        {
            table_obj.open();
            event = table_obj.fetchSingleEvent(mRowId);
            startManagingCursor(event);

            mTitleText.setText(event.getString(event.getColumnIndexOrThrow(NotesDetailTable.KEY_TITLE)));
            mBodyText.setText(event.getString(event.getColumnIndexOrThrow(NotesDetailTable.KEY_BODY)));
            mDateText.setText(event.getString(event.getColumnIndexOrThrow(NotesDetailTable.KEY_DATE)));
        }
    }
}
