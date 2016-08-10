package com.organization.sjhg.e_school.Structure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddharth on 12/12/15.
 */
public class ChapterDetail {

    public int chapterId;
    public String chapterName;
    public List<TopicDetail> topicDetail;


    public ChapterDetail(ContentDetailBase contentObject) {

        chapterId = contentObject.chapterId;
        chapterName = contentObject.chapterName;
        topicDetail = new ArrayList<>();
    }

}
