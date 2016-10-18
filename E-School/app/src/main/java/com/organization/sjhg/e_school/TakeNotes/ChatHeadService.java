package com.organization.sjhg.e_school.TakeNotes;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.TakeEvents.EventDisplayActivity;
import com.organization.sjhg.e_school.TakeEvents.EventListingActivity;

import java.util.Calendar;

/**
 * Created by Arpan on 5/20/2016.
 * For NoteHead to open notes as an external service
 */
public class ChatHeadService extends Service {
    private GestureDetector mGestureDetector;
    private WindowManager windowManager;
    private ImageView chatHead;
    WindowManager.LayoutParams params;
     int MAX_CLICK_DURATION = 25;
     static long  startClickTime=0;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        chatHead = new ImageView(this);
        chatHead.setImageResource(R.drawable.add_note);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        mGestureDetector = new GestureDetector(this, new GestureListener());


        //this code is for dragging the chat head
        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean eventConsume= mGestureDetector.onTouchEvent(event);
                if(eventConsume)
                {
                    Intent intent=new Intent(getApplicationContext(),AddSmallNotesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                switch (event.getAction())
                {

                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                   // case MotionEvent.O
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX
                                + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);


                        return true;

                }
                return false;
            }
        });
        windowManager.addView(chatHead, params);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (chatHead != null) {
            windowManager.removeView(chatHead);
            stopService(new Intent(this, ChatHeadService.class));
       //     stopService(new Intent());
            stopSelf();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
