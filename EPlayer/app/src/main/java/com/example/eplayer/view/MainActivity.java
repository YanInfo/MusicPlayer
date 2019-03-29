package com.example.eplayer.view;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.eplayer.entity.Music;
import com.example.eplayer.entity.MyApplication;
import com.example.eplayer.entity.Values;
import com.example.eplayer.util.ScannerMusic;
import com.example.eplayer.view.adapter.MusicAdapter;
import com.example.eplayer.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * @Author zhangyan
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Music> musicList = new ArrayList<Music>(); //音乐列表
    private MyApplication myApplication;
    private MediaPlayer mediaPlayer;
    private SeekBar musicSeekBar;
    private ImageView btnNextMusic;
    private ImageView btnPreviousMusic;
    private ImageButton btnChangeLoopMode;
    private ListView listViewMusic;
    private NotificationCompat.Builder mBuilder;
    private TextView currentTime;
    // 显示当前音乐进度信息
    private TextView totalTime;
    // 播放按钮
    private ImageView btnPlay;
    private SharedPreferences sharedPreferences;
    private int loopMode = Values.LIST_LOOP;
    private Intent intent = null;
    private ScannerMusic scannerMusic;
    private NotificationManager notificationManager;
    private RemoteViews musicViews;
    private Music music;
    private ImageView imageViewAlbum;
    MyServiceConn mMyserviceconn;


    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Values.UPDATE_IMAGE) {
                if (myApplication.isPlay()) {
                    btnPlay.setImageResource(R.drawable.ic_play_btn_pause);
                    musicViews.setImageViewResource(R.id.widget_play, R.drawable.ic_play_btn_pause);
                } else {
                    musicViews.setImageViewResource(R.id.widget_play, R.drawable.ic_play_btn_play);
                    btnPlay.setImageResource(R.drawable.ic_play_btn_play);
                }

                int position = myApplication.getPosition();
                music = musicList.get(position);
                musicViews.setTextViewText(R.id.widget_title, music.getTitle());
                musicViews.setTextViewText(R.id.widget_artist, music.getAuthor());
                Bitmap b = music.getMusicBitMap();
                musicViews.setImageViewBitmap(R.id.widget_album, b);
                mBuilder.setCustomContentView(musicViews)
                        .setCustomBigContentView(musicViews);
                notificationManager.notify(1, mBuilder.build());
                imageViewAlbum.setImageBitmap(b);
                int duration = music.getDuration();
                musicSeekBar.setMax(duration);
                listViewMusic.setItemChecked(position, true);
                totalTime.setText(new SimpleDateFormat("mm:ss").format(duration));
            }

        }
    };


    private Handler currentSeekbarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Values.UPDATE_SEEKBAR) {
                musicSeekBar.setProgress(msg.arg1);
                currentTime.setText(new SimpleDateFormat("mm:ss").format(msg.arg1));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initNotification();

        listViewMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myApplication.setPosition(position);
                intent = new Intent(MainActivity.this, MusicService.class);
                intent.setAction(Values.NOMAL);
                //启动服务
                startService(intent);
            }

        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, MusicService.class);
                intent.setAction(Values.PLAY);
                startService(intent);
            }
        });

        btnNextMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, MusicService.class);
                intent.setAction(Values.NEXT_MUSIC);
                startService(intent);
            }
        });

        btnPreviousMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, MusicService.class);
                intent.setAction(Values.PERVIOUS_MUSIC);
                startService(intent);
            }
        });

        /**
         * 播放顺序
         */
        btnChangeLoopMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loopMode = myApplication.getLoopMode();
                if (loopMode == Values.LIST_LOOP) {
                    myApplication.setLoopMode(Values.SINGLE_LOOP);
                    btnChangeLoopMode.setImageResource(R.drawable.single);
                } else if (loopMode == Values.SINGLE_LOOP) {
                    myApplication.setLoopMode(Values.RANDOM_LOOP);
                    btnChangeLoopMode.setImageResource(R.drawable.random);
                } else {
                    myApplication.setLoopMode(Values.LIST_LOOP);
                    btnChangeLoopMode.setImageResource(R.drawable.nomal);
                }
            }
        });

        //播放完毕
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar musicSeekBar, int progress, boolean fromUser) {
                //此处无代码
            }

            @Override
            public void onStartTrackingTouch(SeekBar musicSeekBar) {
                //此处无代码
            }

            //设置进度
            @Override
            public void onStopTrackingTouch(SeekBar musicSeekBar) {

                intent = new Intent(MainActivity.this, MusicService.class);
                intent.setAction(Values.CHANGE_CURRENT);
                intent.putExtra("progress", musicSeekBar.getProgress());
                startService(intent);

            }
        });

    }

    private void init() {
        sharedPreferences = getSharedPreferences("myinfo", Context.MODE_PRIVATE);
        int position = sharedPreferences.getInt("position", 0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        myApplication = (MyApplication) getApplication();

        //初始化侧滑栏
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listViewMusic = findViewById(R.id.listView_Music);
        musicSeekBar = findViewById(R.id.seekBar_Music);
        currentTime = findViewById(R.id.textView_CurrentTime);
        totalTime = findViewById(R.id.textView_TotalTime);
        btnPlay = findViewById(R.id.imageButton_Play);
        btnChangeLoopMode = findViewById(R.id.imageButton_Loop);
        btnNextMusic = findViewById(R.id.imageButton_Next);
        btnPreviousMusic = findViewById(R.id.imageButton_Previous);
        imageViewAlbum = findViewById(R.id.imageView_album);

        // 获取音乐列表
        scannerMusic = ScannerMusic.getInstance(MainActivity.this);
        musicList = scannerMusic.ScannerMusic();
        // 配置适配器
        MusicAdapter adapter = new MusicAdapter(MainActivity.this, R.layout.adapter_musicitem, musicList);
        listViewMusic.setAdapter(adapter);

        // 初始化循环模式
        myApplication.setLoopMode(Values.LIST_LOOP);
        myApplication.setMusicList(musicList);
        myApplication.setSeekbarHandler(currentSeekbarHandler);
        myApplication.setUiHandler(uiHandler);

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();    // 重置
            mediaPlayer.setDataSource(musicList.get(position).getUrl());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myApplication.setMediaPlayer(mediaPlayer);
        myApplication.setPosition(position);
        listViewMusic.setItemChecked(position, true);
        music = musicList.get(position);
        imageViewAlbum.setImageBitmap(music.getMusicBitMap());

        intent = new Intent(MainActivity.this, MusicService.class);
        intent.setAction(Values.BIND_SERVICE);
        mMyserviceconn = new MyServiceConn();
        //绑定服务
        bindService(intent, mMyserviceconn, Context.BIND_AUTO_CREATE);

    }

    /**
     * 初始化通知栏
     */
    private void initNotification() {

        //* 实例化通知栏构造器
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);

        NotificationChannel channel;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("10086", "音乐", NotificationManager.IMPORTANCE_LOW);
            //桌面launcher的消息角标
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId("10086");
        }

        musicViews = new RemoteViews(getPackageName(), R.layout.customnotice);
        musicViews.setTextViewText(R.id.widget_title, music.getTitle());
        musicViews.setTextViewText(R.id.widget_artist, music.getAuthor());
        musicViews.setImageViewBitmap(R.id.widget_album, music.getMusicBitMap());

        //设置通知消息的跳转  -->   Intend 和PendingIntent 的使用
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(Values.NEXT_MUSIC);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, FLAG_UPDATE_CURRENT);
        musicViews.setOnClickPendingIntent(R.id.widget_next, pendingIntent);

        intent = new Intent(this, MusicService.class);
        intent.setAction(Values.PLAY);
        pendingIntent = PendingIntent.getService(this, 1, intent, FLAG_UPDATE_CURRENT);
        musicViews.setOnClickPendingIntent(R.id.widget_play, pendingIntent);

        intent = new Intent(this, MusicService.class);
        intent.setAction(Values.CLOSE_NOTIFICATION);
        pendingIntent = PendingIntent.getService(this, 1, intent, FLAG_UPDATE_CURRENT);
        musicViews.setOnClickPendingIntent(R.id.widget_close, pendingIntent);

        intent = new Intent(this, MainActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 1, intent, FLAG_UPDATE_CURRENT);
        musicViews.setOnClickPendingIntent(R.id.widget_album, pendingIntent);

        //设置Builder
        mBuilder.setCustomContentView(musicViews)
                .setCustomBigContentView(musicViews)
                //传入PendingIntent的实例即可
                .setContentIntent(pendingIntent)
                //大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //小图标
                .setSmallIcon(R.drawable.icon)
                // .setVisibility(Notification.VISIBILITY_PUBLIC)
                //首次进入显示的效果
                //.setTicker("测试内容")
                //设置提醒的模式
                .setDefaults(Notification.DEFAULT_SOUND);
        //最后通过nitificationManager.notify将通知发送出去即实现了将通知加入状态栏,标记为id
        // notify中两个参数为 第一个id参数说明：id相当于notification的一一对应标志
        //notify的第一个参数是常量的话，那么多次notify只能弹出一个通知，后续的通知会替换掉原有通知
        // 而如果每次id不同，那么多次调用notify会弹出多个通知
        notificationManager.notify(1, mBuilder.build());

        myApplication.setNotificationManager(notificationManager);

    }

    /**
     * 退出监听
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            //如果没关闭侧滑栏，关闭侧滑栏，否则退出
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                exitMainactivity();
            }
        }
        return false;
    }

    /**
     * 退出
     */
    private void exitMainactivity() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.bridge)
                .setTitle(getResources().getString(R.string.hint))
                .setMessage(getResources().getString(R.string.ensureExit))
                .setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (intent != null) {
                            unbindService(mMyserviceconn);
                            stopService(intent);
                        }
                        sharedPreferences = getSharedPreferences("myinfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("position", myApplication.getPosition());
                        editor.commit();
                        //关闭通知
                        if (musicViews != null) {
                            notificationManager.cancel(1);
                        }
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancle), null)
                .show();
    }

    /**
     * 加载菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 菜单栏
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            exitMainactivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑栏
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 结束播放器
     */
    @Override
    protected void onDestroy() {
        Log.d("mainactivity", "destroy");
        super.onDestroy();
    }
}


