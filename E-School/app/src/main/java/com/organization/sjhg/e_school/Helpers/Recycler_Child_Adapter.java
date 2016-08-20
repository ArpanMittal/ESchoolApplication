package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.ListStructure.InternalList;

import java.util.List;

/**
 * Created by arpan on 8/20/2016.
 */
public class Recycler_Child_Adapter extends RecyclerView.Adapter<Child_View_Holder> {
    private List<InternalList> list;
    private Context context;
    private Activity activity;
    private String title;

    public Recycler_Child_Adapter(List<InternalList> list,Activity activity,Context context,String title)
    {
        this.list=list;
        this.activity=activity;
        this.context=context;
        this.title=title;
    }

    @Override
    public Child_View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }


    @Override
    public void onBindViewHolder(Child_View_Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
