package com.guestworker.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * @author 莫小婷
 * @create 2019/4/20
 * @Describe 文件管理
 */
public class FileManager {

    /**
     * /storage/emulated/0/Android/data/com.example.administrator.jipinshop/files/Pictures
     * 这类文件夹是用户可见，可删的，并且在APP卸载后也会自动卸载掉该文件夹
     * 一般放一些长时间保存的数据
     * @param context
     * @return
     */
    public static String externalFiles(Context context){
        return context.getExternalFilesDir(DIRECTORY_PICTURES).getPath();
    }

    /** 保存方法 */
    public  static String saveBitmap(Context context,Bitmap bm) {
        Log.e("----", "保存图片");
        String sdDir = externalFiles(context);
        String str =  sdDir + "/"+ System.currentTimeMillis() + "bitmap.jpg";
        File f = new File(str);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("----", "已经保存");
            //保存图片后发送广播通知更新数据库
            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(),  f.getAbsolutePath(), f.getName(), "");
            Uri uri = Uri.fromFile(f);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return  str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  str;
    }

}
