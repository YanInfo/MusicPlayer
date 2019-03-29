package com.example.eplayer.view;

    /**
     * 服务连接管理类
     */
    import android.content.ComponentName;
    import android.content.ServiceConnection;
    import android.os.IBinder;

    public class MyServiceConn implements ServiceConnection {

        MusicService.MyBinder binder = null;

        /**
         * 服务连接成功
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MyBinder) service;
        }

        /**
         * 服务连接失败
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }

    }