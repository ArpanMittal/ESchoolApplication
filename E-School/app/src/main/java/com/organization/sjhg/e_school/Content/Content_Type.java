package com.organization.sjhg.e_school.Content;

/**
 * Created by Arpan on 1/29/2016.
 */
public enum Content_Type
{
    PDF(2),
    IMAGE(4),
    VIDEO(3),
    TEST(5),
    ADAPTIVE_TEST(6),
    LAUGHGURU(1);
    int value;
    Content_Type(final int value)
    {
        this.value=value;
    }
    public int getValue()
    {
        return this.value;
    }

}
