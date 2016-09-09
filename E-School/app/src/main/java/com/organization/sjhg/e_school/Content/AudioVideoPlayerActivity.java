package com.organization.sjhg.e_school.Content;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

/**
 * Created by Prateek Tulsyan on 20-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class AudioVideoPlayerActivity extends AppCompatActivity {
    private String videoUrl;
    private VideoView videoView;
    private ProgressDialog pDialog;
    private Button restart,back;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        setContentView(R.layout.activity_audio_video_player);
        videoUrl =getIntent().getStringExtra("path");
        videoView = (VideoView) findViewById(R.id.video_view);
        restart = (Button) findViewById(R.id.restart);
        back = (Button) findViewById(R.id.back);

        // Create a progressbar
        pDialog = new ProgressDialog(this);
        // Set progressbar title
        pDialog.setTitle("Video");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            final MediaController mediacontroller = new MediaController(
                    this);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            final Uri video = Uri.parse(ServerAddress.getServerAddress(this)+videoUrl);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int delaytime = 3000;
                    mediacontroller.show(delaytime-200);
                    Handler h = new Handler();

                    h.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            HideNavigationBar hideNavigationBar=new HideNavigationBar();
                            hideNavigationBar.hideNavigationBar(getWindow());
                        }
                    }, delaytime);
                    return true;
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    restart.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                }
            });

            restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.seekTo(0);
                    videoView.setVideoURI(video);
                    videoView.start();
                    restart.setVisibility(View.GONE);
                }
            });

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioVideoPlayerActivity.this.finish();
                }
            });

        } catch (Exception e) {
            new ToastActivity().makeJsonException(this);
            new LogHelper(e);
            e.printStackTrace();
        }

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoView.start();
            }
        });
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

//    private void videoView(String localFilePath) {
//        final VideoView myVideoView = (VideoView) findViewById(R.id.video_view);
//        final int position = 0;
//        final Dialog progressDialog;
//        MediaController mediaControls = null;
//
//        if (mediaControls == null) {
//            mediaControls = new MediaController(this);
//        }
//
//        progressDialog = new Dialog(this);
//        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        progressDialog.setContentView(getLayoutInflater().inflate(R.layout.pdialog
//                , null));
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        progressDialog.setCancelable(false);
//        ImageView mImageViewFilling = (ImageView) progressDialog.findViewById(R.id.pdialogimageview);
//        ((AnimationDrawable) mImageViewFilling.getBackground()).start();
//        progressDialog.show();
//
//        myVideoView.setMediaController(mediaControls);
//        myVideoView.setVideoURI(Uri.parse(localFilePath));
//
//        myVideoView.requestFocus();
//        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                progressDialog.dismiss();
//                myVideoView.seekTo(position);
//                if (position == 0) {
//                    myVideoView.start();
//                } else {
//                    myVideoView.pause();
//                }
//            }
//        });
//
//    }
}
