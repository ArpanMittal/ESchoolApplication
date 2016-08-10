package com.organization.sjhg.e_school.Content;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

/**
 * Created by Prateek Tulsyan on 20-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class AudioVideoPlayerActivity extends ContentViewer {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceAdminUtil.checkAndPrompt(this);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        setContentView(R.layout.activity_audio_video_player);

        super.onResume();
        videoView(localFilePath);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void videoView(String localFilePath) {
        final VideoView myVideoView = (VideoView) findViewById(R.id.video_view);
        final int position = 0;
        final Dialog progressDialog;
        MediaController mediaControls = null;

        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }

        progressDialog = new Dialog(this);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(getLayoutInflater().inflate(R.layout.pdialog
                , null));
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        ImageView mImageViewFilling = (ImageView) progressDialog.findViewById(R.id.pdialogimageview);
        ((AnimationDrawable) mImageViewFilling.getBackground()).start();
        progressDialog.show();

        myVideoView.setMediaController(mediaControls);
        myVideoView.setVideoURI(Uri.parse(localFilePath));

        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                progressDialog.dismiss();
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    myVideoView.pause();
                }
            }
        });

    }
}
