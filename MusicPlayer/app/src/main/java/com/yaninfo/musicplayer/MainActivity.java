package com.yaninfo.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yaninfo.musicplayer.adapter.MyAdapter;
import com.yaninfo.musicplayer.bean.Song;
import com.yaninfo.musicplayer.util.MusicUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private ListView mListView;
    private List<Song> list;
    private MyAdapter adapter;
    private MediaPlayer mediaPlayer;
    private int playPosition; //当前播放歌曲的序号
    private boolean IsPlay = false; //是否有歌曲在播放
    private Button playPause; //暂停和播放按钮
    private TextView song; //歌曲名
    private TextView singer; //歌手名
    private ImageView imageView; //控制台的图片
    private Animation animation; //动画
    private static int PLAY_LOOP = 0; //设置播放状态,默认是顺序播放
    private final static int PLAY_ORDER = 0;   //顺序播放
    private final static int PLAY_RANDOM = 1;  //随机播放
    private final static int PLAY_REPEAT = 2;  //单曲播放

    private Button playLoop;  //播放顺序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        mediaPlayer = new MediaPlayer();
        //初始化
        initView();
        //设置监听
        setListener();
    }


    /**
     * 初始化view
     */
    private void initView() {
        mListView = findViewById(R.id.main_listview);
        list = new ArrayList<>();
        //把扫描到的音乐赋值给list
        list = MusicUtils.getMusicData(this);
        adapter = new MyAdapter(MainActivity.this, list);
        mListView.setAdapter(adapter);
        playPause = findViewById(R.id.playing_btn_pause);
        playLoop = findViewById(R.id.playing_loop);
        song = findViewById(R.id.control_song);
        singer = findViewById(R.id.control_singer);
        imageView = findViewById(R.id.control_imageview);

        //加载动画
        animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        //设置动画匀速运动
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);

       /* LargeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.music);
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);*/

    }

    /**
     * ListView的监听事件
     */
    private void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //创建一个播放音频的方法，把点击到的地址传过去
                //list.get(i).path这个就是歌曲的地址
                play(list.get(position).path);
                //播放暂停按钮图片变成播放状态
                playPause.setBackgroundResource(R.drawable.pause_press);
                //把当前点击的item的position拿到,知道当前播放歌曲的序号
                playPosition = position;
                //播放音乐的时候把是否在播放赋值为true
                IsPlay = true;
                //点击item让控制台显示出来
                findViewById(R.id.main_control_rl).setVisibility(View.VISIBLE);

                //默认是顺序播放
                playLoop.setBackgroundResource(R.drawable.play_order);
            }
        });
    }

    /**
     * 底部控制栏的点击事件
     *
     * @param view
     */
    public void control(View view) {
        switch (view.getId()) {
            //上一曲
            case R.id.playing_btn_previous:
                //如果播放歌曲的序号小于或者等于0的话点击上一曲就跳到最后一首
                if (playPosition <= 0) {
                    //定位到最后一首
                    playPosition = list.size() - 1;
                } else {
                    //让歌曲的序号减一
                    playPosition--;
                }
                //开始播放
                play(list.get(playPosition).path);
                //设置图片
                imageView.setImageBitmap(list.get(playPosition).getPicture());
                //改变播放位置图片
                playPause.setBackgroundResource(R.drawable.pause_press);
                break;
            //暂停和播放
            case R.id.playing_btn_pause:
                if (IsPlay == false) {
                    //播放暂停按钮图片变成播放状态
                    playPause.setBackgroundResource(R.drawable.pause_press);
                    //继续播放
                    mediaPlayer.start();
                    //播放动画
                    imageView.startAnimation(animation);
                    //是否在播放赋值为true
                    IsPlay = true;
                    animation.start();
                    Toast.makeText(MainActivity.this, "播放" + list.get(playPosition).getSong(), Toast.LENGTH_SHORT).show();
                } else {
                    //播放暂停按钮图片变成暂停状态
                    playPause.setBackgroundResource(R.drawable.play_press);
                    //暂停歌曲
                    mediaPlayer.pause();
                    //停止动画
                    imageView.clearAnimation();
                    //是否在播放赋值为false
                    IsPlay = false;
                    animation.cancel();
                    Toast.makeText(MainActivity.this, "暂停" + list.get(playPosition).getSong(), Toast.LENGTH_SHORT).show();
                }

                break;
            //下一曲
            case R.id.playing_btn_next:
                //歌曲序号大于或者等于歌曲列表的大小-1时,让歌曲序号为第一首
                if (playPosition >= list.size() - 1) {
                    playPosition = 0;
                } else {
                    //点击下一曲让歌曲的序号加一
                    playPosition++;
                }
                //开始播放
                play(list.get(playPosition).path);
                //设置图片
                imageView.setImageBitmap(list.get(playPosition).getPicture());
                //播放暂停按钮图片变成播放状态
                playPause.setBackgroundResource(R.drawable.pause_press);

                break;
            //播放顺序
            case R.id.playing_loop:
                if(PLAY_LOOP==PLAY_ORDER) {
                    PLAY_LOOP = 1;
                    playLoop.setBackgroundResource(R.drawable.play_random);
                } else if(PLAY_LOOP == PLAY_RANDOM ) {
                    PLAY_LOOP = 2;
                    playLoop.setBackgroundResource(R.drawable.play_repeat);
                } else if(PLAY_LOOP == PLAY_REPEAT){
                    PLAY_LOOP =0;
                    playLoop.setBackgroundResource(R.drawable.play_order);
                }
                break;
        }

    }


    /**
     * 播放音频的方法,异步调用
     */
    private void play(String path) {
        //播放之前要先把音频文件重置
        try {
            mediaPlayer.reset();
            //调用方法传进去要播放的音频路径
            mediaPlayer.setDataSource(path);
            //异步准备音频资源
            mediaPlayer.prepareAsync();
            //调用mediaPlayer的监听方法，音频准备完毕会响应此方法
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {

                    mediaPlayer.start();//开始音频
                    setTextAndPic();  //设置歌曲名和歌手
                    //图片动画开始
                    imageView.startAnimation(animation);
                }

            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        //播放顺序监听
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                int playPos = playPosition;
                //顺序播放
                if (PLAY_LOOP == 0) {
                    playPos = (playPos + 1) % list.size();
                }
                //随机播放
                else if (PLAY_LOOP == 1) {
                    playPos = (int) (list.size() * Math.random());
                }
                play(list.get(playPos).path);
            }
        });
    }

    /**
     * 控制歌曲和歌手TextView的方法
     */
    private void setTextAndPic() {
        //设置图片
        imageView.setImageBitmap(list.get(playPosition).picture);
        song.setText(list.get(playPosition).song);
        //当歌曲名字太长是让其滚动
        song.setSelected(true);
        singer.setText(list.get(playPosition).singer);

    }
}
