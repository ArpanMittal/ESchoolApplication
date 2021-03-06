package com.organization.sjhg.e_school.Fragments;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.organization.sjhg.e_school.Helpers.GridDataAdapter;
import com.organization.sjhg.e_school.Helpers.SamplePaperListDataAdapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/6/2016.
 */
public class SamplePaperListFragment extends Fragment {
    List<ChapterList> chapterLists =new ArrayList<>();
    String parent_title;
    String parent_id;
    View rootView;
    Activity activity;
    SamplePaperListDataAdapter samplePaperListDataAdapter;
    public SamplePaperListFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        chapterLists= (List<ChapterList>) bundle.getSerializable(getString(R.string.sendlist));
        parent_title=bundle.getString("PARENT_TITLE");
        parent_id=bundle.getString("PARENT_ID");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.list_view_helper_exam, container, false);

        activity=getActivity();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler);
        TextView textView=(TextView)rootView.findViewById(R.id.title);
        textView.setVisibility(View.GONE);
        //GridDataAdapter gridDataAdapter=new GridDataAdapter(getContext(),chapterLists);
        samplePaperListDataAdapter=new SamplePaperListDataAdapter(getContext(),chapterLists,parent_title,parent_id,activity);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),2));
        }
        else{
            rv.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(),4));
        }
        rv.setAdapter(samplePaperListDataAdapter);
        rv.setHasFixedSize(true);
//        RecyclerView recyclerView=(RecyclerView) rootView.findViewById(R.id.recycler);
//        recyclerView.removeViewAt(2);
//        samplePaperListDataAdapter.notifyItemRemoved(2);
//        samplePaperListDataAdapter.notifyItemRangeChanged(2, chapterLists.size());
    }
}
