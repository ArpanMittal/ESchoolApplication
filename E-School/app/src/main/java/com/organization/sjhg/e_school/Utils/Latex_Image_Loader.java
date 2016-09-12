package com.organization.sjhg.e_school.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by arpan on 9/12/2016.
 */
public class Latex_Image_Loader extends AsyncTask<Object, Void, Bitmap>
{

    private LevelListDrawable mDrawable;
    private TextView textView;

    @Override
    protected Bitmap doInBackground(Object... params) {
        String source = (String) params[0];
        mDrawable = (LevelListDrawable) params[1];
        textView=(TextView)params[2];
        // Log.d(TAG, "doInBackground " + source);
        try {
            InputStream is = new URL(source).openStream();
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //Log.d(TAG, "onPostExecute drawable " + mDrawable);
        //Log.d(TAG, "onPostExecute bitmap " + bitmap);
        if (bitmap != null) {
            BitmapDrawable d = new BitmapDrawable(bitmap);
            mDrawable.addLevel(1, 1, d);
            mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
            mDrawable.setLevel(1);
            // i don't know yet a better way to refresh TextView
            // mTv.invalidate() doesn't work as expected
            CharSequence t = textView.getText();
            textView.setText(t);
        }
    }
}
