package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organization.sjhg.e_school.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by arpan on 8/8/2016.
 */
public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder_Parent> {

    List<Data> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter(List<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder_Parent onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        /*View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activitycardview, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;*/
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_helper,parent,false);
        View_Holder_Parent holder_parent=new View_Holder_Parent(v);
        holder_parent.recyclerView.setHasFixedSize(true);
        holder_parent.recyclerView.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder_parent.recyclerView.setLayoutManager(linearLayoutManager);
        return holder_parent;

    }

    @Override
    public void onBindViewHolder(View_Holder_Parent holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        /*holder.title.setText(list.get(position).title);
        holder.description.setText(list.get(position).description);
        holder.imageView.setImageResource(list.get(position).imageId);*/
        List<Data> data=fill_with_data();
        RecyclerAdapter recyclerAdapter=new RecyclerAdapter(context,data);
        holder.recyclerView.setAdapter(recyclerAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //animate(holder);


    }
    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("Batman vs Superman", "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("X-Men: Apocalypse", "X-Men: Apocalypse is an upcoming American superhero film based on the X-Men characters that appear in Marvel Comics ", R.drawable.chat_text_disabled));
        data.add(new Data("Captain America: Civil War", "A feud between Captain America and Iron Man leaves the Avengers in turmoil.  ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("Kung Fu Panda 3", "After reuniting with his long-lost father, Po  must train a village of pandas", R.drawable.chat_text_disabled));
        data.add(new Data("Warcraft", "Fleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. ", R.drawable.common_google_signin_btn_text_dark_pressed));
        data.add(new Data("Alice in Wonderland", "Alice in Wonderland: Through the Looking Glass ", R.drawable.chat_text_disabled));

        return data;
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}