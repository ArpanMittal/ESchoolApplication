package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;
import com.squareup.picasso.Picasso;

/**
 * Created by arpan on 8/20/2016.
 */
public class Child_View_Holder extends RecyclerView.ViewHolder{
    CardView cv;
    TextView name;
    TextView count;
    ImageView imageView;
    String title;


    Child_View_Holder(View itemView)
    {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.name);
        count = (TextView) itemView.findViewById(R.id.count);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);

    }
}