package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.ListStructure.DashBoardList;
import com.organization.sjhg.e_school.ListStructure.InternalList;
import com.organization.sjhg.e_school.Main_Activity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by arpan on 8/8/2016.
 */
public class View_Holder  extends RecyclerView.ViewHolder{

    CardView cv;
    TextView name;
    TextView count;
    ImageView imageView;
    View view;
    RelativeLayout relativeLayout;

    View_Holder( View itemView) {
        super(itemView);
        view = itemView;
        relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.name);
        count = (TextView) itemView.findViewById(R.id.count);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
