package com.organization.sjhg.e_school.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.organization.sjhg.e_school.Helpers.QuestionOptionAdapter;
import com.organization.sjhg.e_school.ListStructure.AndroidVersion;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.ListStructure.QuestionList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/9/2016.
 */
public class Question_Fragment extends Fragment {

    List<QuestionList> questionLists=new ArrayList<>();

    public  Question_Fragment()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        this.questionLists= (List<QuestionList>) bundle.getSerializable(getString(R.string.sendlist));




    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.question_fragment, container, false);
        ImageView imageView=(ImageView)rootView.findViewById(R.id.question_image);
        TextView textView=(TextView)rootView.findViewById(R.id.question_text);
        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        QuestionOptionAdapter questionOptionAdapter=new QuestionOptionAdapter(getContext(),questionLists.get(0).chapterLists,questionLists.get(0).id,questionLists.get(0).answer);
        recyclerView.setAdapter(questionOptionAdapter);
        String url= ServerAddress.getServerAddress(getContext())+questionLists.get(0).question_image_path;
        Picasso.with(getContext())
                .load(url)
                .resize(200,200)
                .into(imageView);
        textView.setText(Html.fromHtml(questionLists.get(0).question_text));





        return rootView;
    }
}
