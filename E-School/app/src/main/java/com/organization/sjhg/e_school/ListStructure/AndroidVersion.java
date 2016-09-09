package com.organization.sjhg.e_school.ListStructure;

/**
 * Created by arpan on 8/29/2016.
 */

import java.io.Serializable;

public class AndroidVersion implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    private boolean isSelected;

    public AndroidVersion() {

    }

    public AndroidVersion(String name, String emailId) {

        this.name = name;
        this.emailId = emailId;

    }

    public AndroidVersion(String name, String emailId, boolean isSelected) {

        this.name = name;
        this.emailId = emailId;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
