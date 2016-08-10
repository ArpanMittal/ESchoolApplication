package com.organization.sjhg.e_school.Structure;

/**
 * Created by siddharth on 12/12/15.
 */
public class TopicDetail {

    public int topicId;
    public String topicName;

    public TopicDetail(ContentDetailBase contentObject) {
        topicId = contentObject.topicId;
        topicName = contentObject.topicName;
    }
}
