package com.example.eplayer.entity;

import android.app.Application;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

/**
 * @Author zhangyan
 * MyApplication
 */
public class MyApplication extends Application {

    private static Context context;
    private ArrayList<Music> musicList = new ArrayList<>();
    private int position;
    private int loopMode;
    private Handler seekbarHandler;
    private Handler uiHandler;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isPlay = false;
    private int currentDuration = 0;
    private static MyApplication myApplication;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }

    public MyApplication() {
        myApplication = this;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public void setUiHandler(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public int getCurrentDuration() {
        return currentDuration;
    }

    public void setCurrentDuration(int currentDuration) {
        this.currentDuration = currentDuration;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public Handler getSeekbarHandler() {
        return seekbarHandler;
    }

    public void setSeekbarHandler(Handler seekbarHandler) {
        this.seekbarHandler = seekbarHandler;
    }

    public ArrayList<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(ArrayList<Music> musicList) {
        this.musicList = musicList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLoopMode() {
        return loopMode;
    }

    public void setLoopMode(int loopMode) {
        this.loopMode = loopMode;
    }


}
