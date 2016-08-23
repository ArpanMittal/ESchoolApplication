package com.organization.sjhg.e_school.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
public class View_Holder extends RecyclerView.ViewHolder implements RemoteCallHandler {


    CardView cv;
    TextView name;
    TextView count;
    ImageView imageView;
    String title;
    String id;
    String pre;
    Activity activity;
    RecyclerView recyclerView;
    ToastActivity toastActivity=new ToastActivity();




    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        name = (TextView) itemView.findViewById(R.id.name);
        count = (TextView) itemView.findViewById(R.id.count);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //for toggle view
                if(id.equals(pre))
                {
                    recyclerView.setVisibility(View.GONE);
                    pre=null;
                }
                else {
                    pre=id;
                    new RemoteHelper(view.getContext().getApplicationContext()).getItemDetails(View_Holder.this, RemoteCalls.GET_ITEM_DETAILS, title, id);
                }
            }
        });

    }
    //put data from json to list
    private List<InternalList> getList(JSONObject response)
    {
        //return dataList;
        List<InternalList> dataList=new ArrayList<>();
        try {
            //title=response.getString(activity.getString(R.string.title));
            JSONArray data = response.getJSONArray(activity.getString(R.string.data));
            int length=data.length();

            for(int i=0;i<data.length();i++)
            {
                JSONObject internalListObject=data.getJSONObject(i);
                String id=internalListObject.getString(activity.getString(R.string.jsonid));
                String name=internalListObject.getString(activity.getString(R.string.jsonname));
                String count=internalListObject.getString(activity.getString(R.string.jsoncount));
                dataList.add(new InternalList(id,name,count));


            }


            //for (int i = 0; i <)
        }catch (Exception e) {
            e.printStackTrace();
            toastActivity.makeJsonException(activity);
            LogHelper logHelper = new LogHelper(e);

        }
        return dataList;
    }


    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if(!isSuccessful)
        {
            toastActivity.makeUknownErrorMessage(activity);

        }
        else
        {
            try {
                if (response.getString("success").equals("false")) {
                    toastActivity.makeToastMessage(response,activity);
                }
                else
                {
                    title=response.getString(activity.getString(R.string.jsontitle));
                    recyclerView.setHasFixedSize(true);
                    List<InternalList> dataList=new ArrayList<>();
                    dataList=getList(response);
                    Recycler_Child_Adapter adapter = new Recycler_Child_Adapter(dataList, activity,activity.getApplicationContext(),title);
                    recyclerView.setAdapter(adapter);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activity.getApplicationContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }catch (Exception e)
            {
                LogHelper logHelper=new LogHelper(e);
                e.printStackTrace();
            }
        }

    }
}
