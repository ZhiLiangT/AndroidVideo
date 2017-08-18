package com.tianzl.androidvideo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tianzl on 2017/8/6.
 */

public class FileManager {
    private static final String TAG = "FileManager";
    /**
     * 应用缓存的路径
     */
    private static String imgCachePath;
    /**
     * 保存的目录名
     */
    private final static String FOLDER_NAME = "/VideoImage";

    public FileManager(Context context) {
        imgCachePath = context.getExternalCacheDir().getPath();
        Log.e(TAG, "imgCachePath = " + imgCachePath);
    }

    /**
     * 获取图片缓存路径
     * @return
     */
    private String getImageDirectory() {
        return imgCachePath + FOLDER_NAME;
    }

    /**
     * 保存image的方法
     * @param fileName 文件名
     */
    public void saveBitmap(String fileName, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return;
        }
        String path = getImageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdir();
        }
        File file = new File(path + File.separator + fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    /**
     *
     * 从手机存储中获取bitmap
     */
    public Bitmap getBitmap(String fileName) {
        return BitmapFactory.decodeFile(getImageDirectory() + File.separator + fileName);
    }

    /**
     * 判断文件是否存在
     */
    public boolean isFileExists(String fileName) {
        return new File(getImageDirectory() + File.separator + fileName).exists();
    }

    /**
     * 获取文件的大小
     */
    public long getFileSize(String fileName) {
        return new File(getImageDirectory() + File.separator + fileName).length();
    }

    /**
     * 删除缓存的本地图片
     */
    public void deleteFile() {
        File dirFile = new File(getImageDirectory());
        if (! dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] subFile = dirFile.list();
            for (int i = 0; i < subFile.length; i++) {
                new File(dirFile, subFile[i]).delete();
            }
        }
        dirFile.delete();
    }

}
