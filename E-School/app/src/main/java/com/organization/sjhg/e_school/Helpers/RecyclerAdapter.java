package com.organization.sjhg.e_school.Helpers;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.ExaminationParent;
import com.organization.sjhg.e_school.ListActivity;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<View_Holder> implements RemoteCallHandler {

    private final Context mContext;
    private List<InternalList> itemList;
    private RecyclerView recyclerView;
    private String title;
    private String pre;


    public RecyclerAdapter(Context mContext, List<InternalList> list, String title, RecyclerView recyclerView) {
        this.itemList = list;
        this.mContext = mContext;
        this.title=title;
        this.recyclerView=recyclerView;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        //inflate your layout and pass it to view holder
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activitycardview, viewGroup, false);

        return new View_Holder(itemView);
    }

    @Override
    public void onBindViewHolder(View_Holder viewHolder, int position) {
        final InternalList item = itemList.get(position);

        viewHolder.name.setText(item.name);
        viewHolder.count.setText(item.count);
        if(title.equals("Exams"))
        {
            viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#e91e63"));
        }
        else if(title.equals("Subjects"))
        {
            viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#4caf50"));
        }
        else if(title.equals("Classes"))
        {
            viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#2196f3"));
        }
        else
        {
            viewHolder.relativeLayout.setBackgroundColor(Color.parseColor("#2196f3"));
        }
        // to download images
        Picasso.with((Activity)mContext)
            //    .load("http://s10.postimg.org/5ra5n2afd/app_books_xhdpi.png")
               .load(ServerAddress.getServerAddress(mContext)+item.image)

                .into(viewHolder.imageView);

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               takeAction(item);
            }
        });
        viewHolder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAction(item);
            }
        });
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAction(item);
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAction(item);
            }
        });


    }

    private void takeAction(InternalList item)
    {
        if(title.equals("Exams"))
        {
            Intent intent = new Intent(mContext.getApplicationContext(), ExaminationParent.class);
            intent.putExtra(mContext.getString(R.string.jsontitle), title);
            intent.putExtra(mContext.getString(R.string.jsonid), item.id);
            intent.putExtra(mContext.getString(R.string.jsonname), item.name);
            mContext.startActivity(intent);
        }
        else {
            Intent intent = new Intent(mContext.getApplicationContext(), ListActivity.class);
            intent.putExtra(mContext.getString(R.string.title), title);
            intent.putExtra(mContext.getString(R.string.jsonid), item.id);
            intent.putExtra(mContext.getString(R.string.jsonname), item.name);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {

    }
}