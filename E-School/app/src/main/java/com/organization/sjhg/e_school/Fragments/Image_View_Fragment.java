package com.organization.sjhg.e_school.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.VolleyController;
import com.squareup.picasso.Picasso;

/**
 * Created by arpan on 8/18/2016.
 */
public class Image_View_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.imageview_fragment, container, false);
        Context context=v.getContext();
        //TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        ImageView imageView=(ImageView) v.findViewById(R.id.imageView);
        Picasso.with(context)
                .load("https://s9.postimg.io/al1o9ip5r/image.jpg")
                .resize(200,200)
                .into(imageView);
        //tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static Image_View_Fragment newInstance(String text) {

        Image_View_Fragment f = new Image_View_Fragment();
        Bundle b = new Bundle();
        b.putString("msg", text);


        f.setArguments(b);

        return f;
    }
}
