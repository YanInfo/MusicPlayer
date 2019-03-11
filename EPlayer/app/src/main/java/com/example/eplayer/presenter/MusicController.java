package com.example.eplayer.presenter;

import com.example.eplayer.entity.Music;
import com.example.eplayer.entity.MyApplication;
import com.example.eplayer.model.PlayMusic;

import android.app.NotificationManager;
import android.util.Log;

import java.util.ArrayList;

/*
@
*/public class MusicController {

    private static MusicController musicController;
    private PlayMusic playMusic;
    private MyApplication myApplication;
    private int position = 0;
    private int size = 0;
    private String music_url;
    private ArrayList<Music> musicList = new ArrayList<Music>(); //音乐列表

    private MusicController() {
        myApplication = MyApplication.getInstance();
        musicList = myApplication.getMusicList();
        size = musicList.size();
        playMusic = PlayMusic.getInstance();
    }

    public static MusicController getInstance() {
        if (musicController == null) {
            musicController = new MusicController();
        }
        return musicController;
    }

    //修改进度
    public void changeProgress(int currentDuration) {
        playMusic.playAtProgress(currentDuration);
    }

    public void nextMusic() {
        position = myApplication.getPosition();
        position = (position + 1) % size;
        myApplication.setPosition(position);
        music_url = musicList.get(position).getUrl();
        playMusic.play(music_url);
    }

    public void previousMusic() {
        position = myApplication.getPosition();
        position = (size + position - 1) % size;
        myApplication.setPosition(position);
        music_url = musicList.get(position).getUrl();
        playMusic.play(music_url);
    }

    public void playAndPause() {
        playMusic.playAndPause();
    }

    public void play() {
        position = myApplication.getPosition();
        music_url = musicList.get(position).getUrl();
        playMusic.play(music_url);
    }

    public void onDestory() {
        Log.d("controller", "destroy");
        playMusic.onDestory();
    }

    public void closeNotification() {
        NotificationManager notificationManager = myApplication.getNotificationManager();
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
    }
}
