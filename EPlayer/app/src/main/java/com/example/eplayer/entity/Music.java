package com.example.eplayer.entity;

import android.graphics.Bitmap;

/**
 * 音乐实体类
 * @Author zhangyan
 */
public class Music {

    /**
     * 歌曲ID
     */
    int id;

    /**
     * 歌曲名称
     */
    String title;

    /**
     * 歌曲路径
     */
    String url;

    /**
     * 歌曲路径
     */
    String author;

    /**
     * 歌曲时长
     */
    int duration;

    /**
     * 图片
     */
    Bitmap musicBitMap;

    public Music() {
    }

    public Music(int id, String title, String url, String author, int duration, Bitmap musicBitMap) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.author = author;
        this.duration = duration;
        this.musicBitMap = musicBitMap;
    }

    public Bitmap getMusicBitMap() {
        return musicBitMap;
    }

    public void setMusicBitMap(Bitmap musicBitMap) {
        this.musicBitMap = musicBitMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
