package com.example.eplayer.presenter;

import com.example.eplayer.entity.Music;
import com.example.eplayer.entity.MyApplication;
import com.example.eplayer.model.PlayMusic;
import com.example.eplayer.util.LogUtil;
import android.app.NotificationManager;
import java.util.ArrayList;

/**
 * @Author zhangyan
 * 逻辑类
 */
public class MusicController {

    private static MusicController musicController;
    private PlayMusic mPlayMusic;
    private MyApplication myApplication;
    private int position = 0;
    private int size = 0;
    private String musicUrl;
    private ArrayList<Music> musicList;

    /**
     * 无参构造方法
     */
    private MusicController() {
        myApplication = MyApplication.getInstance();
        musicList = myApplication.getMusicList();
        size = musicList.size();
        mPlayMusic = PlayMusic.getInstance();
    }

    /**
     * 有参构造方法
     * @return
     */
    public static MusicController getInstance() {
        if (musicController == null) {
            musicController = new MusicController();
        }
        return musicController;
    }

    /**
     * 修改进度
     * @param currentDuration
     */
    public void changeProgress(int currentDuration) {
        mPlayMusic.playAtProgress(currentDuration);
    }

    /**
     * 下一首
     */
    public void nextMusic() {
        position = myApplication.getPosition();
        position = (position + 1) % size;
        myApplication.setPosition(position);
        musicUrl = musicList.get(position).getUrl();
        mPlayMusic.play(musicUrl);
    }

    /**
     * 上一首
     */
    public void previousMusic() {
        position = myApplication.getPosition();
        position = (size + position - 1) % size;
        myApplication.setPosition(position);
        musicUrl = musicList.get(position).getUrl();
        mPlayMusic.play(musicUrl);
    }

    /**
     * 播放暂停
     */
    public void playAndPause() {
        mPlayMusic.playAndPause();
    }

    /**
     * 播放
     */
    public void play() {
        position = myApplication.getPosition();
        musicUrl = musicList.get(position).getUrl();
        mPlayMusic.play(musicUrl);
    }

    /**
     * 停止
     */
    public void onDestory() {
        LogUtil.d("controller", "destroy");
        mPlayMusic.onDestory();
    }

    /**
     * 关闭通知栏
     */
    public void closeNotification() {
        NotificationManager notificationManager = myApplication.getNotificationManager();
        if (notificationManager != null) {
            notificationManager.cancel(1);
        }
    }
}
