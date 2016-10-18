package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;
import java.util.List;

/**
 * Created by arpan on 8/30/2016.
 */
public class InternalListData implements Serializable {
    public String id;
    public String name;
    public List<ChapterList> chapterLists;
    public InternalListData(String id,String name,List<ChapterList> chapterLists)
    {
        this.id=id;
        this.name=name;
        this.chapterLists=chapterLists;
    }
}
