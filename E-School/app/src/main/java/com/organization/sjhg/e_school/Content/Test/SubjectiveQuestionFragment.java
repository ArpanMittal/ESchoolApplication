package com.organization.sjhg.e_school.Content.Test;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;

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

public class SubjectiveQuestionFragment extends QuestionFragment {
    TextView questionView;
    EditText response;
    TextView correctResponse;
    Button changeLanguage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.activity_test_filln_the_blanks, container, false);
        this.questionView = (TextView) fragmentView.findViewById(R.id.question);
        response = (EditText) fragmentView.findViewById(R.id.answer);
        correctResponse = (TextView) fragmentView.findViewById(R.id.correctAnswer);
        changeLanguage=(Button)fragmentView.findViewById(R.id.changeLanguageButton);
       changeLanguage.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v) {
            changeLanguage();
           }
       });
        return fragmentView;
    }
    private void changeLanguage()
    {
        //change language of keyboard
        InputMethodManager ime = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        if(ime!=null) {
            ime.showInputMethodPicker();
        }

    }
    public void LoadQuestion() {
        response.setText("");
        response.setBackgroundColor(Color.WHITE);
        correctResponse.setText("");

        questionView.setText(TestActivity.test.questions.get(position).questionText);
        if (TestActivity.test.questions.get(position).questionType == 6 ||
                TestActivity.test.questions.get(position).questionType == 8) {
            //for fill in the blanks and jumble words
            response.setText("");
            response.setMaxLines(1);
            response.setSingleLine(true);
        } else {
            response.setText("");
        }
    }

    @Override
    public String getAnswer() {
        return response.getText().toString();
    }


    @Override
    public void setAnswer(String selectedAnswers) {

        response.setText(selectedAnswers);
    }


    // If test is already submitted then all the answers will be displayed.
    @Override
    public void displayAnswer() {

        if (TestActivity.test.questions.get(position).selectedAnswer == null
                || TestActivity.test.questions.get(position).selectedAnswer.equals("@@null"))
            correctAnswer(null);

        else
            correctAnswer(TestActivity.test.questions.get(position).selectedAnswer.substring(2));

        response.setEnabled(false);
    }


    // Checking the answer and displaying the correct answer.
    @Override
    public void correctAnswer(String selectedAnswers) {

        response.setText(selectedAnswers);

        if (selectedAnswers != null) {

            if (selectedAnswers.equalsIgnoreCase(TestActivity.test.questions.get(position).correctAnswer))
                response.setBackgroundColor(Color.GREEN);
            else
                response.setBackgroundColor(Color.RED);
        }

        correctResponse.setText("Correct Answer: " + TestActivity.test.questions.get(position).correctAnswer);
        correctResponse.setTextColor(Color.GREEN);
    }
}