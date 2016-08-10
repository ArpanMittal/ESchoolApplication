package com.organization.sjhg.e_school.Content.Test;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.CheckBox;

import com.organization.sjhg.e_school.Structure.TestDetail;

/**
 * Created by Bharat Lodha on 8/31/2015.
 * Organization : Eurovision Hitech Gurukul
 */
public abstract class QuestionFragment extends Fragment {

    int position;

    public static QuestionFragment getFragment(int position) {
        QuestionFragment returnFragment = null;
        switch (TestActivity.test.questions.get(position).questionType) {
            case 1:
            case 10:
                returnFragment = new MultipleChoiceQuestionFragment();
                break;
            case 5:
                returnFragment = new MatchTheColumnFragment();
                break;
            case 6:
            case 8:
            case 9:
                returnFragment = new SubjectiveQuestionFragment();
                break;
            case 7:
                returnFragment = new LabelTheDiagramFragment();
                break;
        }
        if (returnFragment != null) {
            returnFragment.position = position;
        }
        return returnFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.LoadQuestion();
        if (TestActivity.test.status == TestDetail.TestStatus.TEST_SUBMITTED ||
                TestActivity.test.status == TestDetail.TestStatus.TEST_UPLOADED)
            this.displayAnswer();

        if (TestActivity.test.questions.get(position).selectedAnswer != null &&
                TestActivity.test.status == TestDetail.TestStatus.TEST_STARTED) {
            AlReadyAnswered();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (TestActivity.test.status == TestDetail.TestStatus.TEST_STARTED)
            saveAnswer();

    }


    protected abstract void LoadQuestion();

    public abstract String getAnswer();

    public abstract void setAnswer(String selectedAnswers);

    // To display all the answers in case test is already submitted.
    public abstract void displayAnswer();

    // Answer check and correct answer display in case of practice test
    public abstract void correctAnswer(String selectedAnswers);

    public void saveAnswer() {
        String answers = "@@";
        if (this.getAnswer().length() != 0) {
            answers = answers + this.getAnswer();
            TestActivity.test.questions.get(position).selectedAnswer = String.valueOf(answers);
        } else {
            TestActivity.test.questions.get(position).selectedAnswer = null;
        }
    }

    public void AlReadyAnswered() {
        String selectedAns = TestActivity.test.questions.get(position).selectedAnswer.split("@@")[1];
        this.setAnswer(selectedAns);
        if (TestActivity.test.isPractice)
            this.correctAnswer(selectedAns);
    }

    public void LiveVote(Context context){
        if (this.getAnswer().length() != 0) {
            for(CheckBox box: ((MultipleChoiceQuestionFragment)this).checkBoxes){
                box.setClickable(false);
            }
            if (!TestActivity.test.questions.get(position).isSubmitted){
                TestActivity.test.uploadVoting(this.getAnswer(),TestActivity.test.questions.get(position).questionId,context);
                TestActivity.test.questions.get(position).isSubmitted = true;
            }
        }
    }

}
