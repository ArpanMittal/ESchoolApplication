package com.organization.sjhg.e_school.Structure;

import android.content.Context;
import android.database.Cursor;
import android.util.Base64;
import android.util.Log;

import com.organization.sjhg.e_school.Helpers.ProtectionHelper;
import com.organization.sjhg.e_school.Helpers.StorageManager;
import com.organization.sjhg.e_school.Content.Laughguru.LaughguruDataTable;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Sync.FileDownloader;

import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by IT SJHG on 4/30/2016.
 */
public class LaughguruContentDetailBase {
    public String ContentFileId;
    public String Order1;
    public String imagePath;
    public String audioPath;
    public byte[] protectionDataI;
    public byte[] protectionDataA;
    public String laughguruContentTypeId;

  static Cipher cipher;
    public LaughguruContentDetailBase(String ContentFileId,String Order,String imagePath,String audioPath, String LaughguruContentTypeId) throws JSONException
    {
        this.ContentFileId=ContentFileId.toString();
        this.audioPath=audioPath;
        this.imagePath=imagePath;
        this.Order1=Order.toString();
        this.laughguruContentTypeId=LaughguruContentTypeId;
    }

    public LaughguruContentDetailBase(Cursor c) {
     //   int a=-1;
   //     String s=c.getString();
        //a=Integer.parseInt(LaughguruDataTable.KEY_ContentFileId);
      //  a=(c.getInt(Integer.parseInt(LaughguruDataTable.KEY_ContentFileId)));
       // s =c.getColumnIndex(LaughguruDataTable.KEY_ContentFileId);
       //ContentFileId= String.valueOf((c.getColumnIndex(LaughguruDataTable.KEY_ContentFileId)));
        ContentFileId= c.getString(c.getColumnIndex(LaughguruDataTable.KEY_ContentFileId));
        Order1=c.getString(c.getColumnIndex(LaughguruDataTable.KEY_Order));
        audioPath=c.getString(c.getColumnIndex(LaughguruDataTable.KEY_audioPath));
        imagePath=c.getString(c.getColumnIndex(LaughguruDataTable.KEY_imagePath));
        protectionDataI=c.getBlob(c.getColumnIndex(LaughguruDataTable.KEY_ProtectionDataI));
        protectionDataA=c.getBlob(c.getColumnIndex(LaughguruDataTable.KEY_ProtectionDataA));
        laughguruContentTypeId=c.getString(c.getColumnIndex(LaughguruDataTable.KEY_LaughguruContentTypeId));
            //    Order1 = String.valueOf((c.getColumnIndex(LaughguruDataTable.KEY_Order)));
        //audioPath = String.valueOf((c.getColumnIndex(LaughguruDataTable.KEY_audioPath)));
     //   audioPath=(c.getString(Integer.parseInt(LaughguruDataTable.KEY_audioPath)));
      //  imagePath =(c.getString(Integer.parseInt(LaughguruDataTable.KEY_imagePath)));
       // imagePath = String.valueOf((c.getColumnIndex(LaughguruDataTable.KEY_imagePath)));
    int f=0;
    }

    public void SaveLaughguruContentToLocal(Context context ,String laughguruContentTypeId) throws IOException {
     //   boolean n=(laughguruContentTypeId.equals("1"));
        if(laughguruContentTypeId.equals("1")||laughguruContentTypeId.equals("3")) {
            String remotePathUrl = ServerAddress.getRemoteContentPath(context, imagePath);
            String localFilePath = StorageManager.geLocalFilePathfromRemote(remotePathUrl);
            new FileDownloader(localFilePath, remotePathUrl).Download();
            decData(localFilePath);
            this.protectionDataI = ProtectionHelper.InitialProtect(localFilePath);
            remotePathUrl = ServerAddress.getRemoteContentPath(context, audioPath);
            if(remotePathUrl!=null) {
                localFilePath = StorageManager.geLocalFilePathfromRemote(remotePathUrl);
                new FileDownloader(localFilePath, remotePathUrl).Download();
                decData(localFilePath);
            }
            //  this.protectionDataA=ProtectionHelper.InitialProtect(localFilePath);
        }
        else if(laughguruContentTypeId.equals("2"))
        {
            String remotePathUrl = ServerAddress.getRemoteContentPath(context, imagePath);
            String localFilePath = StorageManager.geLocalFilePathfromRemote(remotePathUrl);

            new FileDownloader(localFilePath, remotePathUrl).Download();
            decData(localFilePath);
            this.protectionDataI = ProtectionHelper.InitialProtect(localFilePath);
        }
    }


    private void decData(String localFilePath)
    {
        String keyString = "D4:6E:AC:3F:F0:BE";
        byte[] encodedKey = keyString.getBytes();
        File file = new File(localFilePath);
        SecretKey key = new SecretKeySpec(encodedKey, "Blowfish");
        try {
            cipher = Cipher.getInstance("Blowfish/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            FileInputStream fin = new FileInputStream(file);
            byte[] encode = new byte[(int) file.length()];
            fin.read(encode);
            String str = new String(encode);
            byte[] encode1 = Base64.decode(str, Base64.DEFAULT);
            byte[] result = cipher.doFinal(encode1);
            str=new String(result);
            encode1=Base64.decode(str,Base64.DEFAULT);
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(encode1);
           // return true;

        }catch (Exception e)
        {
            Log.e("error", "Exception" + e.toString());
           // return false;
        }
    }



    // public abstract InternalContentType getInternalContentType();

 //   public abstract void SaveLaughguruContentToLocal(Context context) throws IOException, JSONException, NetworkErrorException;

}

