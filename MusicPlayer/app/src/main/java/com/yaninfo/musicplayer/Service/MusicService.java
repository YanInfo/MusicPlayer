package com.yaninfo.musicplayer.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 后台控制的Service
 */
public class MusicService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
