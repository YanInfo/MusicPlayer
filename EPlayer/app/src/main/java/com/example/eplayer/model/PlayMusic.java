package com.example.eplayer.model;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.example.eplayer.entity.Music;
import com.example.eplayer.entity.MyApplication;
import com.example.eplayer.entity.Values;

import java.util.ArrayList;

/**
 * 播放逻辑类
 * @Author zhangyan
 */
public class PlayMusic {
    private Thread myThread = null;
    private static PlayMusic playMusic;
    private MediaPlayer mediaPlayer;
    private MyApplication myApplication;
    private int size;
    private ArrayList<Music> musicList;
    private Handler uIHandler;
    private Handler currentSeekbarHandler;

    private PlayMusic() {
        myApplication = MyApplication.getInstance();
        mediaPlayer = myApplication.getMediaPlayer();
        musicList = myApplication.getMusicList();
        size = musicList.size();
        uIHandler = myApplication.getUiHandler();
        currentSeekbarHandler = myApplication.getSeekbarHandler();
        init();
    }

    public static PlayMusic getInstance() {
        if (playMusic == null) {
            playMusic = new PlayMusic();
        }
        return playMusic;
    }

    public void play(String url) {
        try {
            mediaPlayer.reset();    // 重置
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();     // 准备
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
        myApplication.setPlay(true);

        //更新ui
        sendUiChangeMessage();
        sendSeekBarChangeMessage();
    }

    /**
     * 更新播放进度
     * @param currentDuration
     */
    public void playAtProgress(int currentDuration) {
        mediaPlayer.seekTo(currentDuration);
    }

    /**
     * 更新专辑图片等
     */
    public void sendUiChangeMessage() {
        Message m2 = new Message();
        m2.what = Values.UPDATEIMAGE;
        // 直接发送一个what=0的空消息
        uIHandler.sendMessage(m2);
    }

    /**
     * 更新进度条
     */
    public void sendSeekBarChangeMessage() {
        myThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // 每500ms执行一次mTimerTask任务
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        break;
                    }
                    if (myApplication.isPlay()) {
                        Message m = new Message();
                        m.what = Values.UPDATESEEKBAR;
                        try {
                            m.arg1 = mediaPlayer.getCurrentPosition();
                        } catch (IllegalStateException e) {
                            break;
                        }
                        currentSeekbarHandler.sendMessage(m);
                    }
                }
            }
        };
        myThread.start();
    }

    public void playAndPause() {
        if (mediaPlayer.isPlaying()) {
            myApplication.setPlay(false);
            mediaPlayer.pause();
            sendUiChangeMessage();
        } else {
            myApplication.setPlay(true);
            mediaPlayer.start();
            sendUiChangeMessage();
            sendSeekBarChangeMessage();
        }
    }

    public void init() {
        {
            // 因为直接切歌会发生错误，所以增加错误监听器。返回true。就不会回调onCompletion方法了。
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {

                    return true;
                }
            });
            //监听播放完成
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.reset();    // 重置
                    switch (myApplication.getLoopMode()) {
                        case Values.LISTLOOP: {
                            int position = myApplication.getPosition() + 1;
                            myApplication.setPosition(position);
                            play(musicList.get(position).getUrl());
                            break;
                        }
                        case Values.SINGLETUNECIRCULATION: {
                            play(musicList.get(myApplication.getPosition()).getUrl());
                            break;
                        }
                        case Values.RANDOM: {
                            int position = (int) (size * Math.random());
                            myApplication.setPosition(position);
                            play(musicList.get(position).getUrl());
                            break;
                        }
                        default:
                            break;
                    }
                }
            });
        }
    }

    public void onDestory() {
        if (mediaPlayer != null) {
            //关键语句
            mediaPlayer.reset();

            mediaPlayer.stop();  //停止播放
            mediaPlayer.release();    //回收
        }
        if (myThread != null) {
            myThread.interrupt();
        }
    }

}
