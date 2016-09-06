package com.organization.sjhg.e_school.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.Helpers.GridDataAdapter;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/6/2016.
 */
public class SamplePaperListFragment extends Fragment {
    List<ChapterList> chapterLists =new ArrayList<>();
    public SamplePaperListFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        chapterLists= (List<ChapterList>) bundle.getSerializable(getString(R.string.sendlist));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_view_helper, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler);
        GridDataAdapter gridDataAdapter=new GridDataAdapter(getContext(),chapterLists);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setAdapter(gridDataAdapter);
        rv.setHasFixedSize(true);

        return rootView;
    }



}
