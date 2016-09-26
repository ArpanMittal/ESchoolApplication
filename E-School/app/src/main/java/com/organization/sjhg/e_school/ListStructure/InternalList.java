package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;

/**
 * Created by arpan on 8/19/2016.
 */
public class InternalList implements Serializable {
    public String id;
    public String name;
    public String count;
    public String image;
    public InternalList(String id, String name, String count) {
        this.id= id;
        this.name = name;
        this.count = count;
    }
    public InternalList(String id, String name, String count,String image) {
        this.id= id;
        this.name = name;
        this.count = count;
        this.image=image;
    }
}
