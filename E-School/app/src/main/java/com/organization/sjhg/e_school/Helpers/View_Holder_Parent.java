package com.organization.sjhg.e_school.Helpers;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;

/**
 * Created by arpan on 8/9/2016.
 */
public class View_Holder_Parent extends RecyclerView.ViewHolder {
    RecyclerView recyclerView;

    TextView title;

     View_Holder_Parent(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
         title = (TextView) itemView.findViewById(R.id.title);

        //recyclerView.setHasFixedSize(true);
    }
}