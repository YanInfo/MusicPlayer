package com.example.eplayer.view;

    /**
     * 服务连接
     */

    import android.content.ComponentName;
    import android.content.ServiceConnection;
    import android.os.IBinder;

    public class MyServiceConn implements ServiceConnection {

        MusicService.MyBinder binder = null;

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            binder = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub
            binder = null;
        }

    }