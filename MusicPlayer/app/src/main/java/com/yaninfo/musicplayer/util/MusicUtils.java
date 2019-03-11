package com.yaninfo.musicplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import com.yaninfo.musicplayer.R;
import com.yaninfo.musicplayer.bean.Song;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐工具类
 * @Author zhangyan
 */
public class MusicUtils {

    static Context mContext;
    static Bitmap mMusicPic;
    /**
     * 扫描系统里面的音频文件，返回一个list集合
     */
    public static List<Song> getMusicData(Context context) {
        mContext = context;
        List<Song> list = new ArrayList<>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                song.singer = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                song.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                song.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                song.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                //加载图片
                MyPicture (song.path);

                //赋值给picture
                song.picture = mMusicPic;

                list.add(song);
            }
            // 释放资源
            cursor.close();
        }

        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

    /**
     * 获取图片, path是歌曲地址
     * mMusicPic定义的一个音乐图片的全局变量
     */
    public static void MyPicture (String path) {

        Uri selectedAudio = Uri.parse(path);
        MediaMetadataRetriever myRetriever = new MediaMetadataRetriever();
        myRetriever.setDataSource(mContext, selectedAudio);
        byte[] artwork = myRetriever.getEmbeddedPicture();

        if (artwork != null) {

            mMusicPic = BitmapFactory.decodeByteArray(artwork, 0, artwork.length);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mMusicPic.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] bytes = baos.toByteArray();
            mMusicPic = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        } else {
            mMusicPic = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.yaninfo);
        }
    }

}
