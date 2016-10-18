package com.organization.sjhg.e_school.Structure;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
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

public class Question implements Serializable {

    public final static String JSON_TAG_QUESTION_ID = "QuestionId";
    public final static String JSON_TAG_QUESTION_TEXT = "Question";
    public final static String JSON_TAG_TEACHERS_ID = "TeacherId";
    public final static String JSON_TAG_TopicId_ID = "TopicId";
    public final static String JSON_TAG_CORRECT_ANSWER = "COptionId";
    public final static String JSON_TAG_NO_OF_OPTIONS = "NofOption";
    public final static String JSON_TAG_DIAGRAM_PATH = "Diagram";
    public final static String JSON_TAG_QUESTION_TYPE = "QuesType";
    public final static String JSON_TAG_OPTIONS_ARRAY = "Option_Objects";

    public int questionId;
    public String questionText;
    public int teachersId;
    public int topicId;
    public String correctAnswer;
    public int noOfOptions;
    public String diagramPath;
    public String selectedAnswer;
    public int questionType;
    public List<Option> options = new ArrayList<>();
    public boolean isSubmitted = false;

    public Question(JSONObject jsonQuestion) throws JSONException {
        this.questionId = jsonQuestion.getInt(JSON_TAG_QUESTION_ID);
        this.questionText = jsonQuestion.getString(JSON_TAG_QUESTION_TEXT);
        this.teachersId = jsonQuestion.getInt(JSON_TAG_TEACHERS_ID);
        this.topicId = jsonQuestion.getInt(JSON_TAG_TopicId_ID);
        this.correctAnswer = jsonQuestion.getString(JSON_TAG_CORRECT_ANSWER);
        this.noOfOptions = jsonQuestion.getInt(JSON_TAG_NO_OF_OPTIONS);
        this.diagramPath = jsonQuestion.getString(JSON_TAG_DIAGRAM_PATH);
        this.questionType = jsonQuestion.getInt(JSON_TAG_QUESTION_TYPE);

        JSONArray arrayOfOptions = jsonQuestion.getJSONArray(JSON_TAG_OPTIONS_ARRAY);
        for (int iterator = 0; iterator < arrayOfOptions.length(); iterator++) {
            this.options.add(new Option(arrayOfOptions.getJSONObject(iterator)));
        }
    }
}


