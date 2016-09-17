package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/17/2016.
 */
public class QuestionAnswerList implements Serializable {
    public String id;
    public String time_taken;
    public String response;
    public String question_id;
    public String option_id;
    public String question_text;
    public String question_image;
    public String solution_path;
    public String difficulty;
    public List<ChapterList> correctoption=new ArrayList<>();
    public List<ChapterList> useroption=new ArrayList<>();

    public QuestionAnswerList(String id,String time_taken,String response,String question_id,String option_id,String question_text,String question_image,
                              String solution_path,String difficulty,List<ChapterList>correctoption,List<ChapterList>useroption)
    {
        this.id=id;
        this.time_taken=time_taken;
        this.response=response;
        this.question_id=question_id;
        this.option_id=option_id;
        this.question_text=question_text;
        this.question_image=question_image;
        this.solution_path=solution_path;
        this.difficulty=difficulty;
        this.correctoption=correctoption;
        this.useroption=useroption;
    }

}
