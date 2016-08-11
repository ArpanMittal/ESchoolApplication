package com.organization.sjhg.e_school.Content.Test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.organization.sjhg.e_school.ClassSession.LiveVoting;
import com.organization.sjhg.e_school.Database.old.DatabaseOperations;
import com.organization.sjhg.e_school.MainActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.Question;
import com.organization.sjhg.e_school.Structure.TestDetail;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */
/**
 * Edited by Prateek Tulsyan on 06-04-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */
public class TestActivity extends ParentTestActivity {

    //static TestDetail test;
    ImageView btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Bind the tabs to the ViewPager
            PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
            tabs.setViewPager(mViewPager);

        btnSubmit = (ImageView) findViewById(R.id.submitButton);

        btnSubmit.setVisibility(View.VISIBLE);
        btnSubmit.setEnabled(false);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubmit();
            }
        });
        if (test.status == TestDetail.TestStatus.TEST_SUBMITTED || test.status == TestDetail.TestStatus.TEST_UPLOADED) {
            btnSubmit.setImageResource(R.drawable.close);
            btnSubmit.setEnabled(true);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        if (test.status == TestDetail.TestStatus.TEST_NOT_STARTED || test.status == TestDetail.TestStatus.TEST_STARTED) {

            tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    ((QuestionFragment) testCollectionPagerAdapter.getItem(mViewPager.getCurrentItem())).saveAnswer();
                    if(TestActivity.test.isLiveVoting){
                            ((QuestionFragment) testCollectionPagerAdapter.getItem(position)).LiveVote(getApplicationContext());
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    if (TestActivity.test.questions.get(position).selectedAnswer != null &&
                            TestActivity.test.status == TestDetail.TestStatus.TEST_STARTED)
                        ((QuestionFragment) testCollectionPagerAdapter.getItem(position)).AlReadyAnswered();

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if (test.status == TestDetail.TestStatus.TEST_STARTED && !test.isPractice)
                SubmitAlertDialog(true, getString(R.string.test_submit_on_app_kill));
            if (test.isTestTimed)
                EnableTimerFunction();

            btnSubmit.setEnabled(true);
            test.status = TestDetail.TestStatus.TEST_STARTED;
            if (!SaveTestToLocal()) finish();


        }
    }

    // On Click Submit Test
    private void onClickSubmit() {
        ((QuestionFragment) testCollectionPagerAdapter.getItem(mViewPager.getCurrentItem())).saveAnswer();

        Boolean allQuestionsAnswered = true;
        for (int j = 0; j < test.questions.size(); j++) {
            if (test.questions.get(j).selectedAnswer == null ||
                    test.questions.get(j).selectedAnswer.equals("")) {
                allQuestionsAnswered = false;
                break;
            }
        }
        if (test.isLiveVoting){
            for (int j = 0; j < test.questions.size(); j++) {
                Question ques = test.questions.get(j);
                if (!(test.questions.get(j).selectedAnswer == null ||
                        test.questions.get(j).selectedAnswer.equals("")) &&
                        !ques.isSubmitted) {
                    test.uploadVoting(ques.selectedAnswer,ques.questionId,getApplicationContext());
                }
            }
            try {
                DatabaseOperations.deleteContent(test.contentFileId, getApplicationContext());
                File file = new File(test.localFilePath);
                file.delete();
                LiveVoting.isVoting = false;

                Intent intent=new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                super.isSubmittedFromTestButton = true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }
        if (allQuestionsAnswered)
            SubmitAlertDialog(false, getString(R.string.submit_test));
        else
            SubmitAlertDialog(false, getString(R.string.all_questions_not_answered));
    }
}