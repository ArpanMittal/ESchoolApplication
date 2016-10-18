package com.organization.sjhg.e_school.Content.Test;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.Option;

import java.util.ArrayList;
import java.util.List;


public class MultipleChoiceQuestionFragment extends QuestionFragment {

    TextView questionTextView;
    RelativeLayout Screen;
    TableLayout checkBoxTable;
    List<CheckBox> checkBoxes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View questionView = inflater.inflate(R.layout.activity_mcq_and_obj, container, false);
        questionTextView = (TextView) questionView.findViewById(R.id.mcqQuestion);
        Screen = (RelativeLayout) questionView.findViewById(R.id.testMcqAndObjLayout);
        checkBoxTable = (TableLayout) questionView.findViewById(R.id.mcqCheckboxHolderTable);
        return questionView;
    }


    @Override
    public void LoadQuestion() {

        questionTextView.setText(TestActivity.test.questions.get(position).questionText);

        // Create required number of checkboxes
        checkBoxes = new ArrayList<>();
        for (Option option : TestActivity.test.questions.get(position).options) {
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
            checkBox.setMaxWidth(900);
            checkBox.setGravity(Gravity.LEFT);
          //  checkBox.setPadding(0,0,0,0);
            checkBox.setId(option.optionId);
            checkBox.setText(option.optionText);
            checkBox.setTextSize(20);
            checkBoxes.add(checkBox);
        }


        // Allow choosing of only one option from the given options.
        if (TestActivity.test.questions.get(position).questionType == 10) {
            // Add onclick function to all checkboxes
            // Logic is to un-check all other checkboxes
            for (int checkBoxCounter = 0; checkBoxCounter < checkBoxes.size(); checkBoxCounter++) {
                checkBoxes.get(checkBoxCounter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int counter = 0; counter < checkBoxes.size(); counter++) {
                            // Clear all other checkboxes except current
                            if ((view).getId() == checkBoxes.get(counter).getId())
                                continue;
                            checkBoxes.get(counter).setChecked(false);
                        }
                    }
                });
            }
        }

        // Add the checkboxes to the UI
        checkBoxTable.removeAllViewsInLayout();
        for (int checkboxCounter = 0; checkboxCounter < checkBoxes.size(); checkboxCounter++) {

            TableRow row = new TableRow(getActivity());
            row.setMinimumHeight(50);
            row.setGravity(Gravity.CENTER);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            row.addView(checkBoxes.get(checkboxCounter));
            checkBoxTable.addView(row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT
            ));
        }
    }

    @Override
    public String getAnswer() {
        String answer = "";
        for (int checkboxCounter = 0; checkboxCounter < checkBoxes.size(); checkboxCounter++) {
            if (checkBoxes.get(checkboxCounter).isChecked()) {
                answer = answer + "##" + checkBoxes.get(checkboxCounter).getId();
            }
        }
        return answer;
    }


    @Override
    public void setAnswer(String selectedAnswers) {
        String answerAryData[] = selectedAnswers.split("##");
        for (int answerIterator = 1; answerIterator < answerAryData.length; answerIterator++) {
            for (int checkboxCounter = 0; checkboxCounter < checkBoxes.size(); checkboxCounter++) {
                String currentCheckboxId = String.valueOf(checkBoxes.get(checkboxCounter).getId());
                if (currentCheckboxId.equals(answerAryData[answerIterator]) || checkBoxes.get(checkboxCounter).isChecked()) {
                    checkBoxes.get(checkboxCounter).setChecked(true);
                } else {
                    checkBoxes.get(checkboxCounter).setChecked(false);
                }
            }
        }
    }


    private void checkCorrectAnswer(String currentCheckboxId, int checkboxCounter) {
        for (Option option : TestActivity.test.questions.get(position).options) {
            if (option.correctAnswer.equals(currentCheckboxId))
                checkBoxes.get(checkboxCounter).setBackgroundColor(Color.GREEN);
        }
    }


    private void checkIncorrectAnswer(String currentCheckboxId, int checkboxCounter) {
        for (Option option : TestActivity.test.questions.get(position).options) {
            if (!option.correctAnswer.equals(currentCheckboxId) && !option.correctAnswer.equals("null"))
                checkBoxes.get(checkboxCounter).setBackgroundColor(Color.RED);
        }
    }


    // If test is already submitted then all the answers will be displayed.
    @Override
    public void displayAnswer() {

        if (TestActivity.test.questions.get(position).selectedAnswer != null) {
            setAnswer(TestActivity.test.questions.get(position).selectedAnswer);
            correctAnswer(TestActivity.test.questions.get(position).selectedAnswer);
        }

        for (Option option : TestActivity.test.questions.get(position).options) {
            for (CheckBox checkbox : checkBoxes) {
                if (option.correctAnswer.equals(String.valueOf(checkbox.getId())))
                    checkbox.setBackgroundColor(Color.GREEN);
                checkbox.setEnabled(false);
            }
        }
    }

    @Override
    public void correctAnswer(String selectedAnswers) {
        String answerAryData[] = selectedAnswers.split("##");
        for (int answerIterator = 1; answerIterator < answerAryData.length; answerIterator++) {
            for (int checkboxCounter = 0; checkboxCounter < checkBoxes.size(); checkboxCounter++) {
                String currentCheckboxId = String.valueOf(checkBoxes.get(checkboxCounter).getId());
                if (currentCheckboxId.equals(answerAryData[answerIterator]))
                    checkIncorrectAnswer(currentCheckboxId, checkboxCounter);

                checkCorrectAnswer(currentCheckboxId, checkboxCounter);
            }
        }
    }
}




