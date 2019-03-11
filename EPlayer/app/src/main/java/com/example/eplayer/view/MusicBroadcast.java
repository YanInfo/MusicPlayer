package com.example.eplayer.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.eplayer.entity.MyApplication;

/*
@
*/public class MusicBroadcast extends BroadcastReceiver {

    private MyApplication myApplication;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("myBroadcast")) {

            //Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
        }
        if(intent.getAction().equals("next")){

           /* myApplication=(MyApplication) context.getApplication();
            int position = (myApplication.getPosition() + 1) % myApplication.getMusicList().size();
            listViewMusic.setItemChecked(position, true);
            myApplication.setPosition(position);
            intent = new Intent(context, MusicService.class);
            startService(intent);
            totalDuration = musicList.get(myApplication.getPosition()).getDuration();   //获取音乐时长(ms)
            totalTime.setText(new SimpleDateFormat("mm:ss").format(totalDuration));
            musicSeekBar.setMax(totalDuration);//给musicSeekBar设置最大时长 (后续)*/
        }
    }
}

