package com.example.eplayer.view;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.example.eplayer.entity.Values;
import com.example.eplayer.presenter.MusicController;
import com.example.eplayer.util.LogUtil;

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
        LogUtil.i(TAG, "--onBind--");
        return mMyBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.i(TAG, "--onUnbind--");
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtil.i(TAG, "--onStartCommand--");

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Values.NEXT_MUSIC)) {
                mMusicController.nextMusic();
            }
            if (intent.getAction().equals(Values.CLOSE_NOTIFICATION)) {
                mMusicController.closeNotification();
            }
            if (intent.getAction().equals(Values.PERVIOUS_MUSIC)) {
                mMusicController.previousMusic();
            }
            if (intent.getAction().equals(Values.PLAY)) {
                mMusicController.playAndPause();
            }
            if (intent.getAction().equals(Values.CHANGE_CURRENT)) {
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
        LogUtil.i(TAG, "--destroy--");
        super.onDestroy();
        mMusicController.onDestory();
    }

}