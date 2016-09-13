package com.organization.sjhg.e_school.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.organization.sjhg.e_school.R;

import java.util.ArrayList;

/**
 * Created by arpan on 9/13/2016.
 */
public class AccuracyBarChartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.accuracy_bar_chart,container,false);
        BarChart chart = (BarChart) rootview.findViewById(R.id.chart);

        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("My Chart");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        return rootview;
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(110.000f, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(40.000f, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(60.000f, 2);
        valueSet1.add(v1e3);


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(150.000f, 0);
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1);
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2);
        valueSet2.add(v2e3);

        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        BarEntry v3e1 = new BarEntry(150.000f, 0);
        valueSet3.add(v3e1);
        BarEntry v3e2 = new BarEntry(90.000f, 1);
        valueSet3.add(v3e2);
        BarEntry v3e3 = new BarEntry(120.000f, 2);
        valueSet3.add(v3e3);


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Easy");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Medium");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Medium");
        barDataSet3.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        return xAxis;
    }
}
