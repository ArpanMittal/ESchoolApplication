package com.organization.sjhg.e_school.Content.Test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.Option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

public class MatchTheColumnFragment extends QuestionFragment {

    TextView questionView;
    List<TextView> leftSideOptions = new ArrayList<>();
    List<Spinner> rightSideOptions = new ArrayList<>();

    List<TextView> correctMatch = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View questionView = inflater.inflate(R.layout.activity_test_match_the_column, container, false);
        this.questionView = (TextView) questionView.findViewById(R.id.matchQuestion);
        return questionView;
    }

    @Override
    public void LoadQuestion() {
        int totalLeftOption = 0;
        int totalRightOption = 0;
        leftSideOptions.clear();
        rightSideOptions.clear();
        correctMatch.clear();

        questionView.setText(TestActivity.test.questions.get(position).questionText);

        /*String[] Option = test.questions.get(i).TempOption.split("@@");
        String[] OptionId = test.questions.get(i).TempOptionId.split("@@");
        String[] ColumnId = test.questions.get(i).TempColumnId.split("@@");*/
        // List<DataDetails> list = new ArrayList<>();
        List<Option> rightOptions = new ArrayList<>();

        // Add left side options as text views
        for (Option option : TestActivity.test.questions.get(position).options) {
            if (option.isLeftOption) {
                TextView rowTextView = new TextView(getActivity());

                rowTextView.setText(option.optionText);
                rowTextView.setPadding(0, 0, 60, 0);
                rowTextView.setTextSize(20);
                rowTextView.setTextColor(Color.WHITE);
                rowTextView.setId(option.optionId);

                leftSideOptions.add(rowTextView);
                totalLeftOption++;
            } else {
                rightOptions.add(option);
                totalRightOption++;
            }
        }

        // Create a list to be shown as options in the spinner
        Collections.shuffle(rightOptions);
        List<String> rightSpinnerData = new ArrayList<>();
        rightSpinnerData.add("--Select--");

        for (Option option : rightOptions) {
            rightSpinnerData.add(option.optionText);
        }

        // Create spinner for each right side option
        for (int j = 0; j < totalRightOption; j++) {
            Spinner rowSpinner = new Spinner(getActivity());

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_spinner_item, rightSpinnerData);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rowSpinner.setAdapter(dataAdapter);
            rowSpinner.setId(rightOptions.get(j).optionId);
            rightSideOptions.add(rowSpinner);
        }

        // Add a table layout and Add right and left options
        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.dataLayout);
        tableLayout.removeAllViewsInLayout();
        for (int j = 0; j < totalLeftOption; j++) {
            TableRow row = new TableRow(getActivity());

            row.setMinimumHeight(50);
            row.setGravity(Gravity.CENTER);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.FILL_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            if (j < totalLeftOption) {
                row.addView(leftSideOptions.get(j));
            }
            if (j < totalRightOption) {
                row.addView(rightSideOptions.get(j));
            }
            if (j < totalLeftOption) {
                TextView correctAnswerTextView = new TextView(getActivity());
                correctAnswerTextView.setText("");
                correctMatch.add(correctAnswerTextView);
                row.addView(correctMatch.get(j));
            }
            tableLayout.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
        }
    }

    @Override
    public String getAnswer() {
        String answers = "";
        for (int j = 0; j < rightSideOptions.size(); j++) {
            answers = answers + "##" + rightSideOptions.get(j).getSelectedItem();
        }
        return answers;
    }

    @Override
    public void setAnswer(String selectedAnswers) {
        String selectedAns[] = selectedAnswers.split("##");
        for (int j = 1; j < selectedAns.length; j++) {
            for (int ansLoop = 0; ansLoop < rightSideOptions.get(j - 1).getCount(); ansLoop++) {
                if (rightSideOptions.get(j - 1).getItemAtPosition(ansLoop).equals(selectedAns[j])) {
                    rightSideOptions.get(j - 1).setSelection(ansLoop);
                    break;
                }
            }
        }
    }

    private void CorrectMatch(int questionNumber, String selectedAnswer) {
        String answer = rightAnswer(questionNumber);

        if (answer.equals(selectedAnswer))
            rightSideOptions.get(questionNumber).setBackgroundColor(Color.GREEN);
        else
            rightSideOptions.get(questionNumber).setBackgroundColor(Color.RED);
    }


    // If test is already submitted then all the answers will be displayed.
    @Override
    public void displayAnswer() {

        if (TestActivity.test.questions.get(position).selectedAnswer != null) {
            setAnswer(TestActivity.test.questions.get(position).selectedAnswer);
            correctAnswer(TestActivity.test.questions.get(position).selectedAnswer);
        }

        for (int questionNumber = 0; questionNumber < rightSideOptions.size(); questionNumber++) {
            rightAnswer(questionNumber);
            rightSideOptions.get(questionNumber).setEnabled(false);
        }
    }

    private String rightAnswer(int questionNumber) {

        String answer = null;
        for (Option option : TestActivity.test.questions.get(position).options) {
            if (!option.isLeftOption && option.correctAnswer.equals(String.valueOf(leftSideOptions.get(questionNumber).getId()))) {
                correctMatch.get(questionNumber).setTextSize(20);
                correctMatch.get(questionNumber).setPadding(0, 0, 60, 0);
                correctMatch.get(questionNumber).setText(option.optionText);
                correctMatch.get(questionNumber).setTextColor(Color.GREEN);

                answer = option.optionText;
            }
        }
        return answer;
    }


    @Override
    public void correctAnswer(String selectedAnswers) {
        String selectedAns[] = selectedAnswers.split("##");
        for (int j = 1; j < selectedAns.length; j++) {
            for (int ansLoop = 0; ansLoop < rightSideOptions.get(j - 1).getCount(); ansLoop++) {
                if (rightSideOptions.get(j - 1).getItemAtPosition(ansLoop).equals(selectedAns[j])
                        && (!rightSideOptions.get(j - 1).getItemAtPosition(ansLoop).equals("--Select--")))

                    CorrectMatch(j - 1, selectedAns[j]);
            }
        }
    }
}
