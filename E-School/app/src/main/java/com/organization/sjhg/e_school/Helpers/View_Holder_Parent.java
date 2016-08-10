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
    CardView cv;
    TextView title;
    TextView description;
    ImageView imageView;
     View_Holder_Parent(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler);
         cv = (CardView) itemView.findViewById(R.id.cardView);
         title = (TextView) itemView.findViewById(R.id.title);
         description = (TextView) itemView.findViewById(R.id.description);
         imageView = (ImageView) itemView.findViewById(R.id.imageView);
        //recyclerView.setHasFixedSize(true);
    }
}