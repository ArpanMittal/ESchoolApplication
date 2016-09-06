package com.organization.sjhg.e_school.ListStructure;

import android.support.design.internal.ParcelableSparseArray;

import com.organization.sjhg.e_school.Helpers.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 8/19/2016.
 */
public class DashBoardList implements Serializable {
    public String title;
    public List<InternalList> internalLists=new ArrayList<>();
    public List<ExamPrepareList> examPrepareLists=new ArrayList<>();
    public List<ChapterList> chapterLists=new ArrayList<>();
    public DashBoardList(String title,List<InternalList> internalLists)
    {
        this.title=title;
        this.internalLists=internalLists;
    }
    public DashBoardList(String title,List<ExamPrepareList> examPrepareLists,List<ChapterList> chapterLists) {
        this.title=title;
        this.examPrepareLists=examPrepareLists;
        this.chapterLists=chapterLists;
    }
}

