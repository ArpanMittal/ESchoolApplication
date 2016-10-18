package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;

/**
 * Created by Punit Chhajer on 02-09-2016.
 */
public class Topic implements Serializable{
    public String name, hash, contentId,pdf_path, video_path,pdf_hash;
    private Boolean subscribed,lock;
    private int progress;

    public Topic(String name, String hash, String pdf_path, String video_path, String pdf_hash, Boolean subscribed, Boolean lock, int progress){
        this.name = name;
        this.hash = hash;
        this.pdf_path = pdf_path;
        this.video_path = video_path;
        this.pdf_hash = pdf_hash;
        this.subscribed = subscribed;
        this.lock = lock;
        this.progress = progress;
    }

    public Topic(String topic_name, String topic_id) {
        this.name = topic_name;
        this.hash = topic_id;
    }

    public int getProgress(){
        return progress;
    }

    public Boolean islock(){
        return lock;
    }

    public Boolean isSubscribed(){
        return subscribed;
    }
}
