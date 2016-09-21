package com.organization.sjhg.e_school.ListStructure;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/19/2016.
 */
public class StackGraphList implements Serializable {
    public int easy_count;
    public int medium_count;
    public int hard_count;
    public String head;
    public StackGraphList(String head,int easy_count,int medium_count,int hard_count)
    {
        this.head=head;
        this.easy_count=easy_count;
        this.medium_count=medium_count;
        this.hard_count=hard_count;
    }
    public float[] getArray()
    {
        float[] f=new float[3];
        f[0]=easy_count;
        f[1]=medium_count;
        f[2]=hard_count;
        return f;

    }
}
