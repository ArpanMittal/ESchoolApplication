package com.organization.sjhg.e_school;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.ExpandListAdapter;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.Helpers.SearchAdapter;
import com.organization.sjhg.e_school.ListStructure.Topic;
import com.organization.sjhg.e_school.ListStructure.TopicList;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Utils.SharedPrefrence;
import com.organization.sjhg.e_school.Utils.ToastActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Punit Chhajer on 10-09-2016.
 */
public class SearchActivity extends AppCompatActivity implements RemoteCallHandler {

    private ProgressBar mProgressView;
    private List<TopicList> list;
    private HashMap<String,Boolean> check;
    private List<String> DataHeader;
    private HashMap<String , List<String>> DataChild;
    private View empty_state_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        empty_state_layout=(RelativeLayout) findViewById(R.id.emptyStateLayout);
        empty_state_layout.setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.emptyState)).setImageResource(R.drawable.search_empty_state);
        ((TextView)findViewById(R.id.emptyTextDescription)).setText(this.getResources().getText(R.string.searchError)
                +" for \""+getIntent().getStringExtra(SearchManager.QUERY)+"\"");
        ((TextView)findViewById(R.id.emptyTextState)).setText(R.string.no_content);

        mProgressView = (ProgressBar) findViewById(R.id.progress);
        if (savedInstanceState!=null){
            list = (List<TopicList>) savedInstanceState.getSerializable("INTERNAL LIST");
            check = (HashMap<String,Boolean>) savedInstanceState.getSerializable("CHECK LIST");
            if (list!=null){
                showView();
            }else{
                empty_state_layout.setVisibility(View.VISIBLE);
            }
        }else{
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(list!=null ) {
            outState.putSerializable("INTERNAL LIST", (Serializable) list);
            outState.putSerializable("CHECK LIST", (Serializable) check);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,SearchActivity.class)));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            new RemoteHelper(this).searchContent(this, RemoteCalls.GET_ITEM_DETAILS, query);
        }
    }

    private List<TopicList> getList(JSONObject response)
    {
        List<TopicList> list = new ArrayList<>();
        try {
            JSONArray data = response.getJSONArray(this.getString(R.string.data));
            int length=data.length();

            for(int i=0;i<length;i++)
            {
                JSONObject internalListObject=data.getJSONObject(i);
                String chapter_id=""+internalListObject.getString("cl_su_st_ch_id");
                String chapter_name=""+internalListObject.getString("chapter_name");
                String class_name=""+internalListObject.getString("class_name");
                String subject_name=""+internalListObject.getString("subject_name");
                String topic_id=""+internalListObject.getString("hash");
                String topic_name=""+internalListObject.getString("topic_name");

                Topic topic = new Topic(topic_name,topic_id);
                Boolean flag =true;
                for (int j=0;j<list.size();j++){
                    TopicList chapter = list.get(j);
                    if (chapter.chapterId.equals(chapter_id)){
                        chapter.topics.add(topic);
                        list.set(j,chapter);
                        flag = false;
                    }
                }
                if (flag){
                    List<Topic> topics = new ArrayList<>();
                    topics.add(topic);
                    TopicList chapter = new TopicList(chapter_id,topics);
                    chapter.chapter_name = chapter_name;
                    chapter.subject_name = subject_name;
                    chapter.class_name = class_name;
                    list.add(chapter);
                }


            }

        }catch (Exception e) {
            e.printStackTrace();
            new ToastActivity().makeJsonException(this);
            new LogHelper(e);

        }
        return list;
    }

    private void showView() {
        mProgressView.setVisibility(View.GONE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        SearchAdapter adapter = new SearchAdapter(list, this,check);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // for animation in listview
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }else if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.menu_search) {
            return true;
        }else if (id == R.id.menu_filter) {
            DataHeader = new ArrayList<>();
            DataHeader.add("Classes");
            DataHeader.add("Subjects");
            DataHeader.add("Chapters");
            DataChild = new HashMap<>();
            List<String> cls = new ArrayList<>();
            List<String> sub = new ArrayList<>();
            List<String> chp = new ArrayList<>();
            for (int i=0;i<list.size();i++){
                TopicList chap = list.get(i);
                if (!cls.contains(chap.class_name)){
                    cls.add(chap.class_name);
                }
                if (!sub.contains(chap.subject_name)){
                    sub.add(chap.subject_name);
                }
                if (!chp.contains(chap.chapter_name)){
                    chp.add(chap.chapter_name);
                }
            }
            DataChild.put("Classes",cls);
            DataChild.put("Subjects",sub);
            DataChild.put("Chapters",chp);
            if (check==null){
                check = new HashMap<>();
                for(int i=0; i<DataChild.size();i++){
                    for (String itm: DataChild.get(DataHeader.get(i))) {
                        check.put(i+"__"+itm,false);
                    }
                }
            }
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.search_filter);

            ExpandableListView expListView = (ExpandableListView) dialog.findViewById(R.id.lvExp);
            expListView.setGroupIndicator(null);
            final ExpandListAdapter adapter = new ExpandListAdapter(this,DataHeader,DataChild,R.layout.question_option,R.id.option_text, check);
            expListView.setAdapter(adapter);

            Button filter=(Button)dialog.findViewById(R.id.filter);
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check = adapter._check;
                    showView();
                    dialog.dismiss();
                }
            });
            Button cancel=(Button)dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        if(!isSuccessful)
        {
            new ToastActivity().makeUknownErrorMessage(this);
        }
        else
        {
            switch (callFor) {
                case GET_ITEM_DETAILS:
                    try {
                        if (response.get("success").toString().equals("false")) {
                            empty_state_layout.setVisibility(View.VISIBLE);
                        } else {
                            list = getList(response);
                            if (list.size() > 0) {
                                showView();
                            }
                        }
                    } catch (Exception e) {
                        new LogHelper(e);
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
