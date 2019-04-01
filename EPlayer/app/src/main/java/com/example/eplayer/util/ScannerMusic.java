package com.example.eplayer.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import com.example.eplayer.R;
import com.example.eplayer.entity.Music;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * @Author zhangyan
 * 扫描本地音乐工具类
 */
public class ScannerMusic {

    private static ScannerMusic scannerMusic = null;
    private Context mContext;

    /**
     * 歌曲id
     */
    private int mId;

    /**
     * 歌曲名称
     */
    private String mTitle;

    /**
     * 歌手
     */
    private String mAuthor;

    /**
     * 歌曲路径
     */
    private String mUrl;

    /**
     * 总时间
     */
    private int mTime;

    /**
     * 歌曲图片
     */
    private Bitmap mMusicPic;

    public static ScannerMusic getInstance(Context context ) {
        if (scannerMusic == null) {
            scannerMusic = new ScannerMusic(context);
        }
        return scannerMusic;
    }

    private ScannerMusic(Context context) {
        this.mContext = context;
    }

    public ArrayList<Music> ScannerMusic() {

        // 定义一个List存储
        ArrayList<Music> list = new ArrayList<>();

        // 查询媒体数据库
        Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.TITLE);
        // 遍历媒体数据库
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                // 歌曲ID：MediaStore.Audio.Media._ID
                mId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                // 歌曲的名称 ：MediaStore.Audio.Media.TITLE
                mTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                // 歌曲的名称 ：MediaStore.Audio.Media.ARTIST
                mAuthor = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                // 歌曲路径 ：MediaStore.Audio.Media.DATA
                mUrl = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                // 歌曲图片
                MyPicture(mUrl);

                // 歌曲时长 ：MediaStore.Audio.Media.totalDuration
                mTime = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                // 调用Music对象来传参
                Music m = new Music(mId, mTitle, mUrl, mAuthor, mTime, mMusicPic);
                list.add(m);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 获取图片, path是歌曲地址,对应url
     * mMusicPic定义的一个音乐图片的全局变量
     */
    public void MyPicture(String path) {

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
            mMusicPic = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon);
        }
    }
}
