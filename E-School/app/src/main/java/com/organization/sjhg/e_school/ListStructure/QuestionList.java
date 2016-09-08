package com.organization.sjhg.e_school.ListStructure;

import org.json.JSONObject;

import java.io.Serializable;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/7/2016.
 */
public class QuestionList implements Serializable {
    public String id;
    public String hash;
    public String type_id;
    public String question_text;
    public String solution_path;
    public String difficulty;
    public String question_image_path;
    public String answer;
    public List<ChapterList>chapterLists=new ArrayList<>();
    public QuestionList(String id,String hash,String type_id,String question_text,String solution_path,String difficulty,String question_path,String answer,List<ChapterList> chapterLists)
    {
       this.id=id;
        this.hash=hash;
        this.type_id=type_id;
        this.question_text=question_text;
        this.solution_path=solution_path;
        this.difficulty=difficulty;
        this.question_image_path=question_path;
        this.answer=answer;
        this.chapterLists=chapterLists;
    }

}
