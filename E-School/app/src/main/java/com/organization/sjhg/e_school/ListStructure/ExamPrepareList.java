package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/2/2016.
 */
public class ExamPrepareList implements Serializable {
    public String cost;
    public String duration;
    public String id;
    public List<InternalListData> internalListDatas=new ArrayList<>();
    public ExamPrepareList(String cost,String duration,String id,List<InternalListData> internalListDatas)
    {
        this.cost=cost;
        this.duration=duration;
        this.id=id;
        this.internalListDatas=internalListDatas;
    }
}
