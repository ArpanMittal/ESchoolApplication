package com.organization.sjhg.e_school.Structure;

import com.organization.sjhg.e_school.Content.AudioVideoPlayerActivity;
import com.organization.sjhg.e_school.Content.Content_Type;
import com.organization.sjhg.e_school.Content.ImageDisplayActivity;
import com.organization.sjhg.e_school.Content.Laughguru.LaughGuruActivity;
import com.organization.sjhg.e_school.Content.PdfDisplayActivity;
import com.organization.sjhg.e_school.Content.Test.AdaptiveContent;
import com.organization.sjhg.e_school.Content.Test.TestActivity;
import com.organization.sjhg.e_school.Content.Test.TestInstructionActivity;
import com.organization.sjhg.e_school.R;

/**
 * Created by Bharat Lodha on 9/4/2015.
 * Organization : Eurovision Hitech Gurukul
 *
 */


public abstract class InternalContentType {

    public int contentType;
    /*public static final int getContentTypeID(Content_Type ct){
        return ct.getValue();
    }*/
    public static InternalContentType getContentTypeFromContentTypeId(int contentTypeId) {
        InternalContentType returnInternalContentType = null;
        if (contentTypeId == Content_Type.LAUGHGURU.getValue())
            returnInternalContentType = new LaughGuruType();
         else if (contentTypeId == Content_Type.PDF.getValue())
            returnInternalContentType = new PdfType();
        else if (contentTypeId == Content_Type.VIDEO.getValue())
            returnInternalContentType = new VideoType();
        else if (contentTypeId == Content_Type.IMAGE.getValue())
            returnInternalContentType = new ImageType();
        else if (contentTypeId == Content_Type.TEST.getValue())
            returnInternalContentType = new TestType();
        else if(contentTypeId==Content_Type.ADAPTIVE_TEST.getValue())
            returnInternalContentType = new AdaptiveType();

        return returnInternalContentType;

    }
    public abstract int getIconResourceId();

    public abstract Class<?> getViewerClass();

    public abstract String getContentTypeName();

}

class PdfType extends InternalContentType {

    PdfType() {
        super.contentType = 1;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.pdf_thumb;
    }

    @Override
    public String getContentTypeName() {
        return "Books/Notes";
    }

    @Override
    public Class<?> getViewerClass() {
        return PdfDisplayActivity.class;
    }

}

class ImageType extends InternalContentType {

    ImageType() {
        super.contentType = 2;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.image_thumb;
    }

    @Override
    public String getContentTypeName() {
        return "Images";
    }


    @Override
    public Class<?> getViewerClass() {
        return ImageDisplayActivity.class;
    }

}

class TestType extends InternalContentType {
    TestType() {
        super.contentType = 3;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.test_thumb;
    }

    @Override
    public String getContentTypeName() {
        return "Tests";
    }

    @Override
    public Class<?> getViewerClass() {
        return TestInstructionActivity.class;
    }

}

class VideoType extends InternalContentType {
    VideoType() {
        super.contentType = 4;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.video_thumb;
    }

    @Override
    public String getContentTypeName() {
        return "Videos";
    }

    @Override
    public Class<?> getViewerClass() {
        return AudioVideoPlayerActivity.class;
    }

}

class LaughGuruType extends InternalContentType {
    LaughGuruType() {
        super.contentType = 5;
    }

    @Override
    public int getIconResourceId() {
        return R.drawable.laughguru;
    }

    @Override
    public String getContentTypeName() {
        return "Laughguru";
    }

    @Override
    public Class<?> getViewerClass() {
        return LaughGuruActivity.class;
    }
}
 class AdaptiveType extends InternalContentType{
     AdaptiveType(){ super.contentType =6;}

     @Override
     public int getIconResourceId() {
         return R.drawable.test_thumb;
     }

     @Override
     public String getContentTypeName() {
         return "AdaptiveTest";
     }

     @Override
     public Class<?> getViewerClass() {
         return TestInstructionActivity.class;
     }
 }