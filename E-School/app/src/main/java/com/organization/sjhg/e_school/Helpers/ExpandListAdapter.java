package com.organization.sjhg.e_school.Helpers;

/**
 * Created by arpan on 8/25/2016.
 */
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.sjhg.e_school.R;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private int layout, textview;
    public HashMap<String,Boolean> _check;
    public ExpandListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.layout = -1;
        this.textview = -1;
    }

    public ExpandListAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, List<String>> listChildData,int layout, int textview, HashMap<String,Boolean> _check) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.layout = layout;
        this.textview = textview;
        this._check = _check;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (this.layout==-1){
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }else{
                convertView = infalInflater.inflate(this.layout, null);
            }

        }

        TextView txtListChild;
        if (this.textview==-1){
            txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);
        }else{
            txtListChild = (TextView) convertView
                    .findViewById(this.textview);
        }

        if (this.textview!=-1){
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            checkBox.setOnCheckedChangeListener(null);
            convertView.setOnClickListener(null);
            checkBox.setChecked(_check.get(groupPosition+"__"+childText));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        _check.remove(groupPosition+"__"+childText);
                        _check.put(groupPosition+"__"+childText,isChecked);
                        notifyDataSetChanged();
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
                    if (checkBox.isChecked()){
                        checkBox.setChecked(false);
                    }else{
                        checkBox.setChecked(true);
                    }
                }
            });
        }


        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView);
        if(headerTitle.equals("Classes"))
            imageView.setImageResource(R.drawable.blackboard);
        else if(headerTitle.equals("Subjects"))
            imageView.setImageResource(R.drawable.book);
        else if(headerTitle.equals("Exams"))
            imageView.setImageResource(R.drawable.exam);
        else if(headerTitle.equals("Chapters"))
            imageView.setImageResource(R.drawable.chapter_icon);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
