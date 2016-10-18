package com.organization.sjhg.e_school.Content.Test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreyas Tripathy on 07 Dec 15.
 */
public class LabelTheDiagramFragment extends QuestionFragment {
    TextView questionView;
    String imagePath = "/storage/sdcard0/E-SchoolContent/";
    String imagePath2 = "/mnt/sdcard/E-SchoolContent/";
    List<EditText> labels = new ArrayList<>();

    ImageView diagram;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View questionView = inflater.inflate(R.layout.activity_test_label_the_diagram, container, false);
            this.questionView = (TextView) questionView.findViewById(R.id.testLabel);
            this.diagram = (ImageView) questionView.findViewById(R.id.diagram);
            return questionView;
        }

        public void LoadQuestion()
        {

            labels.clear();
            questionView.setText(TestActivity.test.questions.get(position).questionText);
            TableLayout labels1 = (TableLayout) getActivity().findViewById(R.id.labelOptions);
            labels1.removeAllViewsInLayout();

            for(int i=0; i<TestActivity.test.questions.get(position).noOfOptions; i++) {
                EditText editText = new EditText(getActivity());
                editText.setId(i);
                labels1.addView(editText);
                labels.add(i, editText);
            }
/*            for(int i = 0; i<TestActivity.test.questions.get(position).noOfOptions; i++) {
                TableRow row = new TableRow(getActivity());
                row.setMinimumHeight(50);
                row.setGravity(Gravity.CENTER);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.FILL_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                row.addView(labels);
            }*/

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Bitmap bitmap = BitmapFactory.decodeFile(imagePath+TestActivity.test.questions.get(position).diagramPath,options);
            if (bitmap == null){
                bitmap = BitmapFactory.decodeFile(imagePath2+TestActivity.test.questions.get(position).diagramPath,options);
            }
            diagram.setImageBitmap(bitmap);

        }

        @Override
        public String getAnswer() {
            String answers = "";
            int val=labels.size();
            for (int i = 0; i < labels.size(); i++) {
                answers = answers + "##" + labels.get(i).getText();
            }
            return answers;
        }


        @Override
        public void setAnswer(String selectedAnswers) {
            String selectedAns[] = selectedAnswers.split("##");
            for (int j = 1; j < selectedAns.length; j++) {
                labels.get(j).setText(selectedAns[j]);
            }

        }

    // If test is already submitted then all the answers will be displayed.
    @Override
    public void displayAnswer() {
       if (TestActivity.test.questions.get(position).selectedAnswer == null
                || TestActivity.test.questions.get(position).selectedAnswer.equals("@@null"))

            correctAnswer(null);

        else {
          // correctAnswer(TestActivity.test.questions.get(position).selectedAnswer);
           String selectedAns[] = TestActivity.test.questions.get(position).selectedAnswer.split("##");
           int len=selectedAns.length;
           for (int j = 1; j < selectedAns.length; j++) {
               labels.get(j-1).setText(selectedAns[j]);
               labels.get(j-1).setEnabled(false);
           }
       }

    }

        // Checking the answer and displaying the correct answer.
        @Override
        public void correctAnswer(String selectedAnswers) {

     /*       String val=selectedAnswers;
            String selectedAns[] = selectedAnswers.split("##");
            int len=selectedAns.length;
            for (int j = 0; j < selectedAns.length-1; j++) {
                labels.get(j).setText(selectedAns[j]);
                labels.get(j).setEnabled(false);
            }*/

            //correctResponse.setTextColor(Color.GREEN);
        }
}
