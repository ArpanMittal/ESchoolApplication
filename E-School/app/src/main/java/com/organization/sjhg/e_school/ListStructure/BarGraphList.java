package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;

/**
 * Created by arpan on 9/14/2016.
 */
public class BarGraphList implements Serializable {
    public int correct_attempt;
    public int attempt_question;
    public int total_question;
    public String title;
    public BarGraphList(int correct_attempt,int attempt_question, int total_question ,String title)
    {
        this.correct_attempt=correct_attempt;
        this.attempt_question=attempt_question;
        this.total_question=total_question;
        this.title=title;
    }
}
