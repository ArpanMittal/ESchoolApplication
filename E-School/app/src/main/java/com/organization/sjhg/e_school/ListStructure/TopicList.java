package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Punit Chhajer on 02-09-2016.
 */
public class TopicList implements Serializable{
    public String chapterId, chapter_name, class_name, subject_name;
    public List<Topic> topics;

    public TopicList(String chapterId, List<Topic> topics){
        this.chapterId = chapterId;
        this.topics = topics;
    }
}
