package com.organization.sjhg.e_school.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.organization.sjhg.e_school.Content.NewTest.TestActivity;
import com.organization.sjhg.e_school.Content.NewTest.TestSummaryActivity;
import com.organization.sjhg.e_school.LoginActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;

/**
 * Created by Punit Chhajer on 17-09-2016.
 */
public class ExamPracticeFragment extends Fragment{
    private String id;
    private Button test,attempt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.exam_prepare_fragment, container, false);

        Bundle bundle=this.getArguments();
        id=  bundle.getString("Id");

        test = (Button) v.findViewById(R.id.test);

        attempt = (Button)v.findViewById(R.id.analytics);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        final String token = new SharedPrefrence().getAccessToken(getActivity());
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token ==null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    new AlertDialog.Builder(getActivity())
                            .setTitle("WorkSheet")
                            .setMessage("Are you sure you want to attempt this test?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(getActivity(), TestActivity.class);
                                    intent.putExtra("Tag", GlobalConstants.PracticeTag);
                                    intent.putExtra("Id",id);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        attempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token==null){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), TestSummaryActivity.class);
                    intent.putExtra("Tag", GlobalConstants.PracticeTag);
                    intent.putExtra("Id",id);
                    startActivity(intent);
                }
            }
        });
    }
}
