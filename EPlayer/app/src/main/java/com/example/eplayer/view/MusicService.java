package com.example.eplayer.view;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.eplayer.entity.Values;
import com.example.eplayer.presenter.MusicController;

/**
 * @Author zhangyan
 * 后台服务对象
 */
public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private MusicController mMusicController;
    private MyBinder mMyBinder = new MyBinder();

    /**
     * 定义音乐播放器变量
     */
    @Override
    public void onCreate() {
        Log.i(TAG, "--onCreate--");
        super.onCreate();
        mMusicController = MusicController.getInstance();
    }

    /**
     * 进程通信,继承Binder对象
     */
    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "--onBind--");
        return mMyBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "--onUnbind--");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "--onStartCommand--");

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Values.NEXTMUSIC)) {
                mMusicController.nextMusic();
            }
            if (intent.getAction().equals(Values.closeNotification)) {
                mMusicController.closeNotification();
            }
            if (intent.getAction().equals(Values.PERVIOUSMUSIC)) {
                mMusicController.previousMusic();
            }
            if (intent.getAction().equals(Values.PLAY)) {
                mMusicController.playAndPause();
            }
            if (intent.getAction().equals(Values.changeCurrent)) {
                int currentDuration = intent.getIntExtra("progress", 0);
                mMusicController.changeProgress(currentDuration);
            }
            if (intent.getAction().equals(Values.NOMAL)) {
                mMusicController.play();
            }

        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "--destroy--");
        super.onDestroy();
        mMusicController.onDestory();
    }

}