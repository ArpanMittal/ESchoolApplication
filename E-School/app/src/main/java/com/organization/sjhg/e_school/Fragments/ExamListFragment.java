package com.organization.sjhg.e_school.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.organization.sjhg.e_school.Helpers.GridParentDataAdapter;
import com.organization.sjhg.e_school.ListStructure.ExamPrepareList;
import com.organization.sjhg.e_school.ListStructure.InternalListData;
import com.organization.sjhg.e_school.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/2/2016.
 */
public class ExamListFragment extends Fragment {

    List<ExamPrepareList>list=new ArrayList<>();
    Context context;
    public  ExamListFragment()
    {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        list= (List<ExamPrepareList>) bundle.getSerializable(getString(R.string.sendlist));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.list_view_helper, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        List<InternalListData> internalListDatas=new ArrayList<>();
        for(int i=0;i<list.size();i++)
        internalListDatas.addAll(list.get(i).internalListDatas);
        GridParentDataAdapter adapter = new GridParentDataAdapter(getContext(),internalListDatas);
        rv.setAdapter(adapter);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }


}
