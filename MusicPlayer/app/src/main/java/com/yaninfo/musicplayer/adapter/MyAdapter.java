package com.yaninfo.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaninfo.musicplayer.MainActivity;
import com.yaninfo.musicplayer.R;
import com.yaninfo.musicplayer.bean.Song;
import com.yaninfo.musicplayer.util.MusicUtils;

import java.util.List;

/**
 * 适配器
 * @Author zhangyan
 */
public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Song> list;

    public MyAdapter(MainActivity mainActivity, List<Song> list) {
        this.context = mainActivity;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            //引入布局
            view = View.inflate(context, R.layout.item_view, null);
            //实例化对象
            holder.picture = view.findViewById(R.id.list_pic);
            holder.song = view.findViewById(R.id.item_mymusic_song);
            holder.singer = view.findViewById(R.id.item_mymusic_singer);
            holder.duration = view.findViewById(R.id.item_mymusic_duration);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //给控件赋值
        holder.song.setText(list.get(position).getSong());
        holder.singer.setText(list.get(position).getSinger());
        holder.picture.setImageBitmap(list.get(position).getPicture());
        //时间需要转换一下
        int duration = list.get(position).duration;
        //时间转换一下格式
        String time = MusicUtils.formatTime(duration);
        holder.duration.setText(time);

        return view;

    }

    /**
     * 缓存布局
     */
    class ViewHolder {
        /**
         * 图片
         */
        ImageView picture;
        /**
         * 歌曲名
         */
        TextView song;
        /**
         * 歌手
         */
        TextView singer;
        /**
         * 时间
         */
        TextView duration;

    }


}
