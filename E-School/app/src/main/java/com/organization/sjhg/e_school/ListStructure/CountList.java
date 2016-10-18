package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;

/**
 * Created by arpan on 9/19/2016.
 */
public class CountList implements Serializable {
    public int count;
    public String title;
    public CountList(int count,String title)
    {
        this.count=count;
        this.title=title;
    }
}
