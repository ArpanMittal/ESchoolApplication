package com.organization.sjhg.e_school.ListStructure;

import com.numetriclabz.numandroidcharts.StackBarChart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/19/2016.
 */
public class StackGraphList implements Serializable {
    List<CountList> countLists=new ArrayList<>();
    String head;
    public StackGraphList(List<CountList>countLists,String head)
    {
        this.countLists=countLists;
        this.head=head;
    }
}
