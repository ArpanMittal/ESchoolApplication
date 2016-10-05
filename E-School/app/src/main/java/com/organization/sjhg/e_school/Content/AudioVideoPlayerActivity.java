package com.organization.sjhg.e_school.Content;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.github.clans.fab.FloatingActionButton;
import com.organization.sjhg.e_school.Fragments.Notes_Listing_Fragment;
import com.organization.sjhg.e_school.Helpers.LogHelper;
import com.organization.sjhg.e_school.HideNavigationBar;
import com.organization.sjhg.e_school.MainParentActivity;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.TakeNotes.AddSmallNotesActivity;
import com.organization.sjhg.e_school.TakeNotes.whiteboard.WhiteBoardActivity;
import com.organization.sjhg.e_school.Utils.ToastActivity;
import com.organization.sjhg.e_school.deviceadmin.DeviceAdminUtil;

/**
 * Created by Prateek Tulsyan on 20-03-2015.
 * Email: prateek.tulsyan13@gmail.com
 * Organization: St. Joseph's Hitech Gurukul.
 */

public class AudioVideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{
    private String videoUrl;
    private VideoView videoView;
    private ImageButton restart,back;
    private Toolbar toolbar;
    private View transBackground,backBlock,replayBlock;
    private ProgressBar loading;
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_video_player);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        HideNavigationBar hideNavigationBar=new HideNavigationBar();
        hideNavigationBar.hideNavigationBar(getWindow());
        setTranslucentStatusBar(getWindow());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getStringExtra("title");
        if (title!=null){
            setTitle(title);
        }
        videoUrl =getIntent().getStringExtra("path");
        videoView = (VideoView) findViewById(R.id.video_view);
        restart = (ImageButton) findViewById(R.id.restart);
        back = (ImageButton) findViewById(R.id.back);
        transBackground = findViewById(R.id.transBackgound);
        backBlock = findViewById(R.id.backBlock);
        replayBlock  = findViewById(R.id.replayBlock);

        loading = (ProgressBar) findViewById(R.id.loading);

        try {
            // Start the MediaController
            final MediaController mediacontroller = new MediaController(
                    this);
            mediacontroller.setAnchorView(videoView);
            // Get the URL from String VideoURL
            final Uri video = Uri.parse(ServerAddress.getServerAddress(this)+videoUrl);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    //it,s be called without isInPlaybackState()
                    loading.setVisibility(View.GONE);
                    return false;
                }
            });

            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int delaytime = 3000;
                    mediacontroller.show(delaytime-200);
                    Handler h = new Handler();
                    toolbar.setVisibility(View.VISIBLE);
                    h.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            toolbar.setVisibility(View.GONE);
                            HideNavigationBar hideNavigationBar=new HideNavigationBar();
                            hideNavigationBar.hideNavigationBar(getWindow());
                        }
                    }, delaytime);
                    return true;
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    videoView.pause();
                    replayBlock.setVisibility(View.VISIBLE);
                    backBlock.setVisibility(View.VISIBLE);
                    transBackground.setVisibility(View.VISIBLE);
                }
            });
            final Handler handler = new Handler();
            final int[] old_duration = {0};
            final Runnable runnable = new Runnable() {
                public void run() {
                    int duration = videoView.getCurrentPosition();
                    if (old_duration[0] == duration && videoView.isPlaying()) {
                        loading.setVisibility(View.VISIBLE);
                    } else if (videoView.isPlaying()){
                        loading.setVisibility(View.GONE);
                    }
                    old_duration[0] = duration;

                    handler.postDelayed(this, 1000);
                }
            };
            handler.postDelayed(runnable, 0);

            restart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading.setVisibility(View.VISIBLE);
                    videoView.start();
                    backBlock.setVisibility(View.GONE);
                    replayBlock.setVisibility(View.GONE);
                    transBackground.setVisibility(View.GONE);
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
                loading.setVisibility(View.GONE);
                videoView.start();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.fabSimpleNote);
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.fabWhiteBoard);
        FloatingActionButton fab3 = (FloatingActionButton)findViewById(R.id.fablist);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.fabSimpleNote:
                Intent intent=new Intent(getApplicationContext(), AddSmallNotesActivity.class);
                startActivity(intent);
                break;
            case R.id.fabWhiteBoard:
                intent=new Intent(getApplicationContext(), WhiteBoardActivity.class);
                startActivity(intent);
                break;
            case R.id.fablist:
                intent=new Intent(getApplicationContext(), Notes_Listing_Fragment.class);
                startActivity(intent);
                break;
        }
    }
    public static void setTranslucentStatusBar(Window window) {
        if (window == null) return;
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window);
        } else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarKiKat(window);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void setTranslucentStatusBarLollipop(Window window) {
        window.setStatusBarColor(
                window.getContext()
                        .getResources()
                        .getColor(R.color.black));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void setTranslucentStatusBarKiKat(Window window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
// Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
        }
        return false;
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
