package com.organization.sjhg.e_school.Utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by arpan on 9/21/2016.
 */
public class ValueFormatter implements com.github.mikephil.charting.utils.ValueFormatter {


    public String getFormattedValue(float value) {
        if(value>0)
            return Math.round(value)+"";
        else
            return "";
    }

}
