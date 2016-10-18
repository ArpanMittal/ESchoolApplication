package com.organization.sjhg.e_school.Content.Test;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.sjhg.e_school.CommonFragmentTheme;
import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Content.Test.TestCollectionPagerAdapter;
import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Structure.Question;
import com.organization.sjhg.e_school.Structure.TestDetail;
import com.organization.sjhg.e_school.TakeNotes.ChatHeadService;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Arpan on 1/28/2016.
 */
public class ParentTestActivity extends FragmentActivity {
    static TestDetail test;
    TextView timerDisplay;
    public int remainingTimeInSeconds;
    TestCollectionPagerAdapter testCollectionPagerAdapter;
    ViewPager mViewPager;
    protected java.util.Timer timer;
    String localFilePath;
    int numberOfExits = 0;
    long timeOfExit;
    boolean isSubmittedFromTestButton = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stopService(new Intent(this, ChatHeadService.class));
        Intent intent = getIntent();
        localFilePath = intent.getStringExtra("localFilePath");
        try {
            test = TestDetail.getTesDetailObjectFromLocal(localFilePath);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.basic_error, Toast.LENGTH_LONG).show();
            finish();
        }
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());

        View bottomActionBar=findViewById(R.id.bottomBarFragment);
        bottomActionBar.setVisibility(View.INVISIBLE);
        //change layout of test
        ViewPager pager = (ViewPager)  findViewById(R.id.contentList);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)pager.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        pager.setLayoutParams(params);
//        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
//        params = (RelativeLayout.LayoutParams)tabsStrip.getLayoutParams();
//        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        tabsStrip.setLayoutParams(params);
//        if(test.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue())
//        {
//            tabsStrip.setVisibility(View.INVISIBLE);
//            ImageView background = (ImageView) findViewById(R.id.mainBackground);
//            background.setVisibility(View.INVISIBLE);
//        }
        loadFragment();
    }
    protected void loadFragment()
    {

      mViewPager = (ViewPager) findViewById(R.id.contentList);

        if(test.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue())
        {
           testCollectionPagerAdapter=new TestCollectionPagerAdapter(getSupportFragmentManager());

        }
        else {
            testCollectionPagerAdapter = new TestCollectionPagerAdapter(getSupportFragmentManager());
        }

        mViewPager.setAdapter(testCollectionPagerAdapter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


  protected void EnableTimerFunction() {
      // Blinking Animation for warning time.
      final Animation anim = new AlphaAnimation(0.0f, 1.0f);
      anim.setDuration(1000);
      anim.setStartOffset(40);
      anim.setRepeatMode(Animation.REVERSE);
      anim.setRepeatCount(Animation.INFINITE);

      // Timer display UI.
      ViewGroup custom = (ViewGroup) findViewById(R.id.testBar);
      timerDisplay = new TextView(this);
      timerDisplay.setTextColor(Color.WHITE);
      custom.addView(timerDisplay);
      RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)timerDisplay.getLayoutParams();
      params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
      timerDisplay.setLayoutParams(params);
      if (test.timeLimit.equals("")) {
          return;
      }

      // Setting new Timer variable
      timer = new Timer();

      // Converting String time to seconds.
      int timeLimitInSeconds = 0;
      String[] splitTimeLimit = test.timeLimit.split(":");
      timeLimitInSeconds += Integer.parseInt(splitTimeLimit[0]) * GlobalConstants.SECONDS_IN_HOUR;
      timeLimitInSeconds += Integer.parseInt(splitTimeLimit[1]) * GlobalConstants.SECONDS_IN_MINUTE;
      timeLimitInSeconds += Integer.parseInt(splitTimeLimit[2]);

      final int durationInSeconds = timeLimitInSeconds;

      final long endTime = (SystemClock.elapsedRealtime() / GlobalConstants.MILISECONDS_IN_SECOND) + durationInSeconds;

      // Start timer.
      timer.scheduleAtFixedRate(new TimerTask() {

          @Override
          // This will run in duration of 1 second.
          public void run() {
              runOnUiThread(new Runnable() {

                  int seconds
                          ,
                          minutes
                          ,
                          hours;
                  int warningTimeSeconds = 10;

                  public void run() {
                      // Updating remaining time after every second.
                      remainingTimeInSeconds = (int) (endTime - (SystemClock.elapsedRealtime() / GlobalConstants.MILISECONDS_IN_SECOND));
                      seconds = remainingTimeInSeconds % GlobalConstants.SECONDS_IN_MINUTE;
                      minutes = (remainingTimeInSeconds / GlobalConstants.MINUTES_IN_HOUR) % GlobalConstants.MINUTES_IN_HOUR;
                      hours = remainingTimeInSeconds / GlobalConstants.SECONDS_IN_HOUR;

                      // Stopping timer at the end of test duration and auto submitting the answers.
                      if (remainingTimeInSeconds <= 0) {
                          timerDisplay.setText("00:00:00");
                          if(test.contentTypeId!= Content_Type.ADAPTIVE_TEST.getValue()) {
                              ((QuestionFragment) testCollectionPagerAdapter.getItem(mViewPager.getCurrentItem())).saveAnswer();

                          }
                          SubmitAlertDialog(true, "Time's up! Test has been submitted");
                          timer.cancel();


                      } else {
                          // Showing warning time.
                          if (remainingTimeInSeconds <= warningTimeSeconds) {
                              timerDisplay.startAnimation(anim);
                              timerDisplay.setTextColor(Color.RED);
                          }
                          // Displaying timer in HH:MM:SS format.
                          timerDisplay.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                      }
                  }
              });
          }
      }, 1000, 1000);
  }
    // This will check the number of exits are made during the test and time of exit.
    @Override
    public void onPause() {
        if (!isSubmittedFromTestButton && (test.status == TestDetail.TestStatus.TEST_STARTED ||test.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue())&& !test.isPractice) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.moved_out_of_test_message, Toast.LENGTH_LONG);

            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        numberOfExits += 1;
        timeOfExit = SystemClock.elapsedRealtime() / 1000;
        super.onPause();
    }

    // Condition to see if test has to be submitted automatically.
    @Override
    public void onResume() {
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        if ((test.status == TestDetail.TestStatus.TEST_STARTED||test.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue()) && test.isTestTimed && numberOfExits > 0) {
            long timeOfResume = SystemClock.elapsedRealtime() / 1000;
            if (numberOfExits > 1 || timeOfResume - timeOfExit > 15) {

                if(test.contentTypeId!=Content_Type.ADAPTIVE_TEST.getValue()) {
                    ((QuestionFragment) testCollectionPagerAdapter.getItem(mViewPager.getCurrentItem())).saveAnswer();
                }
                SubmitAlertDialog(true, getString(R.string.moved_out_of_test_submit_message));
            }
        }
        super.onResume();
    }


    protected void SubmitAlertDialog(boolean isSubmittedAfterTimeout, String displayMessage)
    {
        final AlertDialog.Builder submitAlert = new AlertDialog.Builder(new ContextThemeWrapper(ParentTestActivity.this, android.R.style.Theme_Holo_Light_Dialog));
        submitAlert.setMessage(displayMessage);
        submitAlert.setCancelable(false);
        submitAlert.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        test.status = TestDetail.TestStatus.TEST_SUBMITTED;
                        SaveTestToLocal();
                        Toast.makeText(getApplication(), getResources().getString(R.string.success_saving_answer), Toast.LENGTH_LONG);
                        Intent goToHome = new Intent(ParentTestActivity.this, MainActivity.class);
                        startActivity(goToHome);
                        dialog.cancel();
                        isSubmittedFromTestButton = true;
                        if (SaveTestToLocal()) {
                            dialog.cancel();
                            isSubmittedFromTestButton = true;
                        }
                    }
                });
        if (!isSubmittedAfterTimeout) {
            submitAlert.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
        }
        AlertDialog dialog = submitAlert.create();
        dialog.show();
    }
    protected boolean SaveTestToLocal() {
        try {
            test.SaveTestToLocal();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if(test.status == TestDetail.TestStatus.TEST_STARTED || test.status == TestDetail.TestStatus.TEST_SUBMITTED){
                ExceptionHandler.showAlertDialogContent(getApplicationContext(),
                        getApplicationContext().getResources().getString(R.string.alert_error_title_wrong),
                        "Test couldn't be saved contact your teacher.");
            }else{
                ExceptionHandler.showAlertDialogContent(getApplicationContext(),
                        getApplicationContext().getResources().getString(R.string.alert_error_title_wrong),
                        "Test was corrupted. Delete and download again.");
            }
            return false;
        }
    }

    protected void submitAdaptiveTest()
    {
            isSubmittedFromTestButton = true;
            Intent goToHome = new Intent(this,MainActivity.class);
            test.status = TestDetail.TestStatus.ADAPTIVETEST_SUBMITTED;
            //clear the question before save to local
            test.questions.clear();
            SaveTestToLocal();
            startActivity(goToHome);
    }

    @Override
    public void onBackPressed() {

        if (!test.isTestTimed&&test.contentTypeId!=Content_Type.ADAPTIVE_TEST.getValue()) {
            ((QuestionFragment) testCollectionPagerAdapter.getItem(mViewPager.getCurrentItem())).saveAnswer();
            if (SaveTestToLocal()) {
                super.onBackPressed();
            }
        } else if (test.status == TestDetail.TestStatus.TEST_SUBMITTED || test.status == TestDetail.TestStatus.TEST_UPLOADED)
            super.onBackPressed();

        else {
            AlertDialog.Builder warningBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));;
            warningBuilder.setMessage(getString(R.string.on_back_press_warning_message));
            warningBuilder.setCancelable(false);

            warningBuilder.setNegativeButton("OK", null).create().show();
        }
    }
}
