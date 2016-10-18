package com.organization.sjhg.e_school.Content.Laughguru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.organization.sjhg.e_school.Helpers.ProtectionHelper;
import com.organization.sjhg.e_school.R;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;

import java.io.File;
import java.io.IOException;

/**
 * Created by ishan on 1/28/2016.
 */

public class LaughGuruFragment extends Fragment {
    //TextView textview;
    public static final String IMAGE_PATH = "imagePath";
    public static final String AUDIO_PATH = "audioPath";
    public static final String ORDER = "Order";
    public static final String PROTECTIONDATAI="protectionDataI";
//    public static final String PROTECTIONDATAA="protectionDataA";

    ImageView LaughGuruImages = null;
    MediaPlayer mp = new MediaPlayer();
    int seekto = 0;
    private  byte[] protectionDataI;
    private byte[] protectionDataA;
    private String imagePath;
    private String audioPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.laugh_guru_content_viewer, container, false);
        Bundle args = getArguments();
        imagePath = args.getString(IMAGE_PATH);
        audioPath = args.getString(AUDIO_PATH);
        String order = args.getString(ORDER);
       protectionDataI = args.getByteArray(PROTECTIONDATAI);
        //protectionDataA = args.getByteArray(PROTECTIONDATAA);
      try {
           ProtectionHelper.AccessFile(getActivity().getApplicationContext(),"/storage/sdcard0/E-SchoolContent/" + imagePath , protectionDataI);
            //for show
        //    ProtectionHelper.AccessFile(getContext(),"/storage/sdcard0/E-SchoolContent/" + audioPath , protectionDataA);
        } catch (IOException e) {
           e.printStackTrace();
        }
        File imgFile = new File("/storage/sdcard0/E-SchoolContent/" + imagePath);

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            LaughGuruImages = (ImageView) view.findViewById(R.id.imageview1);
            LaughGuruImages.setImageBitmap(myBitmap);
        }
        try {
            mp.setDataSource("/storage/sdcard0/E-SchoolContent/" + audioPath);
            mp.prepare();
            mp.isLooping();
            if (order.equals("" + 1)) {

                mp.start();
            }
            //mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

        @Override
        public void onResume() {
            super.onResume();
          try {
                ProtectionHelper.AccessFile(getActivity().getApplicationContext(),"/storage/sdcard0/E-SchoolContent/" + imagePath , protectionDataI);
              //  ProtectionHelper.AccessFile(getContext(),"/storage/sdcard0/E-SchoolContent/" + audioPath , protectionDataA);
           } catch (IOException e) {
              ExceptionHandler.showAlertDialogContent(getActivity().getApplicationContext(), "Error Opening Content",
                      "Could not open the file. PLease contact your IT admin.");
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


 @Override
   public void onDestroy()
    {
        mp.reset();
        ProtectFile();
        super.onDestroy();
    }
     @Override
   public void setMenuVisibility(final boolean visible)
    {
        super.setMenuVisibility(visible);
        if(visible) {
            //if(seekto>=mp.)


                seekto=0;
                mp.seekTo(seekto);

                mp.start();



        }
        else
        {
            mp.pause();
          //  mp.stop();
    //        mp.prepare();
          //  mp.reset();
            //seekto= mp.getCurrentPosition();
        }
    }

}