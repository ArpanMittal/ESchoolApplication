package com.organization.sjhg.e_school.ListStructure;

import android.widget.CheckBox;

import java.io.Serializable;

/**
 * Created by arpan on 8/30/2016.
 */
public class ChapterList implements Serializable {
    public String id;
    public String name;
    public String checked_option_id=null;
    //public CheckBox checkBox=null;
    public ChapterList(String id,String name)
    {
        this.id=id;
        this.name=name;
    }


}
