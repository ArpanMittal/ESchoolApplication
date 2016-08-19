package com.organization.sjhg.e_school.ListStructure;

import com.organization.sjhg.e_school.Helpers.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 8/19/2016.
 */
public class DashBoardList {
    public String title;
    public List<InternalList> internalLists=new ArrayList<>();
    public DashBoardList(String title,List<InternalList> internalLists)
    {
        this.title=title;
        this.internalLists=internalLists;
    }
}

