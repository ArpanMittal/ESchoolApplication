package com.organization.sjhg.e_school.ListStructure;

import com.organization.sjhg.e_school.Helpers.QuestionOptionAdapter;

import java.io.Serializable;

/**
 * Created by arpan on 9/9/2016.
 */
public class QuestionResponseList implements Serializable {
    public String questionId;
    public String optionId;
    public QuestionResponseList(String questionId,String optionId)
    {
        this.questionId=questionId;
        this.optionId=optionId;
    }
}
