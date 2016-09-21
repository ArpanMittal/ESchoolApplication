package com.organization.sjhg.e_school.Helpers;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.organization.sjhg.e_school.ListStructure.BarGraphList;
import com.organization.sjhg.e_school.ListStructure.ChapterList;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Utils.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arpan on 9/16/2016.
 */
public class BarGraphAdapter extends RecyclerView.Adapter<BarGraphAdapter.ViewHolder> {
    private List<BarGraphList> barGraphLists=new ArrayList<>();
    private Context context;
    public BarGraphAdapter (List<BarGraphList> barGraphListList, Context context)
    {
        this.barGraphLists=barGraphListList;
        this.context=context;
    }
    @Override
    public BarGraphAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accuracy_bar_chart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BarGraphAdapter.ViewHolder holder, int position) {

            BarChart barChart=holder.chart;
            BarData data = new BarData(getXAxisValues(), getDataSet(barGraphLists));
            barChart.setData(data);
            barChart.setDescription("My Chart");
            barChart.animateXY(2000, 2000);
            data.setValueFormatter(new ValueFormatter());
            barChart.invalidate();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private BarChart chart;
        public ViewHolder(View view) {
            super(view);
            chart=(BarChart)view.findViewById(R.id.chart);
        }
    }

    private ArrayList<BarDataSet> getDataSet(List<BarGraphList> dataList) {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        ArrayList<BarEntry> valueSet3 = new ArrayList<>();
        for (int i=0;i<dataList.size();i++){
            BarEntry v1e1 = new BarEntry((dataList.get(i).correct_attempt), i); // Jan
            valueSet1.add(v1e1);
            BarEntry v1e2 = new BarEntry((dataList.get(i).attempt_question), i); // Jan
            valueSet2.add(v1e2);
            BarEntry v1e3 = new BarEntry(new float[]{(dataList.get(i).total_question)}, i); // Jan
            valueSet3.add(v1e3);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "correct_attempt");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "attempt_question");
        barDataSet2.setColor(Color.rgb(255 ,0,0));
        BarDataSet barDataSet3 = new BarDataSet(valueSet3, "total_question");
        barDataSet3.setColor(Color.rgb(0,0,255));
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);
        return dataSets;
    }
    private ArrayList<String> getXAxisValues()
    {
        ArrayList<String> xAxis = new ArrayList<>();
        for(int i=0;i<barGraphLists.size();i++) {
            xAxis.add(barGraphLists.get(i).title);
        }
        return xAxis;
    }
}
