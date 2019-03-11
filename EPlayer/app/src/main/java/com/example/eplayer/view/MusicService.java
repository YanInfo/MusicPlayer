package com.example.eplayer.view;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.example.eplayer.entity.Values;
import com.example.eplayer.presenter.MusicController;

public class MusicService extends Service {

    private MusicController musicController;

    //定义音乐播放器变量
    @Override
    public void onCreate() {
        Log.d("service", "create");
        super.onCreate();
        musicController = MusicController.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        System.out.println("--onBind--");
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    //解绑的时候使用的这个方法
    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        System.out.println("--onUnbind--");
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Values.NEXTMUSIC)) {
                musicController.nextMusic();
            }
            if (intent.getAction().equals(Values.closeNotification)) {
                musicController.closeNotification();
            }
            if (intent.getAction().equals(Values.PERVIOUSMUSIC)) {
                musicController.previousMusic();
            }
            if (intent.getAction().equals(Values.PLAY)) {
                musicController.playAndPause();
            }
            if (intent.getAction().equals(Values.changeCurrent)) {
                int currentDuration = intent.getIntExtra("progress", 0);
                musicController.changeProgress(currentDuration);
            }
            if (intent.getAction().equals(Values.NOMAL)) {
                musicController.play();
            }

        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.d("service", "destroy");
        super.onDestroy();
        musicController.onDestory();
    }

}