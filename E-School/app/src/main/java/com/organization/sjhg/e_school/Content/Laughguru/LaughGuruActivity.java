package com.organization.sjhg.e_school.Content.Laughguru;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.organization.sjhg.e_school.Database.old.DatabaseOperations;
import com.organization.sjhg.e_school.Helpers.ProtectionHelper;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Structure.LaughguruContentDetailBase;

import org.json.JSONException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Gaurav Rawat.
 * Email: gauravrawat.official@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 ** Recreated By Ishan Chawla
 * Email: ishanchawla8290@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class LaughGuruActivity extends FragmentActivity {

    private static final String TAG = "LaughGuru";
    private static String contentFileId = "";
    public List<LaughguruContentDetailBase> lgList;
    public List<LaughguruContentDetailBase> lgList1;
    String imagePath;
    private byte[] protectionDataI;
    String d="";
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        contentFileId = intent.getStringExtra("contentFileId");
        try {
            lgList1 = DatabaseOperations.getPath(this, contentFileId);
          count=lgList1.size();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LaughguruDataTable table_obj = new LaughguruDataTable(this);

        try {
            d = DatabaseOperations.getLgctid(getApplicationContext(),contentFileId);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (d.equals("1")||d.equals("3")) {
            setContentView(R.layout.activity_laugh_guru);
            ViewPager viewpager = (ViewPager) findViewById(R.id.laughguru);

            try {
                SwipeAdapterForLaughGuru swipeAdapterForLaughGuru = new SwipeAdapterForLaughGuru(
                        getSupportFragmentManager(),
                        this, contentFileId);
                viewpager.setAdapter(swipeAdapterForLaughGuru);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (d.equals("2")) {

          //  setContentView(R.layout.activity_audio_video_player);

            try {
                lgList = DatabaseOperations.getPath(this, contentFileId);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            imagePath = lgList.get(0).imagePath;
            protectionDataI = lgList.get(0).protectionDataI;
            try {
                ProtectionHelper.AccessFile(this,"/storage/sdcard0/E-SchoolContent/"+imagePath,protectionDataI);
                setContentView(R.layout.activity_audio_video_player);
                VideoView videoView =(VideoView)findViewById(R.id.video_view);
                MediaController mediaController= new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(Uri.parse("/storage/sdcard0/E-SchoolContent/" + imagePath));
                videoView.requestFocus();
                videoView.start();

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }
   @Override
    public void onPause() {
       super.onPause();
        ProtectFile();
    }
    private void ProtectFile() {
        try {

            ProtectionHelper.ProtectFile("/storage/sdcard0/E-SchoolContent/" + imagePath);

            //         ProtectionHelper.ProtectFile("/storage/sdcard0/E-SchoolContent/" + audioPath);
        } catch (IOException e) {
            // Ignoring the exception while protecting the file again.
            e.printStackTrace();
        }
    }



}



