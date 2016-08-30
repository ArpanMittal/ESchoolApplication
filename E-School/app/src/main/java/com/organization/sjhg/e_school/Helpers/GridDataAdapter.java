package com.organization.sjhg.e_school.Helpers;

/**
 * Created by arpan on 8/29/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.ListStructure.AndroidVersion;
import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.ListStructure.InternalListData;
import com.organization.sjhg.e_school.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridDataAdapter extends RecyclerView.Adapter<GridDataAdapter.ViewHolder> {
    private List<InternalListData> android;
    private Context context;

    public GridDataAdapter(Context context,List<InternalListData> android) {
        this.android = android;
        this.context = context;
    }

    @Override
    public GridDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridDataAdapter.ViewHolder viewHolder, int i) {

        //viewHolder.tv_android.setText(android.get(i).getAndroid_version_name());
        //Picasso.with(context).load(android.get(i).getAndroid_image_url()).resize(240, 120).into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return android.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_android;
        private ImageView img_android;
        public ViewHolder(View view) {
            super(view);

            tv_android = (TextView)view.findViewById(R.id.tv_android);
            img_android = (ImageView) view.findViewById(R.id.img_android);
        }
    }

}