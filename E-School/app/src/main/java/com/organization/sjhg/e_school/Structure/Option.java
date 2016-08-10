package com.organization.sjhg.e_school.Structure;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Bharat Lodha on 9/6/2015.
 * Organization : Eurovision Hitech Gurukul
 */
public class Option implements Serializable {

    public final static String JSON_TAG_OPTION_ID = "OptionId";
    public final static String JSON_TAG_OPTION_TEXT = "Opt";
    public final static String JSON_TAG_OPTION_TYPE = "column_id";
    public final static String JSON_TAG_CORRECT_OPTION_ID = "ans";
    public Integer optionId;
    public String optionText;
    public Boolean isLeftOption;
    public String correctAnswer;

    public String selectedOptionId;

    public Option(JSONObject jsonOption) throws JSONException {
        this.optionId = jsonOption.getInt(JSON_TAG_OPTION_ID);
        this.optionText = jsonOption.getString(JSON_TAG_OPTION_TEXT);
        this.isLeftOption = jsonOption.getInt(JSON_TAG_OPTION_TYPE) == 0;
        this.correctAnswer = jsonOption.getString(JSON_TAG_CORRECT_OPTION_ID);
    }
}
