package com.organization.sjhg.e_school.ListStructure;

import java.io.Serializable;

/**
 * Created by arpan on 9/21/2016.
 */
public class TimeGraphList implements Serializable {
    public double total_avg;
    public double user_avg;
    public TimeGraphList(Double total_avg, Double user_avg)
    {
        this.total_avg=total_avg;
        this.user_avg=user_avg;
    }
}
