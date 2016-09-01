package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;
import java.util.List;

/**
 * Created by arpan on 8/30/2016.
 */
public class ItemDataList implements Serializable {
    public String title;
    public List<InternalListData> internalListDatas;
    public ItemDataList(String title,List<InternalListData> internalListDatas)
    {
        this.title=title;
        this.internalListDatas=internalListDatas;
    }

}
