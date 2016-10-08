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
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Remote.VolleyController;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by arpan on 8/18/2016.
 */
public class Image_View_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.imageview_fragment, container, false);
        Context context=v.getContext();
        String image=null;
        String text=null;
        long millis=System.currentTimeMillis();
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        //TextView tv = (TextView) v.findViewById(R.id.tvFragFirst);
        if(getArguments().getString("first").equals("true"))
        {
            int hour=calendar.get(Calendar.HOUR_OF_DAY);
            if(hour>5&&hour<12)
            {
                image="img/app_morning.png";
                text="Good Morning";
            }
            else if(hour>=12&&hour<16)
            {
                image="img/app_afternoon.png";
                text="Good Afternoon";
            }
            else if(hour>=16&&hour<22)
            {
                image="img/app_evening.png";
                text="Good Evening";
            }
            else
            {
                image="img/app_midnight.png";
                text="Burning the midnight oil";
            }
        }
        else
        {
            image=getArguments().getString("image");
            text=getArguments().getString("text");
        }
        ImageView imageView=(ImageView) v.findViewById(R.id.imageView);
        TextView textView=(TextView)v.findViewById(R.id.text);
        Picasso.with(context)
                .load(ServerAddress.getServerAddress(getContext())+image)
                .into(imageView);
        textView.setText(text);
        //tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static Image_View_Fragment newInstance() {

        Image_View_Fragment f = new Image_View_Fragment();
        Bundle b = new Bundle();
        b.putString("first","true");
        f.setArguments(b);
        return f;
    }
    public static  Image_View_Fragment newInstance(String image,String text)
    {
        Image_View_Fragment f = new Image_View_Fragment();
        Bundle b = new Bundle();
        b.putString("first","false");
        b.putString("image", image);
        b.putString("text", text);

        f.setArguments(b);

        return f;
    }
}
