package com.example.eplayer.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.example.eplayer.R;
import com.example.eplayer.entity.Music;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/*
@
*/public class ScannerMusic {
    private static ScannerMusic scannerMusic = null;
    private Context context;

    public static ScannerMusic getInstance(Context context) {
        if (scannerMusic == null) {
            scannerMusic = new ScannerMusic(context);
        }
        return scannerMusic;
    }

    private ScannerMusic(Context context) {
        this.context = context;
    }

    public ArrayList<Music> ScannerMusic() {
        ArrayList<Music> list = new ArrayList<>(); // 查询媒体数据库
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.TITLE);    //按标题排序
        // 遍历媒体数据库
        if (cursor.moveToFirst()) {    //遍历每一行
            while (!cursor.isAfterLast()) {
                //歌曲ID：MediaStore.Audio.Media._ID
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌曲的名称 ：MediaStore.Audio.Media.TITLE
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //歌曲的名称 ：MediaStore.Audio.Media.ARTIST
                String author = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲路径 ：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //歌曲图片 ：MediaStore.Audio.Media.

                Uri selectedAudio = Uri.parse(url);
                MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
                myRetriever.setDataSource(context, selectedAudio); // the URI of audio file
                byte[] artwork = myRetriever.getEmbeddedPicture();
                Bitmap musicBitMap;
                if (artwork != null) {

                    musicBitMap = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    musicBitMap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                    byte[] bytes = baos.toByteArray();
                    musicBitMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                } else {
                    musicBitMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
                }
                //歌曲时长 ：MediaStore.Audio.Media.totalDuration
                int totalDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                Music m = new Music(id, tilte, url, author, totalDuration, musicBitMap);
                list.add(m);
                cursor.moveToNext();
            }  //end while
            cursor.close();
        }  //end if
        return list;
    }
}
