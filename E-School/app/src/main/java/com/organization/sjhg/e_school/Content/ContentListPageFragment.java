package com.organization.sjhg.e_school.Content;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.sjhg.e_school.Database.old.DatabaseOperations;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.ChapterDetail;
import com.organization.sjhg.e_school.Structure.ContentDetailBase;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Structure.TestDetail;
import com.organization.sjhg.e_school.Structure.TopicDetail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Prateek Tulsyan on 18-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

/**
 * Edited by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class ContentListPageFragment extends Fragment {

    public static final String SUBJECT_ID_ARG_NAME = "subject_id";
    public static final String SUBJECT_NAME_ARG_NAME = "subject_name";
    List<ContentDetailBase> contentDetailList = null;
    DisplayContentAdapter mDisplayContentAdapter;
    ExpandableListFilterAdapter mExpandableListFilterAdapter;
    TextView messageView;


    // Update filter objects in expandable filter list
    private static HashMap<String, List<String>> GetUpdatedFilterList(List<String> headerList,
                                                                      List<ChapterDetail> chapterList,
                                                                      List<ChapterDetail> newChapterList,
                                                                      List<String> contentTypeList) {
        // Mapping filter lists to their respective group
        HashMap<String, List<String>> childList = new HashMap<>();

        HashSet<String> chapterNameSet = new HashSet<>();
        HashSet<String> topicNameSet = new HashSet<>();

        List<String> chapterNameList = new ArrayList<>();
        List<String> topicNameList = new ArrayList<>();

        for (ChapterDetail chapterObject : chapterList)
            chapterNameSet.add(chapterObject.chapterName);

        for (ChapterDetail chapterObject : newChapterList) {
            for (TopicDetail topicObject : chapterObject.topicDetail)
                topicNameSet.add(topicObject.topicName);
        }

        chapterNameList.addAll(chapterNameSet);
        topicNameList.addAll(topicNameSet);

        if (newChapterList.isEmpty())
            childList.put(headerList.get(0), contentTypeList);
        else {
            childList.put(headerList.get(0), chapterNameList);
            childList.put(headerList.get(1), topicNameList);
            childList.put(headerList.get(2), contentTypeList);
        }

        return childList;

    }


    // Update content display objects in content display list
    private static List<Pair<ContentDetailBase, String>> GetUpdatedDisplayList(List<ContentDetailBase> subjectContentList) {
        List<Pair<ContentDetailBase, String>> chapterDisplayList = new ArrayList<>();
        List<Pair<ContentDetailBase, String>> testDisplayList = new ArrayList<>();
        List<Pair<ContentDetailBase, String>> contentDisplayList = new ArrayList<>();

        // Adding content entries in test or chapter list
        for (ContentDetailBase contentEntry : subjectContentList) {
            if (contentEntry.contentTypeId == Content_Type.TEST.getValue()||contentEntry.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue())
                testDisplayList.add(new Pair<>(contentEntry, "Tests"));
            else
                chapterDisplayList.add(new Pair<>(contentEntry, contentEntry.chapterName));
        }

        // Adding test label
        if (!testDisplayList.isEmpty())
            testDisplayList.add(0, new Pair<ContentDetailBase, String>(null, "Tests"));

        // Sorting chapter list on basis of chapter name
        Collections.sort(chapterDisplayList, new PairComparator());
        // Adding chapter entries to final display list
        contentDisplayList.addAll(chapterDisplayList);

        // Adding chapter labels to final display list
        for (int i = 0; i < contentDisplayList.size(); i++) {
            if (i == 0)
                contentDisplayList.add(0, new Pair<ContentDetailBase, String>(null, contentDisplayList.get(0).second));
            else {
                if (!contentDisplayList.get(i).second.equals(contentDisplayList.get(i - 1).second))
                    contentDisplayList.add(i, new Pair<ContentDetailBase, String>(null, contentDisplayList.get(i).second));
            }
        }

        // Adding test list to final display list
        contentDisplayList.addAll(testDisplayList);

        return contentDisplayList;
    }


    @Override
    public ViewGroup onCreateView(final LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {

        Bundle args = getArguments();
        final int subjectId = args.getInt(SUBJECT_ID_ARG_NAME);
        final String subjectName = args.getString(SUBJECT_NAME_ARG_NAME);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.content_list_fragment, container, false);


        try {
            contentDetailList = DatabaseOperations.getLocalContentList(getActivity());
        } catch (SQLException e) {
            e.printStackTrace();
            contentDetailList = null;
        }

        Collections.sort(contentDetailList, new CustomComparator());


        //Making particular subject list
        final List<ContentDetailBase> subjectContentList = new ArrayList<>();
        for (ContentDetailBase contentEntry : contentDetailList) {
            if (contentEntry.subjectId == subjectId)
                subjectContentList.add(contentEntry);
        }

        // Setting adapter for content display list
        mDisplayContentAdapter = new DisplayContentAdapter(subjectContentList, getActivity());
        ListView displayList = (ListView) rootView.findViewById(R.id.contentListView);
        displayList.setAdapter(mDisplayContentAdapter);


        // Setting Filter Expandable List
        final List<ChapterDetail> chapterList = new ArrayList<>();
        final List<String> contentTypeList = new ArrayList<>();

        HashSet<String> contentTypeSet = new HashSet<>();

        // Making sets of all possible filter children
        for (ContentDetailBase contentEntry : subjectContentList) {
            if (contentEntry.contentTypeId != Content_Type.TEST.getValue()&&contentEntry.contentTypeId!=Content_Type.ADAPTIVE_TEST.getValue())
                chapterList.add(new ChapterDetail(contentEntry));

            contentTypeSet.add(contentEntry.getInternalContentType().getContentTypeName());
        }

        for (ChapterDetail chapterObject : chapterList) {
            for (ContentDetailBase contentObject : subjectContentList) {
                if (chapterObject.chapterId == contentObject.chapterId)
                    chapterObject.topicDetail.add(new TopicDetail(contentObject));
            }
        }
        // Converting sets to list
        contentTypeList.addAll(contentTypeSet);


        // Making filter header/group name list
        final List<String> headerList = new ArrayList<>();
        if (!chapterList.isEmpty()) {
            headerList.add("Chapters");
            headerList.add("Topics");
        }
        headerList.add("Content Type");

        // Setting adapter for expandable filter list
        mExpandableListFilterAdapter = new ExpandableListFilterAdapter(
                getActivity(), headerList, chapterList, contentTypeList);
        ExpandableListView filterList = (ExpandableListView) rootView.findViewById(R.id.expandableFilterListView);
        filterList.setAdapter(mExpandableListFilterAdapter);

        // Keeping the list in expanded form in beginning
        for (int i = 0; i < headerList.size(); i++)
            filterList.expandGroup(i);

        final List<Pair<Pair<Integer, String>, String>> checkedItemList = new ArrayList<>();
        // Initializing filtering of data sets on click
        filterList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selectedChild = (String) mExpandableListFilterAdapter.getChild(groupPosition, childPosition);
                String selectedGroup = (String) mExpandableListFilterAdapter.getGroup(groupPosition);
                if (checkedItemList.contains(new Pair<>(new Pair<>(groupPosition, selectedGroup), selectedChild)))
                    checkedItemList.remove(new Pair<>(new Pair<>(groupPosition, selectedGroup), selectedChild));
                else
                    checkedItemList.add(new Pair<>(
                            new Pair<>(groupPosition, selectedGroup), selectedChild));
                Collections.sort(checkedItemList, new FilterComparator());
                if (checkedItemList.isEmpty())
                    checkedItemList.add(new Pair<>(new Pair<>(-1, "Clear"), "Clear"));
                mExpandableListFilterAdapter.GetUpdatedChapterList(
                        subjectContentList, chapterList,
                        contentTypeList, headerList, checkedItemList);
                return true;
            }
        });
        // Clear all filters and return to the initial state
        final TextView clearAll = (TextView) rootView.findViewById(R.id.clearAllTextView);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedItemList.clear();
                checkedItemList.add(new Pair<>(new Pair<>(-1, "Clear"), "Clear"));
                mExpandableListFilterAdapter.GetUpdatedChapterList(
                        subjectContentList, chapterList,
                        contentTypeList, headerList, checkedItemList);
            }
        });

        messageView = (TextView) rootView.findViewById(R.id.noAvailableContentView);

        return rootView;

    }


    // Content pair Comparator by chapter name
    private static class PairComparator implements Comparator<Pair<ContentDetailBase, String>> {

        @Override
        public int compare(Pair<ContentDetailBase, String> lhs, Pair<ContentDetailBase, String> rhs) {
            return lhs.second.compareTo(rhs.second);
        }
    }

    // Content object Comparator by internal content type
    private static class CustomComparator implements Comparator<ContentDetailBase> {
        @Override
        public int compare(ContentDetailBase obj1, ContentDetailBase obj2) {
            return ((Integer) obj1.getInternalContentType().contentType).compareTo
                    (obj2.getInternalContentType().contentType);
        }
    }

    // Selected filter comparator by group position
    private class FilterComparator implements Comparator<Pair<Pair<Integer, String>, String>> {

        @Override
        public int compare(Pair<Pair<Integer, String>, String> lhs, Pair<Pair<Integer, String>, String> rhs) {
            return lhs.first.first.compareTo(rhs.first.first);
        }
    }

    // Expandable List View Adapter
    public class ExpandableListFilterAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> headerContentList;
        private HashMap<String, List<String>> childContentList;
        private List<String> checkedItemList;

        public ExpandableListFilterAdapter(Context context, final List<String> headerList, List<ChapterDetail> chapterList, List<String> contentTypeList) {

            this.context = context;
            this.headerContentList = headerList;
            this.childContentList = GetUpdatedFilterList(headerList, chapterList, chapterList, contentTypeList);
            this.checkedItemList = new ArrayList<>();
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getGroupCount() {
            return this.headerContentList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.childContentList.get(headerContentList.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.headerContentList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.childContentList.get(this.headerContentList.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.cotent_filter_header, parent, false);
            }

            String headerName = (String) getGroup(groupPosition);
            TextView headerItem = (TextView) view.findViewById(R.id.filterHeaderTextView);
            headerItem.setText(headerName);

            view.setClickable(false);

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.content_filter_child, parent, false);
            }

            final String childName = (String) getChild(groupPosition, childPosition);
            final CheckBox childItem = (CheckBox) view.findViewById(R.id.filterChildCheckBox);
            childItem.setText(childName);

            if (checkedItemList.contains(childName))
                childItem.setChecked(true);
            else
                childItem.setChecked(false);

            view.setClickable(false);

            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {

        }

        @Override
        public void onGroupCollapsed(int groupPosition) {

        }

        @Override
        public long getCombinedChildId(long groupId, long childId) {
            return childId;
        }

        @Override
        public long getCombinedGroupId(long groupId) {
            return groupId;
        }


        private void GetUpdatedChapterList(List<ContentDetailBase> subjectContentList, List<ChapterDetail> chapterList,
                                           List<String> contentTypeList, List<String> headerList,
                                           List<Pair<Pair<Integer, String>, String>> checkedItemList) {

            this.checkedItemList.clear();

            List<ContentDetailBase> updatedSubjectList = new ArrayList<>();
            List<ChapterDetail> updatedChapterList = new ArrayList<>();
            List<ContentDetailBase> temporaryRemovedContent = new ArrayList<>();
            List<ContentDetailBase> temporaryRemovedTopicContent = new ArrayList<>();

            HashSet<ContentDetailBase> updatedSubjectSet = new HashSet<>();
            HashSet<ChapterDetail> updatedChapterSet = new HashSet<>();

            for (Pair<Pair<Integer, String>, String> selectedPair : checkedItemList) {
                switch (selectedPair.first.second) {
                    case "Chapters":
                        for (ChapterDetail chapterEntry : chapterList) {
                            if (chapterEntry.chapterName.equalsIgnoreCase(selectedPair.second))
                                updatedChapterSet.add(chapterEntry);
                        }
                        for (ContentDetailBase contentEntry : subjectContentList) {
                            if (contentEntry.contentTypeId != Content_Type.TEST.getValue() &&contentEntry.contentTypeId!=Content_Type.ADAPTIVE_TEST.getValue()&&
                                    contentEntry.chapterName.equalsIgnoreCase(selectedPair.second)) {
                                updatedSubjectSet.add(contentEntry);
                            }
                        }
                        break;
                    case "Topics":
                        if (updatedChapterList.isEmpty() || updatedChapterList.containsAll(chapterList)) {
                            updatedChapterSet.addAll(chapterList);
                            for (ContentDetailBase contentEntry : subjectContentList) {
                                if (contentEntry.contentTypeId != Content_Type.TEST.getValue() && contentEntry.contentTypeId!=Content_Type.ADAPTIVE_TEST.getValue()&&
                                        contentEntry.topicName.equalsIgnoreCase(selectedPair.second))
                                    updatedSubjectSet.add(contentEntry);
                            }
                        } else {
                            if (!temporaryRemovedTopicContent.isEmpty()) {
                                for (ContentDetailBase contentEntry : temporaryRemovedTopicContent) {
                                    if (contentEntry.topicName.equalsIgnoreCase(selectedPair.second))
                                        updatedSubjectSet.add(contentEntry);
                                }
                            } else {
                                for (ContentDetailBase contentEntry : updatedSubjectList) {
                                    if (!contentEntry.topicName.equalsIgnoreCase(selectedPair.second)) {
                                        temporaryRemovedTopicContent.add(contentEntry);
                                        updatedSubjectSet.remove(contentEntry);
                                    }
                                }
                            }
                        }
                        break;
                    case "Content Type":
                        if (updatedChapterList.isEmpty()) {
                            updatedChapterSet.addAll(chapterList);
                            for (ContentDetailBase contentEntry : subjectContentList) {
                                if (contentEntry.getInternalContentType().getContentTypeName()
                                        .equalsIgnoreCase(selectedPair.second))
                                    updatedSubjectSet.add(contentEntry);
                                else
                                    temporaryRemovedContent.add(contentEntry);
                            }
                        } else {
                            if (!temporaryRemovedContent.isEmpty() || temporaryRemovedContent.contains(null)) {
                                for (ContentDetailBase contentEntry : temporaryRemovedContent) {
                                    if (contentEntry != null && contentEntry.getInternalContentType().getContentTypeName()
                                            .equalsIgnoreCase(selectedPair.second))
                                        updatedSubjectSet.add(contentEntry);
                                }
                            } else {
                                for (ContentDetailBase contentEntry : updatedSubjectList) {
                                    if (!contentEntry.getInternalContentType().getContentTypeName()
                                            .equalsIgnoreCase(selectedPair.second)) {
                                        temporaryRemovedContent.add(contentEntry);
                                        updatedSubjectSet.remove(contentEntry);
                                    }
                                    if (temporaryRemovedContent.isEmpty())
                                        temporaryRemovedContent.add(null);
                                }
                            }
                        }
                        break;
                    case "Clear":
                        updatedSubjectSet.addAll(subjectContentList);
                        updatedChapterSet.addAll(chapterList);

                        checkedItemList.clear();
                }

                updatedSubjectList = new ArrayList<>();
                updatedSubjectList.addAll(updatedSubjectSet);
                updatedChapterList = new ArrayList<>();
                updatedChapterList.addAll(updatedChapterSet);
                this.checkedItemList.add(selectedPair.second);
            }

            this.childContentList = GetUpdatedFilterList(headerList, chapterList, updatedChapterList, contentTypeList);
            mExpandableListFilterAdapter.notifyDataSetChanged();
            mDisplayContentAdapter.UpdateDisplayList(updatedSubjectList, updatedChapterList);
        }

    }


    //Content List Adapter
    public class DisplayContentAdapter extends ArrayAdapter<Pair<ContentDetailBase, String>> {

        Context context;

        private List<Pair<ContentDetailBase, String>> contentDisplayList;

        public DisplayContentAdapter(List<ContentDetailBase> subjectContentList, Context context) {
            super(context, R.layout.content_list, GetUpdatedDisplayList(subjectContentList));
            this.contentDisplayList = GetUpdatedDisplayList(subjectContentList);
            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.content_list, parent, false);
            }


            TextView contentView = (TextView) convertView.findViewById(R.id.contentDisplayView);

            final Pair<ContentDetailBase, String> contentPair = contentDisplayList.get(position);

            if (contentPair.first == null) {
                contentView.setText(" " + contentPair.second);
                contentView.setBackgroundColor(Color.BLACK);
                contentView.setTextColor(Color.WHITE);
                contentView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            } else {
                contentView.setText(" " + contentPair.first.contentName);
                contentView.setBackgroundColor(Color.TRANSPARENT);
                contentView.setCompoundDrawablesWithIntrinsicBounds(
                        contentPair.first.getInternalContentType().getIconResourceId(), 0, 0, 0);

                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            if (contentPair.first.contentTypeId == Content_Type.TEST.getValue()||contentPair.first.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue()) {
                                TestDetail test = TestDetail.getTesDetailObjectFromLocal(contentPair.first.localFilePath);
                                // Time of test is checked only if test is not practice
                                if (!(test.isPractice)) {

                                    Date dateNow = new Date();
                                    SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
                                    String date = dateformatyyyyMMdd.format(dateNow);



                                    if (contentPair.first.assignedPublishedDate.compareTo(date) <= 0) {



                                        check(contentPair);

                                    } else if (contentPair.first.assignedPublishedDate.compareTo(date) > 0) {

                                        check(contentPair);
                                    }

                                } else {
                                    createBox(contentPair, false);
                                }
                            } else {
                                check(contentPair);
                            }
                        } catch (IOException e) {
                            Log.e("error", "IO Exception");
                        } catch (ClassNotFoundException c) {
                            Log.e("error", "Class not found exception");
                        }

                    }
                });
            }

            return convertView;
        }

        private void createBox(final Pair<ContentDetailBase, String> contentPair, Boolean isTestScheduled)  {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog));
            if (isTestScheduled) {
                builder.setMessage("Are you Sure You want to take the test?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                check(contentPair);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

            } else {
                builder.setMessage("This test is not schedule today.!! Enjoy ");

                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });
            }
            builder.create();
            builder.show();

        }
        //move to desired activity  by checking its content type
        private void check(Pair<ContentDetailBase, String> contentPair)  {
            String cfid=Integer.toString(contentPair.first.contentFileId);
            if(contentPair.first.contentTypeId==Content_Type.ADAPTIVE_TEST.getValue()) {
                try {
                    //check if test has not been given before
                    TestDetail test;
                    test = TestDetail.getTesDetailObjectFromLocal(contentPair.first.localFilePath);
                    if (test.status == TestDetail.TestStatus.ADAPTIVETEST_SUBMITTED || test.status == TestDetail.TestStatus.TEST_UPLOADED || test.status == TestDetail.TestStatus.TEST_SUBMITTED) {
                        Toast toast = Toast.makeText(context,R.string.submitted_test, Toast.LENGTH_LONG);

                        toast.show();

                        return;
                    }
                }catch (Exception e)
                {
                    Log.e(GlobalConstants.LOG_TAG,e.getMessage());
                }
            }

                Intent openContentIntent = new Intent(getActivity(),
                        contentPair.first.getInternalContentType().getViewerClass());

                openContentIntent.putExtra("localFilePath", contentPair.first.localFilePath);
                openContentIntent.putExtra("protectionData", contentPair.first.protectionData);
                openContentIntent.putExtra("subjectId", contentPair.first.subjectId);
                openContentIntent.putExtra("subjectName", contentPair.first.subjectName);
                openContentIntent.putExtra("contentTypeId", contentPair.first.contentTypeId);
                // TODO: This is being sent for all. Should be for laughguru only.
                openContentIntent.putExtra("contentIdentifier", contentPair.first.contentIdentifier);
                openContentIntent.putExtra("contentName", contentPair.first.contentName);
                if (!(contentPair.first.contentTypeId == Content_Type.LAUGHGURU.getValue())) {
                    openContentIntent.putExtra("contentFileId", contentPair.first.contentFileId);
                } else {
                    openContentIntent.putExtra("contentFileId", cfid);
                }
                getActivity().startActivity(openContentIntent);

        }
        private void UpdateDisplayList(List<ContentDetailBase> subjectContentList,
                                       List<ChapterDetail> selectedChapter) {
            List<ContentDetailBase> subjectDisplayList = new ArrayList<>();
            HashSet<ContentDetailBase> subjectDisplaySet = new HashSet<>();


            for (ContentDetailBase subjectEntry : subjectContentList) {
                for (ChapterDetail chapterEntry : selectedChapter) {
                    if (subjectEntry.chapterId == chapterEntry.chapterId)
                        subjectDisplaySet.add(subjectEntry);
                }
                if (subjectEntry.contentTypeId == Content_Type.TEST.getValue()||subjectEntry.contentTypeId ==Content_Type.ADAPTIVE_TEST.getValue())
                    subjectDisplaySet.add(subjectEntry);
            }
            subjectDisplayList.addAll(subjectDisplaySet);
            mDisplayContentAdapter.clear();
            Collections.sort(subjectDisplayList, new CustomComparator());
            this.contentDisplayList = GetUpdatedDisplayList(subjectDisplayList);
            mDisplayContentAdapter.addAll(this.contentDisplayList);
            mDisplayContentAdapter.notifyDataSetChanged();
            if (this.contentDisplayList.isEmpty())
                messageView.setVisibility(View.VISIBLE);
            else
                messageView.setVisibility(View.INVISIBLE);

        }


    }

}
