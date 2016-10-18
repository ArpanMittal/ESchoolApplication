package com.organization.sjhg.e_school.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.Content.NewTest.TestAnswerActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * Created by arpan on 10/8/2016.
 */
public class Tour_Image_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tour_image, container, false);
        Context context=v.getContext();
        String image=null;
        String text=null;

        ImageView imageView=(ImageView) v.findViewById(R.id.imageView);
        int val= getArguments().getInt("Image");
            imageView.setImageResource(getArguments().getInt("Image"));
        TextView textView=(TextView)v.findViewById(R.id.text);
        textView.setText(getArguments().getString("Text"));
//        Picasso.with(context)
//                .load(ServerAddress.getServerAddress(getContext())+image)
//                .into(imageView);

        //tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static Tour_Image_Fragment newInstance(int image,String text) {

        Tour_Image_Fragment f = new Tour_Image_Fragment();
        Bundle b = new Bundle();
        b.putString("first","true");
        b.putInt("Image",image);
        b.putString("Text",text);
        f.setArguments(b);
        return f;
    }

}
